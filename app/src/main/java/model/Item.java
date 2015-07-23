package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by igiagante on 29/6/15.
 */
public class Item implements Parcelable {

    private long id;
    private String itemId;
    private String title;
    private String price;
    private String thumbnail;
    private String imageUrl;
    private Boolean shipping;
    private String description;
    private String expirationDate;

    public Item(){

    }

    public Item(String itemId, String title, String price, String thumbnail, Boolean shipping, String expirationDate) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.shipping = shipping;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getShipping() {
        return shipping;
    }

    public String getDescription() {
        return description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
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
        dest.writeString(imageUrl);
        dest.writeInt(shipping ? 1 : 0);
        dest.writeString(description);
        dest.writeString(expirationDate);
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
        imageUrl = in.readString();
        shipping = in.readInt() == 1;
        description = in.readString();
        expirationDate = in.readString();
    }
}
