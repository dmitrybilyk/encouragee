package com.encouragee.camel;

import com.encouragee.model.camel.MyBean;

public class BeanConvertor {
    public void updateBeanName(MyBean myBean) {
        myBean.setName(myBean.getName() + " changed");
    }
}
