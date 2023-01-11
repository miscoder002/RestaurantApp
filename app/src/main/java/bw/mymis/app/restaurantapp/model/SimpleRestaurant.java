package bw.mymis.app.restaurantapp.model;

public class SimpleRestaurant {
    private String id;
    private String name;
    // private String description;
    private String region;
    // private String town;
    // private String tel;
    private String address;
    // private String opentime;


    public SimpleRestaurant(String id, String name, String region, String address) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
