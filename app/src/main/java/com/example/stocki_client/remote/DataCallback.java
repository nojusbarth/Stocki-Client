package com.example.stocki_client.remote;

public interface DataCallback<T> {

    void onSuccess(T data);
    void onError(Exception e);
}
