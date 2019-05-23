package ru.paswd.nearprofinder;

public class PropertyItem {
    int id;
    String Title;
    String Desctiption;
    String Address;
    long Price;

    PropertyItem(int id, String title, String desctiption, String address, long price) {
        Title = title;
        Desctiption = desctiption;
        Address = address;
        Price = price;
    }
}
