/**
 * 
 */
package com.arsse.smc.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Resmir
 *
 */
public class AbsenteeData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 198951356887728113L;
    private String region;
    private String area ;
    private String territory ;
    private String depot ;
    private String transporter;
    private String roName;    
    private String regNo;
    private Date consignmentDate;
    private int day;
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
     * @return the consignmentDate
     */
    public Date getConsignmentDate() {
        return consignmentDate;
    }
    /**
     * @param consignmentDate the consignmentDate to set
     */
    public void setConsignmentDate(Date consignmentDate) {
        this.consignmentDate = consignmentDate;
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
}
