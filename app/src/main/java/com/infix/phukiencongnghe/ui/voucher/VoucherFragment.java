package com.infix.phukiencongnghe.ui.voucher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.repository.voucher.IVoucherRepository;
import com.infix.phukiencongnghe.ui.adapter.voucher.VoucherAdapter;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SharePrefUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class VoucherFragment extends Fragment {

    private VoucherViewModel viewModel;
    private VoucherAdapter adapter;

    private EditText edtSearchCode;
    private ChipGroup chipGroupFilters;
    private RecyclerView rvVouchers;

    private LoadingDialog loadingDialog;

    // Quản lý trạng thái bộ lọc (mặc định = tất cả)
    private String currentTypeCode = null;
    private DiscountType currentDiscountType = null;
    private String currentKeyword = "";

    // Debounce search
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    // Đánh dấu request hiện tại để bỏ qua kết quả của request cũ trả về trễ
    private long requestToken = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        initViews(view);
        setupRecyclerView();
        setupViewModel();

        setupSearchFilter();
        setupChipFilters();

        fetchVouchers();
    }

    private void initViews(View view) {
        edtSearchCode = view.findViewById(R.id.edtSearchCode);
        chipGroupFilters = view.findViewById(R.id.chipGroupFilter);
        rvVouchers = view.findViewById(R.id.rvVouchers);
    }

    private void setupRecyclerView() {
        adapter = new VoucherAdapter();
        rvVouchers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvVouchers.setAdapter(adapter);
    }

    private void setupViewModel() {
        IVoucherRepository repository = InjectUtils.createVoucherRepository();

        VoucherViewModel.Factory factory = new VoucherViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(VoucherViewModel.class);

        viewModel.userVoucher.observe(getViewLifecycleOwner(), vouchers -> {
            if (vouchers != null) {
                adapter.setVoucherList(vouchers);
            }
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                if (loadingDialog == null) {
                    loadingDialog = LoadingDialog.newInstance();
                }
                if (!loadingDialog.isAdded() && getChildFragmentManager() != null) {
                    loadingDialog.show(getChildFragmentManager(), "VoucherLoadingDialog");
                }
            } else {
                if (loadingDialog != null && loadingDialog.isAdded()) {
                    loadingDialog.dismiss();
                }
            }
        });

        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty() && getView() != null) {
                SnackbarUtils.showBaseSnackbar(getView(), msg, Snackbar.LENGTH_SHORT);
            }
        });
    }

    private void fetchVouchers() {
        // Tăng token mỗi lần gọi mới -> các request cũ tự "hết hạn" khi trả về trễ
        final long myToken = ++requestToken;
        viewModel.getVouchers(currentTypeCode, currentDiscountType, currentKeyword, myToken);
    }

    private void setupSearchFilter() {
        edtSearchCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentKeyword = s.toString().trim();

                searchRunnable = () -> fetchVouchers();
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void setupChipFilters() {
        chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {

            KeyboardUtils.hideKeyboardFromView(edtSearchCode);

            // Hủy mọi search debounce đang chờ, tránh ghi đè ngược kết quả chip
            if (searchRunnable != null) {
                searchHandler.removeCallbacks(searchRunnable);
            }

            // Reset về "Tất cả" trước, rồi mới set lại theo đúng chip được chọn
            currentTypeCode = null;
            currentDiscountType = null;

            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);

                if (checkedId == R.id.chipFixed) {
                    currentDiscountType = DiscountType.FIXED_AMOUNT;
                } else if (checkedId == R.id.chipPercentage) {
                    currentDiscountType = DiscountType.PERCENTAGE;
                } else if (checkedId == R.id.chipShipping) {
                    currentTypeCode = "SHIPPING";
                } else if (checkedId == R.id.chipOrder) {
                    currentTypeCode = "MAIN_ORDER"; // sửa từ "ORDER" -> đúng với backend
                }
                // checkedId == R.id.chipAll -> giữ nguyên null/null (tất cả)
            }
            fetchVouchers();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        if (loadingDialog != null && loadingDialog.isAdded()) {
            loadingDialog.dismiss();
        }
    }
}