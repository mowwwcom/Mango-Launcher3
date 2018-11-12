package com.mango.launcher3.http;

import com.android.launcher3.entity.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author tic
 * created on 18-11-12
 */
public interface UserService {

    @GET("users/{user}/repos")
    Call<User> register(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<User> login(@Path("user") String user);
}
