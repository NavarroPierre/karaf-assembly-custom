package com.navarrop.rest.dto;

import com.google.gson.Gson;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return this.login;
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }
}
