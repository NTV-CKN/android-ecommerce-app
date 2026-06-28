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
import com.infix.phukiencongnghe.data.dto.response.CheckoutProductDTO;
import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.repository.cart.CartRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.payment.PaymentMethodRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentPaymentBinding;
import com.infix.phukiencongnghe.databinding.FragmentProductDetailsBinding;
import com.infix.phukiencongnghe.ui.adapter.payment.PaymentProductAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.user_manage.UserManagerActivity;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageFragment;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class PaymentFragment extends Fragment {
    private FragmentPaymentBinding binding;
    private UserAddressManageViewModel userAddressManageViewModel;
    private PaymentMethodViewModel paymentMethodViewModel;
    private UserAddressDTO selectedAddress;
    private ActivityResultLauncher<Intent> selectAddressLauncher;
    private LoadingDialog loadingDialog;
    private List<UserAddressDTO> listAddress = new ArrayList<>();
    private boolean isBuyNow = false;
    private Integer buyNowProductId = 0;
    private Integer buyNowVariantId = 0;
    private Integer buyNowQuantity = 1;
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
                 this.buyNowProductId = getArguments().getInt("BUY_NOW_PRODUCT_ID");
                 this.buyNowVariantId = getArguments().getInt("BUY_NOW_VARIANT_ID");
                 this.buyNowQuantity = getArguments().getInt("BUY_NOW_QUANTITY", 1);
                String productName =  getArguments().getString("BUY_NOW_PRODUCT_NAME","");
                Long productPrice = getArguments().getLong("BUY_NOW_PRODUCT_PRICE", 0);
                String productImage = getArguments().getString("BUY_NOW_PRODUCT_IMAGE", "");

                showProductCheckout(productName, productPrice,productImage,productImage, buyNowQuantity);
            }
        }
        initPaymentMethods();
        initUserAddressManage();
        setEvent();
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

        long totalPrice = productPrice * buyNowQuantity;
        long shipfee = 0; // chua lam
        long totalPayment = totalPrice + shipfee;// tam thoi
        binding.paymentTvSubtotalValue.setText(format.format(totalPrice));
        binding.tvShippingFeeValue.setText(shipfee == 0 ? "Miễn phí" : format.format(shipfee));
        binding.paymentTvTotalValue.setText(format.format(totalPayment));


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
        binding.btnPlaceOrder.setOnClickListener(v ->{
            if(selectedAddress==null){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Vui lòng chọn địa chỉ giao hàng trước khi đặt hàng!", Snackbar.LENGTH_LONG);
            }
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
    }
}
