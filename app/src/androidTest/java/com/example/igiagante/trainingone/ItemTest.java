package com.example.igiagante.trainingone;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import model.Item;

import static org.junit.Assert.assertEquals;

/**
 * Created by ignaciogiagante on 7/1/15.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ItemTest {

    @Test
    public void test(){

        Parcel parcel = Parcel.obtain();
        Item item = new Item("title", "price", "thumbnail", false);
        item.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Item parceledItem = Item.CREATOR.createFromParcel(parcel);
        assertEquals(item.getTitle(), parceledItem.getTitle());
        assertEquals(item.getPrice(), parceledItem.getPrice());
        assertEquals(item.getThumbnail(), parceledItem.getThumbnail());
        assertEquals(item.getShipping(), parceledItem.getShipping());
    }
}
