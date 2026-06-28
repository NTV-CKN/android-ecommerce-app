package com.infix.phukiencongnghe.data.repository.main.slider_show;

import com.infix.phukiencongnghe.data.dto.response.SliderShowDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.main.SliderShowService;

import java.util.List;

import retrofit2.Call;

public class SliderShowRepositoryImpl implements ISliderShowRepository {

    SliderShowService sliderShowService;

    public SliderShowRepositoryImpl() {
        this.sliderShowService = RetrofitHelper.getSliderShow();
    }

    @Override
    public Call<List<SliderShowDTO>> getSliderShow() {
        return sliderShowService.getSliderShow();
    }
}
