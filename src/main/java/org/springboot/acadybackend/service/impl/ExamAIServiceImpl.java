package org.springboot.acadybackend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springboot.acadybackend.entity.ExamAI;
import org.springboot.acadybackend.repository.ExamAIRepository;
import org.springboot.acadybackend.service.ExamAIService;
import org.springboot.acadybackend.utils.PdfUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ExamAIServiceImpl implements ExamAIService {

    private final ExamAIRepository examAIRepository;
    private final WebClient webClient; // Para comunicarnos con un la web

    public ExamAIServiceImpl(ExamAIRepository examAIRepository, @Value("${gemini.api.key}") String apiKey,
                             @Value("${gemini.endpoint}") String geminiEndpoint) {
        this.examAIRepository = examAIRepository;
        this.webClient = WebClient.builder()
                .baseUrl(geminiEndpoint + "?key=" + apiKey) // url + clave api de gemini
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // headers necesarios para la comunicación
                .build();
    }

    // Plantilla para construir el objeto de examen de IA
    private String buildPrompt(String text) {
        String jsonTemplate = """
                {
                  "studentId": null,
                  "subjectId": null,
                  "examId": null,
                  "grade": 0.0,
                  "questions": [
                    {
                      "number": "1",
                      "question": "...",
                      "answers": [
                        { "letter": "a", "answerText": "..." },
                        { "letter": "b", "answerText": "..." },
                        { "letter": "c", "answerText": "..." },
                        { "letter": "d", "answerText": "..." }
                      ],
                      "correctAnswer": "la letra de la respuesta correcta",
                      "chosenAnswer": ""
                    }
                  ]
                }""";

        return "Basándote en los siguientes apuntes:\n\n" + text + "\n\nGenera un examen de 10 preguntas (asegurándote de que la respuesta correcta pueda ser una de las 4 opciones de manera aleatoria; la a, la b, la c o la d) en formato JSON siguiendo esta estructura:\n" + jsonTemplate + ". Solo devuelve como respuesta esa estructura, sin nada más. Evita /n innecesarios, saltos de línea y elementos que dificulten el parseo a json,";
    }

    public Mono<String> requestExamFromGemini(String text) {
        String prompt = buildPrompt(text);

        String payload = String.format("""
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, escapeJson(prompt)); // Formato para gemini

        return webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class); // Mono actúa como una "caja vacía" que se llenará con un String
    }

    // Para caracteres conflictivos
    private String escapeJson(String raw) {
        return raw
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    public ExamAI parseExamAI(String jsonExam) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); // Lector JSON

        JsonNode rootNode = mapper.readTree(jsonExam); // Convierte a estructura de árbol
        JsonNode contentNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text"); // Navega dentro del árbol hasta llegar al texto

        return mapper.readValue(contentNode.asText(), ExamAI.class); // Convierte el valor en un objeto ExamAI
    }

    @Override
    public Optional<ExamAI> findById(String id) {
        return this.examAIRepository.findById(id);
    }

    @Override
    public Optional<List<ExamAI>> findAllByExamId(String examId) {
        return this.examAIRepository.findAllByExamId(examId);
    }

    @Override
    public Optional<List<ExamAI>> findAllByStudentId(String studentId) {
        return this.examAIRepository.findAllByStudentId(studentId);
    }

    @Override
    public void saveExamAI(ExamAI examAI) {
        this.examAIRepository.save(examAI);
    }

    @Override
    public void deleteById(String id) {
        this.examAIRepository.deleteById(id);
    }

    @Override
    public void deleteAllByExamId(String examId) {
        this.examAIRepository.deleteAllByExamId(examId);
    }

    @Override
    public void deleteAllByChapterIdsContaining(String chapterId) {
        this.examAIRepository.deleteAllByChapterIdsContaining(chapterId);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        this.examAIRepository.deleteAllBySubjectId(subjectId);
    }
}
