package com.arsse.smc.model;

import java.io.Serializable;

public class AvailabilityData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2611975457012100091L;
    
    private String regNo;
    private int day;
    private String available = "";
    private int prevMonthCount;
    private int vehicleCount;

    public int getDay() {
	return day;
    }

    public void setDay(int day) {
	this.day = day;
    }

    public String getAvailable() {
	return available;
    }

    public void setAvailable(String available) {
	this.available = available;
    }

    public String getRegNo() {
	return regNo;
    }

    public void setRegNo(String regNo) {
	this.regNo = regNo;
    }

    public int getPrevMonthCount() {
	return prevMonthCount;
    }

    public void setPrevMonthCount(int prevMonthCount) {
	this.prevMonthCount = prevMonthCount;
    }

    public int getVehicleCount() {
	return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
	this.vehicleCount = vehicleCount;
    }

}
