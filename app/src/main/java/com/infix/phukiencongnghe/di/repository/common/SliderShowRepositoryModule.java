package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.repository.common.slider_show.ISliderShowRepository;
import com.infix.phukiencongnghe.data.repository.common.slider_show.SliderShowRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class SliderShowRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract ISliderShowRepository bindSliderShowRepository(SliderShowRepositoryImpl sliderShowRepository);
}
