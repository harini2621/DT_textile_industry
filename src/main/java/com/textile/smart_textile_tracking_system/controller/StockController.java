package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Stock;
import com.textile.smart_textile_tracking_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    // Worker Stock Page
    @GetMapping("/worker-stock")
    public String workerStockPage(Model model) {

        model.addAttribute("stock", new Stock());
        model.addAttribute("stocks", stockService.getAllStocks());

        return "worker-stock";
    }

    // Save Stock
    @PostMapping("/save-stock")
    public String saveStock(@ModelAttribute Stock stock) {

        stockService.saveStock(stock);

        return "redirect:/worker-stock";
    }

    // Owner Search Page
    @GetMapping("/owner-search")
    public String ownerSearch(Model model) {

        model.addAttribute("productName", "");
        model.addAttribute("stocks", stockService.getAllStocks());

        return "owner-search";
    }

    // Search Product
    @PostMapping("/search-stock")
    public String searchStock(@RequestParam String productName,
                              Model model) {

        model.addAttribute("productName", productName);
        model.addAttribute("stocks",
                stockService.searchProduct(productName));

        return "owner-search";
    }

    // Delete Stock
    @GetMapping("/delete-stock/{id}")
    public String deleteStock(@PathVariable Long id) {

        stockService.deleteStock(id);

        return "redirect:/worker-stock";
    }
}