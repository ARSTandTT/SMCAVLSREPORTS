/**
 * 
 */
package com.arsse.smc.model;

import java.io.Serializable;

/**
 * @author Resmir
 * 
 */
public class ViolationActionTaken implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4960480247371478786L;
    private String regno;
    private String region;
    private String area;
    private String territory;
    private String depot;
    private String transporter;
    private String violationName;
    private String obuId;
    private int violationCount;
    private int explanationSought;
    private int suspension1Day;
    private int suspension1Week;
    private int suspension1Month;
    private int blackListed;
    private int openCases;
    private int closedCases;

    /**
     * @return the closedCases
     */
    public int getClosedCases() {
	return closedCases;
    }

    /**
     * @param closedCases
     *            the closedCases to set
     */
    public void setClosedCases(int closedCases) {
	this.closedCases = closedCases;
    }

    private int totalActionTaken;

    /**
     * @return the regno
     */
    public String getRegno() {
	return regno;
    }

    /**
     * @param regno
     *            the regno to set
     */
    public void setRegno(String regno) {
	this.regno = regno;
    }

    /**
     * @return the region
     */
    public String getRegion() {
	return region;
    }

    /**
     * @param region
     *            the region to set
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
     * @param area
     *            the area to set
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
     * @param territory
     *            the territory to set
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
     * @param depot
     *            the depot to set
     */
    public void setDepot(String depot) {
	this.depot = depot;
    }

    /**
     * @return the violationName
     */
    public String getViolationName() {
	return violationName;
    }

    /**
     * @param violationName
     *            the violationName to set
     */
    public void setViolationName(String violationName) {
	this.violationName = violationName;
    }

    /**
     * @return the violationCount
     */
    public int getViolationCount() {
	return violationCount;
    }

    /**
     * @param violationCount
     *            the violationCount to set
     */
    public void setViolationCount(int violationCount) {
	this.violationCount = violationCount;
    }

    /**
     * @return the explanationSought
     */
    public int getExplanationSought() {
	return explanationSought;
    }

    /**
     * @param explanationSought
     *            the explanationSought to set
     */
    public void setExplanationSought(int explanationSought) {
	this.explanationSought = explanationSought;
    }

    /**
     * @return the suspension1Day
     */
    public int getSuspension1Day() {
	return suspension1Day;
    }

    /**
     * @param suspension1Day
     *            the suspension1Day to set
     */
    public void setSuspension1Day(int suspension1Day) {
	this.suspension1Day = suspension1Day;
    }

    /**
     * @return the suspension1Week
     */
    public int getSuspension1Week() {
	return suspension1Week;
    }

    /**
     * @param suspension1Week
     *            the suspension1Week to set
     */
    public void setSuspension1Week(int suspension1Week) {
	this.suspension1Week = suspension1Week;
    }

    /**
     * @return the suspension1Month
     */
    public int getSuspension1Month() {
	return suspension1Month;
    }

    /**
     * @param suspension1Month
     *            the suspension1Month to set
     */
    public void setSuspension1Month(int suspension1Month) {
	this.suspension1Month = suspension1Month;
    }

    /**
     * @return the blackListed
     */
    public int getBlackListed() {
	return blackListed;
    }

    /**
     * @param blackListed
     *            the blackListed to set
     */
    public void setBlackListed(int blackListed) {
	this.blackListed = blackListed;
    }

    /**
     * @return the openCases
     */
    public int getOpenCases() {
	return openCases;
    }

    /**
     * @param openCases
     *            the openCases to set
     */
    public void setOpenCases(int openCases) {
	this.openCases = openCases;
    }

    /**
     * @return the transporter
     */
    public String getTransporter() {
	return transporter;
    }

    /**
     * @param transporter
     *            the transporter to set
     */
    public void setTransporter(String transporter) {
	this.transporter = transporter;
    }

    /**
     * @return the totalActionTaken
     */
    public int getTotalActionTaken() {
	return totalActionTaken;
    }

    /**
     * @param totalActionTaken
     *            the totalActionTaken to set
     */
    public void setTotalActionTaken(int totalActionTaken) {
	this.totalActionTaken = totalActionTaken;
    }

    /**
     * @return the obuId
     */
    public String getObuId() {
        return obuId;
    }

    /**
     * @param obuId the obuId to set
     */
    public void setObuId(String obuId) {
        this.obuId = obuId;
    }

}
