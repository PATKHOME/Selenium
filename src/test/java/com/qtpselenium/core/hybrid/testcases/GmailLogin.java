package com.qtpselenium.core.hybrid.testcases;

import java.text.ParseException;
import java.util.Hashtable;

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

public class GmailLogin extends BaseTest {

	@BeforeTest
	public void init(){
		 xls = new Xls_Reader(Constants.TestDataSuite_XLS);
		 testName = "GmailTest";
	}

	@Test(dataProvider="getData")
	public void doLogin(Hashtable <String,String> data) throws ParseException{
		test = rep.startTest(testName);
		test.log(LogStatus.INFO, data.toString());
		
		if (DataUtil.isSkip(xls, testName) || data.get("Runmode").equals("N")) {
	      test.log(LogStatus.SKIP, "Skipping the test as Runmode is N");
	      throw new SkipException("Skipping the test as Runmode is N");
		}
	    test.log(LogStatus.INFO, "Starting Gmail Login Test");
		//Xls_Reader xls = new Xls_reader(Constants.SessionSuite_XLS);
		Keywords app = new Keywords(test);
		
		test.log(LogStatus.INFO, "Executing Keywords");
		app.executeKeywords(testName, xls, data);
		// add the screenshots
		//app.getGenericKeyWords().reportFailure("Cannot execute Keywords");
		
		test.log(LogStatus.PASS, "Test Passed");
		app.getGenericKeyWords().takeScreenShot();
						
	}
	
		
	@DataProvider
	public Object[][] getData(){
		return DataUtil.getData(xls, testName);
			
	}
	
}
