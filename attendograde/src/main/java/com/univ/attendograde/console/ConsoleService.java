package com.univ.attendograde.console;

import com.univ.attendograde.entity.Student;
import com.univ.attendograde.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleService implements CommandLineRunner {

    private final StudentService studentService;

    public ConsoleService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) {
        start();
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== Attendograde ======");
            System.out.println("1. Faculty");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Choose role: ");
            int role = sc.nextInt();
            sc.nextLine();

            switch (role) {
                case 1 -> facultyMenu(sc);
                case 2 -> studentMenu(sc);
                case 3 -> {
                    System.out.println("\n🚪 Exiting Attendograde... Goodbye! 👋");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void facultyMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Faculty Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. Update Subject Marks");
            System.out.println("3. Update Attendance");
            System.out.println("4. View All Students");
            System.out.println("5. Delete Student");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Roll No: ");
                    String rollNo = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String department = sc.nextLine();
                    System.out.print("Enter Year: ");
                    Integer year = sc.nextInt();
                    System.out.print("Enter Phone No: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Blood Group: ");
                    String bloodGroup = sc.nextLine();

                    studentService.addStudent(name, rollNo, email, department, year, phone, bloodGroup);
                    System.out.println("✅ Student added!");
                }
                case 2 -> {
                    System.out.print("Enter Student ID: ");
                    Long id = sc.nextLong();
                    sc.nextLine();

                    System.out.print("How many subjects do you want to enter? ");
                    int numSubjects = sc.nextInt();
                    sc.nextLine();

                    for (int i = 1; i <= numSubjects; i++) {
                        System.out.print("Enter Subject " + i + ": ");
                        String subject = sc.nextLine();
                        System.out.print("Enter Marks for " + subject + ": ");
                        int marks = sc.nextInt();
                        sc.nextLine();

                        studentService.updateSubjectMarks(id, subject, marks);
                    }

                    System.out.println("✅ All subject marks updated!");
                }
                case 3 -> {
                    System.out.print("Enter Student ID: ");
                    Long id = sc.nextLong();
                    System.out.print("Enter Attended Classes: ");
                    int attended = sc.nextInt();
                    System.out.print("Enter Total Classes: ");
                    int total = sc.nextInt();
                    sc.nextLine();

                    studentService.updateAttendance(id, attended, total);
                    System.out.println("✅ Attendance updated!");
                }
                case 4 -> studentService.getAllStudents().forEach(System.out::println);
                case 5 -> {
                    System.out.print("Enter Student ID to delete: ");
                    Long id = sc.nextLong();
                    if (studentService.deleteStudent(id)) {
                        System.out.println("🗑 Student deleted successfully!");
                    } else {
                        System.out.println("❌ Student not found!");
                    }
                }
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void studentMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View My Attendance Percentage");
            System.out.println("2. View My Report Card");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Student ID: ");
                    Long id = sc.nextLong();
                    studentService.getStudentById(id).ifPresentOrElse(
                            s -> {
                                double percentage = s.getAttendancePercentage();
                                int bars = (int) (percentage / 10); // 10 bars max
                                StringBuilder progress = new StringBuilder();
                                for (int i = 0; i < bars; i++) progress.append("▮");
                                for (int i = bars; i < 10; i++) progress.append("▯");
                                System.out.println("📊 Attendance: " + String.format("%.1f", percentage) + "% " + progress + " ✅");
                            },
                            () -> System.out.println("❌ Student not found!")
                    );
                }
                case 2 -> {
                    System.out.print("Enter Student ID: ");
                    Long id = sc.nextLong();
                    studentService.getStudentById(id).ifPresentOrElse(
                            s -> {
                                System.out.println("\n══════════ 🎓 Report Card 🎓 ══════════\n");
                                int totalMarks = 0;
                                int subjectCount = s.getSubjectMarks().size();
                                for (Map.Entry<String, Integer> entry : s.getSubjectMarks().entrySet()) {
                                    System.out.println(entry.getKey() + " : " + entry.getValue());
                                    totalMarks += entry.getValue();
                                }
                                if (subjectCount > 0) {
                                    double percentage = totalMarks * 1.0 / subjectCount;
                                    System.out.println("Total Marks: " + totalMarks + "/" + (subjectCount * 100));
                                    System.out.println("Percentage: " + String.format("%.1f", percentage) + "%");
                                    String grade;
                                    if (percentage >= 90) grade = "A";
                                    else if (percentage >= 80) grade = "B";
                                    else if (percentage >= 70) grade = "C";
                                    else if (percentage >= 60) grade = "D";
                                    else grade = "F";

                                    System.out.println("Grade: " + grade);
                                }
                                System.out.println("Attendance: " + s.getAttendancePercentage() + "%");
                                System.out.println("Department: " + s.getDepartment());
                                System.out.println("Year: " + s.getYear());
                                System.out.println("Phone: " + s.getPhone());
                                System.out.println("Blood Group: " + s.getBloodGroup());
                            },
                            () -> System.out.println("❌ Student not found!")
                    );
                }
                case 3 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
