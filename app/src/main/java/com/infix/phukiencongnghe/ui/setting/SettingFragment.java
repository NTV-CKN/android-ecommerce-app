package com.infix.phukiencongnghe.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.infix.phukiencongnghe.R;

public class SettingFragment extends Fragment {
    private Switch switchDarkMode;
    private Spinner spinnerLanguage;
    private SharedPreferences pre;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        pre = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        boolean isDarkModeOn = pre.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkModeOn);
        switchDarkMode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = pre.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }));

        spinnerLanguage = view.findViewById(R.id.spinnerLanguage);
        return view;
    }
}
