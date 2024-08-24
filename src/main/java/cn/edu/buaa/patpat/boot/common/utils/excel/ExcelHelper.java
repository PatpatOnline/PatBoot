package cn.edu.buaa.patpat.boot.common.utils.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.function.Consumer;

public class ExcelHelper {
    private static final int CONVERT_FACTOR = 256;
    private final Sheet sheet;

    private ExcelHelper(Sheet sheet) {
        this.sheet = sheet;
    }

    public static ExcelHelper open(Sheet sheet) {
        return new ExcelHelper(sheet);
    }

    public ExcelHelper setColumnWidth(int column, int width) {
        sheet.setColumnWidth(column, width * CONVERT_FACTOR);
        return this;
    }

    public ExcelHelper setColumnsWidth(int startColumn, int endColumn, int width) {
        for (int i = startColumn; i <= endColumn; i++) {
            setColumnWidth(i, width);
        }
        return this;
    }

    /**
     * Call this after the cells are created.
     */
    public ExcelHelper mergeAndCenter(int row, int startColumn, int endColumn) {
        sheet.addMergedRegion(new CellRangeAddress(row, row, startColumn, endColumn));
        try {
            Cell cell = sheet.getRow(row).getCell(startColumn);
            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        } catch (Exception e) {
            // ignore
        }
        return this;
    }

    public ExcelHelper createRow(int row, Consumer<RowHelper> consumer) {
        Row r = sheet.createRow(row);
        consumer.accept(new RowHelper(r));
        return this;
    }

    public ExcelHelper getRow(int row, Consumer<RowHelper> consumer) {
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        consumer.accept(new RowHelper(r));
        return this;
    }

    public static class RowHelper {
        private final Row row;
        private CellStyle centerStyle;
        private CellStyle rightStyle;

        RowHelper(Row row) {
            this.row = row;
        }

        public RowHelper createCell(int column, int value) {
            return createCell(column, String.valueOf(value));
        }

        public RowHelper createCell(int column, String value) {
            return createCell(column, value, false);
        }

        public RowHelper createCenteredCell(int column, int value) {
            return createCenteredCell(column, String.valueOf(value));
        }

        public RowHelper createCenteredCell(int column, String value) {
            return createCell(column, value, true);
        }

        private RowHelper createCell(int column, String value, boolean center) {
            Cell cell = row.createCell(column);
            cell.setCellValue(value);
            if (center) {
                cell.setCellStyle(getCenterStyle());
            } else {
                cell.setCellStyle(getRightStyle());
            }
            return this;
        }

        private CellStyle getCenterStyle() {
            if (centerStyle == null) {
                centerStyle = row.getSheet().getWorkbook().createCellStyle();
                centerStyle.setAlignment(HorizontalAlignment.CENTER);
            }
            return centerStyle;
        }

        private CellStyle getRightStyle() {
            if (rightStyle == null) {
                rightStyle = row.getSheet().getWorkbook().createCellStyle();
                rightStyle.setAlignment(HorizontalAlignment.RIGHT);
            }
            return rightStyle;
        }
    }

    public static class CellHelper {
        private final Cell cell;

        CellHelper(Cell cell) {
            this.cell = cell;
        }
    }
}
