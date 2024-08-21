package cn.edu.buaa.patpat.boot.modules.task.services.impl;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentInfoView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskScoreView;
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
            List<StudentInfoView> students,
            List<TaskScoreView> scores,
            String submissionPath,
            String archivePath,
            String archiveName
    ) throws IOException {

        Medias.ensureEmptyPath(archivePath);
        String zipFile = Path.of(archivePath, archiveName).toString();

        Zips.zip(submissionPath, zipFile, false);
        String reportPath = generateReport(students, scores, archivePath);
        Zips.update(zipFile, reportPath);

        return Medias.loadAsResource(zipFile);
    }

    private String generateReport(List<StudentInfoView> students, List<TaskScoreView> scores, String tempPath) throws IOException {
        Set<Integer> submittedSet = scores.stream().map(TaskScoreView::getStudentId).collect(Collectors.toSet());
        int total = students.size();
        int submitted = 0;
        List<StudentInfoView> notSubmitted = new ArrayList<>();

        for (StudentInfoView student : students) {
            if (submittedSet.contains(student.getStudentId())) {
                submitted++;
            } else {
                notSubmitted.add(student);
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Submission Report\n\n");
        report.append("        Total: ").append(total).append("\n");
        report.append("    Submitted: ").append(submitted).append("\n");
        report.append("Not submitted: ").append(notSubmitted.size()).append("\n");
        if (submitted != total) {
            report.append("\nNot submitted students: \n");
            for (StudentInfoView student : notSubmitted) {
                report.append("\t").append(student.getBuaaId())
                        .append("\t").append(student.getName()).append("\n");
            }
        }

        Path reportPath = Path.of(tempPath, "README.txt");
        Files.writeString(reportPath, report.toString(), StandardOpenOption.CREATE);

        return reportPath.toString();
    }
}