package cn.edu.buaa.patpat.boot.modules.group.services;

import cn.edu.buaa.patpat.boot.common.Tuple;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.group.dto.CreateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.SubmitGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.dto.UpdateGroupAssignmentRequest;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupAssignment;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupScore;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupAssignmentMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupScoreMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.mappers.GroupStatisticsMapper;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupInfoView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupMemberView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreInfoView;
import cn.edu.buaa.patpat.boot.modules.group.services.impl.DownloadAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupAssignmentService extends BaseService {
    private final GroupAssignmentMapper groupAssignmentMapper;
    private final BucketApi bucketApi;
    private final GroupScoreMapper groupScoreMapper;
    private final GroupStatisticsMapper groupStatisticsMapper;
    private final DownloadAgent downloadAgent = new DownloadAgent();

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

    public GroupScore submit(SubmitGroupAssignmentRequest request) {
        validateAssignment(request.getCourseId(), true, true);

        var submitPath = getSubmitFileRecord(request.getCourseId(), request.getGroup().getId());
        try {
            Medias.ensureEmptyParentPath(submitPath.second);
            Medias.save(submitPath.second, request.getFile());
            saveReadmeFile(Medias.getParentPath(submitPath.second).toString(), request.getGroup(), request.getMembers());
        } catch (IOException e) {
            log.error("Failed to save the submitted project", e);
            throw new InternalServerErrorException(M("system.error.io"));
        }

        GroupScore score = new GroupScore();
        score.setGroupId(request.getGroup().getId());
        score.setCourseId(request.getCourseId());
        score.setScore(Globals.NOT_GRADED);
        score.setRecord(submitPath.first);
        groupScoreMapper.saveOrUpdate(score);
        return groupScoreMapper.find(score.getGroupId());
    }

    public Resource download(int courseId, int groupId, boolean admin) {
        validateAssignment(courseId, !admin, false);
        if (admin) {
            return downloadAdmin(courseId, groupId);
        } else {
            return downloadStudent(courseId, groupId);
        }
    }

    public String getArtifactName(int groupId) {
        return String.format("artifact-%d.zip", groupId);
    }

    public synchronized Resource downloadAll(int courseId) {
        get(courseId);  // ensure the assignment exists

        List<GroupInfoView> groups = groupStatisticsMapper.getGroups(courseId);
        List<GroupScoreInfoView> scores = groupStatisticsMapper.getGroupScores(courseId);
        String submissionPath = getGroupProjectSubmissionPath(courseId);
        String archivePath = bucketApi.recordToPrivatePath(bucketApi.toRecord(
                Globals.TEMP_TAG,
                "proj-download"));
        String archiveName = getArchiveName();
        try {
            Medias.ensurePath(submissionPath);  // prevent empty zip error
            return downloadAgent.download(groups, scores, submissionPath, archivePath, archiveName);
        } catch (IOException e) {
            log.error("Failed to download group projects {}", e.getMessage());
            throw new InternalServerErrorException(M("system.error.io"));
        }
    }

    private String getGroupProjectSubmissionPath(int courseId) {
        String record = bucketApi.toRecord(Globals.PROJECT_TAG, String.valueOf(courseId));
        return bucketApi.recordToPrivatePath(record);
    }

    private String getArchiveName() {
        return String.format("artifacts-%s.zip",
                DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT).format(LocalDateTime.now()));
    }

    private Resource downloadAdmin(int courseId, int groupId) {
        String path = Medias.getParentPath(getSubmitFileRecord(courseId, groupId).second).toString();
        try {
            String zipFilePath = bucketApi.recordToPrivatePath(bucketApi.toRandomRecord(Globals.TEMP_TAG, "")) + ".zip";
            Zips.zip(path, zipFilePath, false);
            return Medias.loadAsResource(zipFilePath);
        } catch (IOException e) {
            throw new NotFoundException(M("group.assignment.download.error"));
        }
    }

    private Resource downloadStudent(int courseId, int groupId) {
        String path = getSubmitFileRecord(courseId, groupId).second;
        try {
            return Medias.loadAsResource(path);
        } catch (IOException e) {
            throw new NotFoundException(M("group.assignment.download.error"));
        }
    }

    private void validateAssignment(int courseId, boolean checkVisible, boolean checkTime) {
        GroupAssignment assignment = get(courseId);
        if (checkVisible && !assignment.isVisible()) {
            throw new NotFoundException(M("group.assignment.exists.not"));
        }
        if (checkTime) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(assignment.getStartTime()) || now.isAfter(assignment.getEndTime())) {
                throw new NotFoundException(M("group.assignment.submit.forbidden"));
            }
        }
    }

    private Tuple<String, String> getSubmitFileRecord(int courseId, int groupId) {
        String record = bucketApi.toRecord(
                Globals.PROJECT_TAG,
                String.valueOf(courseId),
                String.valueOf(groupId),
                getArtifactName(groupId)
        );
        String path = bucketApi.recordToPrivatePath(record);
        return new Tuple<>(record, path);
    }

    private void saveReadmeFile(String path, Group group, List<GroupMemberView> members) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("Group ").append("[").append(group.getId()).append("]: ");
        builder.append(group.getName()).append("\n");

        builder.append("Description: ").append(group.getDescription()).append("\n");

        builder.append("Members: ").append("\n");
        for (var member : members) {
            builder.append("    ")
                    .append(member.getBuaaId())
                    .append(" ").append(member.getName());
            if (member.isOwner()) {
                builder.append(" (Leader)");
            }
            builder.append(String.format(" (%.2f)", (double) member.getWeight() / 100.0)).append("\n");
        }

        Files.writeString(Path.of(path, "README.txt"),
                builder.toString(),
                StandardOpenOption.CREATE);
    }
}
