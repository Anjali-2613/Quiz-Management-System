import java.sql.Timestamp;

public class QuizAttempt {
    private int userId;
    private int quizId;
    private double score;
    private Timestamp attemptDate;

    public QuizAttempt(int userId, int quizId, double score, Timestamp attemptDate) {
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.attemptDate = attemptDate;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public int getQuizId() { return quizId; }
    public double getScore() { return score; }
    public Timestamp getAttemptDate() { return attemptDate; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public void setScore(double score) { this.score = score; }
    public void setAttemptDate(Timestamp attemptDate) { this.attemptDate = attemptDate; }
}
