package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.ResetToken;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResetTokenRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(RT.ID) AS ID,
               RT.TOKEN,
               RT.EXPIRES_AT,
               RAWTOHEX(U.ID) AS USER_ID,
               U.EMAIL,
               U.PASSWORD,
               U.CREATED_AT AS USER_CREATED_AT,
               U.UPDATED_AT AS USER_UPDATED_AT,
               U.ROLE,
               U.PROFILE_PICTURE,
               U.PROFILE_PICTURE_CONTENT_TYPE
        FROM RESET_TOKEN RT
        JOIN USERS U ON RT.USER_ID = U.ID
        """;

    private static final String FIND_BY_TOKEN_SQL = BASE_SELECT + """
        WHERE RT.TOKEN = ?
        """;

    private static final String FIND_BY_ID_SQL = BASE_SELECT + """
        WHERE RAWTOHEX(RT.ID) = ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO RESET_TOKEN (
            ID,
            TOKEN,
            EXPIRES_AT,
            USER_ID
        )
        VALUES (
            HEXTORAW(?),
            ?,
            ?,
            HEXTORAW(?)
        )
        """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM RESET_TOKEN
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String DELETE_ALL_EXPIRED_SQL = """
        DELETE FROM RESET_TOKEN
        WHERE EXPIRES_AT < ?
        """;

    private final DataSource dataSource;

    public ResetTokenRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResetToken save(final ResetToken resetToken) {
        UUID id = resetToken.getId() != null ? resetToken.getId() : UUID.randomUUID();
        insert(id, resetToken);

        return findById(id)
                .orElseThrow(() -> new IllegalStateException("Reset token was inserted but could not be retrieved"));
    }

    public Optional<ResetToken> findById(final UUID id) {
        return findSingle(FIND_BY_ID_SQL, UuidUtil.toRawHex(id), "Failed to fetch reset token by id: " + id);
    }

    public Optional<ResetToken> findByToken(final String token) {
        return findSingle(FIND_BY_TOKEN_SQL, token, "Failed to fetch reset token by token");
    }

    public void delete(final ResetToken resetToken) {
        if (resetToken != null && resetToken.getId() != null) {
            deleteById(resetToken.getId());
        }
    }

    public void deleteById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete reset token by id: " + id, e);
        }
    }

    public void deleteAllByExpiresAtBefore(final LocalDateTime time) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_EXPIRED_SQL)
        ) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(time));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete expired reset tokens before: " + time, e);
        }
    }

    private Optional<ResetToken> findSingle(final String sql, final String parameter, final String errorMessage) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, parameter);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResetToken(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }

        return Optional.empty();
    }

    private void insert(final UUID id, final ResetToken resetToken) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setString(2, resetToken.getToken());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(resetToken.getExpiresAt()));
            preparedStatement.setString(4, UuidUtil.toRawHex(resetToken.getUser().getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert reset token", e);
        }
    }

    private ResetToken mapResetToken(final ResultSet rs) throws SQLException {
        ResetToken resetToken = new ResetToken();
        resetToken.setId(UuidUtil.fromRawHex(rs.getString("ID")));
        resetToken.setToken(rs.getString("TOKEN"));
        resetToken.setExpiresAt(ResultSetUtil.getLocalDateTime(rs, "EXPIRES_AT"));
        resetToken.setUser(mapUser(rs));
        return resetToken;
    }

    private User mapUser(final ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(UuidUtil.fromRawHex(rs.getString("USER_ID")));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setCreatedAt(ResultSetUtil.getLocalDate(rs, "USER_CREATED_AT"));
        user.setUpdatedAt(ResultSetUtil.getLocalDate(rs, "USER_UPDATED_AT"));

        String role = rs.getString("ROLE");
        user.setRole(role != null ? SystemRole.valueOf(role) : SystemRole.USER);

        user.setProfilePicture(rs.getBytes("PROFILE_PICTURE"));
        user.setProfilePictureContentType(rs.getString("PROFILE_PICTURE_CONTENT_TYPE"));
        return user;
    }
}
