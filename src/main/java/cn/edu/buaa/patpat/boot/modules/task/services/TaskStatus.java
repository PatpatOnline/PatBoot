package cn.edu.buaa.patpat.boot.modules.task.services;

public enum TaskStatus {
    /**
     * Task submitted before the start time.
     */
    EARLY,

    /**
     * Task submitted before the deadline.
     */
    PUNCTUAL,

    /**
     * Task submitted after the deadline.
     */
    OVERDUE,

    /**
     * Task submitted after the end time.
     */
    LATE
}
