package model;

import java.util.ArrayList;

/**
 * Created by igiagante on 29/6/15.
 */
public class Search {

    private ArrayList<Item> items;
    private Paging paging;

    public Search(Paging paging, ArrayList<Item> items) {
        this.paging = paging;
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
