package com.example.frenzbook.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetUserDetailsDTO implements Serializable
{
    @SerializedName("id")
    private Long id;
    private String role;
    private String name;
    private String email;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
