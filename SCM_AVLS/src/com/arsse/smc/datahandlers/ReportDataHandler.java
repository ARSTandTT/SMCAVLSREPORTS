/**
 * 
 */
package com.arsse.smc.datahandlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;





import com.arsse.smc.common.Constants;
import com.arsse.smc.database.DBManager;
import com.arsse.smc.database.ProductDBManager;
import com.arsse.smc.model.Absentee;
import com.arsse.smc.model.AbsenteeData;
import com.arsse.smc.model.ObuStatus;
import com.arsse.smc.model.PerformanceData;
import com.arsse.smc.model.PowerDisconnectedData;
import com.arsse.smc.model.ProductData;
import com.arsse.smc.model.TripDetails;
import com.arsse.smc.model.ViolationActionTaken;
import com.ibm.icu.util.Calendar;

/**
 * Data Handler class for various Reports.
 * 
 * @author Resmir
 * 
 */
public class ReportDataHandler {

    private Logger log = Logger.getLogger(ReportDataHandler.class);

    /**
     * Returns power disconnected data.
     * 
     * @param userId
     * @param vehicleId
     * @return
     */
    public List<PowerDisconnectedData> getPowerDisconnectedDataForVehicle(
	    int userId, String vehicleNr, String fromDate, String toDate,String status) {
	List<PowerDisconnectedData> powerDisconnectList = new ArrayList<PowerDisconnectedData>();
	DBManager dbManager = DBManager.getInstance();
	log.info("getPowerDisconnectedDataForVehicle - Vehcle violation summary report");
	try {
	    log.info("getOBUIdForVehicle...");
	    String obuId = dbManager.getOBUIdForVehicle(userId, vehicleNr);
	    if (obuId != null) {
		ProductDBManager prdbManager = ProductDBManager.getInstance();
		log.info("getPowerDisconnectedDataForOBU from mysql...");
		List<String[]> dataList = prdbManager
			.getPowerDisconnectedDataForOBU(obuId, fromDate, toDate,status);
		int dataCount = dataList.size();
		for (String[] dataArray : dataList) {
		    PowerDisconnectedData disconnectedData = new PowerDisconnectedData();
		    disconnectedData.setRecordedTime(dataArray[0]);
		    if(dataArray[1] != null) {
			disconnectedData.setStatus(dataArray[1]);
		    } else {
			disconnectedData.setStatus("-1"); //Status not available
		    }
		    disconnectedData.setTotalCount(dataCount);
		    powerDisconnectList.add(disconnectedData);
		}
	    }
	    log.info("Total Disconnect count : " + powerDisconnectList.size());
	} catch (Exception e) {
	    log.error("Error occured while retrieving power disconnected data : "
		    + e.getMessage());
	}
	return powerDisconnectList;
    }

    /**
     * Returns absentee data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<Absentee> getAbsenteeData(int level, int userId, int month,
	    int year, String regionId, String areaId, String territoryId,
	    String depotId, String endLevelId) {
	log.info("getAbsenteeData... ");
	List<AbsenteeData> pDataList = new ArrayList<AbsenteeData>();
	List<Absentee> absenteeList = new ArrayList<Absentee>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    pDataList = dbManager.getAbsenteeData(level, userId, month, year,
		    regionId, areaId, territoryId, depotId, endLevelId);
	    log.info("Received absentee data...");
	    List<AbsenteeData> absenteeListData = null;
	    HashMap<String, List<AbsenteeData>> absenteeMap = new HashMap<String, List<AbsenteeData>>();
	    String regNo = null;
	    for (AbsenteeData absenteeData : pDataList) {
		regNo = absenteeData.getRegNo() + "%" + absenteeData.getDepot();
		absenteeListData = absenteeMap.get(regNo);
		if (absenteeListData != null) {
		    absenteeListData.add(absenteeData);
		} else {
		    absenteeListData = new ArrayList<AbsenteeData>();
		    absenteeListData.add(absenteeData);
		}
		absenteeMap.put(regNo, absenteeListData);
	    }
	    
	    if(level == Constants.DEPOT_LEVEL) {
		absenteeList = generateAbsenteeDataPerDay(month, year, absenteeMap);
	    } else {
		absenteeList = generateAbsenteeDataTotal(month, year, absenteeMap);
	    }
	} catch (Exception e) {
	    log.error("Error occured while retrieving performance data : "
		    + e.getMessage());
	}
	log.info("Processed Absentee count : " + absenteeList.size());
	return absenteeList;
    }

    /**
     * 
     * @param month
     * @param year
     * @param availMap
     * @return
     */
    private List<Absentee> generateAbsenteeDataPerDay(int month, int year,
	    HashMap<String, List<AbsenteeData>> availMap) {
	List<Absentee> availData = new ArrayList<Absentee>();
	List<AbsenteeData> availListData = null;
	int obuDay = 0;
	int prevDay = 0;
	String fillStatus = null;
	int totalMonthDays = 31;
	switch (month) {
	case 4:
	case 6:
	case 9:
	case 11:
	    totalMonthDays = 30;
	    break;
	case 2:
	    if ((year % 4) == 0) {
		totalMonthDays = 29;
	    } else {
		totalMonthDays = 28;
	    }

	default:
	    totalMonthDays = 31;
	    break;
	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());
	int todayDate = cal.get(Calendar.DATE);
	int todayMonth = cal.get(Calendar.MONTH) + 1;
	int todayYear = cal.get(Calendar.YEAR);
	boolean isCurrentMonth = false;
	if (month == todayMonth && year == todayYear) {
	    isCurrentMonth = true;
	}
	String region = null;
	String area = null;
	String territory = null;
	String depot = null;
	String regNo = null;
	int totalVehicleCount = availMap.size();
	for (String key : availMap.keySet()) {
	    availListData = availMap.get(key);
	    regNo = key.split("%")[0];
	    int countDay = 1;
	    prevDay = 0;

	    for (AbsenteeData absenteeData : availListData) {
		obuDay = absenteeData.getDay();
		if(absenteeData.getConsignmentDate() != null) {
		    region = absenteeData.getRegion();
		    area = absenteeData.getArea();
		    territory = absenteeData.getTerritory();
		    depot = absenteeData.getDepot();
		    if (obuDay == prevDay) {
			continue;
		    }
		    if (countDay < obuDay) {
			// No data till a date
			fillStatus = "N";

			// fill with previous status up to current day.
			for (int k = countDay; k <= obuDay - 1; k++) {
			    Absentee absentee = new Absentee();
			    absentee.setRegNo(regNo);
			    absentee.setAvailable(fillStatus);
			    absentee.setDay(countDay);
			    absentee.setTotalVehicleCount(String
				    .valueOf(totalVehicleCount));
			    absentee.setRegion(region);
			    absentee.setArea(area);
			    absentee.setTerritory(territory);
			    absentee.setDepot(depot);
			    availData.add(absentee);
			    countDay++;
			}
		    }
		    // add received day data
		    Absentee absentee = new Absentee();
		    absentee.setRegNo(regNo);
		    absentee.setAvailable("Y");
		    absentee.setDay(obuDay);
		    absentee.setTotalVehicleCount(String.valueOf(totalVehicleCount));
		    absentee.setRegion(region);
		    absentee.setArea(area);
		    absentee.setTerritory(territory);
		    absentee.setDepot(depot);
		    availData.add(absentee);
		    countDay = obuDay + 1;
		    prevDay = obuDay;
		}
	    }
	    // Fill remaining days up to end of month
	    if ((countDay - 1) < totalMonthDays) {
		for (int k = countDay; k <= totalMonthDays; k++) {
		    Absentee absentee = new Absentee();
		    absentee.setRegNo(regNo);
		    if (!isCurrentMonth) {
			absentee.setAvailable("N");
		    } else {
			if (k > todayDate) {
			    absentee.setAvailable(" ");
			} else {
			    absentee.setAvailable("N");
			}
		    }
		    absentee.setDay(k);
		    absentee.setTotalVehicleCount(String
			    .valueOf(totalVehicleCount));
		    absentee.setRegion(region);
		    absentee.setArea(area);
		    absentee.setTerritory(territory);
		    absentee.setDepot(depot);
		    availData.add(absentee);
		}
	    }
	}

	return availData;
    }

    /**
     * 
     * @param month
     * @param year
     * @param availMap
     * @return
     */
    private List<Absentee> generateAbsenteeDataTotal(int month, int year,
	    HashMap<String, List<AbsenteeData>> availMap) {
	List<Absentee> availData = new ArrayList<Absentee>();
	List<AbsenteeData> availListData = null;
	int totalMonthDays = 31;
	switch (month) {
	case 4:
	case 6:
	case 9:
	case 11:
	    totalMonthDays = 30;
	    break;
	case 2:
	    if ((year % 4) == 0) {
		totalMonthDays = 29;
	    } else {
		totalMonthDays = 28;
	    }

	default:
	    totalMonthDays = 31;
	    break;
	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());
	int todayDate = cal.get(Calendar.DATE);
	int todayMonth = cal.get(Calendar.MONTH) + 1;
	int todayYear = cal.get(Calendar.YEAR);
	boolean isCurrentMonth = false;
	if (month == todayMonth && year == todayYear) {
	    isCurrentMonth = true;
	    totalMonthDays = todayDate;
	}
	String region = null;
	String area = null;
	String territory = null;
	String depot = null;
	String regNo = null;
	for (String key : availMap.keySet()) {
	    availListData = availMap.get(key);
	    regNo = key.split("%")[0];
	    AbsenteeData absenteeData = availListData.get(0);
	    region = absenteeData.getRegion();
	    area = absenteeData.getArea();
	    territory = absenteeData.getTerritory();
	    depot = absenteeData.getDepot();
	    Absentee absentee = new Absentee();
	    absentee.setRegNo(regNo);
	    absentee.setRegion(region);
	    absentee.setArea(area);
	    absentee.setTerritory(territory);
	    absentee.setDepot(depot);
	    int workDayListSize = availListData.size();
	    if(workDayListSize == 1) {
		if(absenteeData.getConsignmentDate() == null) {
		    absentee.setTotalWorkDays(0);
		} else {
		    absentee.setTotalWorkDays(1);
		}
	    } else {
		absentee.setTotalWorkDays(workDayListSize);
	    }
	    
	    absentee.setTotalAbsentDays(totalMonthDays - absentee.getTotalWorkDays());
	    float tot = absentee.getTotalWorkDays() + absentee.getTotalAbsentDays();
	    absentee.setPercentage((absentee.getTotalWorkDays() /tot) * 100);
	    availData.add(absentee);
	}
	return availData;
    }    
    /**
     * Returns Violation actions list.
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
    public List<ViolationActionTaken> getViolationActionTakenData(int level,
	    int userId, String fromDate, String toDate, String regionId,
	    String areaId, String territoryId, String depotId, String endLevelId) {
	List<ViolationActionTaken> vActionList = new ArrayList<ViolationActionTaken>();
	try {
	    DBManager dbManager = DBManager.getInstance();
	    log.info("Get violation action Route...");
	    List<ViolationActionTaken> routeDataList = dbManager
		    .getViolationActionTaken(level, userId, fromDate, toDate,
			    regionId, areaId, territoryId, depotId, endLevelId, 1);
	    log.info("Get violation action Speed...");
	    List<ViolationActionTaken> speedDataList = dbManager
		    .getViolationActionTaken(level, userId, fromDate, toDate,
			    regionId, areaId, territoryId, depotId, endLevelId, 2);
	    log.info("Get violation action Stoppage...");
	    List<ViolationActionTaken> stoppageDataList = dbManager
		    .getViolationActionTaken(level, userId, fromDate, toDate,
			    regionId, areaId, territoryId, depotId, endLevelId, 3);
	    vActionList.addAll(speedDataList);
	    vActionList.addAll(routeDataList);
	    vActionList.addAll(stoppageDataList);

	    ProductDBManager prdbManager = ProductDBManager.getInstance();
	    log.info("Get violation action Power disconnection...");
	    HashMap<String, ViolationActionTaken> powerDisconnectData = prdbManager
		    .getPowerDisconnectedActionTakenData(fromDate, toDate);
	    ViolationActionTaken pwDisconnectAction = null;
	    for (ViolationActionTaken violationActionTaken : speedDataList) {
		ViolationActionTaken pwDisconnectViolation = new ViolationActionTaken();
		pwDisconnectViolation.setRegion(violationActionTaken
			.getRegion());
		pwDisconnectViolation.setArea(violationActionTaken.getArea());
		pwDisconnectViolation.setTerritory(violationActionTaken
			.getTerritory());
		pwDisconnectViolation.setDepot(violationActionTaken.getDepot());
		pwDisconnectViolation.setRegno(violationActionTaken.getRegno());
		pwDisconnectViolation.setTransporter(violationActionTaken
			.getTransporter());
		pwDisconnectViolation.setViolationName("Power Disconnected");
		if(powerDisconnectData.get(violationActionTaken.getObuId()) != null) {
		    pwDisconnectAction = powerDisconnectData.get(violationActionTaken.getObuId());
		    pwDisconnectViolation.setTotalActionTaken(pwDisconnectAction.getTotalActionTaken());
		    pwDisconnectViolation.setOpenCases(pwDisconnectAction.getOpenCases());
		    pwDisconnectViolation.setExplanationSought(pwDisconnectAction.getExplanationSought());
		    pwDisconnectViolation.setBlackListed(pwDisconnectAction.getBlackListed());
		    pwDisconnectViolation.setSuspension1Day(pwDisconnectAction.getSuspension1Day());
		    pwDisconnectViolation.setSuspension1Week(pwDisconnectAction.getSuspension1Week());
		    pwDisconnectViolation.setSuspension1Month(pwDisconnectAction.getSuspension1Month());
		}
		vActionList.add(pwDisconnectViolation);
	    }
	} catch (Exception e) {
	    log.error("Exception occured while executing violation action taken "
		    + e.getMessage());
	}
	log.info("Returning violation action data");
	return vActionList;
    }

    /**
     * Returns heading name to be shown in reports.
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
    public String getHeaderNameByLevel(int level, String regionId, String areaId,
	    String territoryId, String depotId, String endLevelId) {
	String headerName = "";
	String result = "";
	DBManager dbManager = DBManager.getInstance();
	log.info("Executing getHeaderNameByLevel : " + level);
	switch (level) {
	case Constants.ALLINDIA_LEVEL:
	    headerName = " All India";
	    break;
	case Constants.REGION_LEVEL:
	    result = dbManager.executeGetNameQuery("SELECT Level1Name FROM dbo.LocationLevel1 WHERE LocationLevel1Id = " + regionId);
	    headerName = " Region : " + result;
	    break;
	case Constants.AREA_LEVEL:
	    result = dbManager.executeGetNameQuery("SELECT Level2Name FROM dbo.LocationLevel2 WHERE LocationLevel2Id = " + areaId);
	    headerName = " Area : " + result;
	    break;
	case Constants.TERRITORY_LEVEL:
	    result = dbManager.executeGetNameQuery("SELECT Level3Name FROM dbo.LocationLevel3 WHERE LocationLevel3Id = " + territoryId);
	    headerName = " Territory : " + result;
	    break;
	case Constants.DEPOT_LEVEL:
	    result = dbManager.executeGetNameQuery("select dbo.FillingStation.FillingStationName as Depot from dbo.FillingStation where dbo.FillingStation.FillingStationId = " + depotId);
	    headerName = " Depot : " + result;
	    break;
	default:
	    break;
	}

	log.info("Header Name : " + headerName);    
	return headerName;
    }
    
    /**
     * Returns absentee data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<ProductData> getProductWiseData(int materialId, int level, int userId, String fromDate,
	    String toDate, String regionId, String areaId, String territoryId,
	    String depotId, String endLevelId) {
	log.info("getProductWiseData... ");
	List<ProductData> pDataList = new ArrayList<ProductData>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    pDataList = dbManager.getProductWiseData(materialId, level, userId, fromDate, toDate,
		    regionId, areaId, territoryId, depotId, endLevelId);
	    log.info("Received product wise data...");
	} catch (Exception e) {
	    log.error("Error occured while product wise data : "
		    + e.getMessage());
	}
	log.info("Product Data Count : " + pDataList.size());
	return pDataList;
    }
    
    
    /**
     * Returns getTripDetailsDepot data.
     * 
     * @param level
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<TripDetails> getTripDetailsDepot(int userId, String fromDate,
	    String toDate, String regionId, String areaId, String territoryId,
	    String depotId) {
	log.info("getTripDetailsDepot... ");
	List<TripDetails> pDataList = new ArrayList<TripDetails>();
	DBManager dbManager = DBManager.getInstance();
	try {
	    pDataList = dbManager.getTripDetailsDepotData(userId, fromDate, toDate,
		    regionId, areaId, territoryId, depotId);
	    log.info("Received trip details depot data...");
	} catch (Exception e) {
	    log.error("Error occured while trip details depot : "
		    + e.getMessage());
	}
	log.info("Trip Data Count : " + pDataList.size());
	return pDataList;
    }    
    /*
     * public List<OpenViolations> getOpenViolationData(int level, int userId,
     * String fromDate, String toDate, String regionId, String areaId, String
     * territoryId , String depotId, String endLevelId) { List<OpenViolations>
     * vOpenList = new ArrayList<OpenViolations>(); DBManager dbManager =
     * DBManager.getInstance(); vOpenList = dbManager.getOpenViolations(level,
     * userId, fromDate, toDate, regionId, areaId, territoryId, depotId,
     * endLevelId); System.out.println("OpenList size " + vOpenList.size());
     * return vOpenList; }
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	ReportDataHandler dataHandler = new ReportDataHandler();
	//dataHandler.getProductWiseData(0,0, 1, "2012-06-01", "2012-06-10", "1","1","1","49",null);
	dataHandler.getTripDetailsDepot(1, "2013-04-01 00:00:00", "2013-04-01 23:59:59", "3", "14", "29", "75");
	//dataHandler.getViolationActionTakenData(1, 114, "2012-01-01",
	//	"2012-05-31", "1", null, null, null, null);
	//dataHandler.getPowerDisconnectedDataForVehicle(114, "KL01A7777",
	 //"2011-01-01 00:00", "2012-05-31 00:00","-1");
	//dataHandler.getAbsenteeData(4, 132, 5, 2012, "1","1","1","49",null);
	//dataHandler.getAbsenteeData(0, 114, 5, 2012, "1","1","1","49",null);
	// dataHandler.getOpenViolationData(1, 114, "2011-01-01 00:00",
	// "2012-05-31 00:00", "1",null,null,null,null);
    }

}
