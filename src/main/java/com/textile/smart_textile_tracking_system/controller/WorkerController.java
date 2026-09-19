package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Worker;
import com.textile.smart_textile_tracking_system.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping
    public Worker saveWorker(@RequestBody Worker worker) {
        return workerService.saveWorker(worker);
    }

    @GetMapping
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/{id}")
    public Optional<Worker> getWorkerById(@PathVariable Long id) {
        return workerService.getWorkerById(id);
    }

    @GetMapping("/department/{department}")
    public List<Worker> getWorkersByDepartment(@PathVariable String department) {
        return workerService.getWorkersByDepartment(department);
    }

    @GetMapping("/skill/{skill}")
    public List<Worker> getWorkersBySkill(@PathVariable String skill) {
        return workerService.getWorkersBySkill(skill);
    }

    @GetMapping("/availability/{availability}")
    public List<Worker> getWorkersByAvailability(@PathVariable String availability) {
        return workerService.getWorkersByAvailability(availability);
    }

    @DeleteMapping("/{id}")
    public String deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return "Worker deleted successfully";
    }
}
