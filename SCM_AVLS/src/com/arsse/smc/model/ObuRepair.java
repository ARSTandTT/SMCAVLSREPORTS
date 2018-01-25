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
public class ObuRepair implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -9006258482655904144L;
    private String obuId;
    private Date repairStartDate;
    private Date repairEndDate;
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
     * @return the repairStartDate
     */
    public Date getRepairStartDate() {
        return repairStartDate;
    }
    /**
     * @param repairStartDate the repairStartDate to set
     */
    public void setRepairStartDate(Date repairStartDate) {
        this.repairStartDate = repairStartDate;
    }
    /**
     * @return the repairEndDate
     */
    public Date getRepairEndDate() {
        return repairEndDate;
    }
    /**
     * @param repairEndDate the repairEndDate to set
     */
    public void setRepairEndDate(Date repairEndDate) {
        this.repairEndDate = repairEndDate;
    }
    
    
}
