package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Stock;
import com.textile.smart_textile_tracking_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/worker-stock")
    public String workerStockPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        List<Stock> stocks = stockService.getWorkerStocks(username);

        model.addAttribute("stocks", stocks);
        model.addAttribute("totalItems",    stocks.size());
        model.addAttribute("availableItems",stocks.stream().filter(s -> s.getQuantity() > 10).count());
        model.addAttribute("lowStockItems", stocks.stream().filter(s -> s.getQuantity() > 0 && s.getQuantity() <= 10).count());
        model.addAttribute("outOfStock",    stocks.stream().filter(s -> s.getQuantity() == 0).count());
        return "worker-stock";
    }

    @GetMapping("/worker-add-stock")
    public String addStockPage(Model model) {
        model.addAttribute("stock", new Stock());
        return "worker-add-stock";
    }

    @PostMapping("/save-stock")
    public String saveStock(@ModelAttribute Stock stock,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if (stock == null || stock.getProductName() == null || stock.getProductName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Product name is required.");
            return "redirect:/worker-add-stock";
        }
        if (stock.getQuantity() < 0) {
            redirectAttributes.addFlashAttribute("error", "Quantity cannot be negative.");
            return "redirect:/worker-add-stock";
        }
        stock.setWorkerUsername(userDetails.getUsername());
        stockService.saveStock(stock);
        redirectAttributes.addFlashAttribute("success", "Stock added successfully.");
        return "redirect:/worker-stock";
    }

    @GetMapping("/owner-search")
    public String ownerSearch(Model model) {
        model.addAttribute("productName", "");
        model.addAttribute("stocks", stockService.getAllStocks());
        model.addAttribute("totalStock",     stockService.getAllStocks().stream().mapToLong(s -> s.getQuantity()).sum());
        model.addAttribute("availableCount", stockService.getAllStocks().stream().filter(s -> s.getQuantity() > 10).count());
        model.addAttribute("lowStockCount",  stockService.getAllStocks().stream().filter(s -> s.getQuantity() > 0 && s.getQuantity() <= 10).count());
        model.addAttribute("outOfStockCount",stockService.getAllStocks().stream().filter(s -> s.getQuantity() == 0).count());
        return "owner-search";
    }

    @PostMapping("/search-stock")
    public String searchStock(@RequestParam String productName, Model model) {
        model.addAttribute("productName", productName);
        model.addAttribute("stocks", stockService.searchProduct(productName));
        model.addAttribute("totalStock",     stockService.getAllStocks().stream().mapToLong(s -> s.getQuantity()).sum());
        model.addAttribute("availableCount", stockService.getAllStocks().stream().filter(s -> s.getQuantity() > 10).count());
        model.addAttribute("lowStockCount",  stockService.getAllStocks().stream().filter(s -> s.getQuantity() > 0 && s.getQuantity() <= 10).count());
        model.addAttribute("outOfStockCount",stockService.getAllStocks().stream().filter(s -> s.getQuantity() == 0).count());
        return "owner-search";
    }

    @GetMapping("/delete-stock/{id}")
    public String deleteStock(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Stock existing = stockService.getStock(id);
        if (existing == null || !userDetails.getUsername().equals(existing.getWorkerUsername())) {
            redirectAttributes.addFlashAttribute("error", "Stock not found or not owned by you.");
            return "redirect:/worker-stock";
        }
        stockService.deleteStock(id);
        redirectAttributes.addFlashAttribute("success", "Stock deleted successfully.");
        return "redirect:/worker-stock";
    }
}
