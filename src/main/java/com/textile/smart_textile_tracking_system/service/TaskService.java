package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Task;
import com.textile.smart_textile_tracking_system.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByDepartment(String department) {
        return taskRepository.findByDepartment(department);
    }

    public List<Task> getTasksByAssignedWorker(String assignedWorker) {
        return taskRepository.findByAssignedWorker(assignedWorker);
    }

    public List<Task> getTasksByAssignedWorkerAndStatus(String assignedWorker, String status) {
        return taskRepository.findByAssignedWorkerAndStatus(assignedWorker, status);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
