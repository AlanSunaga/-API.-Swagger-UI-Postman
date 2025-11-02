package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Student student;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/students";
        student = new Student();
        student.setName("Test Student");
        student.setAge(20);
    }

    @Test
    public void getStudentsTest() {
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl, String.class))
                .isNotNull();
    }

    @Test
    public void postStudentTest() {
        Assertions
                .assertThat(this.restTemplate.postForObject(baseUrl, student, String.class))
                .isNotNull();
    }

    @Test
    public void getStudentByIdTest() {
        // Сначала создаем студента
        Student createdStudent = this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем получаем его по ID
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/" + createdStudent.getId(), String.class))
                .isNotNull();
    }

    @Test
    public void findStudentsByNameTest() {
        // Сначала создаем студента
        this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем ищем по имени
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "?name=Test Student", String.class))
                .isNotNull();
    }

    @Test
    public void findStudentsByAgeTest() {
        // Сначала создаем студента
        this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем ищем по возрасту
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "?age=20", String.class))
                .isNotNull();
    }

    @Test
    public void findStudentsByNamePartTest() {
        // Сначала создаем студента
        this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем ищем по части имени
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "?part=Test", String.class))
                .isNotNull();
    }

    @Test
    public void getStudentsByAgeRangeTest() {
        // Сначала создаем студентов
        this.restTemplate.postForObject(baseUrl, student, Student.class);

        Student student2 = new Student();
        student2.setName("Another Student");
        student2.setAge(25);
        this.restTemplate.postForObject(baseUrl, student2, Student.class);

        // Затем ищем по диапазону возрастов
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/age-range?min=18&max=30", String.class))
                .isNotNull();
    }

    @Test
    public void getAllStudentsOrderedByAgeTest() {
        // Сначала создаем студентов
        this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем получаем отсортированных по возрасту
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/ordered-by-age", String.class))
                .isNotNull();
    }

    @Test
    public void getStudentsWithAgeLessThanIdTest() {
        // Сначала создаем студента
        Student createdStudent = this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем ищем студентов с возрастом меньше ID
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/age-less-than-id?id=" + (createdStudent.getId() + 10), String.class))
                .isNotNull();
    }

    @Test
    public void deleteStudentTest() {
        // Сначала создаем студента
        Student createdStudent = this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Затем удаляем его
        this.restTemplate.delete(baseUrl + "/" + createdStudent.getId());

        // Проверяем, что после удаления общий список все равно возвращается (не null)
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl, String.class))
                .isNotNull();
    }

    @Test
    public void editStudentTest() {
        // Сначала создаем студента
        Student createdStudent = this.restTemplate.postForObject(baseUrl, student, Student.class);

        // Модифицируем данные
        createdStudent.setName("Updated Student");
        createdStudent.setAge(22);

        // Обновляем студента
        ResponseEntity<Student> response = this.restTemplate.postForEntity(baseUrl, createdStudent, Student.class);

        Assertions
                .assertThat(response.getBody())
                .isNotNull();
    }
}
