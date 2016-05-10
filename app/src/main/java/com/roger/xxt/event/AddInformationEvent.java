package com.roger.xxt.event;

import com.roger.xxt.data.bean.Information;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public class AddInformationEvent {
    public AddInformationEvent(Information information) {
        this.information = information;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    private Information information;

}
