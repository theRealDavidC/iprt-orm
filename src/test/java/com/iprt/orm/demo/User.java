package com.iprt.orm.demo;

import com.iprt.orm.annotation.*;

@Table(name = "users")
public class User {

    @Id
    @AutoIncrement
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phone;

    public User() {}

    public User(String displayName, String phone) {
        this.displayName = displayName;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getPhone() { return phone; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    @Override
    public String toString() {
        return "User{id=" + id + ", displayName=" + displayName + ", phone=" + phone + "}";
    }
}
