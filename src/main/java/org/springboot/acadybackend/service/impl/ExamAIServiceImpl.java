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
    private final WebClient webClient;

    public ExamAIServiceImpl(ExamAIRepository examAIRepository, @Value("${gemini.api.key}") String apiKey,
                             @Value("${gemini.endpoint}") String geminiEndpoint) {
        this.examAIRepository = examAIRepository;
        this.webClient = WebClient.builder()
                .baseUrl(geminiEndpoint)
                .baseUrl(geminiEndpoint + "?key=" + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String extractText(InputStream pdfStream) throws IOException {
        return PdfUtils.exactText(pdfStream);
    }

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
                        { "letter": "b", "answerText": "..." }
                      ],
                      "correctAnswer": "a",
                      "chosenAnswer": ""
                    }
                  ]
                }""";

        // Concatenar el texto que se le pasa como base para generar las preguntas.

        // Devolver el prompt construido
        return "Basándote en los siguientes apuntes:\n\n" + text + "\n\nGenera un examen de 10 preguntas en formato JSON siguiendo esta estructura:\n" + jsonTemplate +". Solo devuelve como respuesta esa estructura, sin nada más. Evita /n innecesarios, saltos de línea y elementos que dificulten el parseo a json,";
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
        """, escapeJson(prompt));

        return webClient.post()
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class);
    }

    private String escapeJson(String raw) {
        return raw
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    public ExamAI parseExamAI(String jsonExam) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(jsonExam);
        JsonNode contentNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");

        return mapper.readValue(contentNode.asText(), ExamAI.class);
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
}
