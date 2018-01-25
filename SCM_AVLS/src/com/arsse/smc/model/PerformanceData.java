/**
 * 
 */
package com.arsse.smc.model;

import java.io.Serializable;

/**
 * @author Resmir
 *
 */
public class PerformanceData implements Serializable{

    private static final long serialVersionUID = 5323531234000167645L;
    
	private String regno;
	private String region;
	private String area ;
	private String territory ;
	private String depot ;
	private int routeViolation ;
	private int speedViolation ;
	private int stoppages ;
	private int powerDisconnected ;
	private int totalTrips ;
	private float onLoadDistance ;
	private float offLoadDistance ;
	private int deliveries ; 
	private float quantityDelivered ;
	private float totalDistance;
	private String obuId;
	private String transporter;
	private String roName;
	private float violationAverage;
	/**
	 * @return the regno
	 */
	public String getRegno() {
		return regno;
	}
	/**
	 * @param regno the regno to set
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
	 * @return the routeViolation
	 */
	public int getRouteViolation() {
		return routeViolation;
	}
	/**
	 * @param routeViolation the routeViolation to set
	 */
	public void setRouteViolation(int routeViolation) {
		this.routeViolation = routeViolation;
	}
	/**
	 * @return the speedViolation
	 */
	public int getSpeedViolation() {
		return speedViolation;
	}
	/**
	 * @param speedViolation the speedViolation to set
	 */
	public void setSpeedViolation(int speedViolation) {
		this.speedViolation = speedViolation;
	}
	/**
	 * @return the stoppages
	 */
	public int getStoppages() {
		return stoppages;
	}
	/**
	 * @param stoppages the stoppages to set
	 */
	public void setStoppages(int stoppages) {
		this.stoppages = stoppages;
	}
	/**
	 * @return the powerDisconnected
	 */
	public int getPowerDisconnected() {
		return powerDisconnected;
	}
	/**
	 * @param powerDisconnected the powerDisconnected to set
	 */
	public void setPowerDisconnected(int powerDisconnected) {
		this.powerDisconnected = powerDisconnected;
	}
	/**
	 * @return the totalTrips
	 */
	public int getTotalTrips() {
		return totalTrips;
	}
	/**
	 * @param totalTrips the totalTrips to set
	 */
	public void setTotalTrips(int totalTrips) {
		this.totalTrips = totalTrips;
	}
	/**
	 * @return the onLoadDistance
	 */
	public float getOnLoadDistance() {
		return onLoadDistance;
	}
	/**
	 * @param onLoadDistance the onLoadDistance to set
	 */
	public void setOnLoadDistance(float onLoadDistance) {
		this.onLoadDistance = onLoadDistance;
	}
	/**
	 * @return the offLoadDistance
	 */
	public float getOffLoadDistance() {
		return offLoadDistance;
	}
	/**
	 * @param offLoadDistance the offLoadDistance to set
	 */
	public void setOffLoadDistance(float offLoadDistance) {
		this.offLoadDistance = offLoadDistance;
	}
	/**
	 * @return the deliveries
	 */
	public int getDeliveries() {
		return deliveries;
	}
	/**
	 * @param deliveries the deliveries to set
	 */
	public void setDeliveries(int deliveries) {
		this.deliveries = deliveries;
	}
	/**
	 * @return the quantityDelivered
	 */
	public float getQuantityDelivered() {
		return quantityDelivered;
	}
	/**
	 * @param quantityDelivered the quantityDelivered to set
	 */
	public void setQuantityDelivered(float quantityDelivered) {
		this.quantityDelivered = quantityDelivered;
	}
	/**
	 * @return the totalDistance
	 */
	public float getTotalDistance() {
		return totalDistance;
	}
	/**
	 * @param totalDistance the totalDistance to set
	 */
	public void setTotalDistance(float totalDistance) {
		this.totalDistance = totalDistance;
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
	 * @return the violationAverage
	 */
	public float getViolationAverage() {
	    return violationAverage;
	}
	/**
	 * @param violationAverage the violationAverage to set
	 */
	public void setViolationAverage(float violationAverage) {
	    this.violationAverage = violationAverage;
	}
}
