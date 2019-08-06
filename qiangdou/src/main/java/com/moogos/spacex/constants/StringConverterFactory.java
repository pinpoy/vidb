package com.moogos.spacex.constants;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * ProjectName: StringConverterFactory
 * Description: 字符串转换工厂（兼容后台返回JSON格式数据，但Gson不支持的对象数据（即：Key是不固定的））
 * <p>
 * review by chenpan, wangkang, wangdong 2017/10/12
 * edit by JeyZheng 2017/10/12
 * author: JeyZheng
 * version: 4.8.0
 * created at: 2017/10/12 14:01
 */
public class StringConverterFactory extends Converter.Factory {

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    private StringConverterFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        if (type == String.class) {

            return new StringResponseBodyConverter();
        }

        return null;
    }

    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (type == String.class) {

            return new StringRequestBodyConverter();
        }

        return null;
    }
}
