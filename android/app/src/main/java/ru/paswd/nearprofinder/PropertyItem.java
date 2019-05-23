package ru.paswd.nearprofinder;

public class PropertyItem {
    int Id;
    String Title;
    String Desctiption;
    String Address;
    long Price;
    String ImageSrc;

    public PropertyItem(int id, String title, String desctiption, String address,
                        long price, String imageSrc) {
        Id = id;
        Title = title;
        Desctiption = desctiption;
        Address = address;
        Price = price;
        ImageSrc = imageSrc;
    }
}
