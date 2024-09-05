/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.statistics.services.impl;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.excel.ExcelHelper;
import cn.edu.buaa.patpat.boot.common.utils.excel.Excels;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.statistics.models.entities.ScoreConfig;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.GroupScoreIndexView;
import cn.edu.buaa.patpat.boot.modules.statistics.models.views.StudentIndexView;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoreExporter {
    private final BucketApi bucketApi;

    public Resource export(ExportScoreRequest request) {
        // prepare workbook
        String filename = String.format("%s-成绩快照-%s.xlsx",
                request.getCourseName(),
                request.getTimestamp().format(DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT)));
        String path = bucketApi.getTempFile(filename);
        try (Workbook workbook = Excels.getWorkbook(); OutputStream out = Medias.getOutputStream(path)) {
            for (var teacher : request.getTeachers()) {
                exportTeacher(workbook, teacher.getId(), request);
            }
            workbook.write(out);
            return Medias.loadAsResource(path, true);
        } catch (Exception e) {
            log.error("Failed to export score", e);
            throw new InternalServerErrorException(M("score.export.error"));
        }
    }

    private void exportTeacher(Workbook workbook, int teacherId, ExportScoreRequest request) {
        TeacherIndexView teacher = request.getTeachers().stream().filter(t -> t.getId() == teacherId).findFirst().orElse(null);
        if (teacher == null) {
            log.warn("Teacher not found: {}", teacherId);
            return;
        }

        var students = request.getStudents().stream().filter(s -> s.getTeacherId() == teacherId).toList();
        if (students.isEmpty()) {
            log.warn("No student found for teacher: {}", teacher.getName());
            return;
        }

        var sheet = workbook.createSheet(
                String.format("折合成绩（%s）", teacher.getName()));
        exportReducedScore(sheet, students, request);
        sheet = workbook.createSheet(
                String.format("原始成绩（%s）", teacher.getName()));
        exportFullScore(sheet, students, request);
    }

    /**
     * Merge all lab scores, iteration scores and convert using the config.
     */
    private void exportReducedScore(Sheet sheet, List<StudentIndexView> students, ExportScoreRequest request) {
        writeBanner(sheet, 0, request);
        writeConfig(sheet, 2, request.getConfig());
        int rowCursor = 3;

        var helper = ExcelHelper.open(sheet)
                .createRow(rowCursor++, row -> row
                        .createCenteredCell(0, "序号")
                        .createCenteredCell(1, "学号")
                        .createCenteredCell(2, "姓名")
                        .createCenteredCell(3, "Lab")
                        .createCenteredCell(4, "迭代")
                        .createCenteredCell(5, "大作业"));

        for (var student : students) {
            final int index = rowCursor - 3;
            final int labScore = calculateLabScore(student.getAccountId(), request);
            final int iterScore = calculateIterScore(student.getAccountId(), request);
            final int projScore = calculateProjScore(student.getAccountId(), request);
            helper.createRow(rowCursor++, row -> row
                    .createCell(0, index)
                    .createCenteredCell(1, student.getBuaaId())
                    .createCenteredCell(2, student.getName())
                    .createCenteredCell(3, labScore)
                    .createCenteredCell(4, iterScore)
                    .createCenteredCell(5, projScore));
        }

        helper.setColumnWidth(0, 10)
                .setColumnsWidth(1, 2, 14)
                .setColumnsWidth(3, 5, 10);
    }

    private void exportFullScore(Sheet sheet, List<StudentIndexView> students, ExportScoreRequest request) {
        writeBanner(sheet, 0, request);

        int rowCursor = 2;

        var helper = ExcelHelper.open(sheet)
                .createRow(rowCursor++, row -> row.createCenteredCell(0, "序号")
                        .createCenteredCell(1, "学号")
                        .createCenteredCell(2, "姓名"));

        int startRow = rowCursor;
        for (var student : students) {
            final int index = rowCursor - startRow + 1;
            helper.createRow(rowCursor++, row -> row.createCell(0, index)
                    .createCenteredCell(1, student.getBuaaId())
                    .createCenteredCell(2, student.getName()));
        }

        int colCursor = 3;
        for (var task : request.getTasks().stream().filter(t -> t.getType() == TaskTypes.LAB).toList()) {
            final int column = colCursor++;
            helper.getRow(startRow - 1, row -> row.createCell(column, task.getTitle()));
            fillTaskScore(sheet, startRow, column, task.getId(), students, request);
        }
        for (var task : request.getTasks().stream().filter(t -> t.getType() == TaskTypes.ITERATION).toList()) {
            final int column = colCursor++;
            helper.getRow(startRow - 1, row -> row.createCell(column, task.getTitle()));
            fillTaskScore(sheet, startRow, column, task.getId(), students, request);
        }

        {
            final int column = colCursor;
            helper.getRow(startRow - 1, row -> row.createCenteredCell(column, "大作业"));
            fillProjScore(sheet, startRow, column, students, request);
        }

        helper.setColumnWidth(0, 10)
                .setColumnsWidth(1, 2, 14)
                .setColumnsWidth(3, colCursor, 10);
    }

    private void fillTaskScore(Sheet sheet, int startRow, int column, int taskId, List<StudentIndexView> students, ExportScoreRequest request) {
        var helper = ExcelHelper.open(sheet);
        var taskScores = Optional.ofNullable(request.getTaskScores().get(taskId));
        for (var student : students) {
            helper.getRow(startRow++, row -> {
                var view = taskScores.map(s -> s.get(student.getAccountId()));
                var score = view.map(v -> v.isLate() ? v.getScore() / 2 : v.getScore()).orElse(0);
                row.createCenteredCell(column, score);
            });
        }
    }

    private void fillProjScore(Sheet sheet, int startRow, int column, List<StudentIndexView> students, ExportScoreRequest request) {
        var helper = ExcelHelper.open(sheet);
        var groupScores = request.getGroupScores();
        if (groupScores == null) {
            for (var ignored : students) {
                helper.getRow(startRow++, row -> row.createCell(column, 0));
            }
            return;
        }

        for (var student : students) {
            helper.getRow(startRow++, row -> {
                var score = Optional.ofNullable(groupScores.get(student.getAccountId()));
                row.createCenteredCell(column, score.map(GroupScoreIndexView::getScore).orElse(0));
            });
        }
    }

    private int calculateLabScore(int accountId, ExportScoreRequest request) {
        int totalScore = request.getLabScores().getOrDefault(accountId, 0);
        return (int) ((double) request.getConfig().getLabScore() * totalScore / (request.getTotalLabs() * Globals.FULL_SCORE_DOUBLE));
    }

    private int calculateIterScore(int accountId, ExportScoreRequest request) {
        int totalScore = request.getIterScores().getOrDefault(accountId, 0);
        return (int) ((double) request.getConfig().getIterScore() * totalScore / (request.getTotalIters() * Globals.FULL_SCORE_DOUBLE));
    }

    private int calculateProjScore(int accountId, ExportScoreRequest request) {
        var groupScores = request.getGroupScores();
        if (groupScores == null) {
            return 0;
        }
        GroupScoreIndexView score = groupScores.get(accountId);
        if (score == null) {
            return 0;
        }
        return (int) ((double) request.getConfig().getProjScore() * (Math.max(0, score.getScore()) / Globals.FULL_SCORE_DOUBLE));
    }

    private void writeBanner(Sheet sheet, int startRow, ExportScoreRequest request) {
        ExcelHelper.open(sheet)
                .createRow(startRow, row -> row
                        .createCenteredCell(0, "课程")
                        .createCenteredCell(1, request.getCourseName()))
                .createRow(startRow + 1, row -> row
                        .createCenteredCell(0, "导出时间")
                        .createCenteredCell(1, request.getTimestamp().format(DateTimeFormatter.ofPattern(Excels.DATE_TIME_FORMAT))))
                .mergeAndCenter(0, 1, 5)
                .mergeAndCenter(1, 1, 5);
    }

    private void writeConfig(Sheet sheet, int startRow, ScoreConfig config) {
        ExcelHelper.open(sheet)
                .createRow(startRow, row -> row
                        .createCenteredCell(0, "Lab")
                        .createCenteredCell(1, config.getLabScore())
                        .createCenteredCell(2, "迭代")
                        .createCenteredCell(3, config.getIterScore())
                        .createCenteredCell(4, "大作业")
                        .createCenteredCell(5, config.getProjScore()));
    }
}
