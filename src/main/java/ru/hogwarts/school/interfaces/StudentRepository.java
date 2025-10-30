package ru.hogwarts.school.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByNameIgnoreCase(String name);

    Collection<Student> findStudentByAge(Integer age);

    Collection<Student> findAllByNameContainsIgnoreCase(String part);

    Collection<Student> findByAgeBetween(Integer min, Integer max);

    Collection<Student> findAllByOrderByAgeAsc();

    Collection<Student> findByAgeLessThan(Long id);

}
