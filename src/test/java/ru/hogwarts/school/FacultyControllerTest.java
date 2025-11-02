package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty faculty;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/faculties";
        faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
    }

    @Test
    public void getAllFacultiesTest() {
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl, String.class))
                .isNotNull();
    }

    @Test
    public void postFacultyTest() {
        Assertions
                .assertThat(this.restTemplate.postForObject(baseUrl, faculty, String.class))
                .isNotNull();
    }

    @Test
    public void getFacultyByIdTest() {
        // Сначала создаем факультет
        Faculty createdFaculty = this.restTemplate.postForObject(baseUrl, faculty, Faculty.class);

        // Затем получаем его по ID
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "/" + createdFaculty.getId(), String.class))
                .isNotNull();
    }

    @Test
    public void findFacultiesByNameTest() {
        // Сначала создаем факультет
        this.restTemplate.postForObject(baseUrl, faculty, Faculty.class);

        // Затем ищем по имени
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "?name=Gryffindor", String.class))
                .isNotNull();
    }

    @Test
    public void findFacultiesByColorTest() {
        // Сначала создаем факультет
        this.restTemplate.postForObject(baseUrl, faculty, Faculty.class);

        // Затем ищем по цвету
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl + "?color=Red", String.class))
                .isNotNull();
    }

    @Test
    public void getFacultyByIdNotFoundTest() {
        // Пытаемся получить несуществующий факультет
        ResponseEntity<String> response = this.restTemplate.getForEntity(baseUrl + "/999", String.class);

        Assertions
                .assertThat(response.getStatusCode().value())
                .isEqualTo(404);
    }

    @Test
    public void deleteFacultyTest() {
        // Сначала создаем факультет
        Faculty createdFaculty = this.restTemplate.postForObject(baseUrl, faculty, Faculty.class);

        // Затем удаляем его
        this.restTemplate.delete(baseUrl + "/" + createdFaculty.getId());

        // Проверяем, что после удаления общий список все равно возвращается (не null)
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl, String.class))
                .isNotNull();
    }

    @Test
    public void editFacultyTest() {
        // Сначала создаем факультет
        Faculty createdFaculty = this.restTemplate.postForObject(baseUrl, faculty, Faculty.class);

        // Модифицируем данные
        createdFaculty.setName("Slytherin");
        createdFaculty.setColor("Green");

        // Обновляем факультет
        ResponseEntity<Faculty> response = this.restTemplate.postForEntity(baseUrl, createdFaculty, Faculty.class);

        Assertions
                .assertThat(response.getBody())
                .isNotNull();
    }

    @Test
    public void getAllFacultiesWhenNoParamsTest() {
        // Просто проверяем, что эндпоинт работает без параметров
        Assertions
                .assertThat(this.restTemplate.getForObject(baseUrl, String.class))
                .isNotNull();
    }
}
