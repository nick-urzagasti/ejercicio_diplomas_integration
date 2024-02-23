package com.meli.obtenerdiploma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.meli.obtenerdiploma.model.StudentDTO;
import com.meli.obtenerdiploma.model.SubjectDTO;
import com.meli.obtenerdiploma.util.TestUtilsGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.swing.text.AbstractDocument;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @BeforeEach @AfterEach
    public void loadStudents(){
        TestUtilsGenerator.loadUserFile();
    }
    private ObjectWriter objectMapper;
    public StudentControllerIntegrationTest(){
        this.objectMapper =  new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false).writer();

    }
    @Test
    void listStudentIntegrationOk() throws Exception{
        Set<StudentDTO> lista = Set.of(
                new StudentDTO(
                        1L,
                        "Juan",
                        null,
                        null,
                        List.of(
                                new SubjectDTO(
                                        "Matematica",
                                        7.0
                                ),
                                new SubjectDTO(
                                        "Fisica", 7.0

                                ),
                                new SubjectDTO("Quimica", 7.0)
                        )
                )
        );

        String expectedResponse = objectMapper.writeValueAsString(lista);
        MvcResult result = mockMvc
                .perform(get("/student/listStudents"))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andReturn()
                ;
        assertEquals(expectedResponse, result.getResponse().getContentAsString());

    }
    @Test
    void registerStudent() throws  Exception {

        StudentDTO studentTesst = new StudentDTO();
        studentTesst.setId(1230120312L);
        studentTesst.setStudentName("John Cena");
        studentTesst.setSubjects(List.of(
                new SubjectDTO(
                        "Matematica",
                        7.0
                ),
                new SubjectDTO(
                        "Fisica", 7.0

                ),
                new SubjectDTO("Quimica", 7.0)

        ));

        String jsonStudentDto  = this.objectMapper.writeValueAsString(studentTesst);

        mockMvc.perform(post("/student/registerStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStudentDto))
                .andExpect(status().isOk());

    }


}
