import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class QuizPanel extends JPanel {
    private User user;
    private JComboBox<String> quizComboBox;
    private JTextArea questionArea;
    private JRadioButton optionA;
    private JRadioButton optionB;
    private JRadioButton optionC;
    private JRadioButton optionD;
    private JButton nextButton;
    private ButtonGroup optionsGroup;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int[] selectedOptions;
    private String[] correctOptions;

    public QuizPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        quizComboBox = new JComboBox<>();
        loadQuizzes();
        topPanel.add(quizComboBox);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        questionArea = new JTextArea(10, 40);
        questionArea.setEditable(false);
        centerPanel.add(new JScrollPane(questionArea), BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);

        centerPanel.add(optionsPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        nextButton = new JButton("Next");
        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestionIndex < questions.size()) {
                    saveSelectedOption();
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questions.size()) {
                        displayQuestion();
                    } else {
                        showResults();
                    }
                }
            }
        });

        quizComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadQuestions();
            }
        });
    }

    private void loadQuizzes() {
        // Load quizzes from database and add to quizComboBox
    }

    private void loadQuestions() {
        try {
            int quizId = quizComboBox.getSelectedIndex() + 1;
            questions = DatabaseConnection.retrieveQuestions(quizId);
            currentQuestionIndex = 0;
            selectedOptions = new int[questions.size()];
            correctOptions = new String[questions.size()];
            displayQuestion();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        questionArea.setText(question.getQuestionText());
        optionA.setText(question.getOptionA());
        optionB.setText(question.getOptionB());
        optionC.setText(question.getOptionC());
        optionD.setText(question.getOptionD());

        correctOptions[currentQuestionIndex] = question.getCorrectOption();
        optionsGroup.clearSelection();
    }

    private void saveSelectedOption() {
        if (optionA.isSelected()) {
            selectedOptions[currentQuestionIndex] = 0;
        } else if (optionB.isSelected()) {
            selectedOptions[currentQuestionIndex] = 1;
        } else if (optionC.isSelected()) {
            selectedOptions[currentQuestionIndex] = 2;
        } else if (optionD.isSelected()) {
            selectedOptions[currentQuestionIndex] = 3;
        }
    }

    private void showResults() {
        int correctCount = 0;
        for (int i = 0; i < correctOptions.length; i++) {
            if (correctOptions[i].charAt(0) - 'A' == selectedOptions[i]) {
                correctCount++;
            }
        }
        double score = ((double) correctCount / correctOptions.length) * 100;

        JOptionPane.showMessageDialog(this, "Quiz completed!\nYour score: " + score + "%", "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
        try {
            DatabaseConnection.saveQuizAttempt(user.getId(), quizComboBox.getSelectedIndex() + 1, score);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
