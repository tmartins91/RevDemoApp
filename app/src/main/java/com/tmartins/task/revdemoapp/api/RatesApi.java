package com.tmartins.task.revdemoapp.api;

import com.tmartins.task.revdemoapp.models.RatesData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RatesApi {

    @GET("latest")
    Observable<RatesData> getAllRates(@Query("base") String baseCurrency);

}
