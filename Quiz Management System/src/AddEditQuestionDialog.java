import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddEditQuestionDialog extends JDialog {
    private JTextField questionField;
    private JTextField optionAField;
    private JTextField optionBField;
    private JTextField optionCField;
    private JTextField optionDField;
    private JTextField correctOptionField;
    private JButton saveButton;
    private JButton cancelButton;
    private Question question;

    public AddEditQuestionDialog(Question question) {
        this.question = question;
        setTitle(question == null ? "Add Question" : "Edit Question");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Question:"), gbc);

        questionField = new JTextField(20);
        gbc.gridx = 1;
        add(questionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Option A:"), gbc);

        optionAField = new JTextField(20);
        gbc.gridx = 1;
        add(optionAField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Option B:"), gbc);

        optionBField = new JTextField(20);
        gbc.gridx = 1;
        add(optionBField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Option C:"), gbc);

        optionCField = new JTextField(20);
        gbc.gridx = 1;
        add(optionCField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Option D:"), gbc);

        optionDField = new JTextField(20);
        gbc.gridx = 1;
        add(optionDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Correct Option:"), gbc);

        correctOptionField = new JTextField(20);
        gbc.gridx = 1;
        add(correctOptionField, gbc);

        saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(saveButton, gbc);

        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        add(cancelButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuestion();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (question != null) {
            loadQuestionData();
        }

        pack();
        setLocationRelativeTo(null);
    }

    private void loadQuestionData() {
        questionField.setText(question.getQuestionText());
        optionAField.setText(question.getOptionA());
        optionBField.setText(question.getOptionB());
        optionCField.setText(question.getOptionC());
        optionDField.setText(question.getOptionD());
        correctOptionField.setText(question.getCorrectOption());
    }

    private void saveQuestion() {
        String questionText = questionField.getText();
        String optionA = optionAField.getText();
        String optionB = optionBField.getText();
        String optionC = optionCField.getText();
        String optionD = optionDField.getText();
        String correctOption = correctOptionField.getText();

        try {
            if (question == null) {
                DatabaseConnection.saveQuestion(questionText, optionA, optionB, optionC, optionD, correctOption, 1); // Assume quizId is 1 for now
            } else {
                DatabaseConnection.updateQuestion(question.getId(), questionText, optionA, optionB, optionC, optionD, correctOption);
            }
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(AddEditQuestionDialog.this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
