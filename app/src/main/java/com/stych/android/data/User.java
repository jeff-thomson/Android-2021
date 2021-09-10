package com.stych.android.data;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public String id;

    @SerializedName("country_code")
    public int country_code;

    @SerializedName("email_address")
    public String email_address;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("phone_number")
    public String phone_number;

    @SerializedName("password")
    public String password;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", country_code=" + country_code +
                ", email_address='" + email_address + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
