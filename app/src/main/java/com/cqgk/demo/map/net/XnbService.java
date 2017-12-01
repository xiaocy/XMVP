package com.cqgk.demo.map.net;

import com.cqgk.demo.map.model.FarmInfo;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/1/0001.
 */

public interface XnbService {

    @FormUrlEncoded
    @Headers({"Content-Type:application/json","Accept:application/json","_sid:t17558290-d42a-4c60-80dd-9bb695153f6c", "_t:1512135531664"})
    @POST("newfarmlist.do")
    Flowable<FarmInfo> getFarmInfos(@Field("lngMax") double lngMax,
                                    @Field("lngMin") double lngMin,
                                    @Field("latMax") double latMax,
                                    @Field("latMin") double latMin,
                                    @Field("lng") double lng,
                                    @Field("lat") double lat);
}
