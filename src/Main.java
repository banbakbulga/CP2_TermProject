import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ArrayList<Task> tasks;
    private DefaultListModel<String> listModel;

    public Main() {
        setTitle("ToDoList");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
    // 홈 화면
    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("ToDoList", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("추가");
        JButton viewButton = new JButton("조회");

        addButton.addActionListener(e -> cardLayout.show(mainPanel, "AddTask"));
        viewButton.addActionListener(e -> cardLayout.show(mainPanel, "ViewTasks"));

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    // 추가 화면
    private JPanel createAddScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel addLabel = new JLabel("할 일 추가", JLabel.CENTER);
        addLabel.setFont(new Font("궁서", Font.BOLD, 40));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField deadlineField = new JTextField();
        JComboBox<String> progressCombo = new JComboBox<>(new String[]{"시작 안함", "진행 중"});

        inputPanel.add(new JLabel("제목:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("내용:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("기한:"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("진행도:"));
        inputPanel.add(progressCombo);

        JPanel buttonPanel = new JPanel();
        JButton addTaskButton = new JButton("추가하기");
        JButton backButton = new JButton("뒤로가기");
        // 추가하기 이벤트 설정
        addTaskButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String deadline = deadlineField.getText();
            String progress = (String) progressCombo.getSelectedItem();

            if (!title.isEmpty() && !description.isEmpty() && !deadline.isEmpty()) {
                Task newTask = new Task(title, description, deadline, progress);
                tasks.add(newTask);
                listModel.addElement(newTask.getTitle());
                JOptionPane.showMessageDialog(this, "할 일이 추가되었습니다.");
                titleField.setText("");
                descriptionField.setText("");
                deadlineField.setText("");
                progressCombo.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "모든 항목을 입력하세요.");
            }
        });
        //뒤로가기 이벤트
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        buttonPanel.add(addTaskButton);
        buttonPanel.add(backButton);

        panel.add(addLabel, BorderLayout.NORTH);
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    //조회 버튼
    private JPanel createViewScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel viewLabel = new JLabel("할 일 조회", JLabel.CENTER);
        viewLabel.setFont(new Font("궁서", Font.BOLD, 40));

        JList<String> taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellHeight(30);
        taskList.setFixedCellWidth(200);

        // 오른쪽 상세 정보 패널
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel titleLabel = new JLabel("제목: ");
        JLabel descriptionLabel = new JLabel("내용: ");
        JLabel deadlineLabel = new JLabel("기한: ");
        JLabel progressLabel = new JLabel("진행도: ");

        detailsPanel.add(titleLabel);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(deadlineLabel);
        detailsPanel.add(progressLabel);

        // task 선택 시 상세 정보 표시 이벤트
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

        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        JButton backButton = new JButton("뒤로가기");

        // 수정 버튼 눌렀을 때 이벤트
        editButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task task = tasks.get(selectedIndex);

                JTextField titleField = new JTextField(task.getTitle());
                JTextField descriptionField = new JTextField(task.getDescription());
                JTextField deadlineField = new JTextField(task.getDeadline());
                JComboBox<String> progressCombo = new JComboBox<>(new String[]{"시작 안함", "진행 중", "완료"});
                progressCombo.setSelectedItem(task.getProgress());
                //수정 버튼 눌렀을 때 패널
                JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
                editPanel.add(new JLabel("제목:"));
                editPanel.add(titleField);
                editPanel.add(new JLabel("내용:"));
                editPanel.add(descriptionField);
                editPanel.add(new JLabel("기한:"));
                editPanel.add(deadlineField);
                editPanel.add(new JLabel("진행도:"));
                editPanel.add(progressCombo);

                int result = JOptionPane.showConfirmDialog(this, editPanel, "할 일 수정", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    task.setTitle(titleField.getText());
                    task.setDescription(descriptionField.getText());
                    task.setDeadline(deadlineField.getText());
                    task.setProgress((String) progressCombo.getSelectedItem());
                    listModel.set(selectedIndex, task.getTitle());
                }
            } else {
                JOptionPane.showMessageDialog(this, "수정할 할 일을 선택하세요.");
            }
        });

        // 삭제 버튼 이벤트
        deleteButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                tasks.remove(selectedIndex);
                listModel.remove(selectedIndex);
                titleLabel.setText("제목: ");
                descriptionLabel.setText("내용: ");
                deadlineLabel.setText("기한: ");
                progressLabel.setText("진행도: ");
            } else {
                JOptionPane.showMessageDialog(this, "삭제할 할 일을 선택하세요.");
            }
        });

        // 뒤로가기 버튼 이벤트
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        panel.add(viewLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(taskList), BorderLayout.WEST);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        new Main();
    }
}

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
