package com.najdi.android.najdiapp.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.HashMap;
import java.util.List;

public class ProductListResponse implements Parcelable {
    int id;
    String name;
    String slug;
    String type;
    String status;
    String catalog_visibility;
    String price;
    String regular_price;
    String sale_price;
    String description;
    String short_description;
    List<Image> images;
    List<Attributes> attributes;
    List<VariationData> variations_data;

    // local data
    private CartResponse.CartData cartData;


    protected ProductListResponse(Parcel in) {
        id = in.readInt();
        name = in.readString();
        slug = in.readString();
        type = in.readString();
        status = in.readString();
        catalog_visibility = in.readString();
        price = in.readString();
        regular_price = in.readString();
        sale_price = in.readString();
        description = in.readString();
        short_description = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
        attributes = in.createTypedArrayList(Attributes.CREATOR);
        variations_data = in.createTypedArrayList(VariationData.CREATOR);
    }

    public static final Creator<ProductListResponse> CREATOR = new Creator<ProductListResponse>() {
        @Override
        public ProductListResponse createFromParcel(Parcel in) {
            return new ProductListResponse(in);
        }

        @Override
        public ProductListResponse[] newArray(int size) {
            return new ProductListResponse[size];
        }
    };

    public CartResponse.CartData getCartData() {
        return cartData;
    }

    public void setCartData(CartResponse.CartData cartData) {
        this.cartData = cartData;
    }

    public List<VariationData> getVariationsData() {
        return variations_data;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return short_description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCatalog_visibility() {
        return catalog_visibility;
    }

    public String getPrice() {
        return price;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Attributes> getAttributesList() {
        return attributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(slug);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(catalog_visibility);
        dest.writeString(price);
        dest.writeString(regular_price);
        dest.writeString(sale_price);
        dest.writeString(description);
        dest.writeString(short_description);
        dest.writeTypedList(images);
        dest.writeTypedList(attributes);
        dest.writeTypedList(variations_data);
    }

    public static class Image implements Parcelable {
        int id;
        String src;
        String name;

        protected Image(Parcel in) {
            id = in.readInt();
            src = in.readString();
            name = in.readString();
        }

        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        public int getId() {
            return id;
        }

        public String getSrc() {
            return src;
        }

        public String getName() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(src);
            dest.writeString(name);
        }
    }

    public static class Attributes implements Parcelable {
        int id;
        String name;
        String slug;
        int position;
        boolean visible;
        List<HashMap<String, String>> options;

        protected Attributes(Parcel in) {
            id = in.readInt();
            name = in.readString();
            slug = in.readString();
            position = in.readInt();
            visible = in.readByte() != 0;
        }

        public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
            @Override
            public Attributes createFromParcel(Parcel in) {
                return new Attributes(in);
            }

            @Override
            public Attributes[] newArray(int size) {
                return new Attributes[size];
            }
        };

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }

        public int getPosition() {
            return position;
        }

        public boolean isVisible() {
            return visible;
        }

        public List<HashMap<String, String>> getOptions() {
            return options;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(slug);
            dest.writeInt(position);
            dest.writeByte((byte) (visible ? 1 : 0));
        }
    }

    public static class VariationData implements Parcelable {
        private int variation_id;
        private String variation_regular_price;
        private String variation_sales_price;
        private HashMap<String, String> attributes;


        protected VariationData(Parcel in) {
            variation_id = in.readInt();
            variation_regular_price = in.readString();
            variation_sales_price = in.readString();
        }

        public static final Creator<VariationData> CREATOR = new Creator<VariationData>() {
            @Override
            public VariationData createFromParcel(Parcel in) {
                return new VariationData(in);
            }

            @Override
            public VariationData[] newArray(int size) {
                return new VariationData[size];
            }
        };

        public int getVariation_id() {
            return variation_id;
        }

        public String getVariationRegularPrice() {
            return variation_regular_price;
        }

        public String getVariationSalesPrice() {
            return variation_sales_price;
        }

        public HashMap<String, String> getAttributes() {
            return attributes;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(variation_id);
            dest.writeString(variation_regular_price);
            dest.writeString(variation_sales_price);
        }
    }
}
