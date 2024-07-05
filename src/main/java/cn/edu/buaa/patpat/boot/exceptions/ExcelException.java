package cn.edu.buaa.patpat.boot.exceptions;

public class ExcelException extends Exception {
    public ExcelException() {
        this("Invalid Excel operation");
    }

    public ExcelException(String message) {
        super(message);
    }
}
