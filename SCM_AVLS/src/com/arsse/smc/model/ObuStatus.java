package com.arsse.smc.model;

import java.io.Serializable;
import java.util.Date;

public class ObuStatus implements Serializable{
    

    /**
     * 
     */
    private static final long serialVersionUID = -7456192651048667840L;
    
    private String obuid;
    private String isOnline;
    private int day;
    private Date obuVehicleDate;
    private Date obuOnlineDate;

    /**
     * @return the obuOnlineDate
     */
    public Date getObuOnlineDate() {
	return obuOnlineDate;
    }

    /**
     * @param obuOnlineDate
     *            the obuOnlineDate to set
     */
    public void setObuOnlineDate(Date obuOnlineDate) {
	this.obuOnlineDate = obuOnlineDate;
    }

    /**
     * @return the obuVehicleDate
     */
    public Date getObuVehicleDate() {
	return obuVehicleDate;
    }

    /**
     * @param obuVehicleDate
     *            the obuVehicleDate to set
     */
    public void setObuVehicleDate(Date obuVehicleDate) {
	this.obuVehicleDate = obuVehicleDate;
    }

    public String getObuid() {
	return obuid;
    }

    public void setObuid(String obuid) {
	this.obuid = obuid;
    }

    public String getIsOnline() {
	return isOnline;
    }

    public void setIsOnline(String isOnline) {
	this.isOnline = isOnline;
    }

    public int getDay() {
	return day;
    }

    public void setDay(int day) {
	this.day = day;
    }

}
