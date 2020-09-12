package com.lifu.aadtop20.service;


import retrofit2.Call;
import retrofit2.http.GET;

public interface MessageService {
    @GET("messages")
    Call<String> getMessages();
}
