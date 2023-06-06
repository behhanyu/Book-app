package edu.monash.fit2081.livestockapp;

public class FruitItem {
    private String itemTitle;
    private String itemFamily;
    private String itemCalories;
    private String itemSugar;
    private String itemCarbs;
    private String itemProtein;


    public FruitItem(String itemTitle, String itemFamily, String itemCalories, String itemSugar, String itemCarbs, String itemProtein) {
        this.itemTitle = itemTitle;
        this.itemFamily = itemFamily;
        this.itemCalories = itemCalories;
        this.itemSugar = itemSugar;
        this.itemCarbs = itemCarbs;
        this.itemProtein = itemProtein;

    }

    public String getItemFamily() {
        return itemFamily;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setItemFamily(String itemFamily) {
        this.itemFamily = itemFamily;
    }

    public String getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories(String itemCalories) {
        this.itemCalories = itemCalories;
    }

    public String getItemSugar() {
        return itemSugar;
    }

    public void setItemSugar(String itemSugar) {
        this.itemSugar = itemSugar;
    }

    public String getItemCarbs() {
        return itemCarbs;
    }

    public void setItemCarbs(String itemCarbs) {
        this.itemCarbs = itemCarbs;
    }

    public String getItemProtein() {
        return itemProtein;
    }



}
