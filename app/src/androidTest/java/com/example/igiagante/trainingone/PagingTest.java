package com.example.igiagante.trainingone;

import android.os.Parcel;

import org.junit.Test;

import model.Item;
import model.Paging;

import static org.junit.Assert.assertEquals;

/**
 * Created by ignaciogiagante on 7/1/15.
 */
public class PagingTest {

    @Test
    public void test(){

        Parcel parcel = Parcel.obtain();
        Paging paging = new Paging(10, 0, 100);
        paging.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Paging parceledPaging = Paging.CREATOR.createFromParcel(parcel);
        assertEquals(paging.getTotal(), parceledPaging.getTotal());
        assertEquals(paging.getOffset(), parceledPaging.getOffset());
        assertEquals(paging.getLimit(), parceledPaging.getLimit());
    }
}
