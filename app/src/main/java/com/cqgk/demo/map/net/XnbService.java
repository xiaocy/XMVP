package com.cqgk.demo.map.net;

import com.cqgk.demo.map.model.FarmInfo;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/1/0001.
 */

public interface XnbService {

    @FormUrlEncoded
    //@Headers({"Content-Type:application/json","Accept:application/json","_sid:t17558290-d42a-4c60-80dd-9bb695153f6c", "_t:1512135531664"})
    @POST("market/newfarmlist.do")
    Flowable<FarmInfo> getFarmInfos(@Field("zoomLevel") Integer zoomLevel,
                                    @Field("lngMax") Double lngMax,
                                    @Field("lngMin") Double lngMin,
                                    @Field("latMax") Double latMax,
                                    @Field("latMin") Double latMin,
                                    @Field("lng") Double lng,
                                    @Field("lat") Double lat);
}
