package controller;

import configuration.TestContainersExtension;
import org.example.Main;
import org.example.dto.CreateBookRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ExtendWith(TestContainersExtension.class)
@ActiveProfiles("test")
class BookControllerIT {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndGetBook_endToEnd() throws Exception {
        // 1. Create a new book via POST /api/books
        CreateBookRequest newBook = CreateBookRequest.builder()
                .title("Integration Testing")
                .author("John Doe")
                .year(2024)
                .build();

        String requestJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration Testing"))
                .andExpect(jsonPath("$.author").value("John Doe"))
                .andExpect(jsonPath("$.year").value("2024"))
                .andReturn()
                .getResponse();

        // 2. Get all books and verify our book is there
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Integration Testing"))
                .andExpect(jsonPath("$[0].author").value("John Doe"))
                .andExpect(jsonPath("$[0].year").value("2024"));
    }

    @Test
    void getBook_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 999))
                .andExpect(status().isNotFound());
    }
}