package org.springboot.acadybackend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("fs.files")
public class DocumentFile {

    @Id
    private String id;

    private String filename;
    private String contentType;
    private String topicId;

    public DocumentFile() {}

    public DocumentFile(String filename, String contentType, String topicId) {
        this.filename = filename;
        this.contentType = contentType;
        this.topicId = topicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
