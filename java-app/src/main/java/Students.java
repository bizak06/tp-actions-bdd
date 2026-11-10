import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Students {

    public record Student(int id, String name, double grade) {}

    private static final String URL = "jdbc:postgresql://localhost:5432/tp";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static int addStudent(Connection conn, String name, double grade) throws SQLException {
        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, grade);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public static Optional<Student> getStudent(Connection conn, String name) throws SQLException {
        String sql = "SELECT id, name, grade FROM students WHERE name = ? ORDER BY id LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("grade")));
            }
        }
    }

    public static Optional<Double> averageGrade(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT AVG(grade) FROM students")) {
            if (rs.next()) {
                double avg = rs.getDouble(1);
                if (rs.wasNull()) {
                    return Optional.empty();
                }
                return Optional.of(avg);
            }
            return Optional.empty();
        }
    }
}
