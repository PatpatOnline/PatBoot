package cn.edu.buaa.patpat.boot.modules.course.services.impl;

import cn.edu.buaa.patpat.boot.common.utils.excel.ExcelException;
import cn.edu.buaa.patpat.boot.common.utils.excel.Excels;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Gender;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilterMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountMapper;
import cn.edu.buaa.patpat.boot.modules.course.dto.ImportStudentResponse;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentFilterMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentImportMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentImportView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentImporter {
    private final AccountMapper accountMapper;
    private final StudentMapper studentMapper;
    private final StudentImportMapper studentImportMapper;
    private final AccountFilterMapper accountFilterMapper;
    private final StudentFilterMapper studentFilterMapper;

    @Transactional
    public ImportStudentResponse importStudents(int courseId, String excelPath, boolean clean) {
        int deleted = 0;
        int created = 0;
        int updated = 0;
        try (Workbook workbook = new HSSFWorkbook(new FileInputStream(excelPath))) {
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
            log.error("Failed to import students: {}", e.getMessage());
            return ImportStudentResponse.of(MessageFormat.format(M("student.import.error"), e.getMessage()));
        }
    }

    private Account extractTeacherFromSheet(Sheet sheet) throws ImportException {
        try {
            String value = Excels.getNonEmptyCellValue(sheet.getRow(2).getCell(4));
            String name = value.substring(value.lastIndexOf("：") + 1);
            Account account = accountFilterMapper.findByName(name);
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
        for (var student : studentImportMapper.getAll(courseId, teacherId)) {
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
        Student student = studentFilterMapper.find(account.getId(), courseId);
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
                studentMapper.update(student);
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
        Account account = accountFilterMapper.findByBuaaId(buaaId);
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
