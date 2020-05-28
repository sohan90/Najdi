package com.najdi.android.najdiapp.shoppingcart.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AttributeCartOptionData implements Parcelable {
    private String attr_id;
    private String attr_name;
    private String attr_selected_option_id;
    private String attr_selected_option;
    private String attr_option_price;
    private String attr_total_price;


    protected AttributeCartOptionData(Parcel in) {
        attr_id = in.readString();
        attr_name = in.readString();
        attr_selected_option_id = in.readString();
        attr_selected_option = in.readString();
        attr_option_price = in.readString();
        attr_total_price = in.readString();
    }

    public static final Creator<AttributeCartOptionData> CREATOR = new Creator<AttributeCartOptionData>() {
        @Override
        public AttributeCartOptionData createFromParcel(Parcel in) {
            return new AttributeCartOptionData(in);
        }

        @Override
        public AttributeCartOptionData[] newArray(int size) {
            return new AttributeCartOptionData[size];
        }
    };

    public String getAttr_id() {
        return attr_id;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public String getAttr_selected_option_id() {
        return attr_selected_option_id;
    }

    public String getAttr_selected_option() {
        return attr_selected_option;
    }

    public String getAttr_option_price() {
        return attr_option_price;
    }

    public String getAttr_total_price() {
        return attr_total_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attr_id);
        dest.writeString(attr_name);
        dest.writeString(attr_selected_option_id);
        dest.writeString(attr_selected_option);
        dest.writeString(attr_option_price);
        dest.writeString(attr_total_price);
    }
}
