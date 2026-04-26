package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.RefreshToken;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefreshTokenRepositoryTest {

    @Test
    void saveInsertsAndReloadsRefreshToken() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        RefreshTokenRepository repository = new RefreshTokenRepository(dataSource);
        RefreshToken refreshToken = refreshToken(null);

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(insertStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubRefreshTokenRow(resultSet);

        RefreshToken saved = repository.save(refreshToken);

        assertEquals("user@example.com", saved.getUser().getEmail());
    }

    @Test
    void findByIdReturnsRefreshTokenWhenPresent() throws Exception {
        RefreshTokenRepository repository = new RefreshTokenRepository(mockFindDataSource(true));

        Optional<RefreshToken> refreshToken = repository.findById(UUID.randomUUID());

        assertTrue(refreshToken.isPresent());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        RefreshTokenRepository repository = new RefreshTokenRepository(mockFindDataSource(false));

        Optional<RefreshToken> refreshToken = repository.findById(UUID.randomUUID());

        assertTrue(refreshToken.isEmpty());
    }

    @Test
    void deleteByIdExecutesDelete() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        RefreshTokenRepository repository = new RefreshTokenRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteById(UUID.randomUUID());
    }

    private DataSource mockFindDataSource(final boolean found) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(found);
        if (found) {
            stubRefreshTokenRow(resultSet);
        }
        return dataSource;
    }

    private void stubRefreshTokenRow(final ResultSet resultSet) throws Exception {
        UUID refreshTokenId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Timestamp createdAt = Timestamp.from(Instant.parse("2026-01-10T10:00:00Z"));
        Timestamp expiresAt = Timestamp.from(Instant.parse("2026-01-11T10:00:00Z"));
        Timestamp localDateTimestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(refreshTokenId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("USER_ID")).thenReturn(userId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("EMAIL")).thenReturn("user@example.com");
        when(resultSet.getString("PASSWORD")).thenReturn("secret");
        when(resultSet.getString("ROLE")).thenReturn(SystemRole.ADMIN.name());
        when(resultSet.getBytes("PROFILE_PICTURE")).thenReturn(new byte[]{1});
        when(resultSet.getString("PROFILE_PICTURE_CONTENT_TYPE")).thenReturn("image/png");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(createdAt);
        when(resultSet.getTimestamp("EXPIRES_AT")).thenReturn(expiresAt);
        when(resultSet.getTimestamp("USER_CREATED_AT")).thenReturn(localDateTimestamp);
        when(resultSet.getTimestamp("USER_UPDATED_AT")).thenReturn(localDateTimestamp);
    }

    private RefreshToken refreshToken(final UUID id) {
        User user = new User();
        user.setId(UUID.randomUUID());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.parse("2026-01-11T10:00:00Z"));
        return refreshToken;
    }
}
