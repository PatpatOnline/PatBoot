package cn.edu.buaa.patpat.boot.modules.discussion.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private int accountId;
    private int discussionId;
    private String buaaId;
}
