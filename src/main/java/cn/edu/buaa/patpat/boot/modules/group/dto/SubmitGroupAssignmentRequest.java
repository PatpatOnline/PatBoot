package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class SubmitGroupAssignmentRequest {
    private int courseId;
    private Group group;
    private List<GroupMemberView> members;
    private MultipartFile file;
}
