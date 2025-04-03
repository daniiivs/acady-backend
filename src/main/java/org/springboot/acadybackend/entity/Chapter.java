package org.springboot.acadybackend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chapters")
public class Chapter {

    @Id
    private String id;

    private String name;
    private int number;
    private String studentId;
    private String subjectId;

    public Chapter() {}

    public Chapter(String name, int number, String studentId, String subjectId) {
        this.name = name;
        this.number = number;
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public Chapter(String id, String name, String studentId, int number, String subjectId) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.studentId = studentId;
        this.subjectId = subjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
