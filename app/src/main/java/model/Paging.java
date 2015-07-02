package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by igiagante on 29/6/15.
 */
public class Paging  implements Parcelable {

    private Integer total;
    private Integer offset;
    private Integer limit;

    public Paging(){}

    public Paging(Integer total, Integer offset, Integer limit){
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(offset);
        dest.writeInt(limit);

        Log.d("Parcel", dest.toString());
    }

    public static final Parcelable.Creator<Paging> CREATOR = new Parcelable.Creator<Paging>() {
        public Paging createFromParcel(Parcel in) {
            return new Paging(in);
        }

        public Paging[] newArray(int size) {
            return new Paging[size];
        }
    };

    private Paging(Parcel in) {
        total = in.readInt();
        offset = in.readInt();
        limit = in.readInt();
    }
}
