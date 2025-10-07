package org.outer;

import org.data.inner.*;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseManager {

    private static final String url = "jdbc:postgresql://localhost:5433/studs";
    private static final String user = "s465527";
    private static final String password = "gYobdNKJPaDxgOiE";

    private static final List<Movie> movieList = Collections.synchronizedList(new ArrayList<>());

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Регистрация пользователя
    public static boolean registerUser(String login, String pwd) throws SQLException, NoSuchAlgorithmException {
        String hashed = hashPassword(pwd);
        String sql = "INSERT INTO users(login, password_hash) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, hashed);
            return ps.executeUpdate() > 0;
        }
    }

    // Авторизация пользователя
    public static boolean authenticateUser(String login, String pwd) throws SQLException, NoSuchAlgorithmException {
        String hashed = hashPassword(pwd);
        String sql = "SELECT password_hash FROM users WHERE login=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return hashed.equals(rs.getString("password_hash"));
            }
            return false;
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // Загрузка всех фильмов в память
    public static void loadAllMovies() throws SQLException {
        List<Movie> temp = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT m.*, p.name AS pname, p.passport_id, p.eye_color, p.nationality, p.loc_x, p.loc_y, p.loc_name, m.owner_login " +
                         "FROM movies m LEFT JOIN persons p ON m.operator_id = p.id";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Location loc = new Location(
                            rs.getObject("loc_x") != null ? rs.getFloat("loc_x") : null,
                            rs.getObject("loc_y") != null ? rs.getDouble("loc_y") : null,
                            rs.getString("loc_name")
                    );
                    Person person = new Person(
                            rs.getString("pname"),
                            rs.getString("passport_id"),
                            rs.getString("eye_color") != null ? Color.valueOf(rs.getString("eye_color")) : null,
                            Country.valueOf(rs.getString("nationality")),
                            loc
                    );
                    Movie movie = new Movie(
                            rs.getLong("id"),
                            rs.getString("name"),
                            new Coordinates(rs.getFloat("coord_x"), rs.getLong("coord_y")),
                            rs.getTimestamp("creation_date").toLocalDateTime(),
                            rs.getLong("oscars_count"),
                            rs.getFloat("budget"),
                            rs.getDouble("usa_box_office"),
                            MpaaRating.valueOf(rs.getString("mpaa_rating")),
                            person
                    );
                    temp.add(movie);
                }
            }
        }
        movieList.clear();
        movieList.addAll(temp);
    }

    public static List<Movie> getMovieList() {
        return movieList;
    }

    // Добавление фильма в БД + коллекцию
    public static synchronized Movie addMovie(Movie movie, String login) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Сохраняем Person
                String sqlPerson = "INSERT INTO persons(name, passport_id, eye_color, nationality, loc_x, loc_y, loc_name) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
                long personId;
                try (PreparedStatement ps = conn.prepareStatement(sqlPerson)) {
                    ps.setString(1, movie.getOperator().getName());
                    ps.setString(2, movie.getOperator().getPassportID());
                    ps.setString(3, movie.getOperator().getEyeColor() != null ? movie.getOperator().getEyeColor().toString() : null);
                    ps.setString(4, movie.getOperator().getNationality().toString());
                    ps.setObject(5, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getX() : null);
                    ps.setObject(6, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getY() : null);
                    ps.setObject(7, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getName() : null);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    personId = rs.getLong(1);
                }

                // 2. Сохраняем Movie
                String sqlMovie = "INSERT INTO movies(name, coord_x, coord_y, oscars_count, budget, usa_box_office, mpaa_rating, operator_id, owner_login) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, creation_date";
                try (PreparedStatement ps = conn.prepareStatement(sqlMovie)) {
                    ps.setString(1, movie.getName());
                    ps.setFloat(2, movie.getCoordinates().getX());
                    ps.setLong(3, movie.getCoordinates().getY());
                    ps.setLong(4, movie.getOscarsCount());
                    ps.setFloat(5, movie.getBudget());
                    ps.setDouble(6, movie.getUsaBoxOffice());
                    ps.setString(7, movie.getMpaaRating().toString());
                    ps.setLong(8, personId);
                    ps.setString(9, login);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    movie.setId(rs.getLong("id"));
                    movie.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                }

                conn.commit();
                movieList.add(movie);
                return movie;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
