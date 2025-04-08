package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Chapter;
import org.springboot.acadybackend.repository.ChapterRepository;
import org.springboot.acadybackend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public Chapter save(Chapter chapter) {
        return this.chapterRepository.save(chapter);
    }

    @Override
    public void deleteById(String id) {
        this.chapterRepository.deleteById(id);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        this.chapterRepository.deleteAllBySubjectId(subjectId);
    }

    @Override
    public Optional<List<Chapter>> getAllBySubjectId(String subjectId) {
        return this.chapterRepository.getAllBySubjectId(subjectId);
    }

    @Override
    public Optional<List<Chapter>> getAllByStudentId(String studentId) {
        return this.chapterRepository.getAllByStudentId(studentId);
    }

    @Override
    public Optional<Chapter> getById(String id) {
        return this.chapterRepository.findById(id);
    }
}
