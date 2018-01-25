package com.arsse.smc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;



import com.arsse.smc.birtreport.WebReport;

/**
 * 
 * @author Lekshmijraj
 * 
 */
public class Util {

    private Logger log = Logger.getLogger(Util.class);

    /**
     * to extract the month from the given date of type string
     * 
     * @param StringDate
     * @return
     */
    public static String getMonthFromDate(String StringDate) {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String month = "";
	Date date = null;
	try {
	    date = format.parse(StringDate);
	    System.out.println(date);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (date != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("MM");
	    System.out.println("Month " + sdf.format(date));
	    month = sdf.format(date);
	}
	return month;

    }

    /**
     * to extract the year from the given date of type string
     * 
     * @param StringDate
     * @return
     */
    public static String getYearFromDate(String StringDate) {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String year = "";
	Date date = null;
	try {
	    date = format.parse(StringDate);
	    System.out.println(date);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (date != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	    System.out.println("Year " + sdf.format(date));
	    year = sdf.format(date);
	}
	return year;

    }
    
    /**
     * to extract the year from the given date of type string
     * 
     * @param StringDate
     * @return
     */
    public static String getDayFromDate(String StringDate) {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String day = "";
	Date date = null;
	try {
	    date = format.parse(StringDate);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (date != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("dd");
	    System.out.println("Day " + sdf.format(date));
	    day = sdf.format(date);
	}
	return day;

    }    

    /**
     * to extract the month and year from the given date of type string
     * 
     * @param StringDate
     * @return
     */
    public static String getMonthAndYearFromDate(String StringDate) {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String year = "";
	Date date = null;
	try {
	    date = format.parse(StringDate);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	if (date != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
	    System.out.println("Year " + sdf.format(date));
	    year = sdf.format(date);
	}
	return year;

    }

    public static int getDayFromDate(Date date) {

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	System.out.println(cal.get(Calendar.DATE));
	return cal.get(Calendar.DATE);
    }

    public static int getMonth(Date date) {

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return cal.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {

	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return cal.get(Calendar.YEAR);
    }

    public static Date buildDate(int day, Date date) {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String stringDate = "";
	int month = getMonth(date) + 1;
	int year = getYear(date);
	stringDate = year + "-" + month + "-" + day;
	Date buildDate = null;
	try {
	    buildDate = format.parse(stringDate);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return buildDate;
    }
    
    /**
     * Copy source file content to destination file.
     * 
     * @param srcFile
     * @param destFile
     */
    public static void copyFile(String srcFile, String destFile) {
	InputStream inStream = null;
	OutputStream outStream = null;
	try {
	    File sfile = new File(srcFile);
	    File dfile = new File(destFile);

	    inStream = new FileInputStream(sfile);
	    outStream = new FileOutputStream(dfile);

	    byte[] buffer = new byte[1024];

	    int length;
	    //copy the file content in bytes 
	    while ((length = inStream.read(buffer)) > 0){
		outStream.write(buffer, 0, length);
	    }

	    inStream.close();
	    outStream.close();

	} catch(IOException e){
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	/*
	 * Util.getMonthFromDate("2012-02-01");
	 * Util.getYearFromDate("2012-05-01");
	 * Util.getMonthAndYearFromDate("2012-02-01");
	 */
	//buildDate(1, new Date());

	//copyFile("C:/BPCLReports/RptDocs/Performance/PerformanceReport_AllIndia_1_0_null_null_null_null_null_null_null_2012-07-09_00_01_2012-07-05_23_59_null_null_full.rptdocument",
	//	"C:/BPCLReports/RptDocs/Performance/PerformanceReport_AllIndia_1_0_null_null_null_null_null_null_null_2012-07-09_00_01_2012-07-05_23_59_null_null_full_1234.rptdocument");
    }

}
