package org.example.userservice.Request;

public class OAuth2UserRequest {

    private String email;
    private String name;

    public OAuth2UserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public OAuth2UserRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
