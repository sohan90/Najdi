package com.najdi.android.najdiapp.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetailBundleModel implements Parcelable {
    Parcelable t;
    int productId;
    boolean isFromCartScreen;

    public ProductDetailBundleModel() {

    }

    public boolean isFromCartScreen() {
        return isFromCartScreen;
    }

    public void setFromCartScreen(boolean fromCartScreen) {
        isFromCartScreen = fromCartScreen;
    }

    private ProductDetailBundleModel(Parcel in) {
        productId = in.readInt();
        t = in.readParcelable(t.getClass().getClassLoader());
        isFromCartScreen = in.readByte() != 0;
    }

    public static final Creator<ProductDetailBundleModel> CREATOR = new Creator<ProductDetailBundleModel>() {
        @Override
        public ProductDetailBundleModel createFromParcel(Parcel in) {
            return new ProductDetailBundleModel(in);
        }

        @Override
        public ProductDetailBundleModel[] newArray(int size) {
            return new ProductDetailBundleModel[size];
        }
    };

    public Parcelable getT() {
        return t;
    }

    public void setT(Parcelable t) {
        this.t = t;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeParcelable(t, flags);
        dest.writeInt((byte) (isFromCartScreen ? 1 : 0));
    }
}
