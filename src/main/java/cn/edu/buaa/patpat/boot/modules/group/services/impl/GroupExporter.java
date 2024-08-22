package cn.edu.buaa.patpat.boot.modules.group.services.impl;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.excel.ExcelHelper;
import cn.edu.buaa.patpat.boot.common.utils.excel.Excels;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.models.views.GroupView;
import cn.edu.buaa.patpat.boot.modules.group.models.views.RogueStudentView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupExporter {
    private final BucketApi bucketApi;

    public Resource exportGroups(
            String courseName,
            List<GroupView> groups,
            List<RogueStudentView> rogueStudents,
            GroupConfig config) {

        LocalDateTime timestamp = LocalDateTime.now();
        String path = bucketApi.getTempFile(String.format("%s-组队信息-%s.xlsx",
                courseName,
                DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT).format(timestamp)));

        try (Workbook workbook = Excels.getWorkbook(); OutputStream out = Medias.getOutputStream(path)) {
            Sheet sheet = workbook.createSheet("组队信息");
            writeHeader(sheet, courseName, timestamp);
            writeGroups(sheet, groups, config);
            applyStyles(sheet, config);
            sheet = workbook.createSheet("未组队");
            writeRogueStudents(sheet, rogueStudents);

            workbook.write(out);
            return Medias.loadAsResource(path, true);
        } catch (Exception e) {
            log.error("Failed to export groups: {}", e.getMessage());
            throw new InternalServerErrorException(M("group.export.error"));
        }
    }

    private void applyStyles(Sheet sheet, GroupConfig config) {
        ExcelHelper.open(sheet)
                .setColumnWidth(1, 20)
                .setColumnWidth(2, 40)
                .setColumnsWidth(3, 3 + config.getMaxSize() * 2, 14)
                .mergeAndCenter(0, 1, 2)
                .mergeAndCenter(0, 4, 5);
    }

    private void writeHeader(Sheet sheet, String courseName, LocalDateTime timestamp) {
        ExcelHelper.open(sheet)
                .createRow(0, row -> row.createCenteredCell(0, "课程")
                        .createCenteredCell(1, courseName)
                        .createCenteredCell(3, "导出时间")
                        .createCenteredCell(4, timestamp.format(DateTimeFormatter.ofPattern(Excels.DATE_TIME_FORMAT))));
    }

    private void writeGroups(Sheet sheet, List<GroupView> groups, GroupConfig config) {
        int rowCursor = 1;

        var helper = ExcelHelper.open(sheet);
        helper.createRow(rowCursor++, row -> {
            int colCursor = 0;
            row.createCenteredCell(colCursor++, "序号")
                    .createCenteredCell(colCursor++, "组名")
                    .createCenteredCell(colCursor++, "简介")
                    .createCenteredCell(colCursor++, "组长学号")
                    .createCenteredCell(colCursor++, "组长姓名");
            for (int i = 1; i < config.getMaxSize(); i++) {
                row.createCenteredCell(colCursor++, "组员 " + i + " 学号");
                row.createCenteredCell(colCursor++, "组员 " + i + " 姓名");
            }
        });

        int i = 0;
        for (GroupView group : groups) {
            final int index = i + 1;
            helper.createRow(rowCursor++, row -> {
                int colCursor = 0;
                row.createCell(colCursor++, String.valueOf(index));
                row.createCenteredCell(colCursor++, group.getName());
                row.createCenteredCell(colCursor++, group.getDescription());
                for (var member : group.getMembers()) {
                    row.createCenteredCell(colCursor++, member.getBuaaId());
                    row.createCenteredCell(colCursor++, member.getName());
                }
            });
        }
    }

    private void writeRogueStudents(Sheet sheet, List<RogueStudentView> rogueStudents) {
        int colCursor = 0;

        var helper = ExcelHelper.open(sheet)
                .setColumnsWidth(1, 2, 14);

        helper.createRow(colCursor++, row -> row
                .createCenteredCell(0, "序号")
                .createCenteredCell(1, "学号")
                .createCenteredCell(2, "姓名"));

        for (RogueStudentView student : rogueStudents) {
            final int index = colCursor;
            helper.createRow(colCursor++, row -> row
                    .createCell(0, String.valueOf(index))
                    .createCenteredCell(1, student.getBuaaId())
                    .createCenteredCell(2, student.getName()));
        }
    }
}
