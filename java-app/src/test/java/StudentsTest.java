import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentsTest {

    static Connection conn;

    @BeforeAll
    static void open() throws SQLException {
        conn = Students.connect();
    }

    @AfterAll
    static void close() throws SQLException {
        conn.close();
    }

    @BeforeEach
    void clean() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM students");
        }
    }

    @Test
    void ajouterEtRelire() throws SQLException {
        Students.addStudent(conn, "Amel", 15);
        Optional<Students.Student> etudiant = Students.getStudent(conn, "Amel");
        assertTrue(etudiant.isPresent());
        assertEquals("Amel", etudiant.get().name());
        assertEquals(15.0, etudiant.get().grade(), 0.001);
    }

    @Test
    void etudiantInexistant() throws SQLException {
        assertEquals(Optional.empty(), Students.getStudent(conn, "Personne"));
    }

    @Test
    void moyenne() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);
        assertEquals(12.0, Students.averageGrade(conn).get(), 0.001);
    }
}
