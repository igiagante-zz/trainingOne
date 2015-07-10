package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by igiagante on 29/6/15.
 */
public class Item implements Parcelable {

    private String itemId;
    private String title;
    private String price;
    private String thumbnail;
    private Boolean shipping;
    private String description;

    public Item(String itemId, String title, String price, String thumbnail, Boolean shipping) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.shipping = shipping;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemId() {
        return itemId;
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

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(thumbnail);
        dest.writeInt(shipping ? 1 : 0);
        dest.writeString(description);
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
        itemId = in.readString();
        title = in.readString();
        price = in.readString();
        thumbnail = in.readString();
        shipping = in.readInt() == 1;
        description = in.readString();
    }
}
