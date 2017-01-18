package com.lotrading.controlwhapp.client;

/**
 * Created by DESARROLLO PORTATIL on 15/12/2016.
 */

@SuppressWarnings("hiding")
public class HTTPResponse<T>{
    String status;
    T wrappedResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getWrappedResponse() {
        return wrappedResponse;
    }

    public void setWrappedResponse(T wrappedResponse) {
        this.wrappedResponse = wrappedResponse;
    }
}