package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.interfaces.StudentRepository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

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
        return studentRepository.findById(idStudent).orElse(null);
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


    public Collection<Student> findByNameIgnoreCase(String name) {
        return studentRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> findStudentByAge(Integer age) {
        return studentRepository.findStudentByAge(age);
    }

    public Collection<Student> findAllByNameContainsIgnoreCase(String part) {
        return studentRepository.findAllByNameContainsIgnoreCase(part);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Collection<Student> getAllStudentsOrderedByAgeAsc() {
        return studentRepository.findAllByOrderByAgeAsc();
    }

    public Collection<Student> getStudentsWithAgeLessThanId(Long id) {
        return studentRepository.findByAgeLessThan(id);
    }
}
