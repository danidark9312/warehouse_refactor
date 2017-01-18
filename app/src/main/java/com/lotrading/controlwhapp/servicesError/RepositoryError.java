package com.lotrading.controlwhapp.servicesError;

/**
 * Created by josetabaresramirez on 15/09/16.
 */
public class RepositoryError extends Exception {

    private int errorId;

    public RepositoryError(String message) {
        super(message);
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
}
