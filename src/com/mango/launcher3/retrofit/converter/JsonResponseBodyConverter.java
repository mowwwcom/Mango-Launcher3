package com.mango.launcher3.retrofit.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author tic
 * created on 18-11-15
 */
public class JsonResponseBodyConverter <T> implements Converter<ResponseBody, T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        return null;
    }
}
