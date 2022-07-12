package com.encouragee.model.camel;

import java.util.Objects;

public class MyBean {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyBean)) return false;
        MyBean myBean = (MyBean) o;
        return getId() == myBean.getId() && getName().equals(myBean.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getId());
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
