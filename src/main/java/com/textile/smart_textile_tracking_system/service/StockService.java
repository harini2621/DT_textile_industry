package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Stock;
import com.textile.smart_textile_tracking_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // Save Stock
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // Get All Stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // Get Stock By ID
    public Stock getStock(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    // Get Worker Stocks
    public List<Stock> getWorkerStocks(String workerUsername) {
        return stockRepository.findByWorkerUsername(workerUsername);
    }

    // Search Available Stock
    public List<Stock> searchProduct(String productName) {
        return stockRepository
                .findByProductNameContainingIgnoreCaseAndQuantityGreaterThan(productName, 0);
    }

    // Delete Stock
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}