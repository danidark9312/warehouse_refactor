package com.lotrading.controlwhapp.servicesError;

import com.lotrading.controlwhapp.config.IConstants;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by DESARROLLO PORTATIL on 15/12/2016.
 */

public class RepositoryMapper {

    public static RepositoryError convertRetrofitErrorToRepositoryError(RetrofitError retrofitError) {
        String messageError;
        int errorId;

        if (retrofitError.getCause() != null && retrofitError.getCause() instanceof SocketTimeoutException
                || retrofitError.getCause() instanceof InterruptedIOException) {
            messageError = IConstants.ERROR_REQUEST_TIMEOUT_MESSAGE;
            RepositoryError repositoryError = new RepositoryError(messageError);
            repositoryError.setErrorId(IConstants.ERROR_DEFAULT_CODE);
            return repositoryError;
        }

        messageError = IConstants.ERROR_DEFAULT_MESSAGE;
        errorId = 0;


        Response response = retrofitError.getResponse();
        if (response != null) {

            errorId = response.getStatus();
            if (errorId == IConstants.ERROR_UNAUTHORIZED_CODE || errorId == IConstants.ERROR_NOT_FOUND || errorId == IConstants.ERROR_BAD_REQUEST) {
                Error error = (Error) retrofitError.getBodyAs(Error.class);
                if (error != null) {
                    messageError = error.getResultMessage();
                } else {
                    messageError = getMessageError(response);
                }
            } else {
                messageError = getMessageError(response);
            }
        }
        if (messageError == null || messageError.isEmpty()) {
            messageError = IConstants.ERROR_DEFAULT_MESSAGE;
        }
        RepositoryError repositoryError = new RepositoryError(messageError);
        repositoryError.setErrorId(errorId);
        return repositoryError;
    }
    private static String getMessageError(Response response) {
        String message = IConstants.ERROR_DEFAULT_MESSAGE;
        if (response != null) {
            int status = response.getStatus();
            if (status == IConstants.ERROR_UNAUTHORIZED_CODE) {
                message = IConstants.ERROR_UNAUTHORIZED_MESSAGE;
            }
        }
        return message;
    }
}
