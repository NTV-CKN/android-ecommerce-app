package com.infix.phukiencongnghe.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.infix.phukiencongnghe.R;

import java.util.Locale;

public class SettingFragment extends Fragment {
    private Switch switchDarkMode;
    private Spinner spinnerLanguage;
    private SharedPreferences pre;
    private boolean isSpinnerInitialized = false;
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
        String[] languages = {getString(R.string.lang_vi),getString(R.string.lang_en)};
        String[] codex = {"vi","en"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);
        String currentLang = pre.getString("lang_code", "vi");
        if (currentLang.equals("en")) {
            spinnerLanguage.setSelection(1);
        } else {
            spinnerLanguage.setSelection(0);
        }
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }

                String selectedLang = codex[position];
                String currentSavedLang = pre.getString("lang_code", "vi");

                if (!selectedLang.equals(currentSavedLang)) {
                    setLocale(selectedLang);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = requireContext().getResources();
        Configuration config = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            requireContext().createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("lang_code", langCode);
        editor.apply();
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
}
