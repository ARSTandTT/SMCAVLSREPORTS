package com.arsse.smc.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.arsse.smc.common.Configuration;
import com.arsse.smc.common.Constants;
import com.arsse.smc.model.AbsenteeData;
import com.arsse.smc.model.PerformanceAvailabilityData;
import com.arsse.smc.model.PerformanceData;
import com.arsse.smc.model.ProductData;
import com.arsse.smc.model.TripDetails;
import com.arsse.smc.model.ViolationActionTaken;

/**
 * 
 * Class to handle Database access methods.
 * 
 * @author Lekshmijraj
 * 
 * 
 */
public class DBManager extends Thread {

    Logger log = Logger.getLogger(DBManager.class);
    //private Connection connection;

    private static DBManager databaseManager;

    private String host;

    private String userName;

    private String password;

    private String database;

    //private CallableStatement cs = null;

    private ResultSet rs = null;

    /**
     * Constructor
     */
    private DBManager() {
	Configuration configuration = Configuration.getInstance();
	host = configuration.getValue("DBURL");
	userName = configuration.getValue("DB_USER");
	password = configuration.getValue("DB_PASSWORD");
	database = configuration.getValue("DBNAME");
	log.info("SQLServer Config");
	log.info("HOST : " + host);
	log.info("DB : " + database);

    }

    /**
     * This method returns the instance of the DatabaseManager
     * 
     * @return DatabaseManager
     */
    public static DBManager getInstance() {
	if (databaseManager == null) {
	    databaseManager = new DBManager();
	}
	return databaseManager;
    }/* getInstance */

    /**
     * This method connects to the database
     * 
     * @throws Exception
     */
  /*  private boolean connect() throws ClassNotFoundException, SQLException {
	// String driverName = "com.mysql.jdbc.Driver";
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	try {
	    Class.forName(driverName);
	    String databaseUrl = host + ":2301;databaseName=" + database;
	    connection = DriverManager.getConnection(databaseUrl, userName,
		    password);
	    log.info("SQL Server Database URL : " + databaseUrl);
	    log.info("Connected to SQLServer DB");
	} catch (ClassNotFoundException e1) {
	    log.error("Error in SQLServer DB Connection : " + e1.getMessage());
	    throw e1;
	} catch (SQLException e) {
	    log.error("Error in SQLServer DB Connection : " + e.getMessage());
	    throw e;
	} catch (Exception e) {
	    log.error("Error in SQLServer DB Connection : " + e.getMessage());
	}
	return true;
    }/* connect */
    
    
    private Connection connect() throws ClassNotFoundException, SQLException {
    	// String driverName = "com.mysql.jdbc.Driver";
    	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    	Connection con=null;
    	try {
    	    Class.forName(driverName);
    	    String databaseUrl = host + ":2301;databaseName=" + database;
    	    con = DriverManager.getConnection(databaseUrl, userName,
    		    password);
    	    log.info("SQL Server Database URL : " + databaseUrl);
    	    log.info("Connected to SQLServer DB");
    	} catch (ClassNotFoundException e1) {
    	    log.error("Error in SQLServer DB Connection : " + e1.getMessage());
    	    throw e1;
    	} catch (SQLException e) {
    	    log.error("Error in SQLServer DB Connection : " + e.getMessage());
    	    throw e;
    	} catch (Exception e) {
    	    log.error("Error in SQLServer DB Connection : " + e.getMessage());
    	}
    	return con;
        }/* connect */

    /**
     * This method returns the database connection
     * 
     * @return Connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    /*public Connection getConnection() throws ClassNotFoundException,
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
  /*  private void checkConnection() {
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
    }*/
/*
    /**
     * Disconnect connection.
     */
  /*  public void disConnect() {
	try {
	    if (connection != null && !connection.isClosed()) {
		connection.close();
	    }
	} catch (Exception e) {
	} finally {
	    connection = null;
	}
    }*/

    /**
     * Returns OBU id and vehicle reg number.
     * 
     * @param userId
     * @return
     */
    public HashMap<String, List<Object>> getOBURegNo(int userId) {

	// List<Availability> availabilityList = new ArrayList<Availability>();
	HashMap<String, List<Object>> obuReg = new HashMap<String, List<Object>>();
	ResultSet rs = null;
	Connection con=null;
	CallableStatement cs = null;
	try {
		con= connect();
	    log.info("{ SPC_GetAvailabilityReport(?) }" + userId);
	    cs = con.prepareCall("{CALL SPC_GetAvailabilityReport(?) }");
	    cs.setInt(1, userId);
	    rs = cs.executeQuery();
	    while (rs.next()) {
		List<Object> obuRegDate = new ArrayList<Object>();
		obuRegDate.add(rs.getString(2));// regno
		obuRegDate.add(rs.getDate(3));// date
		obuReg.put(rs.getString(1), obuRegDate);// key obuid,
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getOBURegNo Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
	    	
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
    		con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
	    		try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return obuReg;
    }

    /**
     * Returns performance data.
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<PerformanceData> getPerformanceData(int level, int userId,
	    String fromDate, String toDate, String regionId, String areaId,
	    String territoryId, String depotId, String endLevelId) {
	List<PerformanceData> pDataList = new ArrayList<PerformanceData>();
	ResultSet rs = null;
	Connection con=null;
	CallableStatement cs = null;
	try {
	    con=connect();
	    
	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataAllIndia(?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		break;
	    case Constants.REGION_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataRegion(?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		break;
	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataArea(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataTerritory(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataDepot(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		break;
	    case Constants.TRANSPORTER_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataTransporter(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		cs.setInt(8, Integer.parseInt(endLevelId));
		break;
	    case Constants.RO_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataRO(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		cs.setInt(8, Integer.parseInt(endLevelId));
		break;
	    default:
		procName = "{CALL spc_GetPerformanceReportDataAllIndia(?,?,?) }";
		break;
	    }
	    if (cs != null) {
		log.info("Executing performance procedure : " + procName);
		rs = cs.executeQuery();
		log.info("Processing received data...");
		while (rs.next()) {
		    PerformanceData performanceData = new PerformanceData();
		    performanceData.setRegion(rs.getString("Region"));
		    performanceData.setArea(rs.getString("Area"));
		    performanceData.setTerritory(rs.getString("Territory"));
		    performanceData.setDepot(rs.getString("Depot"));
		    performanceData.setRegno(rs.getString("Regno"));
		    performanceData.setObuId(rs.getString("ObuId"));
		    performanceData.setSpeedViolation(rs
			    .getInt("SpeedViolation"));
		    performanceData.setRouteViolation(rs
			    .getInt("RouteViolation"));
		    performanceData.setStoppages(rs.getInt("Stoppages"));
		    performanceData.setPowerDisconnected(rs
			    .getInt("PowerDisconnected"));
		    performanceData.setTotalTrips(rs.getInt("TotalTrips"));
		    performanceData.setOnLoadDistance(rs
			    .getFloat("OnLoadDistance"));
		    performanceData.setOffLoadDistance(rs
			    .getFloat("OffLoadDistance"));
		    performanceData.setDeliveries(rs.getInt("Deliveries"));
		    performanceData.setQuantityDelivered(rs
			    .getFloat("QuantityDelivered"));
		    performanceData.setTotalDistance(rs
			    .getFloat("OnLoadDistance")
			    + rs.getFloat("OffLoadDistance"));
		    try {
			performanceData.setTransporter(rs
				.getString("Transporter"));
		    } catch (Exception e) {
		    }
		    try {
			performanceData.setRoName(rs.getString("ROName"));
		    } catch (Exception e) {
		    }
		    pDataList.add(performanceData);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPerformanceData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {    	
	    	
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return pDataList;
    }

    /**
     * 
     * @param number
     * @return
     */
    private float getFormattedNumber(float number) {
	System.out.println("number " + number);
	float value = 0;
	if(number > 0) {
	    value = Float.parseFloat(new DecimalFormat("#.##").format(number));
	}
	System.out.println("value " + value);
	return value;
    }
    
    /**
     * Returns Performance Availability Data.
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
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();

	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataAllIndia(?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		break;
	    case Constants.REGION_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataRegion(?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		break;

	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataArea(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataTerritory(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataDepot(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		break;
	    case Constants.TRANSPORTER_LEVEL:
		procName = "{CALL spc_GetPerformanceAvailDataTransporter(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		cs.setInt(8, Integer.parseInt(endLevelId));
		break;
	    case Constants.RO_LEVEL:
		/*
		 * procName =
		 * "{CALL spc_GetPerformanceAvailDataRO(?,?,?,?,?,?,?,?)}"; cs =
		 * connection.prepareCall(procName); cs.setInt(1, userId);
		 * cs.setString(2, fromDate); cs.setString(3, toDate);
		 * cs.setInt(4, Integer.parseInt(regionId)); cs.setInt(5,
		 * Integer.parseInt(areaId)); cs.setInt(6,
		 * Integer.parseInt(territoryId)); cs.setInt(7,
		 * Integer.parseInt(depotId)); cs.setInt(8,
		 * Integer.parseInt(endLevelId));
		 */
		break;
	    default:
		procName = "{CALL spc_GetPerformanceAvailDataAllIndia(?,?,?) }";
		break;
	    }

	    rs = cs.executeQuery();

	    while (rs.next()) {

		PerformanceAvailabilityData pAvailabilityData = new PerformanceAvailabilityData();
		pAvailabilityData.setRegNo(rs.getString("Regno"));
		pAvailabilityData.setObuId(rs.getString("ObuId"));
		pAvailabilityData.setRegion(rs.getString("Region"));
		pAvailabilityData.setArea(rs.getString("Area"));
		pAvailabilityData.setTerritory(rs.getString("Territory"));
		pAvailabilityData.setDepot(rs.getString("Depot"));
		pAvailabilityData.setTotalTankerLorries(1);
		try {
		    pAvailabilityData.setTransporter(rs
			    .getString("TransporterName"));
		} catch (Exception e) {
		}
		// pAvailabilityData.setTotalOperationalVts(pAvailabilityData.getVehicleCount())
		pDataList.add(pAvailabilityData);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPerformanceAvailabilityData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

	    }
	}
	return pDataList;
    }

    /**
     * Returns absentee data.
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<AbsenteeData> getAbsenteeData(int level, int userId, int month,
	    int year, String regionId, String areaId, String territoryId,
	    String depotId, String endLevelId) {
	List<AbsenteeData> pDataList = new ArrayList<AbsenteeData>();
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();

	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataAllIndia(?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		break;
	    case Constants.REGION_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataRegion(?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		cs.setInt(4, Integer.parseInt(regionId));
		break;
	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataArea(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataTerritory(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataDepot(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		break;
	    case Constants.TRANSPORTER_LEVEL:
		procName = "{CALL spc_GetAbsenteeReportDataTransporter(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setInt(2, month);
		cs.setInt(3, year);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		cs.setInt(8, Integer.parseInt(endLevelId));
		break;
	    default:
		procName = "{CALL spc_GetAbsenteeReportDataAllIndia(?,?,?) }";
		break;
	    }
	    if (cs != null) {
		rs = cs.executeQuery();
		while (rs.next()) {
		    AbsenteeData absenteeData = new AbsenteeData();
		    absenteeData.setRegion(rs.getString("Region"));
		    absenteeData.setArea(rs.getString("Area"));
		    absenteeData.setTerritory(rs.getString("Territory"));
		    absenteeData.setDepot(rs.getString("Depot"));
		    absenteeData.setRegNo(rs.getString("Regno"));
		    try {
			absenteeData.setTransporter(rs.getString("Transporter"));
		    } catch (Exception e) {
		    }
		    if(rs.getDate("ConsignmentDate") != null) {
			absenteeData.setConsignmentDate(rs.getDate("ConsignmentDate"));
		    }
		    try {
			absenteeData.setDay(rs.getInt("ConsignmentDay"));
		    } catch (Exception e) {
		    }
		    pDataList.add(absenteeData);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getAbsenteeData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return pDataList;
    }
    
    /**
     * Returns absentee data.
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<ProductData> getProductWiseData(int materialId, int level, int userId, String fromDate,
	    String toDate, String regionId, String areaId, String territoryId,
	    String depotId, String endLevelId) {
	List<ProductData> pDataList = new ArrayList<ProductData>();
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();

	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetProductWiseDataAllIndia(?,?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, materialId);
		break;
	    case Constants.REGION_LEVEL:
		procName = "{CALL spc_GetProductWiseDataRegion(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, materialId);
		cs.setInt(5, Integer.parseInt(regionId));
		break;
	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetProductWiseDataArea(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, materialId);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetProductWiseDataTerritory(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, materialId);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		cs.setInt(7, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetProductWiseDataDepot(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, materialId);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		cs.setInt(7, Integer.parseInt(territoryId));
		cs.setInt(8, Integer.parseInt(depotId));
		break;
	    default:
		procName = "{CALL spc_GetProductWiseDataAllIndia(?,?,?,?) }";
		break;
	    }
	    if (cs != null) {
		rs = cs.executeQuery();
		while (rs.next()) {
		    ProductData productData = new ProductData();
		    productData.setRegion(rs.getString("Region"));
		    productData.setArea(rs.getString("Area"));
		    productData.setTerritory(rs.getString("Territory"));
		    productData.setDepot(rs.getString("Depot"));
		    productData.setRegNo(rs.getString("Regno"));
		    productData.setRoName(rs.getString("ROName"));
		    productData.setTotalDeliveries(rs.getInt("Deliveries"));
		    productData.setQuantityDelivered(rs.getFloat("QuantityDelivered"));
		    productData.setMaterial(rs.getString("Material"));
		    pDataList.add(productData);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getProductWiseData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		
		if (con != null) {
			con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
				try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

	    }
	}
	return pDataList;
    }    

    /**
     * Get OBUId for vehicle number.
     * 
     * @param userId
     * @param vehicleNr
     * @return
     */
    public String getOBUIdForVehicle(int userId, String vehicleNr) {
	String obuId = null;
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();
	    cs = con.prepareCall("{CALL SPC_GetObuIdFromVehicleNumber(?,?) }");
	    cs.setInt(1, userId);
	    cs.setString(2, vehicleNr);
	    rs = cs.executeQuery();
	    while (rs.next()) {
		obuId = rs.getString(1);// obuId
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getOBURegNo Procedure Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
		
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return obuId;
    }

    /**
     * 
     * @param userId
     * @return
     */
    public int getTotalVehiclesByLevel(int userId) {
	int total = 0;
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();
	    java.sql.Statement stmt;
	    stmt = con.createStatement();

	    rs = stmt.executeQuery("select [dbo].[fn_GetTotalVehicleByLevel]("
		    + userId + ")");
	    while (rs.next()) {
		total = rs.getInt(1);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getTotalVehiclesByLevel Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return total;
    }
    
    /**
     * 
     * @param 
     * @return
     */
    public String executeGetNameQuery(String queryText) {
	String name = "";
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();
	    java.sql.Statement stmt;
	    stmt = con.createStatement();

	    rs = stmt.executeQuery(queryText);
	    while (rs.next()) {
		name = rs.getString(1);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("executeGetNameQuery Execution , Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) 
	    {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

	    }
	}
	return name;
    }
    

    /**
     * Returns violation action taken data.
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<ViolationActionTaken> getViolationActionTaken(int level,
	    int userId, String fromDate, String toDate, String regionId,
	    String areaId, String territoryId, String depotId,
	    String endLevelId, int violationType) {
	List<ViolationActionTaken> pDataList = new ArrayList<ViolationActionTaken>();
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();

	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetViolationActionReportAllIndia(?,?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		break;
	    case Constants.REGION_LEVEL:
		procName = "{CALL spc_GetViolationActionReportRegion(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		cs.setInt(5, Integer.parseInt(regionId));
		break;
	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetViolationActionReportArea(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetViolationActionReportTerritory(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		cs.setInt(7, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetViolationActionReportDepot(?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		cs.setInt(7, Integer.parseInt(territoryId));
		cs.setInt(8, Integer.parseInt(depotId));
		break;
	    case Constants.TRANSPORTER_LEVEL:
		procName = "{CALL spc_GetViolationActionReportTransporter(?,?,?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, violationType);
		cs.setInt(5, Integer.parseInt(regionId));
		cs.setInt(6, Integer.parseInt(areaId));
		cs.setInt(7, Integer.parseInt(territoryId));
		cs.setInt(8, Integer.parseInt(depotId));
		cs.setInt(9, Integer.parseInt(endLevelId));
		break;
	    default:
		procName = "{CALL spc_GetViolationActionReportAllIndia(?,?,?,?) }";
		break;
	    }
	    if (cs != null) {
		rs = cs.executeQuery();
		while (rs.next()) {
		    ViolationActionTaken violationData = new ViolationActionTaken();
		    violationData.setRegion(rs.getString("Region"));
		    violationData.setArea(rs.getString("Area"));
		    violationData.setTerritory(rs.getString("Territory"));
		    violationData.setDepot(rs.getString("Depot"));
		    violationData.setRegno(rs.getString("Regno"));
		    violationData.setObuId(rs.getString("ObuId"));
		    try {
			violationData.setTransporter(rs
				.getString("Transporter"));
		    } catch (Exception e) {
		    }
		    violationData
		    .setViolationCount(rs.getInt("ViolationCount"));
		    switch (violationType) {
		    case 1:
			violationData.setViolationName("Route Violation");
			break;
		    case 2:
			violationData.setViolationName("Speed Violation");
			break;
		    case 3:
			violationData.setViolationName("Unauthorized Stoppage");
			break;
		    default:
			break;
		    }
		    violationData.setExplanationSought(rs
			    .getInt("ExplanationSoughtCount"));
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
			    .getViolationCount()
			    - violationData.getTotalActionTaken());
		    // violationData.setOpenCases(rs.getInt("OpenCases"));
		    violationData.setClosedCases(rs.getInt("ClosedCases"));
		    pDataList.add(violationData);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getViolationActionTaken Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return pDataList;
    }

    /**
     * Returns performance data.
     * 
     * @param userId
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<PerformanceData> getTopBottomPerformersData(int level, int userId,
	    String fromDate, String toDate, String regionId, String areaId,
	    String territoryId, String depotId, boolean isViolation) {
	List<PerformanceData> pDataList = new ArrayList<PerformanceData>();
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	
	try {
		con=connect();
	    String procName = "";

	    switch (level) {
	    case Constants.ALLINDIA_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataAllIndia(?,?,?) }";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		break;
	    case Constants.REGION_LEVEL:
		if(isViolation) {
		    procName = "{CALL spc_GetTopBottomViolationPerformersRegion(?,?,?,?)}";
		} else {
		    procName = "{CALL spc_GetTopBottomPerformersRegion(?,?,?,?)}";
		}
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		break;
	    case Constants.AREA_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataArea(?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		break;
	    case Constants.TERRITORY_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataTerritory(?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		break;
	    case Constants.DEPOT_LEVEL:
		procName = "{CALL spc_GetPerformanceReportDataDepot(?,?,?,?,?,?,?)}";
		cs = con.prepareCall(procName);
		cs.setInt(1, userId);
		cs.setString(2, fromDate);
		cs.setString(3, toDate);
		cs.setInt(4, Integer.parseInt(regionId));
		cs.setInt(5, Integer.parseInt(areaId));
		cs.setInt(6, Integer.parseInt(territoryId));
		cs.setInt(7, Integer.parseInt(depotId));
		break;
	    default:
		procName = "{CALL spc_GetPerformanceReportDataAllIndia(?,?,?) }";
		break;
	    }
	    if (cs != null) {
		rs = cs.executeQuery();
		while (rs.next()) {
		    PerformanceData performanceData = new PerformanceData();
		    performanceData.setRegion(rs.getString("Region"));
		    performanceData.setArea(rs.getString("Area"));
		    performanceData.setTerritory(rs.getString("Territory"));
		    performanceData.setDepot(rs.getString("Depot"));
		    performanceData.setRegno(rs.getString("Regno"));
		    performanceData.setObuId(rs.getString("ObuId"));
		    performanceData.setSpeedViolation(rs
			    .getInt("SpeedViolation"));
		    performanceData.setRouteViolation(rs
			    .getInt("RouteViolation"));
		    performanceData.setStoppages(rs.getInt("Stoppages"));
		    performanceData.setPowerDisconnected(rs
			    .getInt("PowerDisconnected"));
		    performanceData.setTotalTrips(rs.getInt("TotalTrips"));
		    performanceData.setOnLoadDistance(rs
			    .getFloat("OnLoadDistance"));
		    performanceData.setOffLoadDistance(rs
			    .getFloat("OffLoadDistance"));
		    performanceData.setDeliveries(rs.getInt("Deliveries"));
		    performanceData.setQuantityDelivered(rs
			    .getFloat("QuantityDelivered"));
		    performanceData.setTotalDistance(rs.getFloat("OnLoadDistance") + rs
			    .getFloat("OffLoadDistance"));
		    try {
			performanceData.setViolationAverage(rs.getFloat("ViolationAverage"));
			performanceData.setTransporter(rs
				.getString("Transporter"));
		    } catch (Exception e) {
		    }
		    try {
			performanceData.setRoName(rs.getString("ROName"));
		    } catch (Exception e) {
		    }
		    pDataList.add(performanceData);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getPerformanceData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return pDataList;
    }    
    
    public List<TripDetails> getTripDetailsDepotData(int userId,
	    String fromDate, String toDate, String regionId, String areaId,
	    String territoryId, String depotId) {
	List<TripDetails> pDataList = new ArrayList<TripDetails>();
	ResultSet rs = null;
	CallableStatement cs = null;
	Connection con=null;
	try {
		con=connect();

	    String procName = "";
	    procName = "{call spc_GetROsByDepotTripDetails(?,?,?,?,?,?,?)} ";
	    cs = con.prepareCall(procName);
	    cs.setInt(1, userId);
	    cs.setString(2, fromDate);
	    cs.setString(3, toDate);
	    cs.setInt(4, Integer.parseInt(regionId));
	    cs.setInt(5, Integer.parseInt(areaId));
	    cs.setInt(6, Integer.parseInt(territoryId));
	    cs.setInt(7, Integer.parseInt(depotId));

	    if (cs != null) {
		log.info("Executing spc_GetROsByDepotTripDetails procedure : " + procName);
		rs = cs.executeQuery();
		log.info("Processing received data...");
		
		
		while (rs.next()) {
		   
		    TripDetails tripDetails = new TripDetails();
		    tripDetails.setFillingStationName(rs.getString("FillingStationName"));
		    tripDetails.setRoName(rs.getString("ROName"));
		    tripDetails.setTransporterName(rs.getString("TransporterName"));
		    tripDetails.setVehicleId(rs.getInt("VehicleId"));
		    tripDetails.setRegNo(rs.getString("Regno"));
		    tripDetails.setInvoiceNo(rs.getString("InvoiceNo"));
		    tripDetails.setTripId(rs.getInt("TripId"));
		    tripDetails.setStartTime(rs.getTimestamp("StartTime"));
		    tripDetails.setEndTime(rs.getTimestamp("EndTime"));
		    tripDetails.setStartLocation(rs.getString("StartLocation"));
		    tripDetails.setEndLocation(rs.getString("EndLocation"));
		    tripDetails.setMaterialName(rs.getString("MaterialName"));
		    tripDetails.setDistance(rs.getFloat("Distance"));
		    tripDetails.setQuantity(rs.getString("Quantity"));
		    tripDetails.setOffRouteDistance(rs.getFloat("offRouteDistance"));
		    pDataList.add(tripDetails);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("getTripDetailsDepotData Procedure Execution, Exception Occured: "
		    + e.getMessage());
	} finally {
	    try {
		if (cs != null) {
		    cs.close();
		}
		if (rs != null) {
		    rs.close();
		}
		if (con != null) {
		    con.close();
		}
	    } catch (Exception e) {
	    	if (con != null) {
			    try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	    }
	}
	return pDataList;
    }    
    
    public static void main(String[] args) {

	DBManager dbManager = DBManager.getInstance();
	// dbManager.getOBURegNo(1002);
	dbManager.getTripDetailsDepotData(1, "2013-03-01 00:00:00", "2013-06-01 23:59:59", "3", "14", "29", "75");
    }
}
