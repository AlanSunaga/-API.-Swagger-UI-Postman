package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.interfaces.StudentRepository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.stream.Collectors;

@Service

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long idStudent) {
        return studentRepository.findById(idStudent).get();
    }

    public Student editStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(long idStudent) {
        studentRepository.deleteById(idStudent);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getAllStudentsWithAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
