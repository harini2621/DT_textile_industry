package com.textile.smart_textile_tracking_system.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.ProductionOrder;
import com.textile.smart_textile_tracking_system.entity.Worker;
import com.textile.smart_textile_tracking_system.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reports/export")
public class ReportExportController {

    @Autowired private WorkerService workerService;
    @Autowired private OrderService orderService;
    @Autowired private ProductionOrderService productionOrderService;
    @Autowired private StockService stockService;
    @Autowired private UserService userService;

    // ─── PDF helpers ────────────────────────────────────────────────────────────

    private PdfPTable headerRow(String... cols) {
        PdfPTable table = new PdfPTable(cols.length);
        table.setWidthPercentage(100);
        BaseColor headerColor = new BaseColor(13, 110, 253);
        com.itextpdf.text.Font hf = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10,
                com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        for (String col : cols) {
            PdfPCell cell = new PdfPCell(new Phrase(col, hf));
            cell.setBackgroundColor(headerColor);
            cell.setPadding(6);
            table.addCell(cell);
        }
        return table;
    }

    private void addCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value == null ? "-" : value,
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9)));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void startPdf(HttpServletResponse response, String filename) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    private Document openDoc(HttpServletResponse response) throws DocumentException {
        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, response.getOutputStream());
        } catch (IOException e) {
            throw new DocumentException(e);
        }
        doc.open();
        return doc;
    }

    private void addTitle(Document doc, String title) throws DocumentException {
        com.itextpdf.text.Font tf = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
                com.itextpdf.text.Font.BOLD, new BaseColor(13, 110, 253));
        Paragraph p = new Paragraph("TRIZEN — " + title, tf);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(12);
        doc.add(p);
    }

    // ─── Excel helpers ───────────────────────────────────────────────────────────

    private void startExcel(HttpServletResponse response, String filename) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    private CellStyle headerStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private Row createHeaderRow(Sheet sheet, CellStyle style, String... cols) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < cols.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════════
    // 1. WORKER REPORT
    // ════════════════════════════════════════════════════════════════════════════

    @GetMapping("/worker/pdf")
    public void workerPdf(HttpServletResponse response) throws Exception {
        startPdf(response, "worker-report.pdf");
        Document doc = openDoc(response);
        addTitle(doc, "Worker Report");
        List<Worker> workers = workerService.getAllWorkers();
        PdfPTable table = headerRow("#", "Name", "Skill", "Department", "Phone", "Availability");
        int i = 1;
        for (Worker w : workers) {
            addCell(table, String.valueOf(i++));
            addCell(table, w.getWorkerName());
            addCell(table, w.getSkill());
            addCell(table, w.getDepartment());
            addCell(table, w.getPhoneNumber());
            addCell(table, w.getAvailability());
        }
        doc.add(table);
        doc.close();
    }

    @GetMapping("/worker/excel")
    public void workerExcel(HttpServletResponse response) throws Exception {
        startExcel(response, "worker-report.xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Workers");
            CellStyle style = headerStyle(wb);
            createHeaderRow(sheet, style, "#", "Name", "Skill", "Department", "Phone", "Availability");
            List<Worker> workers = workerService.getAllWorkers();
            int r = 1;
            for (Worker w : workers) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(r - 1);
                row.createCell(1).setCellValue(w.getWorkerName());
                row.createCell(2).setCellValue(w.getSkill());
                row.createCell(3).setCellValue(w.getDepartment());
                row.createCell(4).setCellValue(w.getPhoneNumber());
                row.createCell(5).setCellValue(w.getAvailability());
            }
            for (int i = 0; i < 6; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // 2. OWNER REPORT  (all orders)
    // ════════════════════════════════════════════════════════════════════════════

    @GetMapping("/owner/pdf")
    public void ownerPdf(HttpServletResponse response) throws Exception {
        startPdf(response, "owner-report.pdf");
        Document doc = openDoc(response);
        addTitle(doc, "Owner Report");
        List<Order> orders = orderService.getAllOrders();
        PdfPTable table = headerRow("#", "Product", "Qty", "Owner", "Worker", "Status", "Request Date");
        int i = 1;
        for (Order o : orders) {
            addCell(table, String.valueOf(i++));
            addCell(table, o.getProductName());
            addCell(table, String.valueOf(o.getQuantity()));
            addCell(table, o.getOwnerUsername());
            addCell(table, o.getWorkerUsername());
            addCell(table, o.getStatus());
            addCell(table, o.getRequestDate() != null ? o.getRequestDate().toString() : "-");
        }
        doc.add(table);
        doc.close();
    }

    @GetMapping("/owner/excel")
    public void ownerExcel(HttpServletResponse response) throws Exception {
        startExcel(response, "owner-report.xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Owner Orders");
            CellStyle style = headerStyle(wb);
            createHeaderRow(sheet, style, "#", "Product", "Qty", "Owner", "Worker", "Status", "Request Date");
            List<Order> orders = orderService.getAllOrders();
            int r = 1;
            for (Order o : orders) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(r - 1);
                row.createCell(1).setCellValue(o.getProductName());
                row.createCell(2).setCellValue(o.getQuantity());
                row.createCell(3).setCellValue(o.getOwnerUsername());
                row.createCell(4).setCellValue(o.getWorkerUsername() != null ? o.getWorkerUsername() : "-");
                row.createCell(5).setCellValue(o.getStatus());
                row.createCell(6).setCellValue(o.getRequestDate() != null ? o.getRequestDate().toString() : "-");
            }
            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // 3. PRODUCTION REPORT
    // ════════════════════════════════════════════════════════════════════════════

    @GetMapping("/production/pdf")
    public void productionPdf(HttpServletResponse response) throws Exception {
        startPdf(response, "production-report.pdf");
        Document doc = openDoc(response);
        addTitle(doc, "Production Report");
        List<ProductionOrder> orders = productionOrderService.getAllProductionOrders();
        PdfPTable table = headerRow("#", "Order No.", "Product", "Qty", "Status", "Deadline");
        int i = 1;
        for (ProductionOrder p : orders) {
            addCell(table, String.valueOf(i++));
            addCell(table, p.getOrderNumber());
            addCell(table, p.getProductName());
            addCell(table, String.valueOf(p.getQuantity()));
            addCell(table, p.getStatus());
            addCell(table, p.getDeadline());
        }
        doc.add(table);
        doc.close();
    }

    @GetMapping("/production/excel")
    public void productionExcel(HttpServletResponse response) throws Exception {
        startExcel(response, "production-report.xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Production");
            CellStyle style = headerStyle(wb);
            createHeaderRow(sheet, style, "#", "Order No.", "Product", "Qty", "Status", "Deadline");
            List<ProductionOrder> orders = productionOrderService.getAllProductionOrders();
            int r = 1;
            for (ProductionOrder p : orders) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(r - 1);
                row.createCell(1).setCellValue(p.getOrderNumber());
                row.createCell(2).setCellValue(p.getProductName());
                row.createCell(3).setCellValue(p.getQuantity());
                row.createCell(4).setCellValue(p.getStatus());
                row.createCell(5).setCellValue(p.getDeadline());
            }
            for (int i = 0; i < 6; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // 4. BOOKING REPORT  (orders with status filter via ?status=)
    // ════════════════════════════════════════════════════════════════════════════

    @GetMapping("/booking/pdf")
    public void bookingPdf(HttpServletResponse response,
                           @RequestParam(defaultValue = "") String status) throws Exception {
        startPdf(response, "booking-report.pdf");
        Document doc = openDoc(response);
        addTitle(doc, "Booking Report" + (status.isBlank() ? "" : " — " + status));
        List<Order> orders = status.isBlank()
                ? orderService.getAllOrders()
                : orderService.getOrdersByStatus(status);
        PdfPTable table = headerRow("#", "Product", "Qty", "Owner", "Worker", "Status", "Request Date", "Delivery Date");
        int i = 1;
        for (Order o : orders) {
            addCell(table, String.valueOf(i++));
            addCell(table, o.getProductName());
            addCell(table, String.valueOf(o.getQuantity()));
            addCell(table, o.getOwnerUsername());
            addCell(table, o.getWorkerUsername());
            addCell(table, o.getStatus());
            addCell(table, o.getRequestDate() != null ? o.getRequestDate().toString() : "-");
            addCell(table, o.getExpectedDeliveryDate() != null ? o.getExpectedDeliveryDate().toString() : "-");
        }
        doc.add(table);
        doc.close();
    }

    @GetMapping("/booking/excel")
    public void bookingExcel(HttpServletResponse response,
                             @RequestParam(defaultValue = "") String status) throws Exception {
        startExcel(response, "booking-report.xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Bookings");
            CellStyle style = headerStyle(wb);
            createHeaderRow(sheet, style, "#", "Product", "Qty", "Owner", "Worker", "Status", "Request Date", "Delivery Date");
            List<Order> orders = status.isBlank()
                    ? orderService.getAllOrders()
                    : orderService.getOrdersByStatus(status);
            int r = 1;
            for (Order o : orders) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(r - 1);
                row.createCell(1).setCellValue(o.getProductName());
                row.createCell(2).setCellValue(o.getQuantity());
                row.createCell(3).setCellValue(o.getOwnerUsername());
                row.createCell(4).setCellValue(o.getWorkerUsername() != null ? o.getWorkerUsername() : "-");
                row.createCell(5).setCellValue(o.getStatus());
                row.createCell(6).setCellValue(o.getRequestDate() != null ? o.getRequestDate().toString() : "-");
                row.createCell(7).setCellValue(o.getExpectedDeliveryDate() != null ? o.getExpectedDeliveryDate().toString() : "-");
            }
            for (int i = 0; i < 8; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }

    // ════════════════════════════════════════════════════════════════════════════
    // 5. PAYMENT REPORT  (completed orders only)
    // ════════════════════════════════════════════════════════════════════════════

    @GetMapping("/payment/pdf")
    public void paymentPdf(HttpServletResponse response) throws Exception {
        startPdf(response, "payment-report.pdf");
        Document doc = openDoc(response);
        addTitle(doc, "Payment Report");
        List<Order> completed = orderService.getOrdersByStatus("Completed");
        long totalQty = completed.stream().mapToLong(Order::getQuantity).sum();

        PdfPTable table = headerRow("#", "Product", "Qty", "Owner", "Worker", "Delivery Date");
        int i = 1;
        for (Order o : completed) {
            addCell(table, String.valueOf(i++));
            addCell(table, o.getProductName());
            addCell(table, String.valueOf(o.getQuantity()));
            addCell(table, o.getOwnerUsername());
            addCell(table, o.getWorkerUsername());
            addCell(table, o.getExpectedDeliveryDate() != null ? o.getExpectedDeliveryDate().toString() : "-");
        }
        doc.add(table);

        com.itextpdf.text.Font sf = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 11,
                com.itextpdf.text.Font.BOLD);
        Paragraph summary = new Paragraph(
                "\nTotal Completed Jobs: " + completed.size() + "    |    Total Quantity Produced: " + totalQty, sf);
        summary.setSpacingBefore(10);
        doc.add(summary);
        doc.close();
    }

    @GetMapping("/payment/excel")
    public void paymentExcel(HttpServletResponse response) throws Exception {
        startExcel(response, "payment-report.xlsx");
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Payments");
            CellStyle style = headerStyle(wb);
            createHeaderRow(sheet, style, "#", "Product", "Qty", "Owner", "Worker", "Delivery Date");
            List<Order> completed = orderService.getOrdersByStatus("Completed");
            int r = 1;
            for (Order o : completed) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(r - 1);
                row.createCell(1).setCellValue(o.getProductName());
                row.createCell(2).setCellValue(o.getQuantity());
                row.createCell(3).setCellValue(o.getOwnerUsername());
                row.createCell(4).setCellValue(o.getWorkerUsername() != null ? o.getWorkerUsername() : "-");
                row.createCell(5).setCellValue(o.getExpectedDeliveryDate() != null ? o.getExpectedDeliveryDate().toString() : "-");
            }
            // Summary row
            long totalQty = completed.stream().mapToLong(Order::getQuantity).sum();
            Row sumRow = sheet.createRow(r + 1);
            CellStyle bold = wb.createCellStyle();
            Font bf = wb.createFont(); bf.setBold(true); bold.setFont(bf);
            Cell label = sumRow.createCell(1); label.setCellValue("Total Jobs: " + completed.size()); label.setCellStyle(bold);
            Cell qty   = sumRow.createCell(2); qty.setCellValue("Total Qty: " + totalQty);           qty.setCellStyle(bold);

            for (int i = 0; i < 6; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }
}
