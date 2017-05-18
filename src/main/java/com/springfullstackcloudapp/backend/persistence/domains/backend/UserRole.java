package com.springfullstackcloudapp.backend.persistence.domains.backend;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ranji on 5/17/2017.
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {
    /*The Serial Version UID for Serializable Classes*/
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (!user.equals(userRole.user)) return false;
        return role.equals(userRole.role);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }
}
