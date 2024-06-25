import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/quiz_database";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Save a new question to the database
    public static void saveQuestion(String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption, int quizId) throws SQLException {
        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, quiz_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            stmt.setString(2, optionA);
            stmt.setString(3, optionB);
            stmt.setString(4, optionC);
            stmt.setString(5, optionD);
            stmt.setString(6, correctOption);
            stmt.setInt(7, quizId);
            stmt.executeUpdate();
        }
    }

    // Retrieve all questions for a given quiz
    public static List<Question> retrieveQuestions(int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT id, question_text, option_a, option_b, option_c, option_d, correct_option FROM questions WHERE quiz_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quizId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String questionText = rs.getString("question_text");
                    String optionA = rs.getString("option_a");
                    String optionB = rs.getString("option_b");
                    String optionC = rs.getString("option_c");
                    String optionD = rs.getString("option_d");
                    String correctOption = rs.getString("correct_option");
                    questions.add(new Question(id, quizId, questionText, optionA, optionB, optionC, optionD, correctOption));
                }
            }
        }
        return questions;
    }

    // Update an existing question
    public static void updateQuestion(int questionId, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) throws SQLException {
        String sql = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            stmt.setString(2, optionA);
            stmt.setString(3, optionB);
            stmt.setString(4, optionC);
            stmt.setString(5, optionD);
            stmt.setString(6, correctOption);
            stmt.setInt(7, questionId);
            stmt.executeUpdate();
        }
    }

    // Delete a question by ID
    public static void deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            stmt.executeUpdate();
        }
    }

    // Save user quiz attempt
    public static void saveQuizAttempt(int userId, int quizId, double score) throws SQLException {
        String sql = "INSERT INTO quiz_attempts (user_id, quiz_id, score) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, quizId);
            stmt.setDouble(3, score);
            stmt.executeUpdate();
        }
    }

    // Retrieve user quiz attempts
    public static List<QuizAttempt> retrieveQuizAttempts(int userId) throws SQLException {
        List<QuizAttempt> attempts = new ArrayList<>();
        String sql = "SELECT quiz_id, score, attempt_date FROM quiz_attempts WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quizId = rs.getInt("quiz_id");
                    double score = rs.getDouble("score");
                    Timestamp attemptDate = rs.getTimestamp("attempt_date");
                    attempts.add(new QuizAttempt(userId, quizId, score, attemptDate));
                }
            }
        }
        return attempts;
    }
    // Method to register a new user
    public static boolean registerUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Method to check if a username already exists
    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
}
