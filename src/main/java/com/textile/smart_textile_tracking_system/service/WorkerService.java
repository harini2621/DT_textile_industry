package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Worker;
import com.textile.smart_textile_tracking_system.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    // Save Worker
    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    // Get All Workers
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    // Count Workers
    public long countWorkers() {
        return workerRepository.count();
    }

    // Get Worker By ID
    public Optional<Worker> getWorkerById(Long id) {
        return workerRepository.findById(id);
    }

    // Get Workers By Department
    public List<Worker> getWorkersByDepartment(String department) {
        return workerRepository.findByDepartment(department);
    }

    // Get Workers By Skill
    public List<Worker> getWorkersBySkill(String skill) {
        return workerRepository.findBySkill(skill);
    }

    // Get Workers By Availability
    public List<Worker> getWorkersByAvailability(String availability) {
        return workerRepository.findByAvailability(availability);
    }

    // Delete Worker
    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }
}