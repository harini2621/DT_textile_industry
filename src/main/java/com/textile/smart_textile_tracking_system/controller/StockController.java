package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

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
}
