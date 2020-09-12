package com.lifu.aadtop20.service;

import android.os.Build;

import com.lifu.aadtop20.utils.Util;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HoursServiceBuilder {
    private static final String IQ_URL = Util.TOP_SKILL_IQ_BASE_URL;
    private static final String HRS_URL = Util.TOP_LEANERS_BASE_URL;
    private static final String SUBMISSION_URL = Util.SUBMISSION_BASE_URL;

    // Create logger
    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .readTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();

                            request = request.newBuilder()
                                    .addHeader("x-device-type", Build.DEVICE)
                                    .addHeader("Accept-Language", Locale.getDefault().getLanguage())
                                    .build();

                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(logger);


    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(HRS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return retrofit.create(serviceType);
    }
}