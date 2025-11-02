package ru.hogwarts.school;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.interfaces.StudentRepository;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;


    @SpyBean
    private StudentService studentService;


    @InjectMocks
    private StudentController studentController;


    private final List<Student> students = Arrays.asList(
            new Student(1L, "Иван", 20),
            new Student(2L, "Мария", 22)
    );
    private final Student firstStudent = students.get(0);

    private JSONObject studentJSONObject() throws JSONException {
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", firstStudent.getName());
        studentObject.put("age", firstStudent.getAge());
        return studentObject;
    }

    // 1. Тест создания студента
    @Test
    public void saveStudentTest() throws Exception {
        // given
        //...вынес выше

        when(studentRepository.save(any(Student.class))).thenReturn(firstStudent);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstStudent.getId()))
                .andExpect(jsonPath("$.name").value(firstStudent.getName()))
                .andExpect(jsonPath("$.age").value(firstStudent.getAge()));

    }

    // 2. Тест получения студента по ID
    @Test
    public void findStudentTest() throws Exception {
        // given
        //...вынес выше
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(firstStudent));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstStudent.getId()))
                .andExpect(jsonPath("$.name").value(firstStudent.getName()))
                .andExpect(jsonPath("$.age").value(firstStudent.getAge()));
    }

    // 3. Тест обновления студента
    @Test
    public void editStudentTest() throws Exception {
        // given
        //...вынес выше
        when(studentService.editStudent(any(Student.class))).thenReturn(firstStudent);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstStudent.getId()))
                .andExpect(jsonPath("$.name").value(firstStudent.getName()))
                .andExpect(jsonPath("$.age").value(firstStudent.getAge()));
    }

    //4. Тест удаления студента
    @Test
    public void deleteStudentTest() throws Exception {
        // given
        //...вынес выше
        when(studentService.findStudent(1L)).thenReturn(null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/1"))
                .andExpect(status().isOk());
    }


    // 5.1 Тест поиска студентов с фильтрами поиск по имени
    @Test
    public void findStudentsByNameTest() throws Exception {
        // given
        //...вынес выше

        when(studentService.findByNameIgnoreCase("Иван")).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?name=Иван"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Иван"))
                .andExpect(jsonPath("$[1].name").value("Мария"));
    }

    // 5.2 Тест поиска студентов с фильтрами поиск по возрасту
    @Test
    public void findStudentsByAgeTest() throws Exception {
        // given
        //...вынес выше

        when(studentService.findStudentByAge(20)).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?age=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].age").value(22));
    }

    // 5.3 Тест поиска студентов с фильтрами поиск по части имени
    @Test
    public void findStudentsByNamePartTest() throws Exception {
        // given
        //...вынес выше

        when(studentService.findAllByNameContainsIgnoreCase("Ар")).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?part=Ар"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Иван"))
                .andExpect(jsonPath("$[1].name").value("Мария"));
    }

    // 6. Тест на поиск всех студентов
    @Test
    public void getAllStudentsTest() throws Exception {
        // given
        //...вынес выше

        when(studentService.getAllStudents()).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 7. Тест на поиск несуществующего студента
    @Test
    public void editStudentNotFoundTest() throws Exception {
        // given
        when(studentService.editStudent(any(Student.class))).thenReturn(null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // 8. Тест поиска по возрастному диапазону
    @Test
    public void getStudentsByAgeRangeTest() throws Exception {
        // given
        //...вынес выше
        when(studentService.findByAgeBetween(18, 25)).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/age-range?min=18&max=25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].age").value(22));
    }

    // 9. Тест на невалидные параметры
    @Test
    public void getStudentsByAgeRangeInvalidTest() throws Exception {
        // when & then - невалидные параметры
        mockMvc.perform(MockMvcRequestBuilders.get("/students/age-range?min=25&max=18"))
                .andExpect(status().isBadRequest());
    }

    // 10. Тест упорядочивания по возрасту
    @Test
    public void getAllStudentsOrderedByAgeTest() throws Exception {
        // given
        List<Student> students = Arrays.asList(
                new Student(1L, "Младший", 18),
                new Student(2L, "Средний", 20),
                new Student(3L, "Старший", 25)
        );
        when(studentService.getAllStudentsOrderedByAgeAsc()).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/students/ordered-by-age"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[1].age").value(20))
                .andExpect(jsonPath("$[2].age").value(25));
    }

    // 11. Тест студентов с возрастом меньше ID
    @Test
    public void getStudentsWithAgeLessThanIdTest() throws Exception {
        // given
        List<Student> students = Arrays.asList(
                new Student(5L, "Студент1", 4),  // возраст 4 < ID 5
                new Student(10L, "Студент2", 8)  // возраст 8 < ID 10
        );
        when(studentService.getStudentsWithAgeLessThanId(anyLong())).thenReturn(students);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/students/age-less-than-id?id=15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].age").value(4))
                .andExpect(jsonPath("$[1].age").value(8));
    }

    // 12. Тест пустых результатов
    @Test
    public void findStudentsEmptyResultTest() throws Exception {
        // given
        when(studentService.findByNameIgnoreCase("Несуществующий")).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/students?name=Несуществующий"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


}
