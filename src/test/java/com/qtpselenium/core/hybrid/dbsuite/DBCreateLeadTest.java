package com.qtpselenium.core.hybrid.dbsuite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qtpselenium.core.hybrid.Keywords;
import com.qtpselenium.core.hybrid.base.BaseTest;
import com.qtpselenium.core.hybrid.util.Constants;
import com.qtpselenium.core.hybrid.util.DataUtil;
import com.qtpselenium.core.hybrid.util.ExtentManager;
import com.qtpselenium.core.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DBCreateLeadTest extends BaseTest{
	
	  String url = "jdbc:mysql://localhost:3306/";
	  String dbDriver = "com.mysql.jdbc.Driver";
	  String dbName = "lead";
	  
	  String userName = "root"; 
	  String password = "root"; 
	
	  Connection conn = null;
	  PreparedStatement pstmt=null;
	  ResultSet rs = null;
	 //WebDriver driver = null;
	
	@BeforeTest
	public void init(){
		xls = new Xls_Reader(Constants.DBSuite_XLS);
		testName = "DBCreateLeadTest";
		
		try {
			Class.forName(dbDriver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			System.out.println("Could not establish connection");
			e.printStackTrace();
			//Assert.fail("Could not establish connection");
			throw new SkipException("Could not establish connection");
		}// create object of Driver
	}
	
		
	@AfterTest
	public void disConnect(){
		try {
			if(rs!=null)
				rs.close();
			
			if(pstmt!=null)
				pstmt.close();
			
			
			if((conn!=null) && (!conn.isClosed()))
				  conn.close();
				 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test(dataProvider="getData")
	public void loginTest(Hashtable<String,String> data) throws ParseException{
		
		test = rep.startTest(testName);
		test.log(LogStatus.INFO, data.toString());
		
		if(DataUtil.isSkip(xls, testName) || data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		test.log(LogStatus.INFO, "Starting "+testName);
		
		app = new Keywords(test);
		test.log(LogStatus.INFO, "Executing keywords");
		app.executeKeywords(testName, xls,data);
						
		// validate if lead is generated from fe
		// connect to db and validate lead
			try {
				PreparedStatement pstmt = conn.prepareStatement("select * from lead order by LD_ID desc");
				ResultSet rs =pstmt.executeQuery();
			    	if(rs.next()){
						String dbFirstName=rs.getString("LD_FIRST_NAME");
						String dbLastName=rs.getString("LD_LAST_NAME");
						
						Assert.assertEquals(data.get("Firstname"), dbFirstName);
						Assert.assertEquals(data.get("Lastname"), dbLastName);
					}
			} catch (SQLException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		test.log(LogStatus.PASS, "PASS");
		app.getGenericKeyWords().takeScreenShot();
	}
	
	
}

