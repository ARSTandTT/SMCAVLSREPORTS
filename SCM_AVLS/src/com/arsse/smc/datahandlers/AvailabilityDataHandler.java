/**
 * 
 */
package com.arsse.smc.datahandlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.arsse.smc.database.DBManager;
import com.arsse.smc.database.ProductDBManager;
import com.arsse.smc.model.AvailabilityData;
import com.arsse.smc.model.ObuStatus;
import com.arsse.smc.util.Util;
import com.ibm.icu.util.Calendar;

/**
 * HAndler class for availability report.
 * 
 * @author resmir
 * 
 */
public class AvailabilityDataHandler {

    private Logger log = Logger.getLogger(ReportDataHandler.class);
    /**
     * 
     * @param month
     * @param year
     * @param userId
     * @return
     */
    public List<AvailabilityData> getAvailabilityData(int month, int year,
	    int userId) {
	log.info("getAvailabilityData.... ");
	List<AvailabilityData> availData = new ArrayList<AvailabilityData>();
	HashMap<String, List<Object>> obuRegMap = new HashMap<String, List<Object>>();
	DBManager dbManager = DBManager.getInstance();
	try {

	    obuRegMap = dbManager.getOBURegNo(userId);
	    log.info("Vehicles Data : " + obuRegMap);
	    ProductDBManager prdbManager = ProductDBManager.getInstance();
	    prdbManager.getConnection();
	    List<ObuStatus> obuStatusList = prdbManager.getObuStatus(month,
		    year);
	    HashMap<String, List<ObuStatus>> availMap = new HashMap<String, List<ObuStatus>>();
	    String regNo = null;
	    Date obuVehicleDate = null;
	    List<Object> regNoDateList = new ArrayList<Object>();
	    List<ObuStatus> availListData = null;
	    for (ObuStatus obuStatus : obuStatusList) {
		regNoDateList = obuRegMap.get(obuStatus.getObuid());
		if (regNoDateList != null) {
		    regNo = (String) regNoDateList.get(0);
		    obuVehicleDate = (Date) regNoDateList.get(1);

		    if (regNo != null) {
			availListData = availMap.get(regNo);
			if (availListData != null) {
			    obuStatus.setObuVehicleDate(obuVehicleDate);
			    availListData.add(obuStatus);
			} else {
			    availListData = new ArrayList<ObuStatus>();
			    obuStatus.setObuVehicleDate(obuVehicleDate);
			    availListData.add(obuStatus);
			}
			availMap.put(regNo, availListData);

		    }
		}
		int n = availMap.size();

	    }
	    /****** Take Prev mnth details **********/
	    HashMap<String, Integer> prevMnthMap = new HashMap<String, Integer>();
	    int prevMonth = month - 1;
	    int prevYear = year;
	    if (month == 1) {
		prevMonth = 12;
		prevYear = year - 1;
	    }
	    List<ObuStatus> prevStatusList = prdbManager.getObuStatus(
		    prevMonth, prevYear);
	    HashMap<String, List<ObuStatus>> prevAvailMap = new HashMap<String, List<ObuStatus>>();
	    for (ObuStatus obuStatus : prevStatusList) {
		regNoDateList = obuRegMap.get(obuStatus.getObuid());
		if (regNoDateList != null) {
		    regNo = (String) regNoDateList.get(0);
		    obuVehicleDate = (Date) regNoDateList.get(1);

		    if (regNo != null) {
			availListData = prevAvailMap.get(regNo);
			if (availListData != null) {
			    obuStatus.setObuVehicleDate(obuVehicleDate);
			    availListData.add(obuStatus);
			} else {
			    availListData = new ArrayList<ObuStatus>();
			    obuStatus.setObuVehicleDate(obuVehicleDate);
			    availListData.add(obuStatus);
			}
			prevAvailMap.put(regNo, availListData);
		    }
		}
	    }
	    List<AvailabilityData> prevAvailData = generateAvailabilityData(
		    prevMonth, prevYear, prevAvailMap, null);
	    int prevCnt = 0;
	    for (AvailabilityData availabilityData : prevAvailData) {
		if (availabilityData.getAvailable().equalsIgnoreCase("Y")) {
		    if (prevMnthMap.get(availabilityData.getRegNo()) != null) {
			prevCnt = prevMnthMap.get(availabilityData.getRegNo()) + 1;
			prevMnthMap.put(availabilityData.getRegNo(), prevCnt);
		    } else {
			prevMnthMap.put(availabilityData.getRegNo(), 1);
		    }
		}
	    }
	    
	    

	    /****** Take Prev mnth details END **********/
	    availData = generateAvailabilityData(month, year, availMap,
		    prevMnthMap);
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    log.error("Error occured in getAvailabilityData..." + e.getMessage());
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    log.error("Error occured in getAvailabilityData..." + e.getMessage());
	}
	log.info("Returning availability data.." + availData.size());
	return availData;
    }

    /**
     * 
     * @param month
     * @param year
     * @param availMap
     * @return
     */
    private List<AvailabilityData> generateAvailabilityData(int month,
	    int year, HashMap<String, List<ObuStatus>> availMap,
	    HashMap<String, Integer> prevMnthMap) {
	List<AvailabilityData> availData = new ArrayList<AvailabilityData>();
	List<ObuStatus> availListData = null;
	String onlineStatus = null;
	String prevOnlineStatus = null;
	int obuDay = 0;
	int prevDay = 0;
	Date obuFittedDate = null;
	Date obuOnlineDate = null;
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
	int totalVehicleCount = availMap.size();
	for (String key : availMap.keySet()) {
	    availListData = availMap.get(key);
	    int countDay = 1;
	    Date countDate = null;
	    prevOnlineStatus = null;
	    prevDay = 0;
	     for (ObuStatus obuStatus : availListData) {
		 
		onlineStatus = obuStatus.getIsOnline();
		obuDay = obuStatus.getDay();
		obuFittedDate = obuStatus.getObuVehicleDate();
		obuOnlineDate = obuStatus.getObuOnlineDate();
		if (obuDay == prevDay && onlineStatus.equals(prevOnlineStatus)) {
		    continue;
		}
		if (countDay < obuDay) {
		    // No data till a date
		    // if some data is there in first received onlinestatus.
		    if (prevOnlineStatus == null) {
			/*if (onlineStatus.equalsIgnoreCase("Y")) {
			    fillStatus = "N";
			} else {
			    fillStatus = "Y";
			}*/
			fillStatus = "N";
		    } else {
			fillStatus = prevOnlineStatus;
		    }
		    // fill with previous status up to current day.
		    for (int k = countDay; k <= obuDay - 1; k++) {
			AvailabilityData availabilityData = new AvailabilityData();
			availabilityData.setRegNo(key);
			if (obuOnlineDate != null) {
			    countDate = Util.buildDate(k, obuOnlineDate);
			}

			if (countDate != null && obuFittedDate != null
				&& countDate.compareTo(obuFittedDate) < 0) {
			    availabilityData.setAvailable(" ");
			} else {
			    availabilityData.setAvailable(fillStatus);
			}
			availabilityData.setDay(countDay);
			availabilityData.setVehicleCount(totalVehicleCount);
			if (prevMnthMap != null && prevMnthMap.get(key) != null) {
			    availabilityData.setPrevMonthCount(prevMnthMap
				    .get(key));
			}
			availData.add(availabilityData);
			countDay++;
		    }
		}
		// add received day data
		AvailabilityData availabilityData = new AvailabilityData();
		availabilityData.setRegNo(key);
		if (obuOnlineDate != null) {
		    countDate = Util.buildDate(countDay, obuOnlineDate);
		}

		if (countDate != null && obuFittedDate != null
			&& countDate.compareTo(obuFittedDate) < 0) {
		    availabilityData.setAvailable(" ");
		} else {
		    availabilityData.setAvailable(onlineStatus);
		}
		availabilityData.setDay(obuDay);
		availabilityData.setVehicleCount(totalVehicleCount);
		if (prevMnthMap != null && prevMnthMap.get(key) != null) {
		    availabilityData.setPrevMonthCount(prevMnthMap.get(key));
		}
		availData.add(availabilityData);
		countDay = obuDay + 1;
		prevOnlineStatus = onlineStatus;
		prevDay = obuDay;
	    }
	    // Fill remaining days up to end of month
	    if ((countDay - 1) < totalMonthDays) {
		for (int k = countDay; k <= totalMonthDays; k++) {
		    AvailabilityData availabilityData = new AvailabilityData();
		    availabilityData.setRegNo(key);
		    if (!isCurrentMonth) {
			if (obuOnlineDate != null) {
			    countDate = Util.buildDate(k, obuOnlineDate);
			}
			if (countDate != null && obuFittedDate != null
				&& countDate.compareTo(obuFittedDate) < 0) {
			    availabilityData.setAvailable(" ");
			} else {
			    availabilityData.setAvailable(prevOnlineStatus);
			}
		    } else {
			if (k > todayDate) {
			    availabilityData.setAvailable(" ");
			} else {
			    if (obuOnlineDate != null) {
				countDate = Util.buildDate(k, obuOnlineDate);
			    }
			    if (countDate != null && obuFittedDate != null
				    && countDate.compareTo(obuFittedDate) < 0) {
				availabilityData.setAvailable(" ");
			    } else {
				availabilityData.setAvailable(prevOnlineStatus);
			    }
			}
		    }
		    availabilityData.setDay(k);
		    availabilityData.setVehicleCount(totalVehicleCount);
		    
		    if (prevMnthMap != null && prevMnthMap.get(key) != null) {
			availabilityData
				.setPrevMonthCount(prevMnthMap.get(key));
		    }
		   availData.add(availabilityData);
		}
	    }
	}
	return availData;
    }

    public int getTotalVehiclesByLevel(int userId) {

	DBManager dbManager = DBManager.getInstance();
	return dbManager.getTotalVehiclesByLevel(userId);
    }

    public static void main(String[] args) {
	AvailabilityDataHandler availabilityDataHandler = new AvailabilityDataHandler();
	availabilityDataHandler.getAvailabilityData(6, 2012, 99);
    }
}
