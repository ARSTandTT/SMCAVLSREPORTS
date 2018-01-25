/**
 * 
 */
package com.arsse.smc.model;

import java.io.Serializable;

/**
 * 
 * 
 * @author Resmir
 *
 */
public class Absentee implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -6543032566519077322L;
    
    private String region;
    private String area ;
    private String territory ;
    private String depot ;
    private String transporter;
    private String roName;    
    private String regNo;
    //For Depot Level
    private int day;
    private String available = "";
    private String totalVehicleCount = "";
    
    //For higher levels.
    
    private int totalWorkDays;
    private int totalAbsentDays;
    private float percentage;
    
    /**
     * @return the regNo
     */
    public String getRegNo() {
        return regNo;
    }
    /**
     * @param regNo the regNo to set
     */
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }
    /**
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }
    /**
     * @return the available
     */
    public String getAvailable() {
        return available;
    }
    /**
     * @param available the available to set
     */
    public void setAvailable(String available) {
        this.available = available;
    }
    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }
    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }
    /**
     * @return the area
     */
    public String getArea() {
        return area;
    }
    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }
    /**
     * @return the territory
     */
    public String getTerritory() {
        return territory;
    }
    /**
     * @param territory the territory to set
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }
    /**
     * @return the depot
     */
    public String getDepot() {
        return depot;
    }
    /**
     * @param depot the depot to set
     */
    public void setDepot(String depot) {
        this.depot = depot;
    }
    /**
     * @return the transporter
     */
    public String getTransporter() {
        return transporter;
    }
    /**
     * @param transporter the transporter to set
     */
    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }
    /**
     * @return the roName
     */
    public String getRoName() {
        return roName;
    }
    /**
     * @param roName the roName to set
     */
    public void setRoName(String roName) {
        this.roName = roName;
    }
    /**
     * @return the totalVehicleCount
     */
    public String getTotalVehicleCount() {
        return totalVehicleCount;
    }
    /**
     * @param totalVehicleCount the totalVehicleCount to set
     */
    public void setTotalVehicleCount(String totalVehicleCount) {
        this.totalVehicleCount = totalVehicleCount;
    }
    /**
     * @return the totalWorkDays
     */
    public int getTotalWorkDays() {
        return totalWorkDays;
    }
    /**
     * @param totalWorkDays the totalWorkDays to set
     */
    public void setTotalWorkDays(int totalWorkDays) {
        this.totalWorkDays = totalWorkDays;
    }
    /**
     * @return the totalAbsentDays
     */
    public int getTotalAbsentDays() {
        return totalAbsentDays;
    }
    /**
     * @param totalAbsentDays the totalAbsentDays to set
     */
    public void setTotalAbsentDays(int totalAbsentDays) {
        this.totalAbsentDays = totalAbsentDays;
    }
    /**
     * @return the percentage
     */
    public float getPercentage() {
        return percentage;
    }
    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
