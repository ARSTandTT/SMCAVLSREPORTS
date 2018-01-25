package com.arsse.smc.model;

import java.io.Serializable;

/**
 * 
 * @author Lekshmijraj
 * 
 */
public class PerformanceAvailabilityData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -3168309331910133853L;
    
    private String obuId;
    private String regNo;
    private String region;
    private String area;
    private String territory;
    private String depot;
    private String transporter;
    private int totalTankerLorries;
    private int totalVtsUnderRepair;
    private int totalOperationalVts;
    private int totalVtsNotGivingSignals;
    private int totalVtsReporting;
    private int totalVtsTampered;
    private int vtsUpTime;

    public String getTransporter() {
	return transporter;
    }

    public void setTransporter(String transporter) {
	this.transporter = transporter;
    }

    public int getTotalOperationalVts() {
	return totalOperationalVts;
    }

    public void setTotalOperationalVts(int totalOperationalVts) {
	this.totalOperationalVts = totalOperationalVts;
    }

    public int getTotalVtsReporting() {
	return totalVtsReporting;
    }

    public void setTotalVtsReporting(int totalVtsReporting) {
	this.totalVtsReporting = totalVtsReporting;
    }

    public int getVtsUpTime() {
	return vtsUpTime;
    }

    public void setVtsUpTime(int vtsUpTime) {
	this.vtsUpTime = vtsUpTime;
    }

    public String getObuId() {
	return obuId;
    }

    public void setObuId(String obuId) {
	this.obuId = obuId;
    }

    public String getRegNo() {
	return regNo;
    }

    public void setRegNo(String regNo) {
	this.regNo = regNo;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
    }

    public String getArea() {
	return area;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public String getTerritory() {
	return territory;
    }

    public void setTerritory(String territory) {
	this.territory = territory;
    }

    public String getDepot() {
	return depot;
    }

    public void setDepot(String depot) {
	this.depot = depot;
    }

    public int getTotalTankerLorries() {
	return totalTankerLorries;
    }

    public void setTotalTankerLorries(int totalTankerLorries) {
	this.totalTankerLorries = totalTankerLorries;
    }

    public int getTotalVtsUnderRepair() {
	return totalVtsUnderRepair;
    }

    public void setTotalVtsUnderRepair(int totalVtsUnderRepair) {
	this.totalVtsUnderRepair = totalVtsUnderRepair;
    }

    public int getTotalVtsNotGivingSignals() {
	return totalVtsNotGivingSignals;
    }

    public void setTotalVtsNotGivingSignals(int totalVtsNotGivingSignals) {
	this.totalVtsNotGivingSignals = totalVtsNotGivingSignals;
    }

    public int getTotalVtsTampered() {
	return totalVtsTampered;
    }

    public void setTotalVtsTampered(int totalVtsTampered) {
	this.totalVtsTampered = totalVtsTampered;
    }

}
