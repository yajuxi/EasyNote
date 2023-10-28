package com.dxxy.note.entity;

import java.io.Serializable;

/*用户实体*/
public class User implements Serializable {
    private int id;
    private String name;   //用户名
    private String password;   //密码
    private String avatar; //头像

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
