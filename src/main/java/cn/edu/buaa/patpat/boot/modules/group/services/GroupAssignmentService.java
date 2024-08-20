package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupAssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class GroupAssignmentService extends BaseService {
    private final GroupAssignmentMapper groupAssignmentMapper;

    public GroupAssignment create(int courseId, CreateGroupAssignmentRequest request) {
        GroupAssignment assignment = mappers.map(request, GroupAssignment.class);
        assignment.setCourseId(courseId);
        groupAssignmentMapper.saveOrUpdate(assignment);
        return assignment;
    }

    public GroupAssignment update(int courseId, UpdateGroupAssignmentRequest request) {
        GroupAssignment assignment = groupAssignmentMapper.find(courseId);
        if (assignment == null) {
            throw new NotFoundException(M("group.assignment.exists.not"));
        }
        mappers.map(request, assignment);
        if (assignment.getEndTime().isBefore(assignment.getStartTime())) {
            throw new BadRequestException(M("group.assignment.time.invalid"));
        }
        groupAssignmentMapper.saveOrUpdate(assignment);
        return assignment;
    }

    public void delete(int courseId) {
        int updated = groupAssignmentMapper.delete(courseId);
        if (updated == 0) {
            throw new NotFoundException(M("group.assignment.exists.not"));
        }
    }

    public GroupAssignment get(int courseId) {
        GroupAssignment assignment = groupAssignmentMapper.find(courseId);
        if (assignment == null) {
            throw new NotFoundException(M("group.assignment.exists.not"));
        }
        return assignment;
    }
}
