package com.infix.phukiencongnghe.data.repository.common.slider_show;

import com.infix.phukiencongnghe.data.dto.response.SliderShowDTO;

import java.util.List;

import retrofit2.Call;

public interface ISliderShowRepository {
    Call<List<SliderShowDTO>> getSliderShow();
}
