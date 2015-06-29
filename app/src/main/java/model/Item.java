package model;

/**
 * Created by igiagante on 29/6/15.
 */
public class Item {

    private String tittle;
    private String price;

    public Item(String tittle, String price) {
        this.tittle = tittle;
        this.price = price;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
