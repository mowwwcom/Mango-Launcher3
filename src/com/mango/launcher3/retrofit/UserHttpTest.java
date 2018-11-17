package com.mango.launcher3.retrofit;

import com.android.launcher3.entity.User;
import com.mango.launcher3.http.UserService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author tic
 * created on 18-11-12
 */
public class UserHttpTest {

    private final UserService mService;
    private final Retrofits mRetrofits;

    public UserHttpTest() {
        mRetrofits = Retrofits.create();
        mService = mRetrofits.create(UserService.class);
    }

    public void login(String account, String password) {
        Call<User> call = mService.login(account);
        Response<User> user = mRetrofits.execute(call);

    }
}
