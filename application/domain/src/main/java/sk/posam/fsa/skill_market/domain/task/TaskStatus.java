package sk.posam.fsa.skill_market.domain.task;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    IN_REVIEW,
    DONE;

    public static TaskStatus from(String value) {
        try {
            return TaskStatus.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported task status: " + value, exception);
        }
    }
}
