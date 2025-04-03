package org.springboot.acadybackend.service;

import org.springboot.acadybackend.entity.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterService {
    Chapter save(Chapter chapter);
    void deleteById(String id);
    void deleteAllBySubjectId(String subjectId);
    Optional<List<Chapter>> getAllBySubjectId(String subjectId);
}
