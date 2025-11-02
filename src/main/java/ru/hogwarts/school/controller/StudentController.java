package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age, @RequestParam(required = false) String part) {

        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(studentService.findByNameIgnoreCase(name));
        }
        if (age != null) {
            return ResponseEntity.ok(studentService.findStudentByAge(age));
        }
        if (part != null && !part.isBlank()) {
            return ResponseEntity.ok(studentService.findAllByNameContainsIgnoreCase(part));
        }


        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/age-range")
    public ResponseEntity<Collection<Student>> getStudentsByAgeRange(
            @RequestParam int min,
            @RequestParam int max) {

        // Валидация параметров
        if (min < 0 || max < 0) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (min > max) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        Collection<Student> students = studentService.findByAgeBetween(min, max);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/ordered-by-age")
    public ResponseEntity<Collection<Student>> getAllStudentsOrderedByAge(
            @RequestParam(defaultValue = "Сортировка по возрасту") String order) {

        Collection<Student> students;
        students = studentService.getAllStudentsOrderedByAgeAsc();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/age-less-than-id")
    public ResponseEntity<Collection<Student>> getStudentsWithAgeLessThanId(@RequestParam Long id) {
        Collection<Student> students = studentService.getStudentsWithAgeLessThanId(id);
        return ResponseEntity.ok(students);
    }


}
