package com.example.retrofit_image;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

public interface RetrofitImageAPI {
    @GET("json/movies/2.jpg")
    Call<ResponseBody> getImageDetails();
}
