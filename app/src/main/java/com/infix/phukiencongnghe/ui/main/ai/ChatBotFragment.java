package com.infix.phukiencongnghe.ui.main.ai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.FragmentChatBotBinding;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.main.product_detail.ProductDetailsFragment;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class ChatBotFragment extends Fragment {
    private LoadingDialog loadingDialog;

    private FragmentChatBotBinding binding;
    private ChatBotViewModel chatBotViewModel;
    private ChatAdapter chatAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBotBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();

        initChatBotViewModel();
        initRecyclerView();
        observeChatBotViewModel();
        setEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        chatBotViewModel.resetStates();
    }

    private void setEvents() {
        //send
        binding.btnSendMessage.setOnClickListener(v -> {
            chatBotViewModel.sendMessage(binding.edtMessageInput.getText().toString());
        });
    }

    private void initChatBotViewModel() {
        ChatBotViewModel.Factory factory = new ChatBotViewModel.Factory(
                InjectUtils.createChatBotRepository()
        );

        chatBotViewModel = new ViewModelProvider(requireActivity(), factory).get(ChatBotViewModel.class);
    }

    private void initRecyclerView() {
        chatAdapter = new ChatAdapter(product ->
                navigateToProductDetails(product.getId()));

        binding.recyclerViewChat.setAdapter(chatAdapter);
    }

    private void observeChatBotViewModel() {
        //chatbot
        chatBotViewModel.chatHistory.observe(getViewLifecycleOwner(), messageModels -> {
            if(messageModels == null) return;

            chatAdapter.setMessages(messageModels);
        });

        //loading
        chatBotViewModel.isLoading.observe(getViewLifecycleOwner(), isLoad -> {
            if(isLoad == null) return;
            try {
                if(isLoad)
                    loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
                else
                    loadingDialog.dismiss();
            } catch (Exception ignore) {}
            binding.btnSendMessage.setEnabled(!isLoad);
        });

        //message
        chatBotViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if(msg == null) return;

            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });
    }

    private void navigateToProductDetails(Integer id) {
        if(id == null) return;
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main_content, ProductDetailsFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }
}