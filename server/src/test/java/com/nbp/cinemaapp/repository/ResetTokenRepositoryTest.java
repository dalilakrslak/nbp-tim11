package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.ResetToken;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ResetTokenRepositoryTest {

    @Test
    void saveInsertsAndReloadsResetToken() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        ResetTokenRepository repository = new ResetTokenRepository(dataSource);
        ResetToken resetToken = resetToken(null);

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(insertStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubResetTokenRow(resultSet);

        ResetToken saved = repository.save(resetToken);

        assertEquals("token-value", saved.getToken());
    }

    @Test
    void findByIdReturnsResetTokenWhenPresent() throws Exception {
        ResetTokenRepository repository = new ResetTokenRepository(mockFindDataSource(true));

        Optional<ResetToken> resetToken = repository.findById(UUID.randomUUID());

        assertTrue(resetToken.isPresent());
    }

    @Test
    void findByTokenReturnsResetTokenWhenPresent() throws Exception {
        ResetTokenRepository repository = new ResetTokenRepository(mockFindDataSource(true));

        Optional<ResetToken> resetToken = repository.findByToken("token-value");

        assertTrue(resetToken.isPresent());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        ResetTokenRepository repository = new ResetTokenRepository(mockFindDataSource(false));

        Optional<ResetToken> resetToken = repository.findById(UUID.randomUUID());

        assertTrue(resetToken.isEmpty());
    }

    @Test
    void deleteDoesNothingWhenEntityHasNoId() {
        DataSource dataSource = mock(DataSource.class);
        ResetTokenRepository repository = new ResetTokenRepository(dataSource);

        repository.delete(new ResetToken());

        verifyNoInteractions(dataSource);
    }

    @Test
    void deleteByIdExecutesDelete() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResetTokenRepository repository = new ResetTokenRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteById(UUID.randomUUID());
    }

    @Test
    void deleteAllByExpiresAtBeforeExecutesDelete() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResetTokenRepository repository = new ResetTokenRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteAllByExpiresAtBefore(LocalDateTime.of(2026, 1, 10, 12, 0));
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
            stubResetTokenRow(resultSet);
        }
        return dataSource;
    }

    private void stubResetTokenRow(final ResultSet resultSet) throws Exception {
        UUID resetTokenId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(resetTokenId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("TOKEN")).thenReturn("token-value");
        when(resultSet.getString("USER_ID")).thenReturn(userId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("EMAIL")).thenReturn("user@example.com");
        when(resultSet.getString("PASSWORD")).thenReturn("secret");
        when(resultSet.getString("ROLE")).thenReturn(SystemRole.USER.name());
        when(resultSet.getBytes("PROFILE_PICTURE")).thenReturn(new byte[]{1});
        when(resultSet.getString("PROFILE_PICTURE_CONTENT_TYPE")).thenReturn("image/png");
        when(resultSet.getTimestamp("EXPIRES_AT")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2026, 1, 10, 12, 0)));
        when(resultSet.getTimestamp("USER_CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("USER_UPDATED_AT")).thenReturn(timestamp);
    }

    private ResetToken resetToken(final UUID id) {
        User user = new User();
        user.setId(UUID.randomUUID());
        ResetToken resetToken = new ResetToken();
        resetToken.setId(id);
        resetToken.setToken("token-value");
        resetToken.setExpiresAt(LocalDateTime.of(2026, 1, 10, 12, 0));
        resetToken.setUser(user);
        return resetToken;
    }
}
