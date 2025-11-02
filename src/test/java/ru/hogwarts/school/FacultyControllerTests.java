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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.interfaces.FacultyRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private final List<Faculty> faculties = Arrays.asList(
            new Faculty(1L, "Альфа", "Black"),
            new Faculty(2L, "Бета", "Red")
    );
    private final Faculty firstFaculty = faculties.get(0);

    private JSONObject facultyJSONObject() throws JSONException {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", firstFaculty.getId());
        facultyObject.put("name", firstFaculty.getName());
        facultyObject.put("color", firstFaculty.getColor());
        return facultyObject;
    }

    // 1. Тест создания факультета
    @Test
    public void saveFacultyTest() throws Exception {
        // given
        //...вынес выше

        when(facultyRepository.save(any(Faculty.class))).thenReturn(firstFaculty);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculties")
                        .content(facultyJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstFaculty.getId()))
                .andExpect(jsonPath("$.name").value(firstFaculty.getName()))
                .andExpect(jsonPath("$.color").value(firstFaculty.getColor()));

    }

    // 2. Тест получения факультета по ID
    @Test
    public void findFacultyTest() throws Exception {
        // given
        //...вынес выше
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(firstFaculty));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstFaculty.getId()))
                .andExpect(jsonPath("$.name").value(firstFaculty.getName()))
                .andExpect(jsonPath("$.color").value(firstFaculty.getColor()));
    }

    // 3. Тест обновления факультета
    @Test
    public void editFacultyTest() throws Exception {
        // given
        //...вынес выше
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(firstFaculty);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties")
                        .content(facultyJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstFaculty.getId()))
                .andExpect(jsonPath("$.name").value(firstFaculty.getName()))
                .andExpect(jsonPath("$.color").value(firstFaculty.getColor()));
    }

    //4. Тест удаления факультета
    @Test
    public void deleteFacultyTest() throws Exception {
        // given
        //...вынес выше
        when(facultyService.findFaculty(1L)).thenReturn(null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/1"))
                .andExpect(status().isOk());
    }

    // 5.1 Тест поиска факультета с фильтрами поиск по имени
    @Test
    public void findFacultyByNameTest() throws Exception {
        // given
        //...вынес выше

        when(facultyService.findByNameIgnoreCase("Альфа")).thenReturn(faculties);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties?name=Альфа"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Альфа"))
                .andExpect(jsonPath("$[1].name").value("Бета"));
    }

    // 5.2 Тест поиска факультета с фильтрами поиск по цвету
    @Test
    public void findFacultyByColorTest() throws Exception {
        // given
        //...вынес выше

        when(facultyService.findByColorIgnoreCase("Black")).thenReturn(faculties);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties?color=Black"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].color").value("Black"))
                .andExpect(jsonPath("$[1].color").value("Red"));
    }

    // 6. Тест на поиск всех факультетов
    @Test
    public void getAllFacultiesTest() throws Exception {
        // given
        //...вынес выше

        when(facultyService.getAllFaculty()).thenReturn(faculties);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // 7. Тест на поиск несуществующего факультета
    @Test
    public void editFacultyNotFoundTest() throws Exception {
        // given
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(null);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties")
                        .content(facultyJSONObject().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // 12. Тест пустых результатов
    @Test
    public void findFacultiesEmptyResultTest() throws Exception {
        // given
        when(facultyService.findByNameIgnoreCase("Несуществующий")).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties?name=Несуществующий"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
