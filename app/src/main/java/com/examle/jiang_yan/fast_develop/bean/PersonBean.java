package com.examle.jiang_yan.fast_develop.bean;

/**
 * Created by jiang_yan on 2016/9/21.
 */

public class PersonBean {
    private String username;
    private String password;

    public PersonBean() {
    }

    public PersonBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
