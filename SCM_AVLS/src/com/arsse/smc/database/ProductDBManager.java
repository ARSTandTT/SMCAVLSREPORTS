package com.arsse.smc.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.arsse.smc.common.Configuration;
import com.arsse.smc.common.Constants;
import com.arsse.smc.model.ObuRepair;
import com.arsse.smc.model.ObuStatus;
import com.arsse.smc.model.ViolationActionTaken;
import com.mysql.jdbc.Statement;

/**
 * Class to handle MySQL Database access methods.
 * 
 * @author Lekshmijraj
 * 
 * 
 */
public class ProductDBManager extends Thread {

    Logger log = Logger.getLogger(ProductDBManager.class);
    private Connection connection;

    private static ProductDBManager databaseManager;

    private String host;

    private String userName;

    private String password;

    private String database;

    //private CallableStatement cs = null;

    private ResultSet rs = null;

    /**
     * Constructor
     */
    private ProductDBManager() {
	Configuration configuration = Configuration.getInstance();
	host = configuration.getValue("PRODUCT_HOST");
	userName = configuration.getValue("PRODUCT_DB_USER");
	password = configuration.getValue("PRODUCT_DB_PASSWORD");
	database = configuration.getValue("PRODUCT_DB");
	log.info("MySQL Config");
	log.info("HOST : " + host);
	log.info("UserName : " + userName);
	log.info("Password : " + password);
	log.info("DB : " + database);

    }

    /**
     * This method returns the instance of the DatabaseManager
     * 
     * @return DatabaseManager
     */
    public static ProductDBManager getInstance() {
	if (databaseManager == null) {
	    databaseManager = new ProductDBManager();
	}
	return databaseManager;
    }/* getInstance */

    /**
     * This method connects to the database
     * 
     * @throws Exception
     */
    private boolean connect() throws ClassNotFoundException, SQLException {

	String driverName = "com.mysql.jdbc.Driver";
	try {
	    // load mysql driver
	    Class.forName(driverName).newInstance();

	    String databaseUrl = "jdbc:mysql://" + host + ":3306/" + database;
	    log.info("MySQL Database URL : " + databaseUrl);
	    connection = DriverManager.getConnection(databaseUrl, userName,
		    password);
	    com.mysql.jdbc.Connection mySqlcon = (com.mysql.jdbc.Connection) connection;
	    mySqlcon.setAutoReconnectForConnectionPools(true);
	    mySqlcon.setAutoReconnect(true);
	    mySqlcon.setConnectTimeout(99999999);
	    mySqlcon.setAutoReconnectForPools(true);
	    log.info("Connected to MySQL DB");

	} catch (ClassNotFoundException e1) {
	    log.error("Error in MySQL DB Connection : " + e1.getMessage());
	    throw e1;
	} catch (SQLException e) {
	    log.error("Error in MySQL DB Connection : " + e.getMessage());
	    throw e;
	} catch (Exception e) {
	    log.error("Error in MySQL DB Connection : " + e.getMessage());
	}
	return true;
    }/* connect */

    /**
     * This method returns the database connection
     * 
     * @return Connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws ClassNotFoundException,
    SQLException {
	if (connection == null || connection.isClosed()) {
	    connect();
	} else {
	    checkConnection();
	}
	return connection;
    }/* getConnection */

    /**
     * Checks connection
     */
    private void checkConnection() {
	Statement stmt = null;
	try {
	    stmt = (Statement) connection.createStatement();
	    stmt.execute("Select 1");
	} catch (Exception e) {
	    try {
		connection.close();
		connect();
	    } catch (Exception e1) {
	    }
	} finally {
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		}
	    }
	}
    }

    /**
     * Disconnect connection.
     */
    public void disConnect() {
	try {
	    if (connection != null && !connection.isClosed()) {
		connection.close();
	    }
	} catch (Exception e) {
	} finally {
	    connection = null;
	}
    }

    /**
     * Returns OBU online status.
     * 
     * @param month
     * @param year
     * @return
     */
    public List<ObuStatus> getObuStatus(int month, int year) {
	List<ObuStatus> obuList = new ArrayList<ObuStatus>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {

	    getConnection();
	    log.info("{ SPC_GetActiveObus(?,?) }");
	    cs = connection.prepareCall("{CALL SPC_GetActiveObus(?,?) }");
	    cs.setInt(1, month);
	    cs.setInt(2, year);

	    rs = cs.executeQuery();

	    while (rs.next()) {
		ObuStatus obuStatus = new ObuStatus();
		obuStatus.setObuid(rs.getString(1));
		obuStatus.setIsOnline(rs.getString(2));
		obuStatus.setDay(rs.getInt(3));
		obuStatus.setObuOnlineDate(rs.getDate(4));
		obuList.add(obuStatus);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("SPC_GetActiveObus Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {

	    }
	}
	return obuList;

    }

    /**
     * Returns complete power disconnected list for the period.
     * 
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, Integer> getPowerDisconnectedData(String fromDate,
	    String toDate) {
	HashMap<String, Integer> powerDisconnectList = new HashMap<String, Integer>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {
	    getConnection();
	    log.info("{spc_getPowerDisconnected(?,?,?}");
	    cs = connection
		    .prepareCall("{CALL spc_getPowerDisconnected(?,?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);
	    cs.setInt(3, Integer.parseInt(Constants.ALL_STATUS));

	    rs = cs.executeQuery();

	    while (rs.next()) {
		powerDisconnectList.put(rs.getString(1), rs.getInt(2));
		// Key - ObuId, Value - disconnect count
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPowerDisconnectedData Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return powerDisconnectList;
    }
    /**
     * Returns complete power disconnected for an OBU for the given period.
     * 
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<String[]> getPowerDisconnectedDataForOBU(String obuId, String fromDate,
	    String toDate,String status) {
	List<String[]> powerDisconnectList = new ArrayList<String[]>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {
	    getConnection();
	    cs = connection
		    .prepareCall("{CALL spc_getPowerDisconnectedByObu(?,?,?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);
	    cs.setString(3, obuId);
	    cs.setInt(4, Integer.parseInt(status));
	    rs = cs.executeQuery();

	    String[] dataArray = null;
	    while (rs.next()) {
		dataArray = new String[2];
		dataArray[0] = rs.getString(1);//Recorded Time
		dataArray[1] = rs.getString(2);//Status
		powerDisconnectList.add(dataArray);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPowerDisconnectedDataForOBU Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return powerDisconnectList;
    }
    /**
     * getObuRepair
     * 
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, Integer> getObuRepair(String fromDate, String toDate) {
	HashMap<String, Integer> repairObuList = new HashMap<String, Integer>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {
	    getConnection();
	    log.info("{spc_getObuRepair(?,?}");
	    cs = connection.prepareCall("{CALL spc_getObuRepair(?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);

	    rs = cs.executeQuery();
	    while (rs.next()) {
		repairObuList.put(rs.getString(1), rs.getInt(2));
		// Key - ObuId, Value - repair count
		System.out.println("******************");
		System.out.println("OBUID        :: " + rs.getString(1));
		System.out.println("Repair Count :: " + rs.getString(2));
		System.out.println("**********************");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getObuRepair Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return repairObuList;
    }

    /**
     * getTamperedDevices
     * 
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, Integer> getTamperedDevices(String fromDate,
	    String toDate) {
	HashMap<String, Integer> tamperedObuList = new HashMap<String, Integer>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {
	    getConnection();
	    log.info("{spc_getTamperedDevices(?,?}");
	    cs = connection.prepareCall("{CALL spc_getTamperedDevices(?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);

	    rs = cs.executeQuery();

	    while (rs.next()) {
		tamperedObuList.put(rs.getString(1), rs.getInt(2));
		// Key - ObuId,Value -tampered count
		System.out.println("******************");
		System.out.println("OBUID        :: " + rs.getString(1));
		System.out.println("Tamper Count :: " + rs.getString(2));
		System.out.println("**********************");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getTamperedObuList Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return tamperedObuList;
    }

    /**
     * 
     * @param obuId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<String> getObuGivingSignals(String fromDate, String toDate) {
	ResultSet rs = null;
	List<String> obuList = new ArrayList<String>();
	String obu = "";
	CallableStatement cs = null;
	try {
	    getConnection();
	    log.info("{spc_GetObugivingSignals(?,?}");
	    cs = connection
		    .prepareCall("{CALL spc_GetObugivingSignals(?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);

	    rs = cs.executeQuery();

	    while (rs.next()) {
		obu = rs.getString(1);
		System.out.println("ObuID : " + obu);
		obuList.add(obu);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("spc_GetObugivingSignals Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return obuList;

    }
    
    public HashMap<String,ArrayList<ObuRepair>> getOBURepairDetails() {
	ResultSet rs = null;
	CallableStatement cs = null;
	HashMap<String,ArrayList<ObuRepair>> repairMap = new HashMap<String, ArrayList<ObuRepair>>();
	try {
	    getConnection();
	    java.sql.Statement stmt;
	    stmt = connection.createStatement();
	    rs = stmt.executeQuery("select ObuId, RepairStartTime, RepairEndTime from oburepair order by obuId, RepairEndTime asc");
	    ArrayList<ObuRepair> obuRepairList = null;
	    String obuId = null;
	    Date repairStartTime = null;
	    Date repairEndTime = null;
	    while (rs.next()) {
		obuId = rs.getString(1);
		repairStartTime = rs.getDate(2);
		repairEndTime = rs.getDate(3);
		ObuRepair obuRepair = new ObuRepair();
		obuRepair.setRepairStartDate(repairStartTime);
		obuRepair.setRepairEndDate(repairEndTime);
		obuRepairList = repairMap.get(obuId);
		if(obuRepairList != null) {
		    obuRepairList.add(obuRepair);
		} else {
		    obuRepairList = new ArrayList<ObuRepair>();
		    obuRepairList.add(obuRepair);
		}
		repairMap.put(obuId, obuRepairList);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getOBURepairDetails Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {

	    }
	}
	return repairMap;
    }
    
    /**
     * Returns disconnected violation action taken.
     * 
     * @param fromDate
     * @param toDate
     * @return
     */
    public HashMap<String, ViolationActionTaken> getPowerDisconnectedActionTakenData(String fromDate,
	    String toDate) {
	HashMap<String, ViolationActionTaken> powerDisconnectActionList = new HashMap<String, ViolationActionTaken>();
	ResultSet rs = null;
	CallableStatement cs = null;
	try {
	    getConnection();
	    cs = connection
		    .prepareCall("{CALL spc_GetViolationActionTaken(?,?) }");
	    cs.setString(1, fromDate);
	    cs.setString(2, toDate);

	    rs = cs.executeQuery();

	    while (rs.next()) {
		ViolationActionTaken violationData = new ViolationActionTaken();
		violationData.setObuId(rs.getString(1));
		violationData.setViolationCount(rs.getInt("ViolationCount"));
		violationData.setExplanationSought(rs
			.getInt("ExplanationSought"));
		violationData
		.setSuspension1Day(rs.getInt("Suspension1Day"));
		violationData.setSuspension1Week(rs
			.getInt("Suspension1Week"));
		violationData.setSuspension1Month(rs
			.getInt("Suspension1Month"));
		violationData.setBlackListed(rs.getInt("BlackListed"));
		violationData.setTotalActionTaken(violationData
			.getSuspension1Day()
			+ violationData.getSuspension1Week()
			+ violationData.getSuspension1Month()
			+ violationData.getBlackListed()
			+ violationData.getExplanationSought());
		violationData.setOpenCases(violationData
			.getViolationCount()- violationData.getTotalActionTaken());
		powerDisconnectActionList.put(rs.getString(1), violationData);
		// Key - ObuId, Value - action taken list
	    }
	    
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPowerDisconnectedActionTakenData Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
	    } catch (Exception e) {
	    }
	}
	return powerDisconnectActionList;
    }    

   // public List<D>
    public static void main(String[] args) {

	ProductDBManager dbManager = ProductDBManager.getInstance();
	try {
	    dbManager.getConnection();
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// dbManager.getObuStatus(3, 2012);
	// dbManager.getTamperedDevices("2012-05-01","2012-05-31");
	// dbManager.getObuRepair("2012-05-01","2012-05-31");
	dbManager.getObuGivingSignals("2011-05-01", "2012-06-31");

    }
}
