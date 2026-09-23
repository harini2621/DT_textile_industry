package com.textile.smart_textile_tracking_system.entity;

/**
 * Centralized status constants for orders.
 * The DB stores the display string (e.g. "PENDING", "IN PROGRESS", "Completed");
 * lookups are done case-insensitively via findByStatusIgnoreCase.
 */
public enum OrderStatus {
    PENDING("PENDING"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    IN_PROGRESS("IN PROGRESS"),
    COMPLETED("Completed");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
