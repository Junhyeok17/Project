package joo.jae.wan.main;

public class Search {
    String name;
    String address;
    int check;

    public Search(String name, String address, int check) {
        this.name = name;
        this.address = address;
        this.check=check;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCheck(){return check;}

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
