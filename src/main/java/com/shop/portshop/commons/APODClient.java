package com.shop.portshop.commons;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.util.concurrent.CompletableFuture;

public interface APODClient {

    @GET("/planetary/apod")
    @Headers("accept: application/json")
    CompletableFuture<APOD> getApod(@Query("api_key") String apiKey);


}
