package com.mango.launcher3.retrofit.converter;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * @author tic
 * created on 18-11-15
 */
public class JsonRequestBodyConverter <T> implements Converter<T, RequestBody>  {

    @Override
    public RequestBody convert(T value) throws IOException {
        return null;
    }
}
