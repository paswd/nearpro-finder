package ru.paswd.nearprofinder;

public class PropertyItem {
    int Id;
    String Title;
    String Desctiption;
    String Address;
    long Price;

    public PropertyItem(int id, String title, String desctiption, String address, long price) {
        Id = id;
        Title = title;
        Desctiption = desctiption;
        Address = address;
        Price = price;
    }
}
