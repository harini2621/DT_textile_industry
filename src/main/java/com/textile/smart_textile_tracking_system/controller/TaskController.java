package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Task;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import com.textile.smart_textile_tracking_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping
    public Task saveTask(@RequestBody Task task) {
        Task saved = taskService.saveTask(task);
        activityLogService.log("TASK_ASSIGNMENT", task.getAssignedWorker(),
                "Task '" + task.getTaskName() + "' assigned in " + task.getDepartment());
        return saved;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/department/{department}")
    public List<Task> getTasksByDepartment(@PathVariable String department) {
        return taskService.getTasksByDepartment(department);
    }

    @GetMapping("/worker/{assignedWorker}")
    public List<Task> getTasksByAssignedWorker(@PathVariable String assignedWorker) {
        return taskService.getTasksByAssignedWorker(assignedWorker);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }
}