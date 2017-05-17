package com.springfullstackcloudapp.backend.persistence.domains.backend;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by ranji on 5/17/2017.
 */
@Entity
public class Plan implements Serializable {

    /*The Serial Version UID for Serializable Classes*/
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String name;

    public Plan(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        return id.equals(plan.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
