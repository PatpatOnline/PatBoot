package cn.edu.buaa.patpat.boot.common.utils.excel;

import org.apache.poi.ss.usermodel.Cell;

public class Excels {
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private Excels() {}

    /**
     * Try to get cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The cell value as string, or an empty string if the cell is null.
     */
    public static String tryGetCellValue(Cell cell) {
        return tryGetCellValue(cell, "");
    }

    /**
     * Try to get cell value as string.
     *
     * @param cell         The cell to get value from.
     * @param defaultValue The default value to return if the cell is null.
     * @return The cell value as string, or the default value if the cell is null.
     */
    public static String tryGetCellValue(Cell cell, String defaultValue) {
        return cell == null ? defaultValue : cell.getStringCellValue();
    }

    /**
     * Get non-empty cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The non-empty cell value as string.
     * @throws ExcelException If the cell doesn't exist, i.e., null, or the cell is empty.
     */
    public static String getNonEmptyCellValue(Cell cell) throws ExcelException {
        String value = getCellValue(cell);
        if (value.isEmpty()) {
            throw new ExcelException("Cell is empty");
        }
        return value;
    }

    /**
     * Get cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The cell value as string.
     * @throws ExcelException If the cell doesn't exist, i.e., null.
     */
    public static String getCellValue(Cell cell) throws ExcelException {
        if (cell == null) {
            throw new ExcelException("Cell is null");
        }
        return cell.getStringCellValue();
    }
}
