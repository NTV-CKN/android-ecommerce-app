package com.infix.phukiencongnghe.ui.cart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CartItemDTO;
import com.infix.phukiencongnghe.data.dto.response.CheckoutProductDTO;
import com.infix.phukiencongnghe.databinding.FragmentCartBinding;
import com.infix.phukiencongnghe.ui.adapter.cart.CartAdapter;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.payment.PaymentFragment;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SharePrefUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartViewModel viewModel;
    private CartAdapter cartAdapter;

    private LoadingDialog loadingDialog;

    public CartFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = LoadingDialog.newInstance();
        String[] tokens = SharePrefUtils.getAccessRefreshTokenFromPrefFile(
                AuthActivity.USER_AUTH_FILE, AuthActivity.KEY_ACCESS_TOKEN, AuthActivity.KEY_REFRESH_TOKEN, requireContext()
        );

        if (tokens[0] == null || tokens[0].isEmpty() && tokens[1] == null || tokens[1].isEmpty()) {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        setupRecyclerView();

        CartViewModel.Factory factory= new CartViewModel.Factory(
                InjectUtils.createCartRepository()
        );
        viewModel = new ViewModelProvider(requireActivity(), factory).get(CartViewModel.class);
        observeViewModel();

        // 4. Cài đặt các sự kiện Click trên màn hình chính
        setupClickListeners();

        // 5. Kích hoạt tính năng "Bấm ra ngoài khoảng trống tự đóng bàn phím và tự lưu số lượng"
        setupUIFocusKeyboard(binding.getRoot());

        // 6. Gọi API lấy giỏ hàng lần đầu khi vào màn hình
        viewModel.loadCart();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(new OnCartItemClickListener() {
            @Override
            public void onPlusClick(Integer itemId, Integer currentQty) {
                // Tăng 1 đơn vị
                viewModel.updateQuantity(itemId, currentQty, true);
            }

            @Override
            public void onMinusClick(Integer itemId, Integer currentQty) {
                // Giảm 1 đơn vị
                viewModel.updateQuantity(itemId, currentQty, false);
            }

            @Override
            public void onDeleteClick(Integer itemId) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn muốn xóa sản phẩm này khỏi giỏ hàng?")
                        .setPositiveButton("Xóa", (dialog, which) -> viewModel.deleteCartItem(itemId))
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onQuantityTextChanged(Integer itemId, Integer newQty) {
                viewModel.setExactQuantity(itemId, newQty);
            }
        });

        binding.rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnDelAll.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Xóa toàn bộ giỏ")
                    .setMessage("Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng không?")
                    .setPositiveButton("Xóa tất cả", (dialog, which) -> {
                        viewModel.clearAll();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        binding.btnCheckout.setOnClickListener(v -> {
            if(cartAdapter==null||cartAdapter.getItemCount() ==0){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Giỏ hàng của bạn đang trống!", Snackbar.LENGTH_SHORT);
                return;
            }
            ArrayList<CheckoutProductDTO> checkoutProductDTOS =new ArrayList<>();
            for(CartItemDTO itemDTO : cartAdapter.getCurrentList()){
                BigDecimal productPrice = BigDecimal.valueOf(itemDTO.getUnitPrice() != null ? itemDTO.getUnitPrice() : 0.0);
                CheckoutProductDTO dto = new CheckoutProductDTO(itemDTO.getId(),itemDTO.getProductVariantId(),itemDTO.getProductName(),itemDTO.getQuantity(),productPrice,itemDTO.getProductImage());
                checkoutProductDTOS.add(dto);
            }
            //CheckoutActivity tại đây
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), com.infix.phukiencongnghe.ui.main.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("OPEN_PAYMENT", true);
                intent.putExtra("CHECKOUT_PRODUCTS_LIST", checkoutProductDTOS);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void observeViewModel() {
        viewModel.cartLiveData.observe(getViewLifecycleOwner(), cartDTO -> {
            if (cartDTO != null && cartDTO.getCartItems() != null) {
                // Nạp danh sách item cho Adapter xử lý DiffUtil
                cartAdapter.submitList(cartDTO.getCartItems());

                if (cartDTO.getTotalPrice() != null) {
                    binding.tvTotalPrice.setText(String.format("%,.0fđ", cartDTO.getTotalPrice()));
                } else {
                    binding.tvTotalPrice.setText("0đ");
                }
            } else {
                binding.tvTotalPrice.setText("0đ");
                cartAdapter.submitList(null);
            }
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                if (!loadingDialog.isAdded()) {
                    loadingDialog.show(getChildFragmentManager(), "LoadingDialog");
                }
            } else {
                if (loadingDialog.isAdded()) {
                    loadingDialog.dismiss();
                }
            }
        });

        viewModel.notifyMsg.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), error, Snackbar.LENGTH_SHORT);
            }
        });
    }

    /**
     * Hàm nâng cao: Duyệt cây giao diện để phát hiện hành vi người dùng click ra vùng trống ngoài EditText.
     * Nếu họ đang gõ dở một con số và bấm ra ngoài màn hình, hệ thống sẽ tự động ẩn bàn phím và kích hoạt
     * hàm lưu lại số lượng đó trên EditText hiện tại (thông qua lệnh clearFocus).
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupUIFocusKeyboard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View currentFocus = requireActivity().getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        KeyboardUtils.hideKeyboardFromView(currentFocus);
                        currentFocus.clearFocus();
                    }
                }
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUIFocusKeyboard(innerView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}