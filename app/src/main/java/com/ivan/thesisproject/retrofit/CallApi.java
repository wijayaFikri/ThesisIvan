package com.ivan.thesisproject.retrofit;

public interface CallApi<T> {
    void onSuccess(T result);
    void onFailed(String message);
}
