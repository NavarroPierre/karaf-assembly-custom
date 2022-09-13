package com.navarrop.rest.dto;

import com.google.gson.Gson;
import lombok.Data;

import java.security.Principal;
import java.util.List;

@Data
public class UserInfo implements Principal {

    /**
     * User identifier
     */
    private Integer userId;

    /**
     * Last name of the user
     */
    private String lastName;

    /**
     * First name of the user
     */
    private String firstName;

    /**
     * Email of the user
     */
    private String email;

    /**
     * Roles of the user
     */
    private List<String> roles;

    /**
     * Groups of the user
     */
    private List<String> groups;

    private String login;

    public String getName() {
        return this.login;
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }
}
