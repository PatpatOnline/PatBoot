package cn.edu.buaa.patpat.boot.modules.statistics.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskIndexView implements Serializable {
    private int id;
    private int type;
    private String title;
}
