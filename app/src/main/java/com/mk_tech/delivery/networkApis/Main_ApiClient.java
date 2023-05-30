package com.mk_tech.delivery.networkApis;

import android.util.Log;

import com.mk_tech.delivery.Utilities.GlobalConstants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_ApiClient {


    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    public String API_BASE_URL = "";
    private Retrofit retrofit = null;

    public <S> S createService(Class<S> serviceClass, String token, String lan) {
        API_BASE_URL = GlobalConstants.main_url;

        //  String authToken = Credentials.basic(username, password);
        //   return createService(serviceClass, authToken);
        Log.d("token: ", token);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Accept-Language", lan);
                if (!token.isEmpty()) {
                    builder.addHeader("Authorization", "Bearer " + token);
                }
                Request request = builder.build();

                return chain.proceed(request);
            }
        });

        return createService(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.connectTimeout(1200000, TimeUnit.SECONDS)
                            .readTimeout(1200000,
                                    TimeUnit.SECONDS).build())
                    .build();
        }
        return retrofit.create(serviceClass);
    }


}
