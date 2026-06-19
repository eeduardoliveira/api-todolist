package com.example.demo.domain.model;

/**
 * Record to hold task statistics grouped by status
 * @author system
 */
public record TaskStatistics(
        long pendingCount,
        long inProgressCount,
        long completedCount,
        long totalCount
) {
}
