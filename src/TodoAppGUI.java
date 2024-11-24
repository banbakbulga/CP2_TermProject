import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Task {
    private String title;
    private String description;
    private String deadline;
    private String progress;

    public Task(String title, String description, String deadline, String progress) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}

public class TodoAppGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ArrayList<Task> tasks;
    private DefaultListModel<String> listModel;

    public TodoAppGUI() {
        FlatLightLaf.setup();

        setTitle("ToDo List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.put("defaultFont", new Font("Malgun Gothic", Font.PLAIN, 14));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();

        mainPanel.add(createHomeScreen(), "Home");
        mainPanel.add(createAddScreen(), "AddTask");
        mainPanel.add(createViewScreen(), "ViewTasks");

        add(mainPanel);
        cardLayout.show(mainPanel, "Home");
        setVisible(true);
    }

    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("할 일 목록", JLabel.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 48));
        titleLabel.setForeground(new Color(33, 150, 243));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("추가");
        JButton viewButton = createStyledButton("조회");

        addButton.addActionListener(e -> cardLayout.show(mainPanel, "AddTask"));
        viewButton.addActionListener(e -> cardLayout.show(mainPanel, "ViewTasks"));

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JLabel addLabel = new JLabel("할 일 추가", JLabel.CENTER);
        addLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 36));
        addLabel.setForeground(new Color(33, 150, 243));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField deadlineField = new JTextField();
        JComboBox<String> progressCombo = new JComboBox<>(new String[]{"시작 안함", "진행 중"});

        inputPanel.add(new JLabel("제목:", JLabel.RIGHT));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("내용:", JLabel.RIGHT));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("기한:", JLabel.RIGHT));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("진행도:", JLabel.RIGHT));
        inputPanel.add(progressCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton addTaskButton = createStyledButton("추가하기");
        JButton backButton = createStyledButton("뒤로가기");

        addTaskButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String deadline = deadlineField.getText();
            String progress = (String) progressCombo.getSelectedItem();

            if (!title.isEmpty() && !description.isEmpty() && !deadline.isEmpty()) {
                Task newTask = new Task(title, description, deadline, progress);
                tasks.add(newTask);
                listModel.addElement(newTask.getTitle());
                JOptionPane.showMessageDialog(this, "할 일이 추가되었습니다!");
                titleField.setText("");
                descriptionField.setText("");
                deadlineField.setText("");
                progressCombo.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "모든 항목을 입력해주세요!");
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        buttonPanel.add(addTaskButton);
        buttonPanel.add(backButton);

        panel.add(addLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewScreen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        JLabel viewLabel = new JLabel("할 일 조회", JLabel.CENTER);
        viewLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 36));
        viewLabel.setForeground(new Color(33, 150, 243));

        JList<String> taskList = new JList<>(listModel);
        taskList.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellHeight(30);
        taskList.setFixedCellWidth(200);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        detailsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("제목: ");
        JLabel descriptionLabel = new JLabel("내용: ");
        JLabel deadlineLabel = new JLabel("기한: ");
        JLabel progressLabel = new JLabel("진행도: ");

        detailsPanel.add(titleLabel);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(deadlineLabel);
        detailsPanel.add(progressLabel);

        taskList.addListSelectionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task selectedTask = tasks.get(selectedIndex);
                titleLabel.setText("제목: " + selectedTask.getTitle());
                descriptionLabel.setText("내용: " + selectedTask.getDescription());
                deadlineLabel.setText("기한: " + selectedTask.getDeadline());
                progressLabel.setText("진행도: " + selectedTask.getProgress());
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton backButton = createStyledButton("뒤로가기");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        buttonPanel.add(backButton);

        panel.add(viewLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(taskList), BorderLayout.WEST);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put("defaultFont", new Font("Malgun Gothic", Font.PLAIN, 14));
        new TodoAppGUI();
    }
}
