package com.example.igiagante.trainingone;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import model.Item;
import model.Paging;
import model.Search;

import static org.junit.Assert.assertEquals;

/**
 * Created by ignaciogiagante on 7/1/15.
 */
public class SearchTest {

    Item item1 = null;
    Item item2 = null;
    Item item3 = null;
    Paging paging = null;
    ArrayList<Item> items = new ArrayList<>();
    Search search = null;

    @Before
    public void setup(){
        item1 = new Item("title1", "price1", "thumbnail1", false);
        item2 = new Item("title2", "price2", "thumbnail2", false);
        item3 = new Item("title3", "price3", "thumbnail3", false);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        paging = new Paging(10, 0, 100);
        search = new Search();
        search.setItems(items);
        search.setPaging(paging);
    }

    @Test
    public void test(){
        Parcel parcel = Parcel.obtain();
        search.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Search searchParceled = Search.CREATOR.createFromParcel(parcel);
        assertEquals(searchParceled.getItems().get(0).getTitle(), search.getItems().get(0).getTitle());
        assertEquals(searchParceled.getItems().get(0).getPrice(), search.getItems().get(0).getPrice());
        assertEquals(searchParceled.getItems().get(0).getThumbnail(), search.getItems().get(0).getThumbnail());
        assertEquals(searchParceled.getItems().get(0).getShipping(), search.getItems().get(0).getShipping());
    }
}
