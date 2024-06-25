import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminPanel extends JPanel {
    private User user;
    private JButton addQuestionButton;
    private JButton editQuestionButton;
    private JButton deleteQuestionButton;

    public AdminPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + user.getUsername());
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));

        addQuestionButton = new JButton("Add Question");
        editQuestionButton = new JButton("Edit Question");
        deleteQuestionButton = new JButton("Delete Question");

        centerPanel.add(addQuestionButton);
        centerPanel.add(editQuestionButton);
        centerPanel.add(deleteQuestionButton);

        add(centerPanel, BorderLayout.CENTER);

        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddEditDialog(null);
            }
        });

        editQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic to open the edit question dialog
            }
        });

        deleteQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic to delete a question
            }
        });
    }

    private void openAddEditDialog(Question question) {
        AddEditQuestionDialog dialog = new AddEditQuestionDialog(question);
        dialog.setVisible(true);
    }
}
