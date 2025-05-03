package org.springboot.acadybackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.service.impl.ChapterServiceImpl;
import org.springboot.acadybackend.service.impl.ExamAIServiceImpl;
import org.springboot.acadybackend.service.impl.ExamServiceImpl;
import org.springboot.acadybackend.service.impl.PdfFileServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChapterControllerTests {

    @Mock
    private ChapterServiceImpl chapterService;

    @Mock
    private PdfFileServiceImpl pdfFileService;

    @Mock
    private ExamServiceImpl examService;

    @Mock
    private ExamAIServiceImpl examAIService;

    @InjectMocks
    private ChapterController chapterController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chapterController).build();
    }

    @Test
    void addChapter_ShouldReturnSuccessMessage() throws Exception {
        Chapter requestChapter = new Chapter("Vocabulario", 1, "11111", "22222");

        mockMvc.perform(post("/api/chapters/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestChapter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tema creado con éxito"));

        verify(chapterService).save(any(Chapter.class));
    }

    @Test
    void getAllBySubjectId_WhenChaptersExist_ShouldReturnChapters() throws Exception {
        List<Chapter> chapters = Arrays.asList(
                new Chapter("11111", "Álgebra", "11111s", 1, "11111a"),
                new Chapter("22222", "Geometría", "22222s", 2, "22222a")
        );

        when(chapterService.getAllBySubjectId("SUB-MATH")).thenReturn(Optional.of(chapters));

        mockMvc.perform(get("/api/chapters/all/SUB-MATH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("11111"))
                .andExpect(jsonPath("$[0].subjectId").value("11111a"))
                .andExpect(jsonPath("$[1].number").value(2));
    }

    @Test
    void getAllBySubjectId_WhenNoChapters_ShouldReturnNotFound() throws Exception {
        when(chapterService.getAllBySubjectId("11111")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/chapters/all/11111"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByStudentId_WhenChaptersExist_ShouldReturnChapters() throws Exception {
        List<Chapter> chapters = Collections.singletonList(
                new Chapter("11111", "Historia", "11111s", 3, "11111a")
        );

        when(chapterService.getAllByStudentId("11111s")).thenReturn(Optional.of(chapters));

        mockMvc.perform(get("/api/chapters/student/11111s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value("11111s"))
                .andExpect(jsonPath("$[0].name").value("Historia"));
    }

    @Test
    void getAllByStudentId_WhenNoChapters_ShouldReturnNotFound() throws Exception {
        String studentId = "11111";

        when(chapterService.getAllByStudentId(studentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/chapters/student/{studentId}", studentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteChapter_ShouldCleanAllRelatedData() throws Exception {
        String chapterId = "11111";

        mockMvc.perform(delete("/api/chapters/delete/{id}", chapterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tema eliminado con éxito"));

        verify(pdfFileService).deleteAllByChapterId(chapterId);
        verify(examAIService).deleteAllByChapterIdsContaining(chapterId);
        verify(examService).deleteAllByChapterIdsContaining(chapterId);
        verify(chapterService).deleteById(chapterId);
    }
}