package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(ID) AS ID,
               EMAIL,
               PASSWORD,
               CREATED_AT,
               UPDATED_AT,
               ROLE,
               PROFILE_PICTURE,
               PROFILE_PICTURE_CONTENT_TYPE
        FROM USERS
        """;

    private static final String ORDER_BY_EMAIL = """
        ORDER BY EMAIL
        """;

    private static final String COUNT_ALL_SQL = """
        SELECT COUNT(*)
        FROM USERS
        """;

    private static final String FIND_BY_ID_SQL = BASE_SELECT + """
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String FIND_BY_EMAIL_SQL = BASE_SELECT + """
        WHERE LOWER(EMAIL) = LOWER(?)
        """;

    private static final String EXISTS_BY_ID_SQL = """
        SELECT 1
        FROM USERS
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String EXISTS_BY_EMAIL_SQL = """
        SELECT 1
        FROM USERS
        WHERE LOWER(EMAIL) = LOWER(?)
        """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM USERS
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO USERS (
            ID,
            EMAIL,
            PASSWORD,
            CREATED_AT,
            UPDATED_AT,
            ROLE,
            PROFILE_PICTURE,
            PROFILE_PICTURE_CONTENT_TYPE
        )
        VALUES (
            HEXTORAW(?),
            ?,
            ?,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            ?,
            ?,
            ?
        )
        """;

    private static final String UPDATE_SQL = """
        UPDATE USERS
        SET EMAIL = ?,
            PASSWORD = ?,
            UPDATED_AT = CURRENT_TIMESTAMP,
            ROLE = ?,
            PROFILE_PICTURE = ?,
            PROFILE_PICTURE_CONTENT_TYPE = ?
        WHERE RAWTOHEX(ID) = ?
        """;

    private final DataSource dataSource;

    public UserRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> findByEmail(final String email) {
        return findSingle(FIND_BY_EMAIL_SQL, email, "Failed to fetch user by email: " + email);
    }

    public Optional<UserProjection> findProjectedByEmail(final String email) {
        return findByEmail(email).map(this::toProjection);
    }

    public Boolean existsByEmail(final String email) {
        return exists(EXISTS_BY_EMAIL_SQL, email, "Failed to check user existence by email: " + email);
    }

    public boolean existsById(final UUID id) {
        return exists(EXISTS_BY_ID_SQL, UuidUtil.toRawHex(id), "Failed to check user existence by id: " + id);
    }

    public Optional<User> findById(final UUID id) {
        return findSingle(FIND_BY_ID_SQL, UuidUtil.toRawHex(id), "Failed to fetch user by id: " + id);
    }

    public Page<User> findAll(final Pageable pageable) {
        String sql = BASE_SELECT + ORDER_BY_EMAIL + """
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        List<User> users = new ArrayList<>();
        long total = 0;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, pageable.getOffset());
            preparedStatement.setInt(2, pageable.getPageSize());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    users.add(mapUser(rs));
                }
            }

            try (PreparedStatement countStatement = connection.prepareStatement(COUNT_ALL_SQL);
                 ResultSet countRs = countStatement.executeQuery()) {
                if (countRs.next()) {
                    total = countRs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paged users", e);
        }

        return new PageImpl<>(users, pageable, total);
    }

    public User save(final User user) {
        if (user.getId() == null) {
            UUID id = UUID.randomUUID();
            insert(id, user);

            return findById(id)
                    .orElseThrow(() -> new IllegalStateException("User was inserted but could not be retrieved"));
        }

        update(user);

        return findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("User was updated but could not be retrieved"));
    }

    public void deleteById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user by id: " + id, e);
        }
    }

    private Optional<User> findSingle(final String sql, final String parameter, final String errorMessage) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, parameter);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }

        return Optional.empty();
    }

    private boolean exists(final String sql, final String parameter, final String errorMessage) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, parameter);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    private void insert(final UUID id, final User user) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, getRoleName(user));
            preparedStatement.setBytes(5, user.getProfilePicture());
            preparedStatement.setString(6, user.getProfilePictureContentType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    private void update(final User user) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)
        ) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, getRoleName(user));
            preparedStatement.setBytes(4, user.getProfilePicture());
            preparedStatement.setString(5, user.getProfilePictureContentType());
            preparedStatement.setString(6, UuidUtil.toRawHex(user.getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user with id: " + user.getId(), e);
        }
    }

    private User mapUser(final ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(UuidUtil.fromRawHex(rs.getString("ID")));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setCreatedAt(ResultSetUtil.getLocalDate(rs, "CREATED_AT"));
        user.setUpdatedAt(ResultSetUtil.getLocalDate(rs, "UPDATED_AT"));

        String role = rs.getString("ROLE");
        user.setRole(role != null ? SystemRole.valueOf(role) : SystemRole.USER);

        user.setProfilePicture(rs.getBytes("PROFILE_PICTURE"));
        user.setProfilePictureContentType(rs.getString("PROFILE_PICTURE_CONTENT_TYPE"));
        return user;
    }

    private UserProjection toProjection(final User user) {
        return new UserProjection() {
            @Override
            public UUID getId() {
                return user.getId();
            }

            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public SystemRole getRole() {
                return user.getRole();
            }
        };
    }

    private String getRoleName(final User user) {
        return user.getRole() != null ? user.getRole().name() : SystemRole.USER.name();
    }
}
