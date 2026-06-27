package com.infix.phukiencongnghe.ui.main.product_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.data.dto.request.CartLocalDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.repository.cart.CartRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.cart.ICartRepository;
import com.infix.phukiencongnghe.data.repository.main.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.cart.CartService;
import com.infix.phukiencongnghe.data.source.remote.main.FeatureProductService;
import com.infix.phukiencongnghe.databinding.FragmentProductDetailsBinding;
import com.infix.phukiencongnghe.ui.adapter.feature_product.ProductImageSliderAdapter;
import com.infix.phukiencongnghe.ui.adapter.feature_product.ProductReviewAdapter;
import com.infix.phukiencongnghe.ui.adapter.feature_product.ProductVariantAdapter;
import com.infix.phukiencongnghe.ui.adapter.feature_product.RelatedProductAdapter;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.share_viewmodel.MainViewModel;
import com.infix.phukiencongnghe.utils.SharePrefUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment {
    private FragmentProductDetailsBinding binding;
    private ProductDetailsViewModel productDetailsViewModel;
    private LoadingDialog loadingDialog;

    private static final String ARG_PRODUCT_ID = "ProductDetailFragment.ARG_PRODUCT_ID";
    private Integer productId = 7;
    private int selectQuantity = 1;
    private ICartRepository cartRepository;
    private Integer selectedVariantId = null;
    public static ProductDetailsFragment newInstance(Integer productId) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();
        cartRepository = new CartRepositoryImpl(RetrofitHelper.getCartService());
        initProductDetailViewModel();
        setEvents();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productDetailsViewModel.resetStates();
        binding = null;
    }
    private void setEvents() {
        binding.btnDecreaseQty.setOnClickListener(v ->{
            if(selectQuantity > 1){
                selectQuantity--;
                updateQuantityUI();
            }
        });
        binding.btnIncreaseQty.setOnClickListener(v -> {
            selectQuantity++;
            updateQuantityUI();
        });
        binding.edtQuantityNumber.setOnFocusChangeListener((v, hasFocus) ->{
            if(!hasFocus){
                validateAndSetQuantity();
            }
        });
        binding.btnAddToCart.setOnClickListener(v -> {
            // kiem tra dang nhap truoc khi them sp vao gio hang
            boolean isLogin = SharePrefUtils.isLogin(AuthActivity.USER_AUTH_FILE, AuthActivity.KEY_ACCESS_TOKEN,AuthActivity.KEY_REFRESH_TOKEN,v.getContext());
            if(!isLogin){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(),"Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!",Snackbar.LENGTH_SHORT);
                return;
            }


            Integer currentVariantId = this.selectedVariantId;
            if(currentVariantId == null){
                SnackbarUtils.showBaseSnackbar(binding.getRoot(),"Vui lòng chọn một thể loại sản phẩm", Snackbar.LENGTH_SHORT);
                return;
            }
            int quantitySend = 1;
            try{
                String qty = binding.edtQuantityNumber.getText().toString().trim();
                if(!qty.isEmpty()){
                    quantitySend = Integer.parseInt(qty);
                }
            } catch (NumberFormatException e) {
                quantitySend = 1;
            }
            CartLocalDTO request = new CartLocalDTO(currentVariantId, quantitySend);
            productDetailsViewModel.addtoCart(request);
        });
    }

    private void updateQuantityUI() {
        binding.edtQuantityNumber.setText(String.valueOf(selectQuantity));
    }

    private void validateAndSetQuantity() {
        String input = binding.edtQuantityNumber.getText().toString();
        try{
            int nQuantity = Integer.parseInt(input);
            if(nQuantity<1){
                selectQuantity = 1;
            }else{
                selectQuantity = nQuantity;
            }
        } catch (NumberFormatException e) {
           selectQuantity = 1;
        }
        updateQuantityUI();
    }

    private void initProductDetailViewModel() {
        //product service
        FeatureProductService productService =
                RetrofitHelper.getFeatureProductService();
        IProductRepository repository = new FeatureProductRepositoryImpl(productService);
        // cart service
        CartService cartService =
                RetrofitHelper.getCartService();
        ICartRepository cartRepo = new CartRepositoryImpl(cartService);
        // Factory
        ProductDetailsViewModel.Factory factory = new ProductDetailsViewModel.Factory(repository, cartRepo);

        productDetailsViewModel = new ViewModelProvider(this,factory).get(ProductDetailsViewModel.class);
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        productDetailsViewModel.productDetails.observe(getViewLifecycleOwner(), details ->{
            if(details == null) return;
            binding.nameProduct.setText(details.getName() + " " + details.getSubtitle());

            if(details.getMinPrice() !=null){
                binding.tvProductPrice.setText(String.format("%,.0f VNĐ", details.getMinPrice()));
            }else{
                binding.tvProductPrice.setText("0.0 VNĐ");
            }
            binding.tvProductWarranty.setText("Bảo hành: " + details.getWarrantyPeriod());
            binding.tvProductDescription.setText(details.getDescription());
            List<String> allImage = new ArrayList<>();
            if(details.getImages() != null){
                allImage.addAll(details.getImages());
            }
            if(details.getProductVariants() != null){
                for(ProductVariantDTO v : details.getProductVariants()){
                    if(v.getImageUrl() !=null && !v.getImageUrl().isEmpty() && !allImage.contains(v.getImageUrl())){
                        allImage.add(v.getImageUrl());
                    }
                }
            }
            ProductVariantAdapter variantAdapter = new ProductVariantAdapter(details.getProductVariants(), variant -> {
                this.selectedVariantId = variant.getId();
                if(variant.getPrice() != null){
                    binding.tvProductPrice.setText(String.format("%,.0f", variant.getPrice()) + "VNĐ");
                }
                if(variant.getImageUrl() != null && !variant.getImageUrl().isEmpty()){
                    int taget = allImage.indexOf(variant.getImageUrl());
                    if(taget!=-1){
                        binding.mainImage.setCurrentItem(taget,true);
                    }
                }
            });
            binding.rvProductVariants.setAdapter(variantAdapter);
            ProductImageSliderAdapter sliderAdapter = new ProductImageSliderAdapter();
            binding.mainImage.setAdapter(sliderAdapter);
            if(details.getImages()!=null && !details.getImages().isEmpty()){
                sliderAdapter.setImages(allImage);
            }else{
                sliderAdapter.setImages(new ArrayList<>());
            }

            ProductReviewAdapter reviewAdapter = new ProductReviewAdapter(details.getReviews());
            binding.rvProductComments.setAdapter(reviewAdapter);
            binding.rvProductComments.setLayoutManager(new LinearLayoutManager(getContext()));
            RelatedProductAdapter relatedAdapter = new RelatedProductAdapter(details.getRelateProducts());
            binding.rvRelatedProducts.setAdapter(relatedAdapter);
            binding.rvRelatedProducts.setLayoutManager(new LinearLayoutManager(
                    getContext(), LinearLayoutManager.HORIZONTAL, false));
            relatedAdapter.setOnItemClickListener(product -> {
                this.productId = product.getId();
                this.selectQuantity = 1;
                this.selectedVariantId =null;
                updateQuantityUI();
                if (binding.mainScrollView !=null){
                    binding.mainScrollView.smoothScrollTo(0,0);
                }
                productDetailsViewModel.getProductById(this.productId);
            });
        });
        productDetailsViewModel.cartBadgeCount.observe(getViewLifecycleOwner(),totalCount ->{
            if(totalCount ==null){
                return;
            }
            mainViewModel.setCartBadgetCount(totalCount);
        });
        productDetailsViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });
        productDetailsViewModel.isLoading.observe(getViewLifecycleOwner(), bool -> {
            if(bool == null) return;
            if(bool)
                loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
        productDetailsViewModel.getProductById(productId);
    }

}
