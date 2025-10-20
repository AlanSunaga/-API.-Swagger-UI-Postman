package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long idScore = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++idScore);
        faculties.put(idScore, faculty);
        return faculty;
    }

    public Faculty findFaculty(long idFaculty) {
        return faculties.get(idFaculty);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (faculties.containsKey(faculty.getId())) {
            this.faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long idFaculty) {
        return faculties.remove(idFaculty);
    }

    public Collection<Faculty> getAllFaculty() {
        return faculties.values();
    }

    public Collection<Faculty> getAllFacultyWithColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
