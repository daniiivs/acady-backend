package org.springboot.acadybackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springboot.acadybackend.entity.Exam;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
import org.springboot.acadybackend.service.impl.ExamServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ExamControllerTests {

    @Mock
    private ExamServiceImpl examService;

    @Mock
    private ExamAIServiceImpl examAIService;

    @InjectMocks
    private ExamController examController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(examController).build();
    }

    @Test
    void addExam_ShouldReturnSuccessMessage() throws Exception {
        Exam newExam = new Exam("11111", new Date(), false, null,
                Arrays.asList("11111c", "22222c"),
                "11111s", "11111a");

        mockMvc.perform(post("/api/exams/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Examen creado con éxito"));

        verify(examService).save(any(Exam.class));
    }

    @Test
    void getAllBySubjectId_WithExistingExams_ShouldReturnExams() throws Exception {
        List<Exam> exams = Arrays.asList(
                new Exam("11111", new Date(1620000000000L), true, 8.5,
                        Collections.singletonList("11111c"), "11111s", "11111a"),
                new Exam("22222", new Date(1621000000000L), false, null,
                        Arrays.asList("22222c", "33333c"), "11111s", "11111a")
        );

        when(examService.findAllBySubjectId("11111a")).thenReturn(Optional.of(exams));

        mockMvc.perform(get("/api/exams/subject/11111a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subjectId").value("11111a"))
                .andExpect(jsonPath("$[0].completed").value(true))
                .andExpect(jsonPath("$[1].chapterIds.length()").value(2));
    }

    @Test
    void getAllBySubjectId_WithNoExams_ShouldReturnNotFound() throws Exception {
        when(examService.findAllBySubjectId("11111a")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exams/subject/11111a"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByStudentId_WithCompletedExams_ShouldReturnFilteredResults() throws Exception {
        List<Exam> exams = Collections.singletonList(
                new Exam("11111", new Date(), true, 9.0,
                        List.of("11111c"), "11111s", "11111a")
        );

        when(examService.findAllByStudentId("11111s")).thenReturn(Optional.of(exams));

        mockMvc.perform(get("/api/exams/student/11111s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("11111s"))
                .andExpect(jsonPath("$[0].grade").value(9.0));
    }

    @Test
    void deleteExam_ShouldCleanRelatedDataAndReturnSuccess() throws Exception {
        String examId = "11111";

        mockMvc.perform(delete("/api/exams/delete/{id}", examId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Examen eliminado con éxito"));

        verify(examAIService).deleteAllByExamId(examId);
        verify(examService).deleteById(examId);
    }
}