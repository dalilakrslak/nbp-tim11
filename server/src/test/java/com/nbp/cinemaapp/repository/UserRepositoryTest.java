package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRepositoryTest {

    @Test
    void findByEmailReturnsUserWhenPresent() throws Exception {
        UserRepository repository = new UserRepository(mockUserFindDataSource(true));

        Optional<User> user = repository.findByEmail("user@example.com");

        assertTrue(user.isPresent());
        assertEquals("user@example.com", user.get().getEmail());
    }

    @Test
    void findProjectedByEmailReturnsProjectionWhenPresent() throws Exception {
        UserRepository repository = new UserRepository(mockUserFindDataSource(true));

        Optional<UserProjection> projection = repository.findProjectedByEmail("user@example.com");

        assertTrue(projection.isPresent());
        assertEquals("user@example.com", projection.get().getEmail());
    }

    @Test
    void existsByEmailReturnsTrueWhenUserExists() throws Exception {
        UserRepository repository = new UserRepository(mockExistsDataSource(true));

        assertTrue(repository.existsByEmail("user@example.com"));
    }

    @Test
    void existsByIdReturnsFalseWhenUserDoesNotExist() throws Exception {
        UserRepository repository = new UserRepository(mockExistsDataSource(false));

        assertTrue(!repository.existsById(UUID.randomUUID()));
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        UserRepository repository = new UserRepository(mockUserFindDataSource(false));

        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void findAllReturnsPageAndCount() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        ResultSet selectResultSet = mock(ResultSet.class);
        ResultSet countResultSet = mock(ResultSet.class);
        UserRepository repository = new UserRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(selectStatement, countStatement);
        when(selectStatement.executeQuery()).thenReturn(selectResultSet);
        when(countStatement.executeQuery()).thenReturn(countResultSet);
        when(selectResultSet.next()).thenReturn(true, false);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getLong(1)).thenReturn(1L);
        stubUserRow(selectResultSet);

        Page<User> page = repository.findAll(PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("user@example.com", page.getContent().get(0).getEmail());
    }

    @Test
    void saveInsertsWhenUserHasNoId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        UserRepository repository = new UserRepository(dataSource);
        User user = user(null);

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(insertStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubUserRow(resultSet);

        User saved = repository.save(user);

        assertEquals("user@example.com", saved.getEmail());
    }

    @Test
    void saveUpdatesWhenUserHasId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement updateStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        UserRepository repository = new UserRepository(dataSource);
        User user = user(UUID.randomUUID());

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(updateStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubUserRow(resultSet);

        User saved = repository.save(user);

        assertEquals(SystemRole.ADMIN, saved.getRole());
    }

    @Test
    void deleteByIdExecutesDelete() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        UserRepository repository = new UserRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteById(UUID.randomUUID());
    }

    private DataSource mockUserFindDataSource(final boolean found) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(found);
        if (found) {
            stubUserRow(resultSet);
        }
        return dataSource;
    }

    private DataSource mockExistsDataSource(final boolean exists) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(exists);
        return dataSource;
    }

    private void stubUserRow(final ResultSet resultSet) throws Exception {
        UUID userId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(userId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("EMAIL")).thenReturn("user@example.com");
        when(resultSet.getString("PASSWORD")).thenReturn("secret");
        when(resultSet.getString("ROLE")).thenReturn(SystemRole.ADMIN.name());
        when(resultSet.getBytes("PROFILE_PICTURE")).thenReturn(new byte[]{1});
        when(resultSet.getString("PROFILE_PICTURE_CONTENT_TYPE")).thenReturn("image/png");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
    }

    private User user(final UUID id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");
        user.setPassword("secret");
        user.setRole(SystemRole.ADMIN);
        user.setProfilePicture(new byte[]{1});
        user.setProfilePictureContentType("image/png");
        return user;
    }
}
