package com.mango.launcher3.retrofit;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author tic
 * created on 18-11-12
 */
public class Retrofits {

    private Retrofit mRetrofit;

    public Retrofits() {
    }

    public void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(authorizationInterceptor())
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build();
    }

    public <Service> Service create(Class<Service> model) {
        Preconditions.checkNotNull(mRetrofit);
        Service service = mRetrofit.create(model);
        return service;
    }

    private Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .serializeNulls()
                .create();
    }

    private Interceptor authorizationInterceptor() {
        return null;
    }

    private HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public <T> Response<T> execute(Call<T> call) {
        Preconditions.checkNotNull(call);
        try {
            if (call.isExecuted()) {
                return null;
            }
            Response<T> data = call.execute();
            if (data == null) {

            } else {

            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO make empty Response with exception
        return null;
    }

    private <T> Response<T> emptyResponse() {

    }
}
