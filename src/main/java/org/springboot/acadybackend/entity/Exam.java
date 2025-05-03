package org.springboot.acadybackend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("exams")
public class Exam {

    @Id
    private String id;

    private Date date;
    private boolean completed;
    private Double grade;
    private List<String> chapterIds;
    private String studentId;
    private String subjectId;

    public Exam() {}

    public Exam(String id, Date date, boolean completed, Double grade, List<String> chapterIds, String studentId, String subjectId) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.grade = grade;
        this.chapterIds = chapterIds;
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public List<String> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(List<String> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
