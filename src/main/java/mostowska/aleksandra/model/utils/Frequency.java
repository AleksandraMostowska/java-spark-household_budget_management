package mostowska.aleksandra.model.utils;

import java.time.LocalDateTime;

/**
 * Represents how often an event, such as an income or expense, occurs.
 * Includes standard intervals as well as a `CUSTOM` option for user-defined frequencies.
 */
public enum Frequency {
    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUAL,
    CUSTOM;

    /**
     * Calculates the next occurrence of an event based on the current frequency and a custom interval.
     *
     * @param startDate the starting date of the event
     * @param customInterval a custom interval (in days) for the `CUSTOM` frequency
     * @return the new date of the next occurrence or `null` if it only occurs once
     * @throws IllegalArgumentException if `CUSTOM` is selected but no custom interval is provided
     */
    public LocalDateTime renew(LocalDateTime startDate, Long customInterval) {
        if (this == CUSTOM && customInterval == null) {
            throw new IllegalArgumentException("Custom interval must be provided for CUSTOM frequency.");
        }

        return switch (this) {
            case ONCE -> null;
            case DAILY -> startDate.plusDays(1);
            case WEEKLY -> startDate.plusWeeks(1);
            case MONTHLY -> startDate.plusMonths(1);
            case QUARTERLY -> startDate.plusMonths(3);
            case ANNUAL -> startDate.plusYears(1);
            case CUSTOM -> startDate.plusDays(customInterval);
            default -> throw new IllegalArgumentException("Custom interval cannot be null for CUSTOM frequency.");
        };
    }
}
