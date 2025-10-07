package org.inner;
import org.data.inner.Coordinates;
import org.data.inner.Location;
import org.data.inner.Movie;
import org.data.inner.Person;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieRepository {

    private static final String URL = "jdbc:postgresql://localhost:5433/studs";
    private static final String USER = "s465527";
    private static final String PASSWORD = "gYobdNKJPaDxgOiE";

    /** Загружаем коллекцию из базы */
    public static List<Movie> loadAll() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT m.*, p.name AS pname, p.passport_id, p.eye_color, p.nationality, p.loc_x, p.loc_y, p.loc_name " +
                     "FROM movies m LEFT JOIN persons p ON m.operator_id = p.id ORDER BY m.name";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRowToMovie(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Сохраняем всю коллекцию в базу при завершении работы сервера */
    public static void saveAll(List<Movie> collection) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            // 1. Очистить таблицу фильмов
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM movies");
            }

            for (Movie movie : collection) {
                Long personId = null;
                if (movie.getOperator() != null) {
                    // Проверяем, есть ли паспорт
                    String checkSql = "SELECT id FROM persons WHERE passport_id = ?";
                    try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                        psCheck.setString(1, movie.getOperator().getPassportID());
                        try (ResultSet rs = psCheck.executeQuery()) {
                            if (rs.next()) personId = rs.getLong("id");
                        }
                    }

                    // Если нет — вставляем
                    if (personId == null) {
                        String sqlPerson = "INSERT INTO persons(name, passport_id, eye_color, nationality, loc_x, loc_y, loc_name) " +
                                           "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
                        try (PreparedStatement ps = conn.prepareStatement(sqlPerson)) {
                            ps.setString(1, movie.getOperator().getName());
                            ps.setString(2, movie.getOperator().getPassportID());
                            ps.setString(3, movie.getOperator().getEyeColor() != null ? movie.getOperator().getEyeColor().toString() : null);
                            ps.setString(4, movie.getOperator().getNationality().toString());
                            ps.setObject(5, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getX() : null);
                            ps.setObject(6, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getY() : null);
                            ps.setObject(7, movie.getOperator().getLocation() != null ? movie.getOperator().getLocation().getName() : null);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) personId = rs.getLong(1);
                            }
                        }
                    }
                }

                // Вставка фильма
                String sqlMovie = "INSERT INTO movies(name, coord_x, coord_y, oscars_count, budget, usa_box_office, mpaa_rating, operator_id, owner_login) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlMovie)) {
                    ps.setString(1, movie.getName());
                    ps.setFloat(2, movie.getCoordinates().getX());
                    ps.setLong(3, movie.getCoordinates().getY());
                    ps.setLong(4, movie.getOscarsCount());
                    ps.setFloat(5, movie.getBudget());
                    ps.setDouble(6, movie.getUsaBoxOffice());
                    ps.setString(7, movie.getMpaaRating().name());
                    if (personId != null) ps.setLong(8, personId); else ps.setNull(8, Types.BIGINT);
                    ps.setString(9, movie.getUserLogin());
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Movie mapRowToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getLong("id"));
        movie.setName(rs.getString("name"));
        movie.setCoordinates(new Coordinates(rs.getFloat("coord_x"), rs.getLong("coord_y")));
        movie.setOscarsCount(rs.getLong("oscars_count"));
        movie.setBudget(rs.getFloat("budget"));
        movie.setUsaBoxOffice(rs.getDouble("usa_box_office"));
        movie.setMpaaRating(MpaaRating.valueOf(rs.getString("mpaa_rating")));

        Person person = null;
        if (rs.getString("pname") != null) {
            Location loc = new Location(
                    rs.getObject("loc_x") != null ? rs.getFloat("loc_x") : null,
                    rs.getObject("loc_y") != null ? rs.getDouble("loc_y") : null,
                    rs.getString("loc_name")
            );
            person = new Person(
                    rs.getString("pname"),
                    rs.getString("passport_id"),
                    rs.getString("eye_color") != null ? Color.valueOf(rs.getString("eye_color")) : null,
                    Country.valueOf(rs.getString("nationality")),
                    loc
            );
        }
        movie.setOperator(person);

        return movie;
    }
    /** Добавить новый фильм в коллекцию (только в памяти) */
    public static void add(List<Movie> collection, Movie movie, String ownerLogin) {
        movie.setUserLogin(ownerLogin); // помечаем владельца
        collection.add(movie);
    }

    /** Обновить существующий фильм по ID, если владелец совпадает */
    public static boolean update(List<Movie> collection, Movie updatedMovie, String ownerLogin) {
        for (Movie movie : collection) {
            if (movie.getId() == updatedMovie.getId() && ownerLogin.equals(movie.getUserLogin())) {
                movie.setName(updatedMovie.getName());
                movie.setCoordinates(updatedMovie.getCoordinates());
                movie.setOscarsCount(updatedMovie.getOscarsCount());
                movie.setBudget(updatedMovie.getBudget());
                movie.setUsaBoxOffice(updatedMovie.getUsaBoxOffice());
                movie.setMpaaRating(updatedMovie.getMpaaRating());
                movie.setOperator(updatedMovie.getOperator());
                return true;
            }
        }
        return false; // не найден фильм с таким ID и владельцем
    }

    /** Очистить все фильмы конкретного пользователя из коллекции */
    public static void clearByOwner(List<Movie> collection, String ownerLogin) {
        Iterator<Movie> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Movie movie = iterator.next();
            if (ownerLogin.equals(movie.getUserLogin())) {
                iterator.remove();
            }
        }
    }
}
