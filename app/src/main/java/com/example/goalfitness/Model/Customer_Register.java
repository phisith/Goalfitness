package com.example.goalfitness.Model;

public class Customer_Register {
    private String Id, Email, Passsword, Name, Phone, imageURL, search, bios;

    public Customer_Register(){
    }

    public Customer_Register(String Id, String Email, String Password, String Name, String Phone, String imageURL, String search, String bios){
        this.Id = Id;
        this.Email = Email;
        this.Passsword = Password;
        this.Name = Name;
        this.Phone = Phone;
        this.imageURL = imageURL;
        this.search = search;
        this.bios = bios;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasssword() {
        return Passsword;
    }

    public void setPasssword(String passsword) {
        Passsword = passsword;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getBios() {
        return bios;
    }
    public void setBios(String bios) {
        this.bios = bios;
    }
}
