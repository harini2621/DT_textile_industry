package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Report;
import com.textile.smart_textile_tracking_system.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public Report saveReport(@RequestBody Report report) {
        return reportService.saveReport(report);
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Optional<Report> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @GetMapping("/type/{reportType}")
    public List<Report> getReportsByType(@PathVariable String reportType) {
        return reportService.getReportsByType(reportType);
    }

    @GetMapping("/generatedby/{generatedBy}")
    public List<Report> getReportsByGeneratedBy(@PathVariable String generatedBy) {
        return reportService.getReportsByGeneratedBy(generatedBy);
    }

    @DeleteMapping("/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "Report deleted successfully";
    }
}