package com.example.mychicken.api;

/**
 * Created by Robby Dianputra on 10/31/2017.
 */

import com.example.mychicken.model.city.ItemCity;
import com.example.mychicken.model.cost.ItemCost;
import com.example.mychicken.model.province.ItemProvince;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Province
    @GET("province")
    @Headers("key:ffecd68fbfb82b40792a758ae5e688b7")
    Call<ItemProvince> getProvince ();

    // City
    @GET("city")
    @Headers("key:ffecd68fbfb82b40792a758ae5e688b7")
    Call<ItemCity> getCity (@Query("province") String province);

    // Cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost (@Field("key") String Token,
                            @Field("origin") String origin,
                            @Field("destination") String destination,
                            @Field("weight") String weight,
                            @Field("courier") String courier);

}
