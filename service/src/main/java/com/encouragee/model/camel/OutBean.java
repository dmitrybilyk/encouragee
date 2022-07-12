package com.encouragee.model.camel;

import java.util.Objects;

public class OutBean {
    private String place;

    public OutBean(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutBean)) return false;
        OutBean outBean = (OutBean) o;
        return Objects.equals(getPlace(), outBean.getPlace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlace());
    }
}
