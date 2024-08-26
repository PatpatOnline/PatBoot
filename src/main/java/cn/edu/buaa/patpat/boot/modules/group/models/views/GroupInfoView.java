package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupInfoView implements Serializable {
    private int id;
    private String name;
}
