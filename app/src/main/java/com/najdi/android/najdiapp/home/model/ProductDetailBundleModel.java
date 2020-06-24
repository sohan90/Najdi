package com.najdi.android.najdiapp.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetailBundleModel implements Parcelable {
    Parcelable t;
    String productId;
    boolean isFromCartScreen;
    boolean offer;

    public ProductDetailBundleModel() {

    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public boolean isOffer() {
        return offer;
    }

    public boolean isFromCartScreen() {
        return isFromCartScreen;
    }

    public void setFromCartScreen(boolean fromCartScreen) {
        isFromCartScreen = fromCartScreen;
    }

    private ProductDetailBundleModel(Parcel in) {
        productId = in.readString();
        t = in.readParcelable(t.getClass().getClassLoader());
        isFromCartScreen = in.readByte() != 0;
        offer = in.readByte() != 0;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeParcelable(t, flags);
        dest.writeInt((byte) (isFromCartScreen ? 1 : 0));
        dest.writeInt((byte) (offer ? 1 : 0));
    }
}
