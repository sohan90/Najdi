package com.najdi.android.najdiapp.home.viewmodel;

import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

public class ProductListItemModel extends BaseObservable {
    private int showDetailButton;
    private ProductListResponse product;

    public ProductListItemModel(ProductListResponse productListResponse, int visibility) {
        this.product = productListResponse;
        this.showDetailButton = visibility;
    }

    @Bindable
    public String getTitle() {
        return product.getName();
    }

    @Bindable
    public String getCutPrice() {
        return product.getCut_price();
    }

    @Bindable
    public String getPrice() {
        return product.getPrice();
    }

    public void setPrice(String price){
        this.product.setPrice(price);
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getProductImg() {
        return product.getImage();
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).placeholder(R.drawable.najdi_logo).
                into(imageView);
    }

    @BindingAdapter({"isOnSale", "cutPrice", "price"})
    public static void strikeText(TextView textView, boolean isOnSale, String cutPrice, String price) {
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        if (isOnSale) {
            if (!TextUtils.isEmpty(cutPrice)) {
                spanBuilder.append(cutPrice);
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                spanBuilder.setSpan(strikethroughSpan, 0, cutPrice.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanBuilder.append("-").append(price);

            } else if (!TextUtils.isEmpty(price)) {
                spanBuilder.append(price);
            }

        } else if (!TextUtils.isEmpty(price)) {
            spanBuilder.append(price);
        }

        spanBuilder = spanBuilder.append(" ").append(textView.getContext().
                getString(R.string.currency));
        textView.setText(spanBuilder);
    }

    @Bindable
    public String getDesc() {
        String desc = "";
        if (product.getDescription() == null) return desc;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            desc = Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            desc = Html.fromHtml(product.getDescription()).toString();
        }
        return desc;
    }

    @Bindable
    public String getShortDesc() {
        if (showDetailButton == View.VISIBLE) {
            String desc;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                desc = Html.fromHtml(product.getShortDescription(), Html.FROM_HTML_MODE_COMPACT).toString();
            } else {
                desc = Html.fromHtml(product.getShortDescription()).toString();
            }
            return desc;
        } else {
            return getDesc();
        }
    }

    @Bindable
    public boolean isOnSale() {
        return product.isOn_sale();
    }

    @Bindable
    public int getShowDetailButton() {
        return showDetailButton;// home screen differentiator
    }

    @Bindable
    public int getDividerVisibility() {
        return showDetailButton == View.VISIBLE ? View.VISIBLE : View.GONE;
    }

    public void bind(ProductListResponse productListResponse) {
        product = productListResponse;
        notifyChange();
    }

}
