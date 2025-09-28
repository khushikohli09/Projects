package com.univ.attendograde2v.reportcard.service;

import com.univ.attendograde2v.reportcard.entity.Grade;
import com.univ.attendograde2v.reportcard.repository.GradeRepository;
import com.univ.attendograde2v.student.entity.StudentAttendance;
import com.univ.attendograde2v.student.repository.StudentAttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportCardService {

    private final GradeRepository gradeRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;

    public ReportCardService(GradeRepository gradeRepository,
                             StudentAttendanceRepository studentAttendanceRepository) {
        this.gradeRepository = gradeRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
    }

    // Inner class for multiple subject input
    public static class GradeInput {
        private final String subject;
        private final int marksObtained;
        private final int maxMarks;

        public GradeInput(String subject, int marksObtained, int maxMarks) {
            this.subject = subject;
            this.marksObtained = marksObtained;
            this.maxMarks = maxMarks;
        }

        public String getSubject() { return subject; }
        public int getMarksObtained() { return marksObtained; }
        public int getMaxMarks() { return maxMarks; }
    }

    // Check if student exists in attendance table
    public boolean isStudentPresent(String username) {
        return !studentAttendanceRepository.findByStudentUsername(username).isEmpty();
    }

    // Fetch all student usernames
    public List<String> getAllStudentUsernames() {
        return studentAttendanceRepository.findAll()
                .stream()
                .map(StudentAttendance::getStudentUsername)
                .distinct()
                .collect(Collectors.toList());
    }

    // Save a single grade
    public void saveGrade(String studentUsername, String subject, int marksObtained, int maxMarks) {
        Grade grade = new Grade();
        grade.setStudentUsername(studentUsername);
        grade.setSubject(subject);
        grade.setMarksObtained(marksObtained);
        grade.setMaxMarks(maxMarks);
        grade.setGrade(calculateLetterGrade(marksObtained, maxMarks));

        gradeRepository.save(grade);
    }

    // Save multiple grades at once
    public void saveGrades(String studentUsername, List<GradeInput> subjects) {
        for (GradeInput input : subjects) {
            saveGrade(studentUsername, input.getSubject(), input.getMarksObtained(), input.getMaxMarks());
        }
    }

    // Calculate letter grade
    private String calculateLetterGrade(int marksObtained, int maxMarks) {
        double percentage = ((double) marksObtained / maxMarks) * 100;
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }

    // Fetch all grades of a student
    public List<Grade> getGradesByStudent(String studentUsername) {
        if (studentUsername == null) {
            return gradeRepository.findAll();
        }
        return gradeRepository.findByStudentUsername(studentUsername);
    }

    // Calculate percentage
    public double calculatePercentage(List<Grade> grades) {
        double totalObtained = 0;
        double totalMax = 0;
        for (Grade g : grades) {
            totalObtained += g.getMarksObtained();
            totalMax += g.getMaxMarks();
        }
        return totalMax > 0 ? (totalObtained / totalMax) * 100 : 0;
    }

    // Calculate GPA (4-point scale)
    public double calculateGPA(List<Grade> grades) {
        double totalPoints = 0;
        for (Grade g : grades) {
            switch (g.getGrade()) {
                case "A+", "A" -> totalPoints += 4;
                case "B" -> totalPoints += 3;
                case "C" -> totalPoints += 2;
                case "D" -> totalPoints += 1;
            }
        }
        return grades.size() > 0 ? totalPoints / grades.size() : 0;
    }

    // Get top scoring subject
    public Grade getTopSubject(List<Grade> grades) {
        return grades.stream()
                .max((g1, g2) -> g1.getMarksObtained() - g2.getMarksObtained())
                .orElse(null);
    }

    // Get lowest scoring subject
    public Grade getWeakSubject(List<Grade> grades) {
        return grades.stream()
                .min((g1, g2) -> g1.getMarksObtained() - g2.getMarksObtained())
                .orElse(null);
    }
}
