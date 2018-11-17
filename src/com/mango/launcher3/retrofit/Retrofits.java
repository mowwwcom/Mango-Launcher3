package com.mango.launcher3.retrofit;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author tic
 * created on 18-11-12
 */
public class Retrofits {

    private final Builder mBuilder;
    private Retrofit mRetrofit;

    private Retrofits(Builder builder) {
        this.mBuilder = builder;
    }

    /**
     * create Retrofits with default config
     *
     * @return
     */
    public static Retrofits create() {
        return new Builder(Api.BASE_URL).build();
    }

    static class Builder {
        boolean retry;
        long connectTimeout = 15;
        long readTimeout = 15;
        Interceptor authorizationInterceptor;
        HttpLoggingInterceptor httpLoggingInterceptor;

        String baseUrl;
        OkHttpClient client;
        Converter.Factory factory;

        public Builder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        /**
         * connect timeout, TimeUnit second
         *
         * @param connectTimeout time
         */
        public Builder setConnectTimout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setAuthorizationInterceptor(Interceptor interceptor) {
            this.authorizationInterceptor = interceptor;
            return this;
        }

        public Builder setLoggingInterceptor(HttpLoggingInterceptor interceptor) {
            this.httpLoggingInterceptor = interceptor;
            return this;
        }

        public Builder setConverterFactory(Converter.Factory factory) {
            this.factory = factory;
            return this;
        }

        /**
         * 失败重试
         */
        public Builder retryWhenFailure(boolean retry) {
            this.retry = retry;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Retrofits build() {
            if (httpLoggingInterceptor == null) {
                httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            if (client == null) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(httpLoggingInterceptor)
                        .retryOnConnectionFailure(retry)
                        .readTimeout(readTimeout, TimeUnit.SECONDS)
                        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                        .addNetworkInterceptor(authorizationInterceptor)
                        .build();
            }
            if (factory == null) {
                factory = GsonConverterFactory.create();
            }
            return new Retrofits(this);
        }
    }

    public void init() {
        Preconditions.checkNotNull(mBuilder);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBuilder.baseUrl)
                .client(mBuilder.client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(mBuilder.factory)
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

    public <T> Response<T> execute(Call<T> call) {
        Preconditions.checkNotNull(call);
        try {
            if (call.isExecuted()) {
                return null;
            }
            Response<T> data = call.execute();
            if (data == null) {
                return emptyResponse();
            } else {
                if (data.isSuccessful()) {
                }
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyResponse();
    }

    private <T> Response<T> emptyResponse() {
        return Response.error(Http.CODE_ERROR_UNKNOWN,
                ResponseBody.create(MediaType.parse(Http.MIME.TEXT_PLAIN), "empty result"));
    }
}
