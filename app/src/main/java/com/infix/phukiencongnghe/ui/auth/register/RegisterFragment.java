package com.infix.phukiencongnghe.ui.auth.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.FragmentRegisterBinding;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }
}