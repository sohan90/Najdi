package com.najdi.android.najdiapp.home.viewmodel;

import android.os.Build;
import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

public class ProductListItemModel extends BaseObservable {
    private int showDetailButton;
    private ProductListResponse product;
    private String productImg;
    private String price;
    private String title;
    private String desc;


    public ProductListItemModel(ProductListResponse productListResponse, int visibility) {
        this.product = productListResponse;
        this.showDetailButton = visibility;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
        notifyPropertyChanged(BR.productImg);
    }

    @Bindable
    public boolean isOnSale(){
        return product.isOn_sale();
    }


    @Bindable
    public String getProductImg() {
        return product.getImages().get(0).getSrc();
    }

    public void setShowDetailButton(int showDetailButton) {
        this.showDetailButton = showDetailButton;
    }

    @Bindable
    public int getShowDetailButton() {
        return showDetailButton;
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).placeholder(R.drawable.najdi_logo).
                into(imageView);
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getPrice() {
        String price;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            price = Html.fromHtml(product.getPrice_html(), Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            price = Html.fromHtml(product.getPrice_html()).toString();
        }
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);

    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyPropertyChanged(BR.desc);
    }

    @Bindable
    public String getTitle() {
        return product.getName();
    }

    @Bindable
    public String getDesc() {
        String desc;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            desc = Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            desc = Html.fromHtml(product.getDescription()).toString();
        }
        return desc;
    }

    public void bind(ProductListResponse productListResponse) {
        product = productListResponse;
        notifyChange();
    }

}
