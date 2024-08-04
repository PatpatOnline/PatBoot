package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;

public class TaskTypes {
    public static final String LAB_STRING = "lab";
    public static final String ITERATION_STRING = "iter";

    public static final int LAB = 1;
    public static final int ITERATION = 2;

    public static int fromString(String type) {
        if (type.equalsIgnoreCase(LAB_STRING)) {
            return LAB;
        } else if (type.equalsIgnoreCase(ITERATION_STRING)) {
            return ITERATION;
        } else {
            throw new BadRequestException("Invalid task type");
        }
    }

    public void validate(int type) {
        if (type != LAB && type != ITERATION) {
            throw new BadRequestException("Invalid task type");
        }
    }
}
