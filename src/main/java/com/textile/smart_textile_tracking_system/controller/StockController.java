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

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/worker-stock")
    public String workerStockPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("stocks", stockService.getWorkerStocks(username));
        model.addAttribute("totalItems",    stockService.getWorkerStocks(username).size());
        model.addAttribute("availableItems",stockService.getWorkerStocks(username).stream().filter(s -> s.getQuantity() > 10).count());
        model.addAttribute("lowStockItems", stockService.getWorkerStocks(username).stream().filter(s -> s.getQuantity() > 0 && s.getQuantity() <= 10).count());
        model.addAttribute("outOfStock",    stockService.getWorkerStocks(username).stream().filter(s -> s.getQuantity() == 0).count());
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
    public String deleteStock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        stockService.deleteStock(id);
        redirectAttributes.addFlashAttribute("success", "Stock deleted successfully.");
        return "redirect:/worker-stock";
    }
}
