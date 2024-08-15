package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

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
            throw new BadRequestException(M("task.type.invalid"));
        }
    }

    public static String toString(int type) {
        if (type == LAB) {
            return M("task.lab");
        } else if (type == ITERATION) {
            return M("task.iteration");
        } else {
            throw new BadRequestException(M("task.type.invalid"));
        }
    }

    public static String toTag(int type) {
        if (type == LAB) {
            return Globals.LAB_TAG;
        } else if (type == ITERATION) {
            return Globals.ITERATION_TAG;
        } else {
            throw new BadRequestException(M("task.type.invalid"));
        }
    }

    public void validate(int type) {
        if (type != LAB && type != ITERATION) {
            throw new BadRequestException(M("task.type.invalid"));
        }
    }
}
