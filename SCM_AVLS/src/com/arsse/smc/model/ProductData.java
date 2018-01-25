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
public class ProductData implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -8236993299673742498L;
    private String region;
    private String area ;
    private String territory ;
    private String depot ;
    private String roName;
    private String regNo;
    private int totalDeliveries ; 
    private float quantityDelivered ;    
    private String material;
    
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
     * @return the totalDeliveries
     */
    public int getTotalDeliveries() {
        return totalDeliveries;
    }
    /**
     * @param totalDeliveries the totalDeliveries to set
     */
    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
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
     * @return the material
     */
    public String getMaterial() {
        return material;
    }
    /**
     * @param material the material to set
     */
    public void setMaterial(String material) {
        this.material = material;
    }

}
