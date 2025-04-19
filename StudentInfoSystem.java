import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Interface for grading
interface GradingSystem {
    double calculateGrade();
}

// Base class Person
class Person {
    protected String name;
    protected int age;

    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getBasicInfo() {
        return "Name: " + name + "\nAge: " + age;
    }
}

// Student class
class Student extends Person {
    protected int studentId;
    protected String course;
    static int studentCount = 0;

    public Student(String name, int age, int studentId, String course) {
        super(name, age);
        this.studentId = studentId;
        this.course = course;
        studentCount++;
    }

    // Overloaded constructor
    public Student(int studentId, String course) {
        super();
        this.studentId = studentId;
        this.course = course;
        studentCount++;
    }

    public String getInfo() {
        return getBasicInfo() + "\nID: " + studentId + "\nCourse: " + course;
    }

    public static int getStudentCount() {
        return studentCount;
    }
}

// Graduate Student
class GraduateStudent extends Student {
    private String thesisTitle;

    public GraduateStudent(String name, int age, int studentId, String course, String thesisTitle) {
        super(name, age, studentId, course);
        this.thesisTitle = thesisTitle;
    }

    // Overloaded method
    public String getInfo(String prefix) {
        return prefix + getInfo();
    }

    @Override
    public String getInfo() {
        return super.getInfo() + "\nThesis Title: " + thesisTitle;
    }
}

// Course class
class Course implements GradingSystem {
    private String courseName;
    private double[] marks;

    public Course(String courseName, double[] marks) {
        this.courseName = courseName;
        this.marks = marks;
    }

    @Override
    public double calculateGrade() {
        double sum = 0;
        for (double mark : marks) {
            sum += mark;
        }
        return marks.length == 0 ? 0 : sum / marks.length;
    }

    public String getCourseSummary() {
        return "Course: " + courseName + "\nAverage Grade: " + String.format("%.2f", calculateGrade());
    }
}

// GUI Class
public class StudentInfoSystem extends JFrame {
    private JTextField nameField, ageField, idField, courseField, thesisField, marksField;
    private JCheckBox isGraduate;
    private JTextArea outputArea;

    public StudentInfoSystem() {
        setTitle("Student Information System");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        nameField = new JTextField();
        ageField = new JTextField();
        idField = new JTextField();
        courseField = new JTextField();
        thesisField = new JTextField();
        marksField = new JTextField();
        isGraduate = new JCheckBox("Graduate Student");

        inputPanel.add(new JLabel("Name:")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:")); inputPanel.add(ageField);
        inputPanel.add(new JLabel("Student ID:")); inputPanel.add(idField);
        inputPanel.add(new JLabel("Course:")); inputPanel.add(courseField);
        inputPanel.add(new JLabel("Thesis Title (if Grad):")); inputPanel.add(thesisField);
        inputPanel.add(new JLabel("Marks (comma separated):")); inputPanel.add(marksField);
        inputPanel.add(isGraduate);

        add(inputPanel, BorderLayout.NORTH);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Student");
        JButton countButton = new JButton("Show Total Count");
        buttonPanel.add(addButton);
        buttonPanel.add(countButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions without lambda (Java 7 compatible)
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        countButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("Total Students: " + Student.getStudentCount());
            }
        });
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            int id = Integer.parseInt(idField.getText());
            String course = courseField.getText();
            String thesis = thesisField.getText();
            String[] markStrings = marksField.getText().split(",");
            double[] marks = new double[markStrings.length];
            for (int i = 0; i < markStrings.length; i++) {
                marks[i] = Double.parseDouble(markStrings[i].trim());
            }

            Course c = new Course(course, marks);
            String output;
            if (isGraduate.isSelected()) {
                GraduateStudent gs = new GraduateStudent(name, age, id, course, thesis);
                output = "Graduate Student Added:\n" + gs.getInfo() + "\n" + c.getCourseSummary();
            } else {
                Student s = new Student(name, age, id, course);
                output = "Student Added:\n" + s.getInfo() + "\n" + c.getCourseSummary();
            }

            outputArea.setText(output);
            clearFields();
        } catch (Exception ex) {
            outputArea.setText("Error: Please enter valid input in all fields.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        idField.setText("");
        courseField.setText("");
        thesisField.setText("");
        marksField.setText("");
        isGraduate.setSelected(false);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentInfoSystem().setVisible(true);
            }
        });
    }
}
