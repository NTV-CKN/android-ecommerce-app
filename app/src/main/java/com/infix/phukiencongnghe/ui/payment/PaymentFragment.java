package com.infix.phukiencongnghe.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.request.OrderRequestDTO;
import com.infix.phukiencongnghe.data.dto.response.CheckoutProductDTO;
import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.data.repository.payment.PaymentMethodRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentPaymentBinding;
import com.infix.phukiencongnghe.ui.adapter.payment.PaymentProductAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.payment.shipfee.ShippingViewModel;
import com.infix.phukiencongnghe.ui.user_manage.UserManagerActivity;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageViewModel;
import com.infix.phukiencongnghe.ui.voucher.VoucherFragment;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;
import okhttp3.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class PaymentFragment extends Fragment {
    private FragmentPaymentBinding binding;
    private UserAddressManageViewModel userAddressManageViewModel;
    private PaymentMethodViewModel paymentMethodViewModel;
    private ShippingViewModel shippingViewModel;
    private double currentShippingFee = 0.0;
    private double totalPrice = 0.0;
    private UserAddressDTO selectedAddress;
    private ActivityResultLauncher<Intent> selectAddressLauncher;
    private LoadingDialog loadingDialog;
    private List<UserAddressDTO> listAddress = new ArrayList<>();
    private boolean isBuyNow = false;
    private Integer buyNowProductId = 0;
    private Integer buyNowVariantId = 0;
    private final DecimalFormat format = new DecimalFormat("#,###đ");
    private Integer buyNowQuantity = 1;
    private VoucherDTO selectedShippingVoucher = null;
    private VoucherDTO selectedOrderVoucher = null;
    private VoucherDTO appliedVoucher = null;
    private List<CheckoutProductDTO> paymentProductList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSelectAddressLauncher();
    }

    private void registerSelectAddressLauncher() {
        selectAddressLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData()!=null){
                        UserAddressDTO addressDTO = (UserAddressDTO)  result.getData().getSerializableExtra(UserManagerActivity.EXTRA_SELECTED_ADDRESS);
                        if(addressDTO!=null){
                            updateAddressUI(addressDTO);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();
        if (getArguments() != null) {
            boolean isBuyNow = getArguments().getBoolean("IS_BUY_NOW", false);
            if (isBuyNow) {
                // mua ngay
                this.buyNowProductId = getArguments().getInt("BUY_NOW_PRODUCT_ID");
                this.buyNowVariantId = getArguments().getInt("BUY_NOW_VARIANT_ID");
                this.buyNowQuantity = getArguments().getInt("BUY_NOW_QUANTITY", 1);
                String productName =  getArguments().getString("BUY_NOW_PRODUCT_NAME","");
                Long productPrice = getArguments().getLong("BUY_NOW_PRODUCT_PRICE", 0);
                String productImage = getArguments().getString("BUY_NOW_PRODUCT_IMAGE", "");

                showProductCheckout(productName, productPrice,productImage,productImage, buyNowQuantity);
            }else{
                // lay tu gio hang
                if(getArguments().containsKey("CHECKOUT_PRODUCTS_LIST")){
                    ArrayList<CheckoutProductDTO> list = (ArrayList<CheckoutProductDTO>) getArguments().getSerializable("CHECKOUT_PRODUCTS_LIST");
                    if(list !=null){
                        showSomeProductCheckout(list);
                    }
                }
            }
        }

        getParentFragmentManager().setFragmentResultListener("REQUEST_KEY_SHIPPING", getViewLifecycleOwner(), (requestKey, result) -> {
            VoucherDTO voucher = (VoucherDTO) result.getSerializable("SELECTED_VOUCHER");
            if (voucher != null) {
                this.selectedShippingVoucher = voucher;
                binding.paymentTvVoucherStatus.setText("Mã vận chuyển: " + voucher.getCode());
                binding.paymentTvVoucherStatus.setTextColor(Color.parseColor("#2E7D32"));
                calculatePayment();
            }
        });


        getParentFragmentManager().setFragmentResultListener("REQUEST_KEY_ORDER", getViewLifecycleOwner(), (requestKey, result) -> {
            VoucherDTO voucher = (VoucherDTO) result.getSerializable("SELECTED_VOUCHER");
            if (voucher != null) {
                this.selectedOrderVoucher = voucher;
                binding.paymentTvVoucherOrderStatus.setText("Voucher đơn hàng: " + voucher.getCode());
                binding.paymentTvVoucherOrderStatus.setTextColor(Color.parseColor("#2E7D32"));
                calculatePayment();
            }
        });
        initPaymentMethods();
        initUserAddressManage();
        initShippingFee();
        setEvent();
        restoreVouchersUI();
    }

    private void showSomeProductCheckout(ArrayList<CheckoutProductDTO> list) {
        this.paymentProductList.clear();
        this.paymentProductList.addAll(list);
        PaymentProductAdapter adapter = new PaymentProductAdapter(this.paymentProductList);
        binding.rvPaymentItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPaymentItems.setAdapter(adapter);
        this.totalPrice = 0.0;
        for (CheckoutProductDTO item : paymentProductList) {
            if (item.getPrice() != null) {
                double itemPrice = item.getPrice().doubleValue();
                this.totalPrice += (itemPrice * item.getQuantity());
            }
        }
        binding.paymentTvSubtotalValue.setText(format.format(totalPrice));
        calculatePayment();
    }

    private void restoreVouchersUI() {
        if (selectedShippingVoucher != null) {
            binding.paymentTvVoucherStatus.setText("Mã vận chuyển: " + selectedShippingVoucher.getCode());
            binding.paymentTvVoucherStatus.setTextColor(Color.parseColor("#2E7D32"));
        }
        if (selectedOrderVoucher != null) {
            binding.paymentTvVoucherOrderStatus.setText("Voucher đơn hàng: " + selectedOrderVoucher.getCode());
            binding.paymentTvVoucherOrderStatus.setTextColor(Color.parseColor("#2E7D32"));
        }
        calculatePayment();
    }

    private void initShippingFee() {
        ShippingViewModel.Factory factory = new ShippingViewModel.Factory(InjectUtils.createShipFeeByAddressRepository());
        shippingViewModel = new ViewModelProvider(this,factory).get(ShippingViewModel.class);
        shippingViewModel.shipFeeData.observe(getViewLifecycleOwner(), shipfeeDTO->{
            if(shipfeeDTO!=null&&shipfeeDTO.getPrice()!=null){
                this.currentShippingFee = shipfeeDTO.getPrice();
                binding.tvShippingFeeValue.setText(currentShippingFee == 0 ? "Miễn phí" : format.format(currentShippingFee));
                calculatePayment();
            }
        });
        shippingViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Lỗi tính ship: " + msg, Snackbar.LENGTH_SHORT);
            }
        });
    }
    private double getVoucherDiscountAmount(VoucherDTO voucher, double baseAmount) {
        if (voucher == null) return 0.0;

        if (voucher.getDiscountType() != null && "PERCENTAGE".equals(voucher.getDiscountType().name())) {
            double calculatedDiscount = baseAmount * (voucher.getDiscountValue() / 100.0);
            return calculatedDiscount;
        } else {
            return voucher.getDiscountValue();
        }
    }
    private void calculatePayment() {
        double discountOrderValue = 0.0;
        double discountShippingValue = 0.0;

        // 1. Tính toán giảm giá đơn hàng sản phẩm
        if (selectedOrderVoucher != null) {
            discountOrderValue = getVoucherDiscountAmount(selectedOrderVoucher, this.totalPrice);
        }

        // 2. Tính toán giảm giá tiền vận chuyển
        if (selectedShippingVoucher != null) {
            discountShippingValue = getVoucherDiscountAmount(selectedShippingVoucher, this.currentShippingFee);
            if (discountShippingValue > this.currentShippingFee) {
                discountShippingValue = this.currentShippingFee; // Không giảm vượt quá tiền ship gốc
            }
        }

        // 3. Cập nhật hiển thị tiền ship sau giảm giá
        double finalShippingFee = this.currentShippingFee - discountShippingValue;
//        binding.tvShippingFeeValue.setText(finalShippingFee == 0 ? "Miễn phí" : format.format(finalShippingFee));

        if (discountOrderValue > 0 && discountShippingValue > 0) {
            String detailDiscount = "-" + format.format(discountShippingValue) + " (Ship) + -" + format.format(discountOrderValue) + " (Đơn)";
            binding.tvDiscountValue.setText(detailDiscount);
        } else if (discountShippingValue > 0) {
            binding.tvDiscountValue.setText("-" + format.format(discountShippingValue) + " (Ship)");
        } else if (discountOrderValue > 0) {
            binding.tvDiscountValue.setText("-" + format.format(discountOrderValue) + " (Đơn)");
        } else {
            binding.tvDiscountValue.setText("0đ");
        }

        // 4. Tính tổng hóa đơn cuối cùng cần thanh toán
        double finalProductPrice = this.totalPrice - discountOrderValue;
        if (finalProductPrice < 0) finalProductPrice = 0;

        double totalPayment = finalProductPrice + finalShippingFee;
        binding.paymentTvTotalValue.setText(format.format(totalPayment));
    }

    private void initPaymentMethods() {
        PaymentMethodViewModel.Fatory fatory = new PaymentMethodViewModel.Fatory(new PaymentMethodRepositoryImpl());
        paymentMethodViewModel = new ViewModelProvider(this,fatory).get(PaymentMethodViewModel.class);
        paymentMethodViewModel.getPaymentMethods();

        paymentMethodViewModel.paymentMethods.observe(getViewLifecycleOwner(), methods->{
            if(methods == null || methods.isEmpty()) return;
            for(PaymentMethodDTO dto : methods){
                if(dto.getId() == 1){
                    binding.rbCod.setText(dto.getNameMethod() + "\n" + dto.getSubTitle());
                    binding.rbCod.setTag(dto.getId());
                }else if(dto.getId() == 2){
                    binding.rbBankTransfer.setText(dto.getNameMethod()+ "\n" + dto.getSubTitle());
                    binding.rbBankTransfer.setTag(dto.getId());
                }
            }
        });
        paymentMethodViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(binding.getRoot(), msg, Snackbar.LENGTH_SHORT);
        });
    }

    private void showProductCheckout(String productName, Long productPrice, String productImage, String productImage1, int buyNowQuantity) {
        DecimalFormat format =  new DecimalFormat("#,###đ");
        BigDecimal price = BigDecimal.valueOf(productPrice);
        CheckoutProductDTO byNow = new CheckoutProductDTO(this.buyNowProductId, this.buyNowVariantId, productName, buyNowQuantity, price,productImage);
        List<CheckoutProductDTO> listItem = new ArrayList<>();
        listItem.add(byNow);

        PaymentProductAdapter adapter = new PaymentProductAdapter(listItem);
        binding.rvPaymentItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPaymentItems.setAdapter(adapter);

        this.totalPrice = productPrice * buyNowQuantity;
        binding.paymentTvSubtotalValue.setText(format.format(totalPrice));
        calculatePayment();
    }

    private void setEvent() {
        binding.paymentTvChangeAddress.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), UserManagerActivity.class);
            intent.putExtra("IS_FROM_PAYMENT", true);
            if(listAddress == null || listAddress.isEmpty()){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(),"Đang chuyển đến trang tạo địa chỉ...",Snackbar.LENGTH_SHORT);
            }
            selectAddressLauncher.launch(intent);
        });
        binding.paymentCardVoucher.setOnClickListener(v->{
            VoucherFragment voucherFragment = new VoucherFragment();
            Bundle args =  new Bundle();
            args.putString("PAYMENT_TARGET_TYPE", "SHIPPING");
            args.putDouble("CURRENT_TOTAL_PRICE", this.totalPrice);
            voucherFragment.setArguments(args);

            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fcv_main_content, voucherFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        binding.paymentCardVoucherOrder.setOnClickListener(v->{
            VoucherFragment voucherFragment = new VoucherFragment();
            Bundle args = new Bundle();
            args.putString("PAYMENT_TARGET_TYPE", "MAIN_ORDER");
            args.putDouble("CURRENT_TOTAL_PRICE", this.totalPrice);
            voucherFragment.setArguments(args);

            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fcv_main_content, voucherFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        binding.btnPlaceOrder.setOnClickListener(v ->{
            if(selectedAddress==null){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Vui lòng chọn địa chỉ giao hàng trước khi đặt hàng!", Snackbar.LENGTH_LONG);
                return;
            }
            Integer selectedPaymentMethodId = null;
            if (binding.rbCod.isChecked()) {
                selectedPaymentMethodId = (Integer) binding.rbCod.getTag();
            } else if (binding.rbBankTransfer.isChecked()) {
                selectedPaymentMethodId = (Integer) binding.rbBankTransfer.getTag();
            }

            if (selectedPaymentMethodId == null) {
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Vui lòng chọn phương thức thanh toán!", Snackbar.LENGTH_LONG);
                return;
            }
            List<OrderRequestDTO.OrderProductItem> products = new ArrayList<>();
            if (buyNowVariantId != null && buyNowVariantId != 0) {
                products.add(new OrderRequestDTO.OrderProductItem(buyNowVariantId, buyNowQuantity));
            }else {
                for (CheckoutProductDTO dto : paymentProductList) {
                    products.add(new OrderRequestDTO.OrderProductItem(dto.getProductVariantId(), dto.getQuantity()));
                }
            }
            List<String> vouchers = new ArrayList<>();
            if(selectedOrderVoucher!=null){
                vouchers.add(selectedOrderVoucher.getCode());
            }
            if(selectedShippingVoucher!=null){
                vouchers.add(selectedShippingVoucher.getCode());
            }
            String note = "";
            OrderRequestDTO orderRequest = new OrderRequestDTO(selectedAddress.getId(),selectedPaymentMethodId,note.isEmpty()?null:note,vouchers,products);
            binding.btnPlaceOrder.setEnabled(false);
            if (!loadingDialog.isAdded()) {
                loadingDialog.show(getParentFragmentManager(), "PlacingOrder");
            }
            android.content.SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
            String tokenSaved = sharedPreferences.getString("access_token", "");
            String authToken = "Bearer " + tokenSaved;

            com.infix.phukiencongnghe.data.repository.order.IOrderRepository orderRepository =
                    new com.infix.phukiencongnghe.data.repository.order.OrderRepositoryImpl(RetrofitHelper.getOrderService());
            orderRepository.createOrder(authToken, orderRequest).enqueue(new retrofit2.Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loadingDialog.isAdded()) {
                        loadingDialog.dismiss();
                    }
                    binding.btnPlaceOrder.setEnabled(true);
                    if(response.isSuccessful()){
                        SnackbarUtils.showBaseSnackbar(binding.getRoot(),"Đặt hàng thành công!",Snackbar.LENGTH_SHORT);
                        if (getActivity() != null) {
                            getActivity().getOnBackPressedDispatcher().onBackPressed();
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorStr = response.errorBody().string();
                                // Log ra Logcat để bạn xem chi tiết lỗi JSON từ Spring Boot trả về
                                android.util.Log.e("API_ERROR", "Backend trả về lỗi (" + response.code() + "): " + errorStr);

                                // Hiển thị Toast trực tiếp lên màn hình
                                android.widget.Toast.makeText(getContext(), "Thất bại: " + errorStr, android.widget.Toast.LENGTH_LONG).show();
                            } else {
                                android.util.Log.e("API_ERROR", "Backend trả về lỗi " + response.code() + " nhưng không có errorBody");
                                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Đặt hàng thất bại (mã lỗi " + response.code() + ")", Snackbar.LENGTH_LONG);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    if (loadingDialog.isAdded()) {
                        loadingDialog.dismiss();
                    }
                    binding.btnPlaceOrder.setEnabled(true);
                    SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Không thể kết nối tới máy chủ: " + throwable.getMessage(), Snackbar.LENGTH_LONG);
                }
            });
        });

    }

    private void initUserAddressManage() {
        UserAddressManageViewModel.Factory factory = new UserAddressManageViewModel.Factory(InjectUtils.createUserAddressManageRepository(requireContext()));
        userAddressManageViewModel = new ViewModelProvider(this, factory).get(UserAddressManageViewModel.class);
        userAddressManageViewModel.getUserAddresses();

        userAddressManageViewModel.userAddresses.observe(getViewLifecycleOwner(), list -> {
            this.listAddress = list;
            if (list.isEmpty()) {
                selectedAddress = null;
                binding.paymentTvReceiverNamePhone.setText("Chưa có địa chỉ giao hàng!");
                binding.paymentTvAddressDetail.setText("Vui lòng tạo địa chỉ giao hàng trước khi thanh toán.");
                binding.paymentTvAddressDetail.setTextColor(Color.RED);
                binding.paymentTvChangeAddress.setText("Thêm địa chỉ");
            } else {
                if (selectedAddress == null) {
                    updateAddressUI(list.get(0));
                } else {
                    updateAddressUI(selectedAddress);
                }
                binding.paymentTvChangeAddress.setText("Thay đổi");
            }
        });

        userAddressManageViewModel.isLoading.observe(getViewLifecycleOwner(), bool -> {
            if (bool == null) return;
            if (bool) {
                if (!loadingDialog.isAdded()) {
                    loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
                }
            } else {
                if (loadingDialog.isAdded()) {
                    loadingDialog.dismiss();
                }
            }
        });

        userAddressManageViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Lỗi hệ thống: " + msg, Snackbar.LENGTH_SHORT);
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void updateAddressUI(UserAddressDTO addressDTO) {
        this.selectedAddress = addressDTO;
        binding.paymentTvReceiverNamePhone.setText(addressDTO.getReceiverName() + " · " + addressDTO.getPhoneNumber());
        binding.paymentTvAddressDetail.setText(addressDTO.getAddressDetail());
        binding.paymentTvAddressDetail.setTextColor(Color.GRAY);
        if (addressDTO.getProvinceCity() != null && !addressDTO.getProvinceCity().isEmpty()) {
            shippingViewModel.calculateShippingFee(addressDTO.getProvinceCity());
        }
    }
}