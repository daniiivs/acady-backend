package org.springboot.acadybackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springboot.acadybackend.entity.Subject;
import org.springboot.acadybackend.service.impl.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTests {

    @Mock
    private SubjectServiceImpl subjectService;

    @Mock
    private ChapterServiceImpl chapterService;

    @Mock
    private PdfFileServiceImpl pdfFileService;

    @Mock
    private TaskServiceImpl taskService;

    @Mock
    private ExamServiceImpl examService;

    @Mock
    private ExamAIServiceImpl examAIService;

    @InjectMocks
    private SubjectController subjectController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void currentSubjects_ShouldReturnAllSubjectsForStudent() throws Exception {
        List<Subject> subjects = Arrays.asList(
                new Subject("11111", "MAT", "Matemáticas", "#FF0000", "11111s"),
                new Subject("22222", "FIS", "Física", "#00FF00", "11111s")
        );

        when(subjectService.findAllByStudentId("11111s")).thenReturn(subjects);

        mockMvc.perform(get("/api/subjects/all/11111s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("MAT"))
                .andExpect(jsonPath("$[1].color").value("#00FF00"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findById_WhenSubjectExists_ShouldReturnSubject() throws Exception {
        Subject subject = new Subject("11111", "QUI", "Química", "#0000FF", "11111s");
        when(subjectService.findById("11111")).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/api/subjects/11111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Química"))
                .andExpect(jsonPath("$.studentId").value("11111s"));
    }

    @Test
    void findById_WhenSubjectNotExists_ShouldReturnNotFound() throws Exception {
        when(subjectService.findById("11111")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/subjects/11111"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addSubject_ShouldCreateNewSubjectWithUniqueCode() throws Exception {
        Subject newSubject = new Subject("BIO", "Biología", "#FFA500", "11111s");

        mockMvc.perform(post("/api/subjects/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSubject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Asignatura creada con éxito"));

        verify(subjectService).save(any(Subject.class));
    }

    @Test
    void deleteSubject_ShouldRemoveAllRelatedData() throws Exception {
        String subjectId = "11111";

        mockMvc.perform(delete("/api/subjects/delete/{id}", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Asignatura eliminada con éxito"));

        // Verificar orden de eliminación
        InOrder inOrder = inOrder(
                pdfFileService,
                examAIService,
                examService,
                chapterService,
                taskService,
                subjectService
        );

        inOrder.verify(pdfFileService).deleteAllBySubjectId(subjectId);
        inOrder.verify(examAIService).deleteAllBySubjectId(subjectId);
        inOrder.verify(examService).deleteAllBySubjectId(subjectId);
        inOrder.verify(chapterService).deleteAllBySubjectId(subjectId);
        inOrder.verify(taskService).deleteAllBySubjectId(subjectId);
        inOrder.verify(subjectService).deleteById(subjectId);
    }
}