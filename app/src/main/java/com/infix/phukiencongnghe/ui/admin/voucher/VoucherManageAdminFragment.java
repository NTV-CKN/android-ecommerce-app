package com.infix.phukiencongnghe.ui.admin.voucher;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.request.VoucherReqDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;
import com.infix.phukiencongnghe.ui.adapter.admin.voucher.OnVoucherItemClickListener;
import com.infix.phukiencongnghe.ui.adapter.admin.voucher.VoucherAdminAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoucherManageAdminFragment extends Fragment implements OnVoucherItemClickListener {

    private VoucherAdminViewModel viewModel;
    private VoucherAdminAdapter adapter;
    private LoadingDialog loadingDialog;

    // Các thành phần UI chính
    private RecyclerView rvVouchers;
    private FloatingActionButton fabAddVoucher;
    private EditText edtSearchCode;
    private ChipGroup chipGroupFilters;

    // Các biến trạng thái để lọc
    private String currentTypeCode = null;
    private DiscountType currentDiscountType = null;
    private String currentKeyword = "";

    // Xử lý delay khi nhập tìm kiếm (Debounce)
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher_manage_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupListeners();
        setupSearchAndFilter();

        // Load dữ liệu lần đầu tiên
        loadVoucherList();
    }

    private void initViews(View view) {
        rvVouchers = view.findViewById(R.id.rvVouchers);
        fabAddVoucher = view.findViewById(R.id.fabAddVoucher);
        edtSearchCode = view.findViewById(R.id.edtSearchCode);
        chipGroupFilters = view.findViewById(R.id.chipGroupFilters);

        loadingDialog = LoadingDialog.newInstance();
    }

    private void setupRecyclerView() {
        adapter = new VoucherAdminAdapter(this);
        rvVouchers.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvVouchers.setAdapter(adapter);
    }

    private void setupViewModel() {
        VoucherAdminViewModel.Factory factory = new VoucherAdminViewModel.Factory(
                InjectUtils.createAdminVoucherRepository()
        );
        viewModel = new ViewModelProvider(this, factory).get(VoucherAdminViewModel.class);

        // 1. Lắng nghe dữ liệu danh sách
        viewModel.adminVoucher.observe(getViewLifecycleOwner(), voucherAdminDTOS -> {
            if (voucherAdminDTOS != null) {
                adapter.setVoucherList(voucherAdminDTOS);
            }
        });

        // 2. Lắng nghe trạng thái Loading và dùng LoadingDialog
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

        // 3. Lắng nghe thông báo và hiển thị bằng SnackbarUtils
        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                SnackbarUtils.showBaseSnackbar(requireView(), msg, 3000);
            }
        });

        // 4. Lắng nghe sự kiện Thêm/Sửa/Xóa thành công
        viewModel.actionSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                SnackbarUtils.showBaseSnackbar(requireView(), "Thao tác thành công!", 3000);
                loadVoucherList(); // Tải lại danh sách
                viewModel.resetActionStatus();
            }
        });
    }

    private void setupListeners() {
        if (fabAddVoucher != null) {
            fabAddVoucher.setOnClickListener(v -> showVoucherDialog(null));
        }
    }

    private void setupSearchAndFilter() {
        // Cấu hình thanh tìm kiếm có delay 500ms
        edtSearchCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                currentKeyword = s.toString().trim();
                searchRunnable = () -> loadVoucherList();
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });

        // Cấu hình Bộ lọc Chip
        chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            currentTypeCode = null;
            currentDiscountType = null;

            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipPercentage) {
                    currentDiscountType = DiscountType.PERCENTAGE;
                } else if (checkedId == R.id.chipFixedAmount) {
                    currentDiscountType = DiscountType.FIXED_AMOUNT;
                } else if (checkedId == R.id.chipShipping) {
                    currentTypeCode = "SHIPPING";
                } else if (checkedId == R.id.chipOrder) {
                    currentTypeCode = "MAIN_ORDER";
                }
            }
            loadVoucherList();
        });
    }

    private void loadVoucherList() {
        viewModel.getVoucher(currentTypeCode, currentDiscountType, currentKeyword, System.currentTimeMillis());
    }

    @Override
    public void onEditClick(VoucherAdminDTO voucherAdminDTO) {
        showVoucherDialog(voucherAdminDTO);
    }

    @Override
    public void onDeleteClick(VoucherAdminDTO voucherAdminDTO) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa voucher mã '" + voucherAdminDTO.getCode() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    viewModel.deleteVoucher(voucherAdminDTO.getId());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showVoucherDialog(@Nullable VoucherAdminDTO oldVoucher) {
        boolean isEditMode = (oldVoucher != null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_voucher, null);
        bottomSheetDialog.setContentView(dialogView);

        // Ánh xạ View
        TextView tvDialogTitle = dialogView.findViewById(R.id.tvDialogTitle);
        TextInputEditText edtCode = dialogView.findViewById(R.id.edtCode);
        TextInputEditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        AutoCompleteTextView spinnerDiscountType = dialogView.findViewById(R.id.spinnerDiscountType);
        AutoCompleteTextView spinnerVoucherType = dialogView.findViewById(R.id.spinnerVoucherType);
        TextInputEditText edtDiscountValue = dialogView.findViewById(R.id.edtDiscountValue);
        TextInputEditText edtMinPrice = dialogView.findViewById(R.id.edtMinPrice);
        TextInputEditText edtStartDate = dialogView.findViewById(R.id.edtStartDate);
        TextInputEditText edtEndDate = dialogView.findViewById(R.id.edtEndDate);
        TextInputEditText edtLimit = dialogView.findViewById(R.id.edtLimit);
        AutoCompleteTextView spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Cài đặt Dropdown
        String[] discountTypes = {"Giảm tiền mặt (FIXED_AMOUNT)", "Giảm phần trăm (PERCENTAGE)"};
        spinnerDiscountType.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, discountTypes));
        spinnerDiscountType.setText(discountTypes[0], false);

        String[] voucherTypes = {"Đơn hàng (MAIN_ORDER)", "Vận chuyển (SHIPPING)"};
        spinnerVoucherType.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, voucherTypes));
        spinnerVoucherType.setText(voucherTypes[0], false); // Mặc định là Đơn hàng

        String[] statusOptions = {"Hoạt động", "Ngừng hoạt động"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, statusOptions));
        spinnerStatus.setText(statusOptions[0], false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        setupDatePicker(edtStartDate, sdf);
        setupDatePicker(edtEndDate, sdf);

        // Điền dữ liệu cũ (Sửa) hoặc tạo mới
        if (isEditMode) {
            tvDialogTitle.setText("Cập nhật Voucher");
            edtCode.setText(oldVoucher.getCode());
            edtCode.setEnabled(false);
            edtTitle.setText(oldVoucher.getTitle());

            spinnerDiscountType.setText(oldVoucher.getDiscountType() == DiscountType.PERCENTAGE ? discountTypes[1] : discountTypes[0], false);

            if (oldVoucher.getVoucherType() != null && "SHIPPING".equalsIgnoreCase(oldVoucher.getVoucherType().getCode())) {
                spinnerVoucherType.setText(voucherTypes[1], false);
            } else {
                spinnerVoucherType.setText(voucherTypes[0], false);
            }

            if (oldVoucher.getDiscountValue() != 0) edtDiscountValue.setText(String.valueOf(oldVoucher.getDiscountValue()));
            if (oldVoucher.getMinPriceAllow() != 0) edtMinPrice.setText(String.valueOf(oldVoucher.getMinPriceAllow()));
            if (oldVoucher.getStartDate() != null) edtStartDate.setText(oldVoucher.getStartDate());
            if (oldVoucher.getEndDate() != null) edtEndDate.setText(oldVoucher.getEndDate());
            if (oldVoucher.getUsageLimit() != null) edtLimit.setText(String.valueOf(oldVoucher.getUsageLimit()));

            spinnerStatus.setText((oldVoucher.getStatus() != null && oldVoucher.getStatus() == 0) ? statusOptions[1] : statusOptions[0], false);
        } else {
            tvDialogTitle.setText("Thêm mới Voucher");
            String currentDateTimeStr = sdf.format(new Date());
            edtStartDate.setText(currentDateTimeStr);
            edtEndDate.setText(currentDateTimeStr);
        }

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String code = edtCode.getText().toString().trim();
            String title = edtTitle.getText().toString().trim();
            String discountValStr = edtDiscountValue.getText().toString().trim();
            String minPriceStr = edtMinPrice.getText().toString().trim();
            String startDateStr = edtStartDate.getText().toString().trim();
            String endDateStr = edtEndDate.getText().toString().trim();
            String limitStr = edtLimit.getText().toString().trim();

            if (code.isEmpty() || title.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
                SnackbarUtils.showBaseSnackbar(dialogView, "Vui lòng nhập đủ các thông tin bắt buộc!", 3000);
                return;
            }

            int voucherTypeId = spinnerVoucherType.getText().toString().contains("SHIPPING") ? 2 : 1;

            DiscountType discountType = spinnerDiscountType.getText().toString().contains("PERCENTAGE") ? DiscountType.PERCENTAGE : DiscountType.FIXED_AMOUNT;
            BigDecimal discountValue = discountValStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(discountValStr);
            BigDecimal minPrice = minPriceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(minPriceStr);
            int limit = limitStr.isEmpty() ? 0 : Integer.parseInt(limitStr);
            int status = spinnerStatus.getText().toString().equals("Hoạt động") ? 1 : 0;

            VoucherReqDTO request = new VoucherReqDTO();
            request.setCode(code);
            request.setTitle(title);
            request.setDiscountType(discountType);
            request.setDiscountValue(discountValue);
            request.setMinPriceAllow(minPrice);
            request.setStartDate(startDateStr);
            request.setEndDate(endDateStr);
            request.setUsageLimit(limit);
            request.setStatus(status);
            request.setVoucherTypeId(voucherTypeId);

            if (isEditMode) {
                viewModel.updateVoucher(oldVoucher.getId(), request);
            } else {
                viewModel.createVoucher(request);
            }

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void setupDatePicker(TextInputEditText editText, SimpleDateFormat formatter) {
        editText.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String dateString = formatter.format(new Date(selection));
                editText.setText(dateString);
            });

            datePicker.show(getChildFragmentManager(), "DATE_PICKER");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}