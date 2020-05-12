package com.example.goalfitness.Model;

public class Customer_Register {
    private String Email, Passsword, Name, Phone;

    public Customer_Register(){
    }

    public Customer_Register(String Email, String Password, String Name, String Phone){
        this.Email = Email;
        this.Passsword = Password;
        this.Name = Name;
        this.Phone = Phone;
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
}
