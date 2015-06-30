package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by igiagante on 29/6/15.
 */
public class Item implements Parcelable {

    private String title;
    private String price;
    private String thumbnail;
    private Boolean shipping;

    public Item(String title, String price, String thumbnail, Boolean shipping) {
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.shipping = shipping;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setShipping(Boolean shipping) {
        this.shipping = shipping;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Boolean getShipping() {
        return shipping;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(thumbnail);
        dest.writeInt(shipping ? 1 : 0); //if myBoolean == true, byte == 1

        Log.d("Parcel", dest.toString());
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in) {
        title = in.readString();
        price = in.readString();
        thumbnail = in.readString();
        shipping = in.readInt() == 1;
    }
}
