package cn.edu.buaa.patpat.boot.modules.group.services.impl;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupInfoView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupScoreInfoView;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DownloadAgent {
    public Resource download(
            List<GroupInfoView> groups,
            List<GroupScoreInfoView> scores,
            String submissionPath,
            String archivePath,
            String archiveName
    ) throws IOException {

        Medias.ensureEmptyPath(archivePath);
        String zipFile = Path.of(archivePath, archiveName).toString();

        Zips.zip(submissionPath, zipFile, false);
        String reportPath = generateReport(groups, scores, archivePath);
        Zips.update(zipFile, reportPath);

        return Medias.loadAsResource(zipFile);
    }

    private String generateReport(List<GroupInfoView> groups, List<GroupScoreInfoView> scores, String tempPath) throws IOException {
        Set<Integer> submittedSet = scores.stream().map(GroupScoreInfoView::getGroupId).collect(Collectors.toSet());
        int total = groups.size();
        int submitted = 0;
        List<GroupInfoView> notSubmitted = new ArrayList<>();

        for (GroupInfoView group : groups) {
            if (submittedSet.contains(group.getId())) {
                submitted++;
            } else {
                notSubmitted.add(group);
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Project Submission Report\n\n");
        report.append("        Total: ").append(total).append("\n");
        report.append("    Submitted: ").append(submitted).append("\n");
        report.append("Not submitted: ").append(notSubmitted.size()).append("\n");
        if (submitted != total) {
            report.append("\nNot submitted groups: \n");
            for (GroupInfoView group : notSubmitted) {
                report.append("\t").append(group.getName()).append("\n");
            }
        }

        Path reportFile = Path.of(tempPath, "README.txt");
        Files.writeString(reportFile, report.toString(), StandardOpenOption.CREATE);

        return reportFile.toString();
    }
}
