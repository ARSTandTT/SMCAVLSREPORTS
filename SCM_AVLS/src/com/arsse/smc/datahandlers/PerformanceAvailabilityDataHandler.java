package com.arsse.smc.datahandlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.layout.pdf.ISplitter;

import com.arsse.smc.database.DBManager;
import com.arsse.smc.database.ProductDBManager;
import com.arsse.smc.model.ObuRepair;
import com.arsse.smc.model.ObuStatus;
import com.arsse.smc.model.PerformanceAvailabilityData;

/**
 * handles data for performance availability Report
 * 
 * @author Lekshmijraj
 * 
 */
public class PerformanceAvailabilityDataHandler {

    private Logger log = Logger
	    .getLogger(PerformanceAvailabilityDataHandler.class);

    /**
     * Returns performance availability data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @param regionId
     * @param areaId
     * @param territoryId
     * @param depotId
     * @param endLevelId
     * @return
     */
    public List<PerformanceAvailabilityData> getPerformanceAvailabilityData(
	    int level, int userId, String fromDate, String toDate,
	    String regionId, String areaId, String territoryId, String depotId,
	    String endLevelId) {
	List<PerformanceAvailabilityData> pDataList = new ArrayList<PerformanceAvailabilityData>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    log.info("getPerformanceAvailabilityData from SQL server... ");
	    pDataList = dbManager.getPerformanceAvailabilityData(level, userId,
		    fromDate, toDate, regionId, areaId, territoryId, depotId,
		    endLevelId);
	    log.info("Received data from SQL server");
	    if (pDataList.size() > 0) {
		ProductDBManager prdbManager = ProductDBManager.getInstance();
		log.info("Get obu repair details from mysql..");
		HashMap<String, ArrayList<ObuRepair>> obuRepairDetails = prdbManager
			.getOBURepairDetails();
		log.info("Get obu tampered data from mysql");
		HashMap<String, Integer> obuTamperedData = prdbManager
			.getTamperedDevices(fromDate, toDate);
		//VTS not giving signals 
		log.info("Get obu not giving signals from mysql...");
		List<String> obuGivingSignalsList = prdbManager.getObuGivingSignals(fromDate, toDate);
		String obuId = null;
		ArrayList<ObuRepair> repairList = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date periodStartDate = dateFormat.parse(fromDate);
		Date periodEndDate = dateFormat.parse(toDate);
		boolean isUnderRepair = false;
		boolean isOperational = true;
		log.info("Processing availability data....");
		for (PerformanceAvailabilityData performanceAvailabilityData : pDataList) {
		    obuId = performanceAvailabilityData.getObuId();
		    repairList = obuRepairDetails.get(obuId);
		    
		    //VTS under repair and total operational vts.
		    if (repairList != null && repairList.size() > 0) {
			ObuRepair obuRepair = repairList.get(0);
			isUnderRepair = false;
			isOperational = true;
			if(obuRepair.getRepairEndDate() == null) {
			    if(obuRepair.getRepairStartDate().before(periodEndDate)) {
				//Under repair which is not completed, so not operational
				isUnderRepair = true;
				isOperational = false;
			    }
			} else {
			    //Take latest repair info
			    obuRepair = repairList.get(repairList.size() - 1);
			    Date repairStartDate = obuRepair.getRepairStartDate();
			    Date repairEndDate = obuRepair.getRepairEndDate();
			    
			    isUnderRepair = true;
			    if(repairEndDate.before(periodStartDate)) {
				isUnderRepair = false;
			    }
			    if(((repairStartDate.compareTo(periodStartDate) == 0) 
				    && (repairEndDate.compareTo(periodEndDate) == 0)) ||
				    (repairStartDate.before(periodStartDate) && repairEndDate.after(periodEndDate))) {
				//Completely under repair for the period, so not operational
				isOperational = false;
			    } 
 			}
			if(isUnderRepair) {
			    performanceAvailabilityData.setTotalVtsUnderRepair(1);
			}
			if(isOperational) {
			    performanceAvailabilityData.setTotalOperationalVts(1);
			}
		    } else {
			//No Repair found yet for the period, so operational
			performanceAvailabilityData.setTotalVtsUnderRepair(0);
			performanceAvailabilityData.setTotalOperationalVts(1);
		    }
		    		    
		    //Tampered
		    if (obuTamperedData.get("OBU " + obuId) != null) {
			performanceAvailabilityData.setTotalVtsTampered(1);
		    }
		    if(performanceAvailabilityData.getTotalOperationalVts() == 1) {
			//VTS not giving signals 
			if (obuGivingSignalsList != null && obuGivingSignalsList.contains(obuId) ) {
			    //Obu is giving signals
			    performanceAvailabilityData.setTotalVtsNotGivingSignals(0);
			} else {
			    performanceAvailabilityData.setTotalVtsNotGivingSignals(1); //not contained in yes list , so it is not giving signals
			}
		    }
		}

	    }
	} catch (Exception e) {
	    log.error("Error occured while retrieving performance data : "
		    + e.getMessage());
	}
	log.info("Performance availability data size " + pDataList.size());
	return pDataList;
    }

    public static void main(String[] args) {
	PerformanceAvailabilityDataHandler dataHandler = new PerformanceAvailabilityDataHandler();

	/*
	 * dataHandler.getPerformanceAvailabilityData(0, 114,
	 * "2011-01-01 00:00", "2012-05-31 00:00", null, null, null, null,
	 * null);
	 */
	List<PerformanceAvailabilityData> list = dataHandler
		.getPerformanceAvailabilityData(0, 114, "2013-01-01 00:00",
			"2013-06-30 00:00", "1", null, null, null, null);
	System.out.println("******************************************");
	System.out.println("dataset");
	System.out.println("******************************************");
	for (PerformanceAvailabilityData data : list) {
	    System.out.println("****************************************");
	    System.out.println("Regno   ::" + data.getRegNo());
	    System.out.println("OBU id  ::" + data.getObuId());
	    System.out.println("Total TL ::" + data.getTotalTankerLorries());
	    System.out.println("VTS Repair::" + data.getTotalVtsUnderRepair());
	    System.out.println("No signals::" + data.getTotalVtsNotGivingSignals());
	    System.out.println("Tampered  ::" + data.getTotalVtsTampered());
	    System.out.println("Operational : " + data.getTotalOperationalVts());
	}
    }
}
