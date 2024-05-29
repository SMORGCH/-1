package zametli;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных
 * @author Гоенко Игорь
 * @version 1.0
 */
public class DBNotes {
    /**
     * <p> URL для подключения к БД</p>
     */
    private static final String URL = "jdbc:mysql://localhost:3306/notes_db";
    /**
     * <p>Пользователь БД</p>
     */
    private static final String USER = "root";
    /**
     * <p>Пароль для подключения к БД</p>
     */
    private static final String PASSWORD = "123456";

    /**
     * <p>Метод для подключения к БД</p>
     */
    public static void connectDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("База данных подключена.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * <p>Метод для создания таблицы в БД, если она не существует</p>
     */
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notes (\n"
                + " id INT AUTO_INCREMENT PRIMARY KEY,\n"
                + " content TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Не удалось создать новую таблицу Заметки");
        }
    }

    /**
     * <p>Метод для создания первой заметки в БД</p>
     */
    public static void initFirstNote() {
        String sql = "INSERT INTO notes (content) SELECT 'Это ваша первая заметка!' "
                + "WHERE NOT EXISTS (SELECT 1 FROM notes);";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * <p>Метод для создания новой заметки</p>
     */
    public static void createNote(String content) {
        String sql = "INSERT INTO notes(content) VALUES(?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.executeUpdate();
            System.out.println("Заметка создана.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Заметка не была создана.");

        }
    }

    /**
     * <p>Метод для получения всех заметок из БД</p>
     */
    public static List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String text = rs.getString("content");
                notes.add(new Note(id,text));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return notes;
    }

    /**
     * <p>Метод для редактирования заметки</p>
     */
    public static void editNote(int id, String content) {
        getAllNotes();
        String sql = "UPDATE notes SET content = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Заметка обновлена.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * <p>Метод для удаления заметки</p>
     */
    public static void deleteNote(int id) {
        getAllNotes();
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Заметка удалена.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
