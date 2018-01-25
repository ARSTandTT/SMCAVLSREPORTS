package com.arsse.smc.birtreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.birt.data.aggregation.impl.SummaryAccumulator;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.core.runtime.IPath;
import com.arsse.smc.common.Configuration;
import com.arsse.smc.common.Constants;
import com.arsse.smc.util.Util;
import com.ibm.icu.util.Calendar;

/**
 * Servlet that accepts reports generation requests.
 * 
 * @author Resmir
 * 
 */
public class WebReport extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IReportEngine birtReportEngine = null;
    private Configuration config = Configuration.getInstance();
    private Logger log = Logger.getLogger(WebReport.class);

    IReportRunnable design = null;
    ReportDesignHandle reportHandle = null;
    boolean cachingEnabled = false;

    /**
     * Default Constructor.
     */
    public WebReport() {
	super();
    }

    /**
     * Initialization of the servlet.
     * 
     * @throws ServletException
     *             if an error occure
     */
    public void init(ServletConfig sc) throws ServletException {
	BirtEngine.initBirtConfig();
	this.birtReportEngine = BirtEngine
		.getBirtEngine(sc.getServletContext());
	if(config.getValue("CACHING_ENABLED") != null && config.getValue("CACHING_ENABLED").trim().equals("1")) {
	    cachingEnabled = true;
	}
	log.info("Caching Enabled : " + cachingEnabled);
    }

    /**
     * Destruction of the servlet.
     */
    public void destroy() {
	super.destroy();
	BirtEngine.destroyBirtEngine();
    }

    /**
     * The doGet method of the servlet.
     * 
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	// get report name and launch the engine
	log.info("**********************************************");
	log.info("Received Report Generation Request");
	String reportType = req.getParameter("ReportType").trim();
	String repFormat = req.getParameter("ReportFormat");
	String userId = req.getParameter("UserId");
	boolean hasChart = false;
	log.info("Report Type : " + reportType);
	log.info("Report Format : " + repFormat);
	log.info("UserID : " + userId);
	String ReportCaption="";
	String HeaderCaption="";
	String from = req.getParameter("from");
	int	depotid =0;
	String to = req.getParameter("to");
	String date=req.getParameter("date");
	String vehicle = null;
	String regionId = null;
	String areaId = null;
	String territoryId = null;
	String DepartmentName=null;
	String transporterId = null;
	String roId = null;
	String chartVal = null;
	String status = null;  
	String calledfrom = null;  
	String calledto = null;  
	String statusID = null; 
	String callstatus = null;  
	int vehicletypeid = 0;
	String DeptZoneId=null;
	int FleetTypeID = 0;
	int reporttypeid = 0;
	//String route=null;
	int serviceid = 0;
	String ServicegroupId=null;
	String Stopid=null;
	String RouteID="";
	int showonlymissed = 0;
	int showexceptional=0;
	int groupid=0;
	int groupidmod=0;
	int month1=0;
	int year1=0;
	int Regnum = 0;
	int userid;
	int MachineNo=0;
	int rowcount=0;
	int roworder=0;
	int Draft=0;
	String Group=null;
	String reportFileName = "";
    String reportName = "";
	int materialId = 0;
	int summarytypeid=0;
	int MaterialType=0;
	log.info("From Date : " + from);
	log.info("To Date : " + to);
	String selectTypeMonth=null;
	String vmonth=null;
	String vyear=null;
	String groupBy =null;
	String	summaryBy=null;
	String serviceno=null;
	String companyname=config.getValue("COMPANYNAME");
	String companyUrl=config.getValue("COMPANYURL");
	String VersionNumber=config.getValue("VersionNumber");
	String depotId = req.getParameter("depotId");
	int operatorId=0;
	String OperatorIdDMS=null;
	
	int VehicleId=0;
	int DriverId=0;
	int driverId=0;	
	int DeptDataId=0;
	int DeptId=0;
	String DepartmentId=null;
	int ShiftId=0;
	String OperatorName=null;
	String DriverName=null;
	String VehicleNumber=null;
	int UOM=0;
 	float PhysicalStockQty=0;
 	float SystemStockQty=0;
	String IndentNo=null;
	String PurchaseOrderNo=null;
 	String IssueNo=null;
	String RetReferenceNo=null;
	String DesignationId=null;
	String ExpiryDate=null;
	String BusNo=null;
	int IsFatality=0;
	String Item=null;
	String Frequency=null;
	String Period=null;
	int RejectionStatus=0;
	String empId=null;
	String Id=null;
	
	if(depotId != null)
	{
		System.out.println( depotId );
	}
	else
	{
		depotId = req.getParameter("depotid");
	}
	
	if ( reportType
		.equalsIgnoreCase(Constants.NMMT_ROUTE_VIOLATION_REPORT)
		|| reportType
		.equalsIgnoreCase(Constants.NMMT_STOPPAGE_VIOLATION_REPORT)
		|| reportType
		.equalsIgnoreCase(Constants.NMMT_AVAILABILITY_REPORT)
		){
	    vehicle = req.getParameter("regno");
	    status = req.getParameter("status");
	    if (status == null){
		if(reportType
		.equalsIgnoreCase(Constants.VEHICLE_VIOLATION_SUMMARY_REPORT)) {
		    status = Constants.ALL_STATUS;
		} else {
		    status = Constants.OPEN_STATUS;
		}
	    }
	    log.info("Vehicle Number : " + vehicle);
	    log.info(" Status        : " +status);
	    
	   

		// For vehicle specific violation reports
		if (reportType != null){
	    	log.info("report name enter");
		    reportType = reportType.trim();
		    reportFileName = reportType + ".rptdesign";
		    reportName = reportType;
		    log.info("report name enter="+reportName);
		}
	}
	int levelNumber = 0;
	
	//availability report
	if (reportType.equalsIgnoreCase("lavailability")) {
	    regionId = req.getParameter("RegionId");
	    log.info("RegionId: " + regionId);
	    areaId = req.getParameter("AreaId");
	    log.info("AreaId: " + areaId);
	    territoryId = req.getParameter("TerritoryId");
	    log.info("TerritoryId: " + areaId);
	    depotId = req.getParameter("DepotId");
	    log.info("DepotId: " + depotId);
	    String level = req.getParameter("level");
	    log.info("Level: " + level);
	    if (level != null) {
		levelNumber = Integer.parseInt(level.trim());
		if (levelNumber == Constants.LOWEST_LEVEL) {
		    if(req.getParameter("istrans") != null) {
			try {
			    int isTransporter = Integer.parseInt(req
				    .getParameter("istrans"));
			    log.info("Is transporter : " + isTransporter);
			    if (isTransporter == Constants.TRANSPORTER) {
				levelNumber = Constants.TRANSPORTER_LEVEL;
			    } else if (isTransporter == Constants.RETAIL_OUTLET) {
				levelNumber = Constants.RO_LEVEL;
			    }
			} catch (Exception e) {
			}
		    }
		}
	    }
	    if(levelNumber == Constants.TRANSPORTER_LEVEL) {
		transporterId = req.getParameter("EndLevelId");
		log.info("TransporterId: " + transporterId);
	    } else if(levelNumber == Constants.RO_LEVEL) {
		roId = req.getParameter("EndLevelId");
		log.info("ROId: " + roId);
	    }
	}
	
	//added start by sunil on 01-04-2014
		if( reportType.equalsIgnoreCase(Constants.ROUTE_VIOLATION_REPORT))
		{
			userId=req.getParameter("UserId");
			String FleetTypeID1=req.getParameter("fleetTypeID");
			try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
			driverId=Integer.parseInt(req.getParameter("DriverId").trim());
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			OperatorName=req.getParameter("OperatorName");
			DriverName=req.getParameter("DriverName");
			VehicleNumber=req.getParameter("VehicleNumber");
			DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
			DepartmentId=req.getParameter("DepartmentId");
			RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
			depotId = req.getParameter("depotid");
			ServicegroupId=req.getParameter("Serviceid");
			Regnum=Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			//groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = Constants.ROUTE_VIOLATION_REPORT_ALL +".rptdesign";
			reportName =Constants.ROUTE_VIOLATION_REPORT_ALL;
			log.info("report name enter2");
			System.out.println(ServicegroupId+groupid+depotId+driverId+userId+operatorId+FleetTypeID+Regnum);

		}
		
		if( reportType.equalsIgnoreCase(Constants.BDCPerformance))
		{
			
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			
			Regnum=Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			//groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = Constants.BDCPerformance +".rptdesign";
			reportName =Constants.BDCPerformance;
			log.info("report name enter2");
			System.out.println(groupid+operatorId+Regnum);

		}
		
		if( reportType.equalsIgnoreCase("LaneViolation"))
		{
			userId=req.getParameter("UserId");
			String FleetTypeID1=req.getParameter("fleetTypeID");
			try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
			driverId=Integer.parseInt(req.getParameter("DriverId").trim());
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			OperatorName=req.getParameter("OperatorName");
			DriverName=req.getParameter("DriverName");
			VehicleNumber=req.getParameter("VehicleNumber");
			DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
			RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
			depotId = req.getParameter("depotid");
			ServicegroupId=req.getParameter("Serviceid");
			Regnum=Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			//groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = "LaneViolation.rptdesign";
			reportName ="LaneViolation";
			log.info("report name enter2");
			System.out.println(ServicegroupId+groupid+depotId+driverId+userId+operatorId+FleetTypeID+Regnum);

		}
		
		if( reportType.equalsIgnoreCase("speedviolation"))
		{
			userId=req.getParameter("UserId");
			String FleetTypeID1=req.getParameter("fleetTypeID");
			try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
			driverId=Integer.parseInt(req.getParameter("DriverId"));
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			depotId = req.getParameter("depotid");
			ServicegroupId=req.getParameter("Serviceid");
			OperatorName=req.getParameter("OperatorName");
			DriverName=req.getParameter("DriverName");
			VehicleNumber=req.getParameter("VehicleNumber");
			RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
			DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
			DepartmentId=req.getParameter("DepartmentId");

			//log.info("vehicletypeid"+vehicletypeid);
			//RouteID=req.getParameter("routeID");
			Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = "speedviolation.rptdesign";
			reportName ="speedviolation";
			log.info("report name enter2");
			System.out.println(ServicegroupId+groupid+depotId+driverId+userId+operatorId+Regnum+FleetTypeID+Regnum+groupidmod);

		}
		if( reportType.equalsIgnoreCase("SpeedViolationChart"))
		{
			userId=req.getParameter("UserId");
			String FleetTypeID1=req.getParameter("fleetTypeID");
			try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
			driverId=Integer.parseInt(req.getParameter("DriverId"));
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			depotId = req.getParameter("depotid");
			ServicegroupId=req.getParameter("Serviceid");
			OperatorName=req.getParameter("OperatorName");
			DriverName=req.getParameter("DriverName");
			VehicleNumber=req.getParameter("VehicleNumber");
			
			//log.info("vehicletypeid"+vehicletypeid);
			//RouteID=req.getParameter("routeID");
			Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = "SpeedViolationChart.rptdesign";
			reportName ="SpeedViolationChart";
			log.info("report name enter2");
			System.out.println(ServicegroupId+groupid+depotId+driverId+userId+operatorId+Regnum+FleetTypeID+Regnum);

		}
		
		//added end by sunil on 01-04-2014
		//added Start by sunil on 02-04-2014
		if( reportType.equalsIgnoreCase(Constants.STOPPAGE_VIOLATION_REPORT))
		{
			userId=req.getParameter("UserId");
			String FleetTypeID1=req.getParameter("fleetTypeID");
			try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
			driverId=Integer.parseInt(req.getParameter("DriverId"));
			operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
			OperatorName=req.getParameter("OperatorName");
			DriverName=req.getParameter("DriverName");
			VehicleNumber=req.getParameter("VehicleNumber");
			DepartmentId=req.getParameter("DepartmentId");
			RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
			depotId = req.getParameter("depotid");
			ServicegroupId=req.getParameter("Serviceid");
			//log.info("vehicletypeid"+vehicletypeid);
			//RouteID=req.getParameter("routeID");
			Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
			groupid = Integer.parseInt(req.getParameter("groupId"));
			groupidmod=Integer.parseInt(req.getParameter("groupId"));
			log.info("report name enter1");
			reportFileName = Constants.STOPPAGE_VIOLATION_REPORT_ALL + ".rptdesign";
			reportName =Constants.STOPPAGE_VIOLATION_REPORT_ALL;
			log.info("report name enter2");
			System.out.println("disp"+ServicegroupId+groupid+depotId+driverId+userId+operatorId+Regnum+FleetTypeID);
	
		}
		
		//added end by sunil on 02-04-2014
		//added Start by sunil on 07-04-2014
		if( reportType.equalsIgnoreCase(Constants.DATA_ANALYSIS_REPORT))
		{
			reportName = "GPSAnalysisReport";
	    	reportFileName = reportName + ".rptdesign";	
	    	String vehicletypid=req.getParameter("Vehicletypeid");
			vehicletypeid= Integer.parseInt(vehicletypid);
			String Reporttypid=req.getParameter("ReportTypeID");
			reporttypeid= Integer.parseInt(Reporttypid);
			log.info("vehicletypeid==Sunil=="+vehicletypeid);
	    	month1=Integer.parseInt(req.getParameter("Month"));
		 	year1=Integer.parseInt(req.getParameter("Year"));
		 	ReportCaption=req.getParameter("ReportCaption");
		 	HeaderCaption=req.getParameter("HeaderCaption");
		}
		if( reportType.equalsIgnoreCase(Constants.DATA_ANALYSIS_REPORT_SUMMARY))
		{
			reportName = "GPSSummaryReport";
	    	reportFileName = reportName + ".rptdesign";	
	    	String vehicletypid=req.getParameter("Vehicletypeid");
			vehicletypeid= Integer.parseInt(vehicletypid);
			String Reporttypid=req.getParameter("ReportTypeID");
			reporttypeid= Integer.parseInt(Reporttypid);
			log.info("vehicletypeid==Sunil=="+vehicletypeid);
	    	month1=Integer.parseInt(req.getParameter("Month"));
		 	year1=Integer.parseInt(req.getParameter("Year"));
		 	ReportCaption=req.getParameter("ReportCaption");
		 	HeaderCaption=req.getParameter("HeaderCaption");
		}
		//added end by sunil on 07-04-2014
	if( reportType.equalsIgnoreCase("TravelledStoppageDetails"))
	{
		userId=req.getParameter("UserId");
		log.info("report name Travelled"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		ServicegroupId=req.getParameter("Serviceid");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		//groupBy = req.getParameter("groupId");
		log.info("report groupBy Travelled333"+groupBy);
		//summaryBy=req.getParameter("summarytypeID");
		showonlymissed=	Integer.parseInt(req.getParameter("Showonlymissed").trim());
		//userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		operatorId=Integer.parseInt(req.getParameter("OperatorID").trim());
		depotId =req.getParameter("DepotId");
		log.info("report name Travelled"+reportType);
		reportFileName="TravelledStoppageDetails .rptdesign";
		reportName="TravelledStoppageDetails ";
		System.out.println(userId+FleetTypeID1+groupBy+showonlymissed+Regnum);
	}
	//added start by sunil
	if( reportType.equalsIgnoreCase(Constants.TRIP_DISTANCE_DETAILS) || reportType.equalsIgnoreCase("exception"))
	{
		log.info("report name Travelled"+reportType);
		String vehicletypid=req.getParameter("Vehicletypeid");
		vehicletypeid= Integer.parseInt(vehicletypid);
		log.info("vehicletypeid"+vehicletypeid);
		serviceno=req.getParameter("Serviceid");
		showexceptional=Integer.parseInt(req.getParameter("ShowExceptional").trim());
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		log.info("report name Travelled333"+reportType);
		//reportFileName="TravelledStoppageDetails .rptdesign";
		// reportName = reportType;
		reportName = "TripDistanceDetails_RO";
		reportFileName = reportName + ".rptdesign";
		
	}
	//added end by sunil
	if( reportType.equalsIgnoreCase(Constants.TRIP_REPORT) )
	{
		log.info("report name Travelled"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

		ServicegroupId=req.getParameter("Serviceid");	
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		showexceptional=Integer.parseInt(req.getParameter("ShowExceptional").trim());
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		log.info("report name Travelled333"+reportType);
		userId=req.getParameter("UserId");
		reportName = "TripReport";
		reportFileName = reportName + ".rptdesign";
	}
	
	//trip report for payments
	if( reportType.equalsIgnoreCase(Constants.TripReportWithPenalty) )
	{
		log.info("report name TripReportForPayments"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
		//DeptId=Integer.parseInt(req.getParameter("DepartmentId"));
		//DepartmentId=req.getParameter("DepartmentId");
		//DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));	
		
		Draft=Integer.parseInt(req.getParameter("Draft"));
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		reportName = Constants.TripReportWithPenalty;
		reportFileName = reportName + ".rptdesign";
	}
	
	if( reportType.equalsIgnoreCase("TripReportWithPenaltyforCity") )
	{
		log.info("report name TripReportWithPenaltyforCity"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
			
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		reportName = "TripReportWithPenaltyforCity";
		reportFileName = "TripReportWithPenaltyforCity.rptdesign";
	}
	
	if( reportType.equalsIgnoreCase("TripReportWithPenaltyforCityDriver") )
	{
		log.info("report name TripReportWithPenaltyforCityDriver"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
			
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		reportName = "TripReportWithPenaltyforCityDriver";
		reportFileName = "TripReportWithPenaltyforCityDriver.rptdesign";
	}
	
	if( reportType.equalsIgnoreCase("DriverBehaviourRanking") )
	{
		log.info("report name DriverBehaviourRanking"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
			
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		reportName = "DriverBehaviourRanking";
		reportFileName = "DriverBehaviourRanking.rptdesign";
	}
	if( reportType.equalsIgnoreCase("DriverBehaviour") )
	{
		log.info("report name DriverBehaviour"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
			
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		reportName = "DriverBehaviour";
		reportFileName = "DriverBehaviour.rptdesign";
	}
	
	if( reportType.equalsIgnoreCase("AgentDailyLoginReport") )
	{
		log.info("report name AgentDailyLoginReport"+reportType);
		
		reportName = "AgentDailyLoginReport";
		reportFileName = "AgentDailyLoginReport.rptdesign";
		userId=req.getParameter("UserId");
	}
	
	
	if( reportType.equalsIgnoreCase("AgentDailyPerformanceReport") )
	{
		log.info("report name AgentDailyPerformanceReport"+reportType);
		
		reportName = "AgentDailyPerformanceReport";
		reportFileName = "AgentDailyPerformanceReport.rptdesign";
		
		userId=req.getParameter("UserId");
	}
	
	if( reportType.equalsIgnoreCase("OperationsDailyReport") )
	{
		log.info("report name OperationsDailyReport"+reportType);
		
		reportName = "OperationsDailyReport";
		reportFileName = "OperationsDailyReport.rptdesign";
		userId=req.getParameter("UserId");
	}
	
	
	if( reportType.equalsIgnoreCase("Invoice") )
	{
		log.info("report name Invoice"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
			
		//ServicegroupId=req.getParameter("Serviceid");
		//selectTypeMonth=req.getParameter("selectType");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//DriverName=req.getParameter("DriverName");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		//userId=req.getParameter("UserId");

		operatorId=Integer.parseInt(req.getParameter("operatorId"));
	//	OperatorName=req.getParameter("OperatorName");
		reportName = "Invoice";
		reportFileName = "Invoice.rptdesign";
	}
	
	if( reportType.equalsIgnoreCase(Constants.TripReportWithPenalty_monthly) )
	{
		log.info("report name TripReportForPayments"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
			
		ServicegroupId=req.getParameter("Serviceid");
		selectTypeMonth=req.getParameter("selectType");

		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		OperatorName=req.getParameter("OperatorName");
		reportName = Constants.TripReportWithPenalty_monthly;
		reportFileName = reportName + ".rptdesign";
	}
	if( reportType.equalsIgnoreCase(Constants.IncidentSummaryReport) )
	{
		log.info("report name IncidentSummaryReport"+reportType);
		reportName = Constants.IncidentSummaryReport;
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		//groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		summaryBy=req.getParameter("summaryBy");	
		summaryBy=req.getParameter("summaryBy");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//depotId =req.getParameter("DepotId");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+groupid+summaryBy+depotId+DriverId+userId+operatorId+Regnum);

		
	}

	
	if( reportType.equalsIgnoreCase(Constants.MissedStopViolationReport) )
	{
		log.info("report name MissedStopViolationReport"+reportType);
		reportName = Constants.MissedStopViolationReport;
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
		//depotId =req.getParameter("DepotId");
		driverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+DriverName+VehicleNumber);

		
	}
	//Revenues
	if( reportType.equalsIgnoreCase("Revenues") )
	{
		log.info("report name Revenues_Daily"+reportType);
		reportName = "Revenues";
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		
		summaryBy=req.getParameter("summaryBy");	
		summaryBy=req.getParameter("summaryBy");	
		MachineNo=Integer.parseInt(req.getParameter("MachineNo"));
		//depotId =req.getParameter("DepotId");
		driverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+MachineNo+DriverName+VehicleNumber);

		
	}
	//Total Commuters
	if( reportType.equalsIgnoreCase("TotalCommuters") )
	{
		log.info("report name TotalCommuters_Daily"+reportType);
		reportName ="TotalCommuters";
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		
		summaryBy=req.getParameter("summaryBy");	
		summaryBy=req.getParameter("summaryBy");	
		MachineNo=Integer.parseInt(req.getParameter("MachineNo"));
		//depotId =req.getParameter("DepotId");
		driverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+MachineNo+DriverName+VehicleNumber);

		
	}
	
	if( reportType.equalsIgnoreCase(Constants.OperationalViolationReport) )
	{
		log.info("report name OperationalViolationReport"+reportType);
		reportName = Constants.OperationalViolationReport;
		reportFileName =  "OperationalPenalties.rptdesign"; 
		//ServicegroupId=req.getParameter("Serviceid");
		//groupBy = req.getParameter("groupId");
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		//OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		
		//depotId =req.getParameter("DepotId");
		driverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		//Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(DriverId+userId+operatorId+DriverName);

		
	}
	
	if( reportType.equalsIgnoreCase("new_report_4") )
	{
		log.info("report name new_report_2"+reportType);
		reportName = "new_report_4";
		reportFileName = reportName + ".rptdesign"; 
		//ServicegroupId=req.getParameter("Serviceid");
		//groupBy = req.getParameter("groupId");
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		
		//depotId =req.getParameter("DepotId");
	//	driverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		//operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		//Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+DriverName+VehicleNumber);

		
	}
	
	if( reportType.equalsIgnoreCase(Constants.Accident_Breakdown_Report) )
	{
		log.info("report name Accident_Breakdown_Report"+reportType);
		reportName = Constants.Accident_Breakdown_Report;
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		//groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		
		//depotId =req.getParameter("DepotId");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+OperatorName+DriverName+VehicleNumber);

		
	}
	if( reportType.equalsIgnoreCase(Constants.Deviation_Report) )
	{
		log.info("report name Deviation_Report"+reportType);
		reportName = Constants.Deviation_Report;
		reportFileName = reportName + ".rptdesign"; 
		serviceid=Integer.parseInt(req.getParameter("Serviceid"));
		groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		
		//depotId =req.getParameter("DepotId");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(serviceid+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+DriverName+VehicleNumber);

		
	}

	if( reportType.equalsIgnoreCase("TripStatusReport") )
	{
		log.info("report name TripStatusReport"+reportType);
		reportName = "TripStatusReport";
		reportFileName = reportName + ".rptdesign"; 
		ServicegroupId=req.getParameter("Serviceid");
		groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		
		//summaryBy=req.getParameter("summaryBy");	
		//summaryBy=req.getParameter("summaryBy");	
		
		//depotId =req.getParameter("DepotId");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+DriverName+VehicleNumber);

		
	}
	
	if( reportType.equalsIgnoreCase(Constants.Travel_Distance_Report) )
	{
		log.info("report name Dead_Report"+reportType);
		reportName = Constants.Travel_Distance_Report;
		reportFileName = reportName + ".rptdesign"; 
		serviceid=Integer.parseInt(req.getParameter("Serviceid"));
		//groupBy = req.getParameter("groupId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
	    //summaryBy=req.getParameter("summaryBy");	
		summaryBy=req.getParameter("summaryBy");	
		
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		System.out.println(ServicegroupId+DriverId+userId+operatorId+Regnum+groupBy+OperatorName+DriverName+VehicleNumber);

		
	}

	//test for shashi
	if( reportType.equalsIgnoreCase("TripReportWithPenalty_backup") )
	{
		log.info("report name TripReportForPayments"+reportType);
	
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
			
		ServicegroupId=req.getParameter("Serviceid");
		selectTypeMonth=req.getParameter("selectType");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		
		reportName = "trippenalty";
		reportFileName ="TripReportWithPenalty_backup.rptdesign";
	}
	
	
	//ROUTE NOT ENTER BY DRIVER...
	if( reportType.equalsIgnoreCase(Constants.ROUTE_NOT_ENTER_BY_DRIVER) )
	{
		log.info("report name Route not enter by driver"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
		ServicegroupId=req.getParameter("Serviceid");	
		
		showexceptional=0;//Integer.parseInt(req.getParameter("ShowExceptional").trim());
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum =0; //Integer.parseInt(req.getParameter("RegNo").trim());
		log.info("report name Travelled333"+reportType);
		userId=req.getParameter("UserId");	
		
		reportName = Constants.ROUTE_NOT_ENTER_BY_DRIVER;
		reportFileName = reportName + ".rptdesign";
	}
	
	
	
	//Bus Service Disruption...
	
	if( reportType.equalsIgnoreCase(Constants.BusServiceDisruption) )
	{
		log.info("report name Bus Service Disrution"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		
		
		ServicegroupId=req.getParameter("Serviceid");
		
		
	//	showexceptional=Integer.parseInt(req.getParameter("ShowExceptional").trim());
	//	userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		log.info("report name Travelled333"+reportType);
		
		reportName = "BusServiceDisruption";
		reportFileName = reportName + ".rptdesign";
	}
	
	//Test for shashi.....
	if(reportType.equalsIgnoreCase(Constants.TripPenaltySummary_monthly))
	{
		//log.info("report name TripReportForPayments"+reportType);

		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}
		ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		selectTypeMonth=req.getParameter("selectType");
		
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		log.info("report name test for shashi  "+reportType);
		reportName=Constants.TripPenaltySummary_monthly;
		reportFileName=reportName +".rptdesign";
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.TripPenaltySummary))
	{
		//log.info("report name TripReportForPayments"+reportType);

		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}*/
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DriverName=req.getParameter("DriverName");
		//ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		//selectTypeMonth=req.getParameter("selectType");
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		log.info("report name test for shashi  "+reportType);
		reportName=Constants.TripPenaltySummary;
		reportFileName=reportName +".rptdesign";
		
	}


	
	//test
	
	if(reportType.equalsIgnoreCase("TripPenaltySummary_logo"))
	{
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
			}
			catch(Exception ee)
			{
				FleetTypeID=0;
				ee.printStackTrace();
			}*/
		//ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		selectTypeMonth=req.getParameter("selectType");
		operatorId=Integer.parseInt(req.getParameter("operatorId"));
		log.info("report name test for shashi  "+reportType);
		reportName="TripPenaltySummary";
		reportFileName="TripPenaltySummary_logo.rptdesign";
		
	}

	
	//Bus Equipment Fault Summary ...
	
	if( reportType.equalsIgnoreCase(Constants.BusEquipmentFaultSummary) )
	{
		log.info("report name Bus Equipment Fault Summary"+reportType);
		
		ServicegroupId=req.getParameter("Serviceid");
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		log.info("report name Travelled333"+reportType);
		
		reportName = "BusEquipmentFaultSummary";
		reportFileName = reportName + ".rptdesign";
	}
	
	
	//Unit On Off Report  
	if( reportType.equalsIgnoreCase(Constants.UNITONOFF) )
	{
		userId=req.getParameter("UserId");
		ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		log.info("report name Travelled333"+reportType);
		
		reportName = "UnitOnOffReport";
		reportFileName = reportName + ".rptdesign";
	}
	//Batery Report
	if( reportType.equalsIgnoreCase(Constants.BATERYREPORT) )
	{
		log.info("report name BATERYREPORT "+reportType);			
		ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		log.info("report name BateryReport"+reportType);		
		reportName = "BatteryReport";
		reportFileName = reportName + ".rptdesign";
		userId=req.getParameter("UserId");
	}
//	/Ignition Report
	if( reportType.equalsIgnoreCase(Constants.IGNITION) )
	{
		log.info("report name IgnitionReport "+reportType);
		ServicegroupId=req.getParameter("Serviceid");
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		log.info("report name IgnitionReport "+reportType);
		
		reportName = "IgnitionReport";
		reportFileName = reportName + ".rptdesign";
		userId=req.getParameter("UserId");
	}

	if( reportType.equalsIgnoreCase("ChartReport") )
	{
		log.info("report name Travelled"+reportType);
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
		ServicegroupId=req.getParameter("Serviceid");
		
		
		showexceptional=Integer.parseInt(req.getParameter("ShowExceptional").trim());
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		log.info("report name Travelled333"+reportType);
		
		reportName = "TestPiCartTripReport";
		reportFileName = reportName + ".rptdesign";
		userId=req.getParameter("UserId");
	}
	
	

	if( reportType.equalsIgnoreCase("AdherenceReport") )
	{
		log.info("report name Travelled"+reportType);
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		*/
		ServicegroupId=req.getParameter("Serviceid");
		
		userId=req.getParameter("UserId");
		//showexceptional=Integer.parseInt(req.getParameter("ShowExceptional").trim());
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		log.info("report name Travelled333"+reportType);
		
		reportName = "PiCartTripAdherence";
		reportFileName = reportName + ".rptdesign";
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.MISSEDTRAVELLEDSTOPPAGE_REPORT))
	{
		ServicegroupId=req.getParameter("Serviceid");
		String vehicletypid=req.getParameter("Vehicletypeid");
		vehicletypeid= Integer.parseInt(vehicletypid);
		log.info("vehicletypeid"+vehicletypeid);
		serviceno=req.getParameter("Serviceid");
		showonlymissed=	Integer.parseInt(req.getParameter("Showonlymissed").trim());
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		reportFileName =Constants.MISSEDTRAVELLEDSTOPPAGE_REPORT + ".rptdesign";
		reportName =Constants.MISSEDTRAVELLEDSTOPPAGE_REPORT;
	}
	
	//added by renuka
	if(reportType.equalsIgnoreCase(Constants.SCHEDULE_REPORT))
	{
		
		reportFileName =Constants.SCHEDULE_REPORT + ".rptdesign";
		reportName =Constants.SCHEDULE_REPORT;
		depotId = req.getParameter("depotId");
		ServicegroupId=req.getParameter("Serviceid");
		
		try{
		Stopid=req.getParameter("StopId");
		}
		catch(Exception e)
		{
			Stopid="0";
		}
		
		userId=req.getParameter("UserId");
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DAILY_SCHEDULE_REPORT))
	{
		
		reportFileName =Constants.DAILY_SCHEDULE_REPORT + ".rptdesign";
		reportName =Constants.DAILY_SCHEDULE_REPORT;
		ServicegroupId=req.getParameter("Serviceid");
		//RouteID=req.getParameter("routeID");
		//depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

	}
	
	
	if(reportType.equalsIgnoreCase(Constants.VehicleCrewAllocation))
	{
		
		reportFileName =Constants.VehicleCrewAllocation + "Report.rptdesign";
		reportName ="VehicleCrewAllocationReport";
		//ServicegroupId=req.getParameter("Serviceid");
		//RouteID=req.getParameter("routeID");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.VehicleMaintenance))
	{
		
		reportFileName =Constants.VehicleMaintenance + "Report.rptdesign";
		reportName ="VehicleMaintenanceReport";
		//ServicegroupId=req.getParameter("Serviceid");
		//RouteID=req.getParameter("routeID");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.VEHICLE_AVAILABILITY_HOURLY_REPORT))
	{
		
		reportFileName =Constants.VEHICLE_AVAILABILITY_HOURLY_REPORT + ".rptdesign";
		reportName =Constants.VEHICLE_AVAILABILITY_HOURLY_REPORT;
		
		date=req.getParameter("date");
		//vyear=req.getParameter("year");
		
		groupBy = req.getParameter("groupBy");
		summaryBy=req.getParameter("summaryBy");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.ViolationReport))
	{
		
		reportFileName =Constants.ViolationReport + ".rptdesign";
		reportName ="ViolationReport";		
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		System.out.println("i m here");
		ServicegroupId=req.getParameter("Serviceid");
		userId=req.getParameter("UserId");
		System.out.println("Serviceid ***************"+ServicegroupId);
		groupBy = req.getParameter("groupId");
		summaryBy=req.getParameter("summarytypeid");		
		summaryBy=req.getParameter("summarytypeid");	
		System.out.println(req.getParameter("summarytypeid"));
		depotid = Integer.parseInt(req.getParameter("depotid").trim());
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

		operatorId=Integer.parseInt(req.getParameter("OperatorId"));

		System.out.println(ServicegroupId+Regnum+groupBy+summaryBy+depotId+userId+FleetTypeID1);
	}
	
	if(reportType.equalsIgnoreCase("Copy of ViolationReport"))
	{
		
		reportFileName ="Copy of ViolationReport.rptdesign";
		reportName ="Copy of ViolationReport";		
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		System.out.println("i m here");
		ServicegroupId=req.getParameter("Serviceid");
		userId=req.getParameter("UserId");
		System.out.println("Serviceid ***************"+ServicegroupId);
		groupBy = req.getParameter("groupId");
		summaryBy=req.getParameter("summarytypeid");		
		summaryBy=req.getParameter("summarytypeid");	
		System.out.println(req.getParameter("summarytypeid"));
		depotid = Integer.parseInt(req.getParameter("depotid").trim());
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		VehicleNumber=req.getParameter("VehicleNumber");
		DriverId=Integer.parseInt(req.getParameter("DriverId").trim());
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

		operatorId=Integer.parseInt(req.getParameter("OperatorId"));

		System.out.println(ServicegroupId+Regnum+groupBy+summaryBy+depotId+userId+FleetTypeID1);
	}
	//Deepali
	if(reportType.equalsIgnoreCase(Constants.DelayReport))
	{
		
		reportFileName =Constants.DelayReport + ".rptdesign";
		reportName ="DelayViolationReport";		
		
		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		//userId=req.getParameter("UserId");
		
		System.out.println("i m here");				
	}
	
	if(reportType.equalsIgnoreCase(Constants.MissedPointReport))
	{		
		reportFileName =Constants.MissedPointReport + ".rptdesign";
		reportName ="MissedPoints";		
				
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
	
		userId=req.getParameter("UserId");
		
		System.out.println("operatorId: "+operatorId);		
	}
	
	if(reportType.equalsIgnoreCase(Constants.IdlingReport))
	{		
		reportFileName =Constants.IdlingReport + ".rptdesign";
		reportName ="IdlingViolationReport";		
				
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		//userId=req.getParameter("UserId");
		
		System.out.println("i m here");		
	}
	
	/*if(reportType.equalsIgnoreCase(Constants.ViolationSummaryReport))
	{		
		reportFileName =Constants.ViolationSummaryReport + ".rptdesign";
		reportName ="SummaryReport";	
		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId").trim());
		DepartmentName=req.getParameter("DepartmentName");	
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID").trim());
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		
		System.out.println(Regnum+operatorId);		
	}*/
	if(reportType.equalsIgnoreCase("SummaryReport"))
	{		
		reportFileName ="SummaryReport.rptdesign";
		reportName ="SummaryReport";	
		userId=req.getParameter("UserId");
		DeptZoneId=req.getParameter("DeptZoneId");
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId").trim());
		DepartmentName=req.getParameter("DepartmentName");	
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
	
		
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		String FleetTypeID1=req.getParameter("FleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
		System.out.println(operatorId);		
	}
	
	if(reportType.equalsIgnoreCase("DepartmentSummaryReport"))
	{		
		reportFileName ="DepartmentSummaryReport.rptdesign";
		reportName ="DepartmentSummaryReport";	
		userId=req.getParameter("UserId");
		DeptZoneId=req.getParameter("DeptZoneId");
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId").trim());
		DepartmentName=req.getParameter("DepartmentName");	
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId").trim());
		
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		String FleetTypeID1=req.getParameter("FleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		
		System.out.println(Regnum+operatorId);		
	}
	
	
	/*if(reportType.equalsIgnoreCase("DepartmentSummaryReport"))
	{		
		reportFileName ="DepartmentSummaryReport.rptdesign";
		reportName ="DepartmentSummaryReport";	
		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId").trim());
		DepartmentName=req.getParameter("DepartmentName");	
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		//operatorId=Integer.parseInt(req.getParameter("OperatorID").trim());
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		
		System.out.println(Regnum+operatorId);		
	}*/
	
	
	
	if(reportType.equalsIgnoreCase(Constants.ComplaintSummaryReport))
	{		
		reportFileName =Constants.ComplaintSummaryReport + ".rptdesign";
		reportName ="ComplaintSummaryReport";	
		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());*/
		
		System.out.println("i m here");		
	}
	
	if(reportType.equalsIgnoreCase(Constants.ComplaintDetailedReport))
	{		
		reportFileName =Constants.ComplaintDetailedReport + ".rptdesign";
		reportName ="ComplaintDetailedReport";	
				
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());
		/*		
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		Regnum =Integer.parseInt(req.getParameter("VehicleID").trim());*/
		
		System.out.println("i m here");		
	}
// Dipali ended
	if(reportType.equalsIgnoreCase(Constants.OnTimePerformanceReport))
	{
		
		reportFileName =Constants.OnTimePerformanceReport + ".rptdesign";
		reportName =Constants.OnTimePerformanceReport;		
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		ServicegroupId=req.getParameter("Serviceid");
		//RouteID=req.getParameter("routeID");
		groupBy = req.getParameter("groupId");
		summaryBy=req.getParameter("SummarytypeID");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

	}

	if(reportType.equalsIgnoreCase(Constants.ReserveVehicleAtDepotReport))
	{
		
		reportFileName =Constants.ReserveVehicleAtDepotReport + ".rptdesign";
		reportName =Constants.ReserveVehicleAtDepotReport;
		
		ServicegroupId=req.getParameter("Serviceid");
		groupBy = req.getParameter("groupBy");
		date =req.getParameter("date");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DailyOutSheddingReport))
	{
		try
		{
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		}
		catch(Exception e)
		{
			Regnum = 0;
			e.printStackTrace();
		}
		userId=req.getParameter("UserId");
		reportFileName =Constants.DailyOutSheddingReport + ".rptdesign";
		reportName =Constants.DailyOutSheddingReport;
		
		groupBy = req.getParameter("groupId");
		//date =req.getParameter("date");
		depotId = req.getParameter("depotid");
		
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.VehicleRunSummaryReport))
	{
		
		reportFileName =Constants.VehicleRunSummaryReport+".rptdesign";
		reportName =Constants.VehicleRunSummaryReport;
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try
		{
			FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		//DeptId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		ServicegroupId=req.getParameter("Serviceid");
		Regnum = Integer.parseInt(req.getParameter("VehicleID").trim());
		groupBy = req.getParameter("groupId");
		summaryBy=req.getParameter("summarytypeid");
		depotId = req.getParameter("depotid");
		userId=req.getParameter("UserId");
		System.out.println(ServicegroupId+Regnum+groupBy+summaryBy+depotId+userId+DeptId);
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.WAITING_PERIOD_REPORT))
	{
		
		reportFileName =Constants.WAITING_PERIOD_REPORT + ".rptdesign";
		reportName =Constants.WAITING_PERIOD_REPORT;
		
		serviceno=req.getParameter("serviceno");
		depotId = req.getParameter("depotId");
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.VEHICLE_AVAILABILITY_MONTHLY_REPORT))
	{
		
		reportFileName =Constants.VEHICLE_AVAILABILITY_MONTHLY_REPORT + ".rptdesign";
		reportName =Constants.VEHICLE_AVAILABILITY_MONTHLY_REPORT;
		
		vmonth=req.getParameter("month");
		vyear=req.getParameter("year");
		
		groupBy = req.getParameter("groupBy");
		summaryBy=req.getParameter("summaryBy");
		depotId = req.getParameter("depotId");
		
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.VEHICLEDISTANCETRAVELLED_REPORT)){
		String vehicletypid=req.getParameter("Vehicletypeid");
		vehicletypeid= Integer.parseInt(vehicletypid);
		serviceno=req.getParameter("Serviceid");
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		groupid = Integer.parseInt(req.getParameter("groupId"));
		summarytypeid=Integer.parseInt(req.getParameter("SummarytypeID"));
		reportFileName =Constants.VEHICLEDISTANCETRAVELLED_REPORT + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName =Constants.VEHICLEDISTANCETRAVELLED_REPORT;
	}
	

	if(reportType.equalsIgnoreCase(Constants.TripSummaryReport)){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		depotId = req.getParameter("DepotId");
		//serviceno=req.getParameter("Serviceid");
		ServicegroupId=req.getParameter("Serviceid");
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		summaryBy=req.getParameter("SummarytypeID");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");

		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName =Constants.TripSummaryReport + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName =Constants.TripSummaryReport;
	}
	if(reportType.equalsIgnoreCase(Constants.OperationReconciliation)){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//serviceno=req.getParameter("Serviceid");
		ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DepartmentId=req.getParameter("DepartmentId");
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName =Constants.OperationReconciliation + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName =Constants.OperationReconciliation;
	}
	
	if(reportType.equalsIgnoreCase(Constants.CDRReport)){
		
		callstatus=req.getParameter("status");
		calledfrom=req.getParameter("CalledFrom");
		calledto=req.getParameter("CalledTo");
		reportFileName =Constants.CDRReport + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName =Constants.CDRReport;
	}
	
	
	if(reportType.equalsIgnoreCase("Copy of OperationReconciliation")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//serviceno=req.getParameter("Serviceid");
		ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		RejectionStatus=Integer.parseInt(req.getParameter("RejectionStatus"));
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DepartmentId=req.getParameter("DepartmentId");
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="Copy of OperationReconciliation.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="Copy of OperationReconciliation";
	}
	
	if(reportType.equalsIgnoreCase("SummaryCCApproval")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		groupid = Integer.parseInt(req.getParameter("groupId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="SummaryCCApproval.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="SummaryCCApproval";
		System.out.println(DeptDataId+groupBy+userId);
	}
	
	if(reportType.equalsIgnoreCase("DMSCrewUtilization"))
	{
		
		reportFileName ="DMSCrewUtilization.rptdesign";
		reportName ="DMSCrewUtilization";
		
		
		
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.DMSItemTransaction))
	{
		reportFileName =Constants.DMSItemTransaction+".rptdesign";
		reportName =Constants.DMSItemTransaction;
		//userId=req.getParameter("UserId");
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.DMSAttendanceJobForMail))
	{
		reportFileName =Constants.DMSAttendanceJobForMail+".rptdesign";
		reportName =Constants.DMSAttendanceJobForMail;
	
	}
	if(reportType.equalsIgnoreCase(Constants.DMSAttendanceReport))
	{
		reportFileName =Constants.DMSAttendanceReport+".rptdesign";
		reportName =Constants.DMSAttendanceReport;
	
		OperatorIdDMS=req.getParameter("OperatorIdDMS");
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.DMSQuarantine))
	{
		reportFileName =Constants.DMSQuarantine+".rptdesign";
		reportName =Constants.DMSQuarantine;
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSCrewlicenserenewalhistory))
	{
		reportFileName =Constants.DMSCrewlicenserenewalhistory+".rptdesign";
		reportName =Constants.DMSCrewlicenserenewalhistory;
		userId=req.getParameter("UserId");
		empId=req.getParameter("EmpId");
		//Id=req.getParameter("Id");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSFuelConsumption))
	{
		reportFileName =Constants.DMSFuelConsumption+".rptdesign";
		reportName =Constants.DMSFuelConsumption;
		userId=req.getParameter("UserId");
		groupid=Integer.parseInt(req.getParameter("groupId"));
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSAssetDetail))
	{
		reportFileName =Constants.DMSAssetDetail+".rptdesign";
		reportName =Constants.DMSAssetDetail;
		userId=req.getParameter("UserId");
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSAssetSummary))
	{
		reportFileName =Constants.DMSAssetSummary+".rptdesign";
		reportName =Constants.DMSAssetSummary;
		userId=req.getParameter("UserId");
		
	}
	if(reportType.equalsIgnoreCase(Constants.DMSPhysicalItemStock))
	{
		reportFileName =Constants.DMSPhysicalItemStock+".rptdesign";
		reportName =Constants.DMSPhysicalItemStock;
		userId=req.getParameter("UserId");
		UOM= Integer.parseInt(req.getParameter("UOM").trim());
	//	PhysicalStockQty= Float.parseFloat(req.getParameter("PhysicalStockQty").trim());
	//	SystemStockQty= Float.parseFloat(req.getParameter("SystemStockQty").trim());
		//ExpiryDate=req.getParameter("ExpiryDate");
		Item=req.getParameter("Item").trim();
		System.out.println(userId+UOM+Item);
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSIndentsReport))
	{
		reportFileName =Constants.DMSIndentsReport+".rptdesign";
		reportName =Constants.DMSIndentsReport;
			
		userId=req.getParameter("UserId");
		IndentNo=req.getParameter("IndentNo");
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSPurchaseOrders))
	{
		reportFileName =Constants.DMSPurchaseOrders+".rptdesign";
		reportName =Constants.DMSPurchaseOrders;
			
		userId=req.getParameter("UserId");
		PurchaseOrderNo=req.getParameter("PurchaseOrderNo");
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSIssuedItems))
	{
		reportFileName =Constants.DMSIssuedItems+".rptdesign";
		reportName =Constants.DMSIssuedItems;
			
		userId=req.getParameter("UserId");
		IssueNo=req.getParameter("IssueNo");	
	}
	if(reportType.equalsIgnoreCase(Constants.DMSReceivedItems))
	{
		reportFileName =Constants.DMSReceivedItems+".rptdesign";
		reportName =Constants.DMSReceivedItems;
			
		userId=req.getParameter("UserId");
		IssueNo=req.getParameter("IssueNo");	
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSReturnedItems))
	{
		reportFileName =Constants.DMSReturnedItems+".rptdesign";
		reportName =Constants.DMSReturnedItems;
		userId=req.getParameter("UserId");
		RetReferenceNo=req.getParameter("RetReferenceNo");	
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSStockStatus))
	{
		reportFileName =Constants.DMSStockStatus+".rptdesign";
		reportName =Constants.DMSStockStatus;
		userId=req.getParameter("UserId");	
	}

	if(reportType.equalsIgnoreCase(Constants.DMSWorkOrders))
	{
		reportFileName =Constants.DMSWorkOrders+".rptdesign";
		reportName =Constants.DMSWorkOrders;	
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSWorkOrderUsage))
	{
		reportFileName =Constants.DMSWorkOrderUsage+".rptdesign";
		reportName =Constants.DMSWorkOrderUsage;	
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSEmployee))
	{
		reportFileName =Constants.DMSEmployee+".rptdesign";
		reportName =Constants.DMSEmployee;
		userId=req.getParameter("UserId");	
		DepartmentId=req.getParameter("DepartmentId");
		DesignationId=req.getParameter("DesignationId");
		OperatorIdDMS=req.getParameter("OperatorIdDMS");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSGRN))
	{
		reportFileName =Constants.DMSGRN+".rptdesign";
		reportName =Constants.DMSGRN;
		userId=req.getParameter("UserId");	
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSDepotStation))
	{
		reportFileName =Constants.DMSDepotStation+".rptdesign";
		reportName =Constants.DMSDepotStation;
		userId=req.getParameter("UserId");	
		
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSBuses))
	{
		reportFileName =Constants.DMSBuses+".rptdesign";
		reportName =Constants.DMSBuses;
		userId=req.getParameter("UserId");	
		OperatorIdDMS=req.getParameter("OperatorIdDMS");
	}
	if(reportType.equalsIgnoreCase(Constants.DMSFuelLog))
	{
		reportFileName =Constants.DMSFuelLog+".rptdesign";
		reportName =Constants.DMSFuelLog;
			
		userId=req.getParameter("UserId");
	}
	if(reportType.equalsIgnoreCase(Constants.DMSInsuranceLog))
	{
		reportFileName =Constants.DMSInsuranceLog+".rptdesign";
		reportName =Constants.DMSInsuranceLog;
			
		userId=req.getParameter("UserId");
	}
	if(reportType.equalsIgnoreCase(Constants.DMSMaintenanceLog))
	{
		reportFileName =Constants.DMSMaintenanceLog+".rptdesign";
		reportName =Constants.DMSMaintenanceLog;
			
		userId=req.getParameter("UserId");
	}
	if(reportType.equalsIgnoreCase(Constants.DMSNonOperationalBuses))
	{
		reportFileName =Constants.DMSNonOperationalBuses+".rptdesign";
		reportName =Constants.DMSNonOperationalBuses;
		userId=req.getParameter("UserId");
		OperatorIdDMS=req.getParameter("OperatorIdDMS");
		
	}
	if(reportType.equalsIgnoreCase(Constants.DMSSuspendedStocks))
	{
		reportFileName =Constants.DMSSuspendedStocks+".rptdesign";
		reportName =Constants.DMSSuspendedStocks;
		userId=req.getParameter("UserId");	
		
	}
	if(reportType.equalsIgnoreCase(Constants.DMSScheduleallocation))
	{
		reportFileName =Constants.DMSScheduleallocation+".rptdesign";
		reportName =Constants.DMSScheduleallocation;
		userId=req.getParameter("UserId");	
		
	}
	if(reportType.equalsIgnoreCase(Constants.DMSCrewUtilizations))
	{
		reportFileName =Constants.DMSCrewUtilizations+".rptdesign";
		reportName =Constants.DMSCrewUtilizations;
		OperatorIdDMS=req.getParameter("OperatorId");
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));

		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSFitnessCertificate))
	{
		reportFileName =Constants.DMSFitnessCertificate+".rptdesign";
		reportName =Constants.DMSFitnessCertificate;
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSVehicleMaintenanceHistory))
	{
		reportFileName =Constants.DMSVehicleMaintenanceHistory+".rptdesign";
		reportName =Constants.DMSVehicleMaintenanceHistory;
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		userId=req.getParameter("UserId");
		groupid = Integer.parseInt(req.getParameter("groupId"));
     	BusNo=req.getParameter("BusNo");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSPendingMaintenance))
	{
		reportFileName =Constants.DMSPendingMaintenance+".rptdesign";
		reportName =Constants.DMSPendingMaintenance;
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		userId=req.getParameter("UserId");
	}
	
	if(reportType.equalsIgnoreCase(Constants.DMSStaffOverTimeDetails))
	{
		reportFileName =Constants.DMSStaffOverTimeDetails+".rptdesign";
		reportName =Constants.DMSStaffOverTimeDetails;
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		userId=req.getParameter("UserId");
	}
	if(reportType.equalsIgnoreCase(Constants.DMSDriverWiseIncidents))
	{
		reportFileName =Constants.DMSDriverWiseIncidents+".rptdesign";
		reportName =Constants.DMSDriverWiseIncidents;
		OperatorName=req.getParameter("OperatorName");
		DriverName=req.getParameter("DriverName");
		IsFatality= Integer.parseInt(req.getParameter("IsFatality"));
		userId=req.getParameter("UserId");
	}
	if(reportType.equalsIgnoreCase(Constants.ConsolidatedDailyReport))
	{
		reportFileName =Constants.ConsolidatedDailyReport+".rptdesign";
		reportName =Constants.ConsolidatedDailyReport;
	
		Frequency=req.getParameter("Frequency");
		Period=req.getParameter("Period");
		
	}
	
	
	
	if(reportType.equalsIgnoreCase("VehicleOperationSummary")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="VehicleOperationSummary.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="VehicleOperationSummary";
		System.out.println(OperatorName+groupBy+userId+operatorId);
	}
	if(reportType.equalsIgnoreCase("VehicleOperationSummaryV2")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		DepartmentId=req.getParameter("DepartmentId");
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="VehicleOperationSummaryV2.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="VehicleOperationSummaryV2";
		System.out.println(OperatorName+DeptDataId+userId+operatorId);
	}
	
	if(reportType.equalsIgnoreCase("OperationSummaryforfirstSix")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//Group = req.getParameter("groupId");
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		rowcount=Integer.parseInt(req.getParameter("rowcount"));
		roworder=Integer.parseInt(req.getParameter("roworder"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));-
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="OperationSummaryforfirstSix.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="OperationSummaryforfirstSix";
		System.out.println(userId+operatorId+DeptDataId);
	}
	
	if(reportType.equalsIgnoreCase("DailyOperationSummary")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//Group = req.getParameter("groupId");
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		//rowcount=Integer.parseInt(req.getParameter("rowcount"));
		//roworder=Integer.parseInt(req.getParameter("roworder"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DepartmentName=req.getParameter("DepartmentName");
		DepartmentId=req.getParameter("DepartmentId");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="DailyOperationSummary.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="DailyOperationSummary";
		System.out.println(userId+operatorId+DeptDataId);
	}
	
	if(reportType.equalsIgnoreCase("TravelSummaryofWorkShop")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//Group = req.getParameter("groupId");
		operatorId=Integer.parseInt(req.getParameter("OperatorID"));
		//rowcount=Integer.parseInt(req.getParameter("rowcount"));
		//roworder=Integer.parseInt(req.getParameter("roworder"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		//DepartmentName=req.getParameter("DepartmentName");
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="TravelSummaryofWorkShop.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="TravelSummaryofWorkShop";
		System.out.println(userId+operatorId+DeptDataId);
	}
	
	
	if(reportType.equalsIgnoreCase("OperationSummaryforlastSix")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		//OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//Group = req.getParameter("groupId");
		operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		//rowcount=Integer.parseInt(req.getParameter("rowcount"));
		//roworder=Integer.parseInt(req.getParameter("roworder"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		DeptDataId=Integer.parseInt(req.getParameter("DepartmentId"));
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="OperationSummaryforlastSix.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="OperationSummaryforlastSix";
		System.out.println(userId+operatorId+DeptDataId);
	}
	
	if(reportType.equalsIgnoreCase("CCSupervisor")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		//DeptDataId=Integer.parseInt(req.getParameter("DeptDataId"));
		/*String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}*/
		OperatorName=req.getParameter("OperatorName");
		//DriverName=req.getParameter("DriverName");
		//VehicleNumber=req.getParameter("VehicleNumber");
		//serviceno=req.getParameter("Serviceid");
		//ServicegroupId=req.getParameter("Serviceid");
		//Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		//operatorId=Integer.parseInt(req.getParameter("OperatorId"));
		//ShiftId=Integer.parseInt(req.getParameter("ShiftId"));
		//groupBy = req.getParameter("groupId");
		//summaryBy=req.getParameter("summarytypeid");	
		//summaryBy=req.getParameter("SummarytypeID");	
		
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="CCSupervisor.rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="CCSupervisor";
		System.out.println(OperatorName+groupBy+userId+operatorId);
	}
	
	
	if(reportType.equalsIgnoreCase("TripSummaryChart")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
		userId=req.getParameter("UserId");
		String FleetTypeID1=req.getParameter("fleetTypeID");
		try{
		FleetTypeID= Integer.parseInt(FleetTypeID1);
		}
		catch(Exception ee)
		{
			FleetTypeID=0;
			ee.printStackTrace();
		}
		//serviceno=req.getParameter("Serviceid");
		ServicegroupId=req.getParameter("Serviceid");
		Regnum = Integer.parseInt(req.getParameter("RegNo").trim());
		//groupid = Integer.parseInt(req.getParameter("groupId"));
		
		groupBy = req.getParameter("groupId");
		summaryBy=req.getParameter("summarytypeid");	
		summaryBy=req.getParameter("SummarytypeID");	
		
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="TripSummaryChart" + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="TripSummaryChart";
		System.out.println(ServicegroupId+Regnum+groupBy+summaryBy+userId+FleetTypeID);

		
	}
	
	if(reportType.equalsIgnoreCase("watermark")){
		//String Route=req.getParameter("Vehicletypeid");
		//RouteID= req.getParameter("routeID");
			
		userId=req.getParameter("UserId");
		//summarytypeid=Integer.parseInt(req.getParameter("summarytypeid"));
		reportFileName ="watermark" + ".rptdesign";
		//reportFileName ="new_report.rptdesign";
		reportName ="watermark";
		System.out.println(ServicegroupId+Regnum+groupBy+summaryBy+userId+FleetTypeID);

		
	}
	
	
	if(reportType.equalsIgnoreCase(Constants.MONTHLY_ANALYSIS_REPORT))
	{
		groupid = Integer.parseInt(req.getParameter("groupId"));
		summarytypeid=Integer.parseInt(req.getParameter("SummarytypeID"));
		ReportCaption=req.getParameter("ReportCaption");
	 	HeaderCaption=req.getParameter("HeaderCaption");
	 	String vehicletypid=req.getParameter("Vehicletypeid");
		vehicletypeid= Integer.parseInt(vehicletypid);
		String Reporttypid=req.getParameter("ReportTypeID");
		reporttypeid= Integer.parseInt(Reporttypid);
		log.info("vehicletypeid==Sunil=="+vehicletypeid);
	 	reportFileName =Constants.MONTHLY_ANALYSIS_REPORT + ".rptdesign";
		reportName =Constants.MONTHLY_ANALYSIS_REPORT;
	}
	
	if(reportType.equalsIgnoreCase(Constants.TRIPTRACKING_REPORT)){
	
		reportFileName =Constants.TRIPTRACKING_REPORT + ".rptdesign";
		reportName =Constants.TRIPTRACKING_REPORT;
	}
	if(reportType.equalsIgnoreCase(Constants.NMMT_INVENTORY))
	{
		userid=Integer.parseInt(req.getParameter("UserId").trim());
		MaterialType=Integer.parseInt(req.getParameter("MaterialType").trim());
		reportFileName =Constants.NMMT_INVENTORY + ".rptdesign";
		reportName =Constants.NMMT_INVENTORY ;
	}
	
	
	
	
	if (reportType != null && reportType.equalsIgnoreCase(Constants.NMMT_AVAILABILITY_REPORT )&&(vehicle.equals(null)||vehicle.equalsIgnoreCase("all")))
	{log.info("inside all");
		reportFileName = Constants.PERFORMANCE_REPORT + ".rptdesign";
		reportName =Constants.PERFORMANCE_REPORT;
	}
	
	//Vehicle Violation consolidated summary
	if (reportType != null
		&& reportType
		.equalsIgnoreCase(Constants.VEHICLE_VIOLATION_SUMMARY_REPORT)) {
		
	}

	
	  if(reportType.equalsIgnoreCase(Constants.ABSENTEE_REPORT)) {			   	
	    	reportName = "FleetAttendanceReport";
	    	reportFileName = reportName + ".rptdesign";	
	    	String vehicletypid=req.getParameter("Vehicletypeid");
			vehicletypeid= Integer.parseInt(vehicletypid);
			statusID = req.getParameter("status");
			log.info("vehicletypeid==Sunil=="+vehicletypeid);
		    log.info(" statusID        : " +statusID);
	    	month1=Integer.parseInt(req.getParameter("Month"));
		 	year1=Integer.parseInt(req.getParameter("Year"));
	  }	
	//Performance, Performance Availability and Violation Action taken reports
	if (reportType != null
		&& (reportType.equalsIgnoreCase("Performance") || reportType
			.equalsIgnoreCase("PerformanceAvailability"))
			|| reportType.equalsIgnoreCase("ViolationActionTaken")) {
	    String level = req.getParameter("level");
	    reportName = "PerformanceReport";
	    if (reportType.equalsIgnoreCase("PerformanceAvailability")) {
		reportName = "PerformanceAvailabilityReport";
	    } else if (reportType.equalsIgnoreCase("ViolationActionTaken")) {
		reportName = "ViolationActionTakenReport";
	    }
	    if (level != null) {
		levelNumber = Integer.parseInt(level.trim());
		if (levelNumber == Constants.LOWEST_LEVEL) {
		    if(req.getParameter("istrans") != null) {
			try {
			    int isTransporter = Integer.parseInt(req
				    .getParameter("istrans"));
			    log.info("Is transporter : " + isTransporter);
			    if (isTransporter == Constants.TRANSPORTER) {
				levelNumber = Constants.TRANSPORTER_LEVEL;
			    } else if (isTransporter == Constants.RETAIL_OUTLET) {
				levelNumber = Constants.RO_LEVEL;
			    }
			} catch (Exception e) {
			    // TODO: handle exception
			}
		    }
		}
	    }
	    log.info("Report Level : " + levelNumber);
	    switch (levelNumber) {
	    case Constants.ALLINDIA_LEVEL:
		reportName = reportName + "_AllIndia";
		hasChart = true;
		break;
	    case Constants.REGION_LEVEL:
		reportName = reportName + "_Region";
		regionId = req.getParameter("RegionId");
		log.info("RegionId : " + regionId);
		hasChart = true;
		break;
	    case Constants.AREA_LEVEL:
		reportName = reportName + "_Area";
		regionId = req.getParameter("RegionId");
		areaId = req.getParameter("AreaId");
		log.info("RegionId : " + regionId);
		log.info("AreaId : " + areaId);
		hasChart = true;
		break;
	    case Constants.TERRITORY_LEVEL:
		reportName = reportName + "_Territory";
		regionId = req.getParameter("RegionId");
		areaId = req.getParameter("AreaId");
		territoryId = req.getParameter("TerritoryId");
		log.info("RegionId : " + regionId);
		log.info("AreaId : " + areaId);
		log.info("TerritoryId : " + territoryId);
		hasChart = true;
		break;
	    case Constants.DEPOT_LEVEL:
		reportName = reportName + "_Depot";
		regionId = req.getParameter("RegionId");
		areaId = req.getParameter("AreaId");
		territoryId = req.getParameter("TerritoryId");
		depotId = req.getParameter("DepotId");
		log.info("RegionId : " + regionId);
		log.info("AreaId : " + areaId);
		log.info("TerritoryId : " + territoryId);
		log.info("DepotId : " + depotId);
		break;
	    case Constants.TRANSPORTER_LEVEL:
		reportName = reportName + "_Transporter";
		regionId = req.getParameter("RegionId");
		areaId = req.getParameter("AreaId");
		territoryId = req.getParameter("TerritoryId");
		depotId = req.getParameter("DepotId");
		transporterId = req.getParameter("EndLevelId");
		log.info("RegionId : " + regionId);
		log.info("AreaId : " + areaId);
		log.info("TerritoryId : " + territoryId);
		log.info("DepotId : " + depotId);
		log.info("TransporterId : " + transporterId);
		break;
	    case Constants.RO_LEVEL:
		reportName = reportName + "_RO";
		regionId = req.getParameter("RegionId");
		areaId = req.getParameter("AreaId");
		territoryId = req.getParameter("TerritoryId");
		depotId = req.getParameter("DepotId");
		roId = req.getParameter("EndLevelId");
		log.info("RegionId : " + regionId);
		log.info("AreaId : " + areaId);
		log.info("TerritoryId : " + territoryId);
		log.info("DepotId : " + depotId);
		log.info("ROId : " + roId);
		break;
	    default:
		break;
	    }
	    
	    if (hasChart){
		chartVal = req.getParameter("ch");
	    }
	    reportFileName = reportName + ".rptdesign";
	}
	
	log.info("Report Design File Name : " +reportFileName);
	
	//Check for Caching and report document creation required or not
	boolean recreateRPTDoc = true; //even if caching is enabled there are cases where to recreate rpt doc.
	log.info("Caching Enabled : " + cachingEnabled);
	String rptDocName = "";
	String rptDocFile = "";
	try {
	    String fromStr = null;
	    String toStr = null;
	    if(from != null) {
		fromStr = from.replaceAll(":", "_");
		fromStr = fromStr.replaceAll(" ", "_");
	    }
	    if(to != null) {
		toStr = to.replaceAll(":", "_");
		toStr = toStr.replaceAll(" ", "_");
	    }
	    Calendar nowCal = Calendar.getInstance();
	    nowCal.setTime(new Date());
	    int curMonth = nowCal.get(Calendar.MONTH) + 1;
	    int curYear = nowCal.get(Calendar.YEAR);

	    boolean isTodayReport = false;
	    boolean isFutureDate = false;
	    if (to != null && !to.equalsIgnoreCase("")) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
		    Date endDate = format.parse(to);
		    endDate = removeTime(endDate);
		    Date today = removeTime(new Date());
		    int cmpRes = endDate.compareTo(today);
		    if(cmpRes == 0) {
			isTodayReport = true;
		    } else if(cmpRes > 0) {
			isFutureDate = true;
		    }
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    String rptDocStr = reportName + "_" + levelNumber + "_" + month1+"_"+year1+"_"+
	    	vehicletypeid + "_" + serviceid + "_" + showonlymissed + "_" + Regnum + "_" +
		    groupid + "_" +summarytypeid +"_"+regionId + "_" + areaId + "_" + territoryId + "_" + depotId + "_" +
		    transporterId + "_" + roId + "_" + vehicle + "_" + fromStr + "_" + toStr + "_" + 
		    chartVal + "_" + status + "_" + materialId;
	    
	 /*   String rptDocStr = reportName + "_" + levelNumber + "_" + 
	    		vehicletypeid + "_" + serviceid + "_" + showonlymissed + "_" + Regnum + "_" +
		    transporterId + "_" +  vehicle + "_" + fromStr + "_" + toStr + "_" + 
		    chartVal + "_" + status + "_" + materialId;*/
	    
	    
	    if(isTodayReport || isFutureDate) {
		rptDocStr = rptDocStr + "_mid"; // data may not be full if we take this report on next day 
	    } else {
		rptDocStr = rptDocStr + "_full";
	    }

	    String rptDocPath = config.getValue("CACHING_FOLDER_PATH");
	    if(rptDocPath == null) {
		rptDocPath = "C:/NMMT11/RptDocs";
	    }	
	    rptDocPath = rptDocPath + "/" + reportType;	
	    rptDocFile = rptDocPath + "/" + rptDocStr;
	    rptDocName = rptDocFile + ".rptdocument";
	    log.info("Checking for rptDoc : " + rptDocName);
	    File rptFile = new File(rptDocName);
	    if(rptFile.exists() && cachingEnabled) {
		log.info("Report Document already exists...");
		recreateRPTDoc = false;
		if (reportType.equalsIgnoreCase("1availability"))
			{
		    if (from != null && !from.equalsIgnoreCase("")) {
			int month = Integer.parseInt(Util.getMonthFromDate(from
				.trim()));
			int year = Integer.parseInt(Util.getYearFromDate(from
				.trim()));
			if(month == curMonth && year == curYear) {
			    recreateRPTDoc = true; //Recreate for current month
			    log.info("Report for current month , so recreate report document");
			}
		    }
		} else {
		    if(isTodayReport || isFutureDate) {
			log.info("Report for today , so recreate report document");
			recreateRPTDoc = true; 
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("Error occured while caching " + e.getMessage());
	    recreateRPTDoc = true;
	    rptDocName = "C:/NMMT11/RptDocs/"+ reportName + ".rptdocument";
	    rptDocFile = "C:/NMMT11/RptDocs/"+ reportName;
	}
	
	//Avoid caching for following reports (since past data can be changed - violation management)
	if (reportType.equalsIgnoreCase(Constants.SPEED_VIOLATION_REPORT)
		|| reportType
		.equalsIgnoreCase(Constants.ROUTE_VIOLATION_REPORT_ALL)
		|| reportType
		.equalsIgnoreCase(Constants.STOPPAGE_VIOLATION_REPORT_ALL)
		|| reportType
		.equalsIgnoreCase(Constants.NMMT_AVAILABILITY_REPORT)
		|| reportType
		.equalsIgnoreCase("ViolationActionTaken")||reportType.equalsIgnoreCase("TravelledStoppageDetails")){
	    recreateRPTDoc = true;
	    log.info("Recreating as changeable reports...");
	}
	try 
	{
	    ServletContext sc = req.getSession().getServletContext();
	    this.birtReportEngine = BirtEngine.getBirtEngine(sc);
	    IRunTask runTask = null;
	    // Open report design
	    if(!cachingEnabled || recreateRPTDoc) {
		design = birtReportEngine.openReportDesign(sc.getRealPath("/Reports") + "\\" + reportFileName);
		String dbUrl = config.getValue("DBURL") + ";databaseName="
			+ config.getValue("DBNAME");
		log.info("DB Url : " + dbUrl);
		setDbCredentials(design, dbUrl);
		// create task to run and render report
		// IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(
		// design );

		runTask = birtReportEngine.createRunTask(design);
		runTask.getAppContext().put("BIRT_VIEWER_HTTPSERVLET_REQUEST", req);
		
		if (VersionNumber != null) 
		{     System.out.println("VersionNumber "+ VersionNumber);
			runTask.setParameterValue("VersionNumber", VersionNumber.trim());
		}
		
		if (selectTypeMonth != null) 
		{     System.out.println("SelectType "+ selectTypeMonth);
			runTask.setParameterValue("SelectType", selectTypeMonth.trim());
		}
		
		if (companyname != null) 
		{     System.out.println("companyname "+ companyname);
			runTask.setParameterValue("companyname", companyname.trim());
		}
		
		if (serviceno != null) 
		{      System.out.println("serviceno "+ serviceno);
			try
			{
				runTask.setParameterValue("serviceno", serviceno.trim());//changed by rajeev
			}
			catch(NumberFormatException ex)
			{
				runTask.setParameterValue("serviceno", serviceno.trim());
				ex.printStackTrace();
			}
		}
		
		if (Stopid != null) 
		{      System.out.println("StopId "+ Stopid);
		
			try
			{
			runTask.setParameterValue("StopId",Integer.parseInt(Stopid.trim()));
			}
			catch(Exception e)
			{
				runTask.setParameterValue("StopId",Integer.parseInt("0"));
				log.debug(e.getMessage());
			}
		}
		
		if (companyUrl != null) 
		{      System.out.println("companyUrl "+ companyUrl);
			runTask.setParameterValue("companyUrl", companyUrl.trim());
		}
		if (vehicle != null) {
		    runTask.setParameterValue("Regno", vehicle.trim()); 
		    log.info("reg logger: " + vehicle.trim());
		}
		//if (Regnum != 0 || Regnum == 0) {
		    runTask.setParameterValue("RegNo", Regnum); 
		    log.info("reg logger: " + Regnum);
		//}
		//if (Regnum != 0 || Regnum == 0) {
		    runTask.setParameterValue("VehicleID", Regnum); 
		    log.info("reg logger: " + Regnum);
		//}
		//if (DriverId != 0 || DriverId == 0) {
		    runTask.setParameterValue("DriverId", driverId); 
		    log.info("DriverId logger: " + driverId);
		//}
		
	    runTask.setParameterValue("driverId", driverId); 
	    
		
		
		if (vmonth != null) 
		{
			try
			{
			runTask.setParameterValue("Month",Integer.parseInt(vmonth.trim()));
			}
			catch(Exception e)
			{
				runTask.setParameterValue("Month",Integer.parseInt("0"));
				log.debug(e.getMessage());
			}
			
		}
		
		if (vyear != null) 
		{
			try
			{
			runTask.setParameterValue("Year",Integer.parseInt(vyear.trim()));
			}
			catch(Exception e)
			{
				runTask.setParameterValue("Year",Integer.parseInt("0"));
				log.debug(e.getMessage());
			}
			
		}
		
		
		
		//if (serviceid != 0) {
		    runTask.setParameterValue("Serviceid",serviceid); 
		    log.info("reg logger: " + serviceid);
		//}
		
		if (ServicegroupId != null) 
		{      System.out.println("ServicegroupId "+ ServicegroupId);
		
			try
			{
			runTask.setParameterValue("ServiceId",Integer.parseInt(ServicegroupId.trim()));
			
			System.out.println(ServicegroupId +"ServicegroupId**********");
			}
			catch(Exception e)
			{
				runTask.setParameterValue("ServiceId",Integer.parseInt("0"));
				log.debug(e.getMessage());
			}
		}
		if (vehicletypeid != 0 || vehicletypeid == 0) {
		    runTask.setParameterValue("Vehicletypeid", vehicletypeid); 
		    log.info("reg logger: " + vehicletypeid);
		}
		
		if (FleetTypeID != 0 || FleetTypeID == 0) {
		    runTask.setParameterValue("FleetTypeID", FleetTypeID); 
		    log.info("reg logger: " + FleetTypeID);
		}
		
		if (showonlymissed!= 0) {
		    runTask.setParameterValue("Showonlymissed", showonlymissed); 
		    log.info("reg logger: " + showonlymissed);
		}
		if (showexceptional!= 0 || showexceptional==0) {
		    runTask.setParameterValue("ShowExceptional", showexceptional); 
		    log.info("reg logger: " + showexceptional);
		}
		if (reporttypeid!= 0 || reporttypeid==0) {
		    runTask.setParameterValue("ReportTypeID", reporttypeid); 
		    log.info("reg logger reporttypeid: " + reporttypeid);
		}
		if (ReportCaption != null) {
		    runTask.setParameterValue("ReportCaption", ReportCaption);
		}
		if (HeaderCaption != null) {
		    runTask.setParameterValue("HeaderCaption", HeaderCaption);
		}
		//aDDED START BY SUNIL
		//if (groupid != 0 ) {
		  //  runTask.setParameterValue("groupId",groupid); 
		    //log.info("groupid logger: " + groupid);
		//}
		if (groupidmod != 0 ) {
		    runTask.setParameterValue("groupidmod",groupidmod); 
		    log.info("groupidmod logger: " + groupidmod);
		}

		if (summarytypeid != 0 || summarytypeid == 0) {
		    runTask.setParameterValue("SummarytypeID",summarytypeid); 
		    log.info("reg logger: " + summarytypeid);
		}
		if (MaterialType != 0) {
		    runTask.setParameterValue("MaterialType",MaterialType); 
		    log.info("reg logger: " + MaterialType);
		}
		
		if (month1 != 0) {
		    runTask.setParameterValue("Month",month1); 
		    log.info("reg logger: " + month1);
		    log.info("Month : " + month1);
		}
		if (year1 != 0) {
		    runTask.setParameterValue("Year",year1); 
		    log.info("reg logger: " + year1);
		    log.info("Year : " + year1);
		}
		if (RouteID != null) 
		{
			try{
			 runTask.setParameterValue("RouteID", Integer.parseInt(RouteID.trim()));
			}
			catch(Exception ee)
			{
				runTask.setParameterValue("RouteID", 0);
			}
		}
		
		
		
		
		//ADDED END BY SUNIL
		if (status != null) 
		{
			 runTask.setParameterValue("StatusId", Integer.parseInt(status));
		}
			
		if (userId != null) 
		{
			 runTask.setParameterValue("User", Integer.parseInt(userId));
		}
		
		if (statusID != null) 
		{
			 runTask.setParameterValue("StatusID", statusID);
			log.info("reg logger StatusId 1: " + statusID);
		}
	/*	if (statusID == null) 
		{
			 runTask.setParameterValue("StatusId", statusID);
			 log.info("reg logger StatusId 2: " + statusID);
		}*/
		
		if (groupBy!= null) {
		    runTask.setParameterValue("groupBy",
			    Integer.parseInt(groupBy.trim()));
		}
		if (summaryBy!= null) {
		    runTask.setParameterValue("summaryBy",
			    Integer.parseInt(summaryBy.trim()));
		   // System.out.println("test"+summaryBy);
		}
	
		if (from != null) {
		    runTask.setParameterValue("FromDate", from.trim());
		}
		if (date != null) {
		    runTask.setParameterValue("date", date.trim());
		}
		if (to != null) {
		    runTask.setParameterValue("ToDate", to.trim());
		}
		if (userId != null) {
			try{
		    runTask.setParameterValue("UserId",
		    		
			    Integer.parseInt(userId.trim()));
			}
			catch(Exception ee)
			{
				//runTask.setParameterValue("UserId",0);
				ee.printStackTrace();
			}
		}
		if (repFormat != null) {
		    runTask.setParameterValue("ReportFormat", repFormat.trim());
		}
		if (regionId != null) {
		    runTask.setParameterValue("RegionId",
			    Integer.parseInt(regionId.trim()));
		}
		if (areaId != null) {
		    runTask.setParameterValue("AreaId",
			    Integer.parseInt(areaId.trim()));
		}
		if (territoryId != null) {
		    runTask.setParameterValue("TerritoryId",
			    Integer.parseInt(territoryId.trim()));
		}
		if (depotId != null) {
		    runTask.setParameterValue("DepotId",
			    Integer.parseInt(depotId.trim()));
		}
		if (transporterId != null) {
		    runTask.setParameterValue("TransporterId",
			    Integer.parseInt(transporterId.trim()));
		}
		if (roId != null) {
		    runTask.setParameterValue("ROId", Integer.parseInt(roId.trim()));
		} 
		if (chartVal != null) {
		    runTask.setParameterValue("ch", chartVal);
		}
		if (reportType != null
			&& (reportType.equalsIgnoreCase(Constants.PRODUCT_REPORT))) {
		    runTask.setParameterValue("Level", String.valueOf(levelNumber));
		    
		    if(reportType.equalsIgnoreCase(Constants.PRODUCT_REPORT)) {
			runTask.setParameterValue("MaterialId", materialId);
		    }
		}
		if (reportType.equalsIgnoreCase("1availability")
			) {
		    if (from != null && !from.equalsIgnoreCase("")) {
			int month = Integer.parseInt(Util.getMonthFromDate(from
				.trim()));
			int year = Integer.parseInt(Util.getYearFromDate(from
				.trim()));
			String monYear = Util.getMonthAndYearFromDate(from.trim());
			runTask.setParameterValue("Month", month);
			runTask.setParameterValue("Year", year);
			runTask.setParameterValue("MonthYear", monYear);
			log.info("Month : " + month);
			log.info("Year : " + year);
			log.info("MonthYear : " + monYear);
		    }
		}
		 if(reportType.equalsIgnoreCase(Constants.ABSENTEE_REPORT)) {	
			 runTask.setParameterValue("Vehicletypeid", vehicletypeid);
			 log.info("reg logger: " + vehicletypeid);
		 }
		 //if (operatorId != 0) {
		  runTask.setParameterValue("DepartmentId", DeptDataId);
			    runTask.setParameterValue("operatorId", operatorId);
			    runTask.setParameterValue("ShiftId", ShiftId);
			    runTask.setParameterValue("VehicleId", VehicleId);
			    runTask.setParameterValue("OperatorName", OperatorName);
			    runTask.setParameterValue("DepartmentName", DepartmentName);
			    runTask.setParameterValue("DriverName", DriverName);
			    runTask.setParameterValue("VehicleNumber", VehicleNumber);
			    runTask.setParameterValue("OperatorId", operatorId);
			    runTask.setParameterValue("RejectionStatus", RejectionStatus);
			    runTask.setParameterValue("DepartmentId", DeptId);
			    runTask.setParameterValue("EmpId",  empId);
			    runTask.setParameterValue("Id", Id);
			    
			    
			    runTask.setParameterValue("DeptDataId", DeptDataId);
			    runTask.setParameterValue("OperatorID", operatorId);
			    runTask.setParameterValue("depotid", depotid);
			    runTask.setParameterValue("MachineNo",  MachineNo);
			    runTask.setParameterValue("rowcount", rowcount);
			    runTask.setParameterValue("roworder", roworder);
			    runTask.setParameterValue("groupId", Group);
			    runTask.setParameterValue("groupId", groupid);
			    
			    runTask.setParameterValue("DesignationId", DesignationId);
			    runTask.setParameterValue("UOM", UOM);
			    runTask.setParameterValue("PhysicalStockQty", PhysicalStockQty);
			  	runTask.setParameterValue("SystemStockQty", SystemStockQty);
			  	runTask.setParameterValue("IndentNo", IndentNo);
			  	runTask.setParameterValue("PurchaseOrderNo", PurchaseOrderNo);
			  	runTask.setParameterValue("IssueNo", IssueNo);
			  	runTask.setParameterValue("RetReferenceNo", RetReferenceNo);
				runTask.setParameterValue("ExpiryDate", ExpiryDate);
				runTask.setParameterValue("Item", Item);
				runTask.setParameterValue("BusNo", BusNo);
				runTask.setParameterValue("IsFatality", IsFatality);
				runTask.setParameterValue("Frequency", Frequency);
				runTask.setParameterValue("Period", Period);
				runTask.setParameterValue("DepartmentId", DepartmentId);
				runTask.setParameterValue("OperatorIdDMS", OperatorIdDMS);
				
				runTask.setParameterValue("Draft", Draft);
				runTask.setParameterValue("CalledFrom", calledfrom);
				runTask.setParameterValue("CalledTo", calledto);
				runTask.setParameterValue("status", callstatus);
				
				runTask.setParameterValue("DeptZoneId", DeptZoneId);
			//}
		
		log.info("Creating Report document..... " + rptDocName);
		Random random = new Random();
		String tempFile = rptDocFile + "_" + System.currentTimeMillis() + "_" + random.nextFloat() + ".rptdocument";
		log.info("Creating temp file : " + tempFile);
		try {
			// START
			log.debug("---------------------------");
			log.debug(runTask.getParameterValues());
			log.debug("---------------------------");
			// END
		    runTask.run(tempFile);
		    log.info("Copying temp file ");
		    Util.copyFile(tempFile, rptDocName);
		    log.info("Reportdoc copied : " + rptDocName);
		    File ftempFileObj = new File(tempFile);
		    ftempFileObj.delete();
		} catch (Exception e) {
		    runTask.run(rptDocName);
		}
		log.info("RPTDOC creation completed ");
	    }
	    IReportDocument rptDoc = null;
	    try {
		rptDoc = birtReportEngine
			.openReportDocument(rptDocName);
	    } catch (Exception e) {
		log.error("Error while opening report document : " + e.getMessage());
		Random random = new Random();
		String destFile = rptDocFile + "_" + System.currentTimeMillis() + "_" + random.nextFloat() + ".rptdocument";
		log.info("Copying rptdocument : " + destFile);
		Util.copyFile(rptDocName, destFile);
		log.info("Using new rptdoc file...");
		rptDoc = birtReportEngine
			.openReportDocument(destFile);
	    }

	    // Create Render Task
	    IRenderTask renderTask = birtReportEngine.createRenderTask(rptDoc);
	    RenderOption option = null;

	    repFormat = repFormat.trim();
	    if (repFormat.equalsIgnoreCase("pdf")) {
		option = new RenderOption();
		option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition", "inline; filename=\""
			+ reportName + ".pdf\"");
		option.setOutputStream(resp.getOutputStream());
		renderTask.setRenderOption(option);
		log.info("PDF Rendering..");
	    } 
	    else if (repFormat.equalsIgnoreCase("xls")) {
		EXCELRenderOption excelOption = new EXCELRenderOption();
		resp.setContentType("application/vnd.ms-excel"); 
		excelOption.setOutputFormat("xls");
		excelOption.setEnableMultipleSheet(false);
		resp.setHeader("Content-Disposition", "inline; filename=\""
			+ reportName + ".xls\"");
		excelOption.setOutputStream(resp.getOutputStream());
		
		if (hasChart) {
		   // excelOption.setEmitterID("org.uguess.birt.report.engine.emitter.xls");
		    // //For charts (Tribix emitter - show in multiple sheets)
		    excelOption.setEmitterID("org.eclipse.birt.report.engine.emitter.nativexls"); // (Native
		    // emitter)supports
		    // both
		    // images/charts
		    // in
		    // sinle
		    // sheet
		} else {
		    // excelOption.setEmitterID("org.eclipse.birt.report.engine.emitter.prototype.excel");//Default
		    // Emitter, Do not support images/chart
		    excelOption
		    .setEmitterID("org.eclipse.birt.report.engine.emitter.nativexls");
		}
		renderTask.setRenderOption(excelOption);
		log.info("Excel rendering..");
	    } else if (repFormat.equalsIgnoreCase("doc")) {
		option = new RenderOption();
		option.setOutputFormat("doc");
		resp.setContentType("application/vnd.ms-word");
		resp.setHeader("Content-Disposition", "inline; filename=\""
			+ reportName + ".doc\"");
		option.setOutputStream(resp.getOutputStream());
		renderTask.setRenderOption(option);
		log.info("Word rendering..");
	    } else {
		HTMLRenderOption htmlOption = new HTMLRenderOption();
		resp.setContentType("text/html");
		htmlOption.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		htmlOption.setOutputStream(resp.getOutputStream());
		htmlOption.setActionHandler(new HTMLActionHandler());
		htmlOption.setImageHandler(new HTMLServerImageHandler());
		// htmlOption.setBaseImageURL(req.getContextPath()+"/Reports/Images");
		htmlOption.setBaseImageURL(req.getScheme() + "://"
			+ config.getValue("SERVER_IP") + req.getContextPath()
			+ "/Reports/Images");
		htmlOption.setImageDirectory(sc.getRealPath("/Reports/Images"));
		log.info("Image directory.."+htmlOption.getImageDirectory());
		// htmlOption.setEmbeddable(true);
		htmlOption.setHTMLIDNamespace("BPCLRPT");
		// htmlOption.setHTMLIndent(true);

		renderTask.setRenderOption(htmlOption);
		log.info("HTML rendering..");
	    }
	    renderTask.render();
	    renderTask.close();
	    log.info("Rendering completed...");
	    if(runTask != null){
		runTask.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("Exception occured while generating report : "
		    + e.getMessage());
	    try {
		if(rptDocName != null && !rptDocName.equals("")) {
		    File rptFile = new File(rptDocName);
		    if(rptFile.exists()) {
			rptFile.delete();
			log.info("Report Doc file deleted " + rptDocName);
		    }
		}
	    } catch (Exception ex) {
		// TODO: handle exception
	    }
	    resp.setContentType("text/html");
	    PrintWriter out =  resp.getWriter();
	    out.write("<html><body><center><h3>Report cannot be generated</h3></center></body></html>");
	    out.flush();
	    out.close();
	    return;
	    //throw new ServletException(e);
	}
	log.info("Report Generation Completed");
	log.info("**********************************************");
    }

    /**
     * Setting Database credentials.
     * 
     * @param design
     * @param dbUrl
     */
    private void setDbCredentials(IReportRunnable design, String dbUrl) {
	log.info("Setting DbCredentials");
	log.info("Setting DbCredentials="+dbUrl);
	DesignElementHandle deh = design.getDesignHandle();
	SlotHandle slotHandle = deh
		.getSlot(ReportDesignHandle.DATA_SOURCE_SLOT);
	Iterator<Object> iter = slotHandle.iterator();
	try {
	    while (iter.hasNext()) {
		Object obj = iter.next();
		if (obj instanceof OdaDataSourceHandle) {
		    OdaDataSourceHandle odaSrcHdl = (OdaDataSourceHandle) obj;
		    Iterator<PropertyHandle> propIter = odaSrcHdl
			    .getPropertyIterator();
		    while (propIter.hasNext()) {

			PropertyHandle propHdl = (PropertyHandle) propIter
				.next();
			if (propHdl.getPropertyDefn().getName()
				.equalsIgnoreCase("odaURL")) {
			    propHdl.setStringValue(dbUrl);
			} else if (propHdl.getPropertyDefn().getName()
				.equalsIgnoreCase("odaUser")) {
			    propHdl.setStringValue(config.getValue("DB_USER"));
			} else if (propHdl.getPropertyDefn().getName()
				.equalsIgnoreCase("odaPassword")) {
			    propHdl.setStringValue(config
				    .getValue("DB_PASSWORD"));
			}
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("Exception occured while setting DB credentials : "
		    + e.getMessage());
	}
	log.info("SetDbCredentials OK");
    }

    /**
     * The doPost method of the servlet.
     * 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	/*response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
	out.println("<HTML>");
	out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
	out.println("  <BODY>");
	out.println(" Post Not Supported");
	out.println("  </BODY>");
	out.println("</HTML>");
	out.flush();
	out.close();*/
	
	doGet(request, response);
    }
    
    /**
     * remove time from date time
     * 
     * @param date
     * @return
     */

    public Date removeTime(Date date) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }
    
   
}
