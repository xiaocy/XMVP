package com.cqgk.demo.map.net;

import com.cqgk.demo.map.model.FarmInfo;
import com.cqgk.demo.map.model.GankResults;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface GankService {

    @GET("data/{type}/{number}/{page}")
    Flowable<GankResults> getGankData(@Path("type") String type,
                                      @Path("number") int pageSize,
                                      @Path("page") int pageNum);

    @POST("data/{type}/{number}/{page}")
    Flowable<FarmInfo> getFarmInfos(@Path("type") String type,
                                          @Path("number") int pageSize,
                                          @Path("page") int pageNum);
}
