package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final HashMap<Long, Student> students = new HashMap<>();
    private long idScore = 0;

    public Student addStudent(Student student) {
        student.setId(++idScore);
        students.put(idScore, student);
        return student;
    }

    public Student findStudent(long idStudent) {
        return students.get(idStudent);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            this.students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long idStudent) {
        return students.remove(idStudent);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Collection<Student> getAllStudentsWithAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
