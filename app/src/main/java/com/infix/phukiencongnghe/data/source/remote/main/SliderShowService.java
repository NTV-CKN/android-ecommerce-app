package com.infix.phukiencongnghe.data.source.remote.main;

import com.infix.phukiencongnghe.data.dto.response.SliderShowDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SliderShowService {

    @GET("/api/v1/slider-show")
    Call<List<SliderShowDTO>> getSliderShow();

}
