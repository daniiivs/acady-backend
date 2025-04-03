package org.springboot.acadybackend.service.impl;

import org.springboot.acadybackend.entity.Task;
import org.springboot.acadybackend.repository.TaskRepository;
import org.springboot.acadybackend.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAllByStudentId(String studentId) {
        return this.taskRepository.findAllByStudentId(studentId);
    }

    @Override
    public List<Task> findAllBySubjectId(String subjectId) {
        return this.taskRepository.findAllBySubjectId(subjectId);
    }

    @Override
    public void save(Task task) {
        this.taskRepository.save(task);
    }

    @Override
    public void deleteAllById(String id) {
        this.taskRepository.deleteAllById(id);
    }

    @Override
    public void deleteAllBySubjectId(String subjectId) {
        this.taskRepository.deleteAllBySubjectId(subjectId);
    }

    @Override
    public void deleteAllByStudentId(String studentId) {
        this.taskRepository.deleteAllByStudentId(studentId);
    }
}
