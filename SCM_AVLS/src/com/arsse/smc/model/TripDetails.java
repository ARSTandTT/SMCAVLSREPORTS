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
public class TripDetails implements Serializable {

	private String fillingStationName;
	private String roName;
	private String transporterName;
	private int vehicleId;
	private int vehicletypeId;
	private int serviceId;
	private int showonlymissed;
	private String regNo;
	private String invoiceNo;
	private int tripId;
	
	private Date startTime;
	private Date endTime;
	private String startLocation;
	private String endLocation;
	private String materialName;
	private float distance;
	private String quantity;
	private float offRouteDistance;
	private String tripStartTime;
	

	/**
	 * @return the fillingStationName
	 */
	public String getFillingStationName() {
		return fillingStationName;
	}

	/**
	 * @param fillingStationName
	 *            the fillingStationName to set
	 */
	public void setFillingStationName(String fillingStationName) {
		this.fillingStationName = fillingStationName;
	}

	public int getVehicletypeId() {
		return vehicletypeId;
	}

	public void setVehicletypeId(int vehicletypeId) {
		this.vehicletypeId = vehicletypeId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getShowonlymissed() {
		return showonlymissed;
	}

	public void setShowonlymissed(int showonlymissed) {
		this.showonlymissed = showonlymissed;
	}

	/**
	 * @return the roName
	 */
	public String getRoName() {
		return roName;
	}

	/**
	 * @param roName
	 *            the roName to set
	 */
	public void setRoName(String roName) {
		this.roName = roName;
	}

	/**
	 * @return the transporterName
	 */
	public String getTransporterName() {
		return transporterName;
	}

	/**
	 * @param transporterName
	 *            the transporterName to set
	 */
	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	/**
	 * @return the vehicleId
	 */
	public int getVehicleId() {
		return vehicleId;
	}

	/**
	 * @param vehicleId
	 *            the vehicleId to set
	 */
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	/**
	 * @return the regNo
	 */
	public String getRegNo() {
		return regNo;
	}

	/**
	 * @param regNo
	 *            the regNo to set
	 */
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * @param invoiceNo
	 *            the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/**
	 * @return the tripId
	 */
	public int getTripId() {
		return tripId;
	}

	/**
	 * @param tripId
	 *            the tripId to set
	 */
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the startLocation
	 */
	public String getStartLocation() {
		return startLocation;
	}

	/**
	 * @param startLocation
	 *            the startLocation to set
	 */
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	/**
	 * @return the endLocation
	 */
	public String getEndLocation() {
		return endLocation;
	}

	/**
	 * @param endLocation
	 *            the endLocation to set
	 */
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * @param materialName
	 *            the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the offRouteDistance
	 */
	public float getOffRouteDistance() {
		return offRouteDistance;
	}

	/**
	 * @param offRouteDistance
	 *            the offRouteDistance to set
	 */
	public void setOffRouteDistance(float offRouteDistance) {
		this.offRouteDistance = offRouteDistance;
	}

	/**
	 * @return the tripStartTime
	 */
	public String getTripStartTime() {
		return tripStartTime;
	}

	/**
	 * @param tripStartTime
	 *            the tripStartTime to set
	 */
	public void setTripStartTime(String tripStartTime) {
		this.tripStartTime = tripStartTime;
	}

}
