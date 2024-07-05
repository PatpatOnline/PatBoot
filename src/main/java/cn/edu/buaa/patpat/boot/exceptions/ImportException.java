package cn.edu.buaa.patpat.boot.exceptions;

public class ImportException extends Exception {
    public ImportException() {
        this("Failed to import students");
    }

    public ImportException(String message) {
        super(message);
    }
}
