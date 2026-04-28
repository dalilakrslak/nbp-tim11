package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.RefreshToken;
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
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(RT.ID) AS ID,
               RT.CREATED_AT,
               RT.EXPIRES_AT,
               RAWTOHEX(U.ID) AS USER_ID,
               U.EMAIL,
               U.PASSWORD,
               U.CREATED_AT AS USER_CREATED_AT,
               U.UPDATED_AT AS USER_UPDATED_AT,
               U.ROLE,
               U.PROFILE_PICTURE,
               U.PROFILE_PICTURE_CONTENT_TYPE
        FROM REFRESH_TOKENS RT
        JOIN USERS U ON RT.USER_ID = U.ID
        """;

    private static final String FIND_BY_ID_SQL = BASE_SELECT + """
        WHERE RAWTOHEX(RT.ID) = ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO REFRESH_TOKENS (
            ID,
            USER_ID,
            CREATED_AT,
            EXPIRES_AT
        )
        VALUES (
            HEXTORAW(?),
            HEXTORAW(?),
            CURRENT_TIMESTAMP,
            ?
        )
        """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM REFRESH_TOKENS
        WHERE RAWTOHEX(ID) = ?
        """;

    private final DataSource dataSource;

    public RefreshTokenRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RefreshToken save(final RefreshToken refreshToken) {
        UUID id = refreshToken.getId() != null ? refreshToken.getId() : UUID.randomUUID();

        insert(id, refreshToken);

        return findById(id)
                .orElseThrow(() -> new IllegalStateException("Refresh token was inserted but could not be retrieved"));
    }

    public Optional<RefreshToken> findById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRefreshToken(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch refresh token by id: " + id, e);
        }

        return Optional.empty();
    }

    public void deleteById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete refresh token by id: " + id, e);
        }
    }

    private void insert(final UUID id, final RefreshToken refreshToken) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setString(2, UuidUtil.toRawHex(refreshToken.getUser().getId()));
            preparedStatement.setTimestamp(3, Timestamp.from(refreshToken.getExpiresAt()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert refresh token", e);
        }
    }

    private RefreshToken mapRefreshToken(final ResultSet rs) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UuidUtil.fromRawHex(rs.getString("ID")));
        refreshToken.setCreatedAt(getInstant(rs, "CREATED_AT"));
        refreshToken.setExpiresAt(getInstant(rs, "EXPIRES_AT"));
        refreshToken.setUser(mapUser(rs));
        return refreshToken;
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

    private Instant getInstant(final ResultSet rs, final String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp != null ? timestamp.toInstant() : null;
    }
}
