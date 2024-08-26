package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupMember implements Serializable {
    private int id;
    private int courseId;
    private int groupId;
    private int accountId;
    private boolean owner;
    private int weight;
}
