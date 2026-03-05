package com.example.SocialMedia.Dto;

public class UserSummaryDto {

    private String name;
    private String surname;
    private String email;

    public UserSummaryDto(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
}
