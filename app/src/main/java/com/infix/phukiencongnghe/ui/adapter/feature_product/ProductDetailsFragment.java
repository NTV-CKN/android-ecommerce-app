package com.infix.phukiencongnghe.ui.adapter.feature_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.repository.main.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentProductDetailsBinding;
import com.infix.phukiencongnghe.databinding.FragmentUserAddressManageBinding;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class ProductDetailsFragment extends Fragment {
    private FragmentProductDetailsBinding binding;
    private ProductDetailsViewModel productDetailsViewModel;
    private LoadingDialog loadingDialog;

    private static final String ARG_PRODUCT_ID = "ProductDetailFragment.ARG_PRODUCT_ID";
    private Integer productId = 7;
    private int selectQuantity = 1;

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
        com.infix.phukiencongnghe.data.source.remote.main.FeatureProductService productService =
                RetrofitHelper.getFeatureProductService();
        IProductRepository repository = new FeatureProductRepositoryImpl(productService);
        ProductDetailsViewModel.Factory factory = new ProductDetailsViewModel.Factory(repository);

        productDetailsViewModel = new ViewModelProvider(this,factory).get(ProductDetailsViewModel.class);

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
            binding.rvProductComments.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

            RelatedProductAdapter relatedAdapter = new RelatedProductAdapter(details.getRelateProducts());
            binding.rvRelatedProducts.setAdapter(relatedAdapter);
            binding.rvRelatedProducts.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(
                    getContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
            relatedAdapter.setOnItemClickListener(product -> {
                this.productId = product.getId();
                this.selectQuantity = 1;
                updateQuantityUI();
                if (binding.mainScrollView !=null){
                    binding.mainScrollView.smoothScrollTo(0,0);
                }
                productDetailsViewModel.getProductById(this.productId);
            });
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
