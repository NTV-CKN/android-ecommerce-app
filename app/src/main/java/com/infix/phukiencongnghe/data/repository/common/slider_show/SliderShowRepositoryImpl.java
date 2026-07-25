package com.infix.phukiencongnghe.data.repository.common.slider_show;

import com.infix.phukiencongnghe.data.dto.response.SliderShowDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.main.SliderShowService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class SliderShowRepositoryImpl implements ISliderShowRepository {
    SliderShowService sliderShowService;

    @Inject
    public SliderShowRepositoryImpl(SliderShowService sliderShowService) {
        this.sliderShowService = sliderShowService;
    }

    @Override
    public Call<List<SliderShowDTO>> getSliderShow() {
        return sliderShowService.getSliderShow();
    }
}
