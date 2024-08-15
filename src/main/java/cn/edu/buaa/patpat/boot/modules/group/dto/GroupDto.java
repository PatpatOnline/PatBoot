package cn.edu.buaa.patpat.boot.modules.group.dto;

import lombok.Data;

@Data
public class GroupDto {
    private int id;
    private String name;
    private String description;
    private boolean locked;
}
