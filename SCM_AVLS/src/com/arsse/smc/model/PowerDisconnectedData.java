/**
 * 
 */
package com.arsse.smc.model;

import java.io.Serializable;


/**
 * 
 * @author Resmir
 *
 */
public class PowerDisconnectedData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 6248003411850771008L;
    private String recordedTime;
    private int totalCount;
    private String status;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the recordedTime
     */
    public String getRecordedTime() {
        return recordedTime;
    }

    /**
     * @param recordedTime the recordedTime to set
     */
    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
