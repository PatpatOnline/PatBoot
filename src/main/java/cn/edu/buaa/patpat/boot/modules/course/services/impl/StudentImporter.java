/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.services.impl;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.Tuple;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.excel.ExcelException;
import cn.edu.buaa.patpat.boot.common.utils.excel.ExcelHelper;
import cn.edu.buaa.patpat.boot.common.utils.excel.Excels;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.modules.account.api.AccountApi;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Gender;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentFilterMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentImportMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentExportView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentImportView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentImporter {
    private final AccountMapper accountMapper;
    private final StudentMapper studentMapper;
    private final StudentImportMapper studentImportMapper;
    private final StudentFilterMapper studentFilterMapper;
    private final BucketApi bucketApi;
    private final AccountApi accountApi;

    @Transactional
    public ImportStudentResponse importStudents(int courseId, String excelPath, boolean clean) {
        int deleted = 0;
        int created = 0;
        int updated = 0;
        try (Workbook workbook = Excels.getWorkbook(excelPath)) {
            Sheet sheet = workbook.getSheetAt(0);
            Account teacher = extractTeacherFromSheet(sheet);
            if (clean) {
                deleted = cleanStudents(sheet, courseId, teacher.getId());
            }
            for (int i = 6; i <= sheet.getLastRowNum(); i++) {
                int ret = createOrUpdateStudent(sheet.getRow(i), courseId, teacher.getId());
                if (ret == 1) {
                    created++;
                } else {
                    updated++;
                }
            }
            return ImportStudentResponse.of(created, updated, deleted);
        } catch (Exception e) {
            log.error("Failed to import students", e);
            return ImportStudentResponse.of(MessageFormat.format(M("student.import.error"), e.getMessage()));
        }
    }

    public Resource exportStudents(int courseId, String courseName, int teacherId) {
        List<Tuple<Integer, String>> teachers = getTeachers(teacherId);
        List<Tuple<String, List<StudentExportView>>> students = getStudentsToExport(courseId, teachers);

        LocalDateTime timestamp = LocalDateTime.now();
        String filename = String.format("%s-学生名单-%s.xlsx",
                courseName,
                timestamp.format(DateTimeFormatter.ofPattern(Globals.FILE_DATE_FORMAT)));
        String path = bucketApi.getTempFile(filename);

        boolean hasStudent = false;
        try (Workbook workbook = Excels.getWorkbook(); OutputStream out = Medias.getOutputStream(path)) {
            for (var student : students) {
                if (student.second.isEmpty()) {
                    continue;
                }
                exportStudentsToSheet(workbook, student, courseName, timestamp);
                hasStudent = true;
            }
            if (!hasStudent) {
                throw new BadRequestException(M("student.export.empty"));
            }
            workbook.write(out);
            return Medias.loadAsResource(path, true);
        } catch (Exception e) {
            log.error("Failed to export students", e);
            throw new InternalServerErrorException(M("student.export.error"));
        }
    }

    private List<Tuple<Integer, String>> getTeachers(int teacherId) {
        List<TeacherIndexView> teachers = accountApi.queryTeachers();
        if (teacherId == 0) {
            return teachers.stream().map(t -> new Tuple<>(t.getId(), t.getName())).toList();
        } else {
            return teachers.stream().filter(t -> t.getId() == teacherId).map(t -> new Tuple<>(t.getId(), t.getName())).toList();
        }
    }

    private List<Tuple<String, List<StudentExportView>>> getStudentsToExport(int courseId, List<Tuple<Integer, String>> teachers) {
        return teachers.stream().map(t -> {
            List<StudentExportView> studentViews = studentImportMapper.getAllExport(courseId, t.getFirst());
            return new Tuple<>(t.getSecond(), studentViews);
        }).toList();
    }

    private void exportStudentsToSheet(Workbook workbook, Tuple<String, List<StudentExportView>> data, String course, LocalDateTime timestamp) {
        String teacher = data.first;
        List<StudentExportView> students = data.second;
        Sheet sheet = workbook.createSheet(teacher);
        var helper = ExcelHelper.open(sheet).createRow(0, row -> row
                        .createCenteredCell(0, "课程")
                        .createCenteredCell(1, course)
                        .createCenteredCell(6, "任课教师")
                        .createCenteredCell(7, teacher))
                .createRow(1, row -> row
                        .createCenteredCell(0, "导出时间")
                        .createCenteredCell(1, timestamp.format(DateTimeFormatter.ofPattern(Excels.DATE_TIME_FORMAT))))
                .createRow(2, row -> row
                        .createCenteredCell(0, "序号")
                        .createCenteredCell(1, "学号")
                        .createCenteredCell(2, "姓名")
                        .createCenteredCell(3, "性别")
                        .createCenteredCell(4, "学院")
                        .createCenteredCell(5, "专业")
                        .createCenteredCell(6, "班级")
                        .createCenteredCell(7, "备注"));

        int i = 0;
        for (var student : students) {
            final int index = ++i;
            helper.createRow(i + 2, row -> row
                    .createCell(0, index)
                    .createCenteredCell(1, student.getBuaaId())
                    .createCenteredCell(2, student.getName())
                    .createCenteredCell(3, Gender.toString(student.getGender()))
                    .createCenteredCell(4, student.getSchool())
                    .createCenteredCell(5, student.getMajor())
                    .createCenteredCell(6, student.getClassName())
                    .createCenteredCell(7, student.isRepeat() ? "重修" : ""));
        }

        helper.setColumnWidth(0, 10)
                .setColumnsWidth(1, 2, 14)
                .setColumnWidth(3, 8)
                .setColumnsWidth(4, 5, 12)
                .setColumnWidth(6, 10)
                .setColumnWidth(7, 14)
                .mergeAndCenter(0, 1, 3)
                .mergeAndCenter(1, 1, 3);
    }

    private Account extractTeacherFromSheet(Sheet sheet) throws ImportException {
        try {
            String value = Excels.getNonEmptyCellValue(sheet.getRow(2).getCell(4));
            String name = value.substring(value.lastIndexOf("：") + 1);
            Account account = accountApi.findByName(name);
            if (account == null) {
                throw new ImportException(MessageFormat.format(M("student.import.error.teacher"), name));
            }
            return account;
        } catch (ExcelException e) {
            throw new ImportException(e.getMessage());
        }
    }

    private int cleanStudents(Sheet sheet, int courseId, int teacherId) throws ExcelException {
        int deleted = 0;
        var buaaIds = getAllBuaaIds(sheet);
        for (var student : studentImportMapper.getAllImport(courseId, teacherId)) {
            if (!buaaIds.contains(student.getBuaaId())) {
                deleteStudent(student, courseId);
                deleted++;
            }
        }
        return deleted;
    }

    /**
     * Create or update student.
     *
     * @param row       A row in the Excel file.
     * @param courseId  The course ID.
     * @param teacherId The teacher ID.
     * @return 1 if the student is created, 0 if the student is updated.
     * @throws ImportException If failed to import the student.
     */
    private int createOrUpdateStudent(Row row, int courseId, int teacherId) throws ImportException {
        Account account = createOrUpdateAccount(row);
        Student student = studentFilterMapper.findByAccountAndCourse(account.getId(), courseId);
        boolean create = student == null;
        if (create) {
            student = new Student();
        }
        student.setAccountId(account.getId());
        student.setCourseId(courseId);
        student.setTeacherId(teacherId);

        try {
            extractStudentFromRow(row, student);
            if (create) {
                studentMapper.save(student);
            } else {
                studentMapper.importUpdate(student);
            }
        } catch (ExcelException e) {
            throw new ImportException(e.getMessage());
        }

        return create ? 1 : 0;
    }

    private Set<String> getAllBuaaIds(Sheet sheet) throws ExcelException {
        Set<String> buaaIds = new HashSet<>();
        for (int i = 6; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            buaaIds.add(Excels.getNonEmptyCellValue(row.getCell(1)));
        }
        return buaaIds;
    }

    private void deleteStudent(StudentImportView student, int courseId) {
        var courses = studentImportMapper.getAllCourseId(student.getAccountId());
        if (courses.size() == 1 && courses.get(0) == courseId) {
            accountMapper.delete(student.getAccountId());
        }
        studentMapper.delete(student.getId());
    }

    private Account createOrUpdateAccount(Row row) throws ImportException {
        String buaaId = row.getCell(1).toString();
        Account account = accountApi.findByBuaaId(buaaId);
        boolean create = account == null;
        if (create) {
            account = new Account();
            account.setTa(false);
            account.setTeacher(false);
            account.setPassword(buaaId);
        }
        try {
            extractAccountFromRow(row, account);
            if (create) {
                accountMapper.save(account);
            } else {
                accountMapper.updateInfo(account);
            }
        } catch (ExcelException e) {
            log.error("Failed to extract account from row: {}", e.getMessage());
            throw new ImportException(e.getMessage());
        }
        return account;
    }

    private void extractStudentFromRow(Row row, Student old) throws ExcelException {
        old.setMajor(Excels.getNonEmptyCellValue(row.getCell(5)));
        old.setClassName(Excels.getNonEmptyCellValue(row.getCell(6)));
        old.setRepeat("重修".equals(Excels.getCellValue(row.getCell(29))));
    }

    private void extractAccountFromRow(Row row, Account old) throws ExcelException {
        old.setBuaaId(Excels.getNonEmptyCellValue(row.getCell(1)));
        old.setName(Excels.getNonEmptyCellValue(row.getCell(2)));
        old.setGender(Gender.fromString(Excels.getNonEmptyCellValue(row.getCell(3))));
        old.setSchool(Excels.getNonEmptyCellValue(row.getCell(4)));
        if (old.getAvatar() == null) {
            old.setAvatar(Gender.toAvatar(old.getGender()));
        }
    }
}
