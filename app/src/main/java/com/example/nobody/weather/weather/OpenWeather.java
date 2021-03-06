package com.example.nobody.weather.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String city, @Query("appid") String api_key);
}
