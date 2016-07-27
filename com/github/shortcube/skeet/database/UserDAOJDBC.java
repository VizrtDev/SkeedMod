package com.github.shortcube.skeet.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.shortcube.skeet.database.model.User;

/**
 * This class represents a concrete JDBC implementation of the {@link UserDAO} interface.
 *
 * @author Leon Merten
 */
public class UserDAOJDBC implements UserDAO {
    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, capeURL FROM User WHERE id = ?";
    private static final String SQL_LIST_ORDER_BY_ID =
            "SELECT id, name, capeURL FROM User ORDER BY id";
    private static final String SQL_INSERT =
            "INSERT INTO User (id, name, capeURL) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE User SET id = ?, name = ?, capeURL = ? WHERE id = ?";
    private static final String SQL_DELETE =
            "DELETE FROM User WHERE id = ?";

    private DAOFactory daoFactory;

    /**
     * Construct an User DAO for the given DAOFactory. Package private so that it can be constructed
     * inside the DAO package only.
     * @param daoFactory The DAOFactory to construct this User DAO for.
     */
    UserDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public User find( String id ) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    /**
     * Returns the user from the database matching the given SQL query with the given values.
     * @param sql The SQL query to be executed in the database.
     * @param values The PreparedStatement values to be set.
     * @return The user from the database matching the given SQL query with the given values.
     * @throws DAOException If something fails at database level.
     */
    private User find(String sql, Object... values) throws DAOException {
        User user = null;

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DAOUtil.prepareStatement(connection, sql, false, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                user = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return user;
    }

    @Override
    public List<User> list() throws DAOException {
        List<User> users = new ArrayList<>();

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_LIST_ORDER_BY_ID);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                users.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return users;
    }

    @Override
    public void create(User user) throws IllegalArgumentException, DAOException {
        if(exists( user.getId().toString() )) {
            throw new IllegalArgumentException( "User is already created yet," +
                    " the table contains the user with same the uuid" );
        }

        Object[] values = {
                user.getId().toString(),
                user.getName(),
                user.getCapeURL()
        };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DAOUtil.prepareStatement(connection, SQL_INSERT, true, values);
        ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    user.setId( UUID.fromString( generatedKeys.getString( "id" ) ) );
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        if (!exists( user.getId().toString() )) {
            throw new IllegalArgumentException("User is not created yet, the table does not contains the user.");
        }

        Object[] values = {
                user.getId().toString(),
                user.getName(),
                user.getCapeURL(),
                user.getId().toString()
        };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DAOUtil.prepareStatement(connection, SQL_UPDATE, false, values);
        ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(User user) throws DAOException {
        Object[] values = {
                user.getId()
        };

        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DAOUtil.prepareStatement(connection, SQL_DELETE, false, values);
        ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting user failed, no rows affected.");
            } else {
                user.setId(null);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Map the current row of the given ResultSet to an User.
     * @param resultSet The ResultSet of which the current row is to be mapped to an User.
     * @return The mapped User from the current row of the given ResultSet.
     * @throws SQLException If something fails at database level.
     */
    private static User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId( UUID.fromString( resultSet.getString("id") ) );
        user.setName(resultSet.getString("name"));
        user.setCapeURL( resultSet.getString( "capeURL" ) );
        return user;
    }

    public boolean exists(String id) {
        if(find( id ) == null)
            return false;
        else
            return true;
    }
}
