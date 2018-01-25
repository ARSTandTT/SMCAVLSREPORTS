/**
 * 
 */
package com.arsse.smc.datahandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.arsse.smc.database.DBManager;
import com.arsse.smc.database.ProductDBManager;
import com.arsse.smc.model.PerformanceData;

/**
 * Handles data for performance reports.
 * 
 * @author Resmir
 *
 */
public class PerformanceDataHandler {

    private Logger log = Logger.getLogger(PerformanceDataHandler.class);
    /**
     * Returns performance data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<PerformanceData> getPerformanceData(int level, int userId, String fromDate, String toDate,
	    String regionId, String areaId, String territoryId
	    , String depotId, String endLevelId) {
	List<PerformanceData> pDataList = new ArrayList<PerformanceData>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    log.info("Getting peformance data from DB..");
	    pDataList = dbManager.getPerformanceData(level, userId, fromDate, toDate, regionId, areaId, territoryId
		    , depotId, endLevelId);
	    log.info("Received DB SQLServer data Size : " + pDataList.size());
	    if(pDataList.size() > 0) {
		ProductDBManager prdbManager = ProductDBManager.getInstance();
		log.info("Get PowerDisconnect data from MySQL..");
		HashMap<String, Integer> powerDisconnectData = prdbManager.getPowerDisconnectedData(fromDate, toDate);
		log.info("Recieved PowerDisconnect data from MySQL and Processing..");
		String obuId = null;
		for (PerformanceData performanceData : pDataList) {
		    obuId = performanceData.getObuId();
		    if(powerDisconnectData.get(obuId) != null) {
			performanceData.setPowerDisconnected(powerDisconnectData.get(obuId));
		    }
		}
	    }
	    log.info("Total Data Count for performance level " + level + " : " + pDataList.size());
	} catch (Exception e) {
	    log.error("Error occured while retrieving performance data : " + e.getMessage());
	}
	return pDataList;
    }

    /**
     * Returns performance data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<PerformanceData> getTopBottomPerformersData(int level, int userId, String fromDate, String toDate,
	    String regionId, String areaId, String territoryId
	    , String depotId, int percentage, int top, boolean isViolation) {
	List<PerformanceData> pDataList = new ArrayList<PerformanceData>();
	List<PerformanceData> returnDataList = new ArrayList<PerformanceData>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    pDataList = dbManager.getTopBottomPerformersData(level, userId, fromDate, toDate, regionId, areaId, territoryId
		    , depotId, isViolation);
	    int totalListSize = pDataList.size();
	    int countRequired = (totalListSize *  percentage) / 100;
	    //TODO CHECK for countRequired size- Handle if size < 0
	    if(totalListSize > 0) {
		if(top == 1) {
		    //Top performers
		    PerformanceData data = null;
		    int i = 0;
		    for (int cnt = 0; cnt < countRequired; cnt++) {
			data = pDataList.get(i);
			if(data.getTotalDistance() > 0) {
			    returnDataList.add(data);
			} else {
			    cnt--;
			}
			i++;
		    }
		} else {
		    //bottom performers
		    int i = totalListSize - 1;
		    PerformanceData data = null;
		    for (int cnt = 0; cnt < countRequired; cnt++) {
			data = pDataList.get(i);
			if(data.getTotalDistance() > 0) {
			    returnDataList.add(data);
			} else {
			    cnt--;
			}
			i--;
		    }
		}
	    }
	    
	    log.info("Total Data Count for performance level " + level + " : " + returnDataList.size());
	} catch (Exception e) {
	    log.error("Error occured while retrieving performance data : " + e.getMessage());
	}
	return returnDataList;
    }
    public static void main(String[] args) {
	PerformanceDataHandler dataHandler = new PerformanceDataHandler();
	//dataHandler.getPerformanceData(0, 114, "2011-01-01 00:00", "2012-05-31 00:00", null,null,null,null,null);
	//dataHandler.getTopBottomPerformersData(1, 1, "2012-06-01 00:00", "2012-06-13 00:00", "3",null,null,null,10,1, false);
	dataHandler.getTopBottomPerformersData(1, 1, "2012-06-01 00:00", "2012-06-13 00:00", "3",null,null,null,10,1, true);
	/*dataHandler.getPerformanceData(1, 114, "2011-01-01 00:00", "2012-05-31 00:00", "1",null,null,null,null);
	dataHandler.getPerformanceData(2, 114, "2011-01-01 00:00", "2012-05-31 00:00", "1","1",null,null,null);
	dataHandler.getPerformanceData(3, 114, "2011-01-01 00:00", "2012-05-31 00:00", "1","1","1",null,null);
	dataHandler.getPerformanceData(4, 114, "2011-01-01 00:00", "2012-05-31 00:00", "1","1","1","1",null);
	dataHandler.getPerformanceData(5, 114, "2011-01-01 00:00", "2012-05-31 00:00", "1","1","1","40","25");*/
    }
}
