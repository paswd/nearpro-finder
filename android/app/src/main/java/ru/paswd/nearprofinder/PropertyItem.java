package ru.paswd.nearprofinder;

public class PropertyItem {
    int Id;
    String Title;
    int Price;
    String ImageSrc;
    double Lat;
    double Lng;
    String Address;
    String Href;
    String Description;

    public PropertyItem(int id, String title, int price, String imageSrc, double lat, double lng,
                        String address, String href, String description) {
        Id = id;
        Title = title;
        Price = price;
        ImageSrc = imageSrc;
        Lat = lat;
        Lng = lng;
        Address = address;
        Href = href;
        Description = description;
    }

//    public PropertyItem(int id, String title, String desctiption, String address,
//                        long price, String imageSrc) {

}
