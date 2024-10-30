package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerException;

import java.util.ArrayList;

import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlDataAccess implements DataAccess {

    public SqlDataAccess() throws ServerException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM UserData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    } else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public void saveUser(UserData userData) throws DataAccessException{
        var statement = "INSERT INTO UserData (username,password,email) VALUES (?,?,?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void saveAuth(AuthData authData) {

    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void saveGame(GameData updatedGame) {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        executeUpdate(statement);
        statement = "TRUNCATE AuthData";
        executeUpdate(statement);
        statement = "TRUNCATE GameData";
        executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username,password,email);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else if (param instanceof Object o) {
                        ps.setString(i + 1, o.toString());
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  GameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
