package com.qtpselenium.core.hybrid;

import java.text.ParseException;
import java.util.Hashtable;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.qtpselenium.core.hybrid.util.Constants;
import com.qtpselenium.core.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Keywords {

	ExtentTest test;
	AppKeywords app;
	
	public Keywords(ExtentTest test) {
		this.test=test;
	}

	public void executeKeywords(
			String testUnderExecution,
			Xls_Reader xls,
			Hashtable<String,String> testData) throws ParseException{
		/*
		GenericKeywords app = new GenericKeywords();
		app.openBrowser("Mozilla");
		app.navigate("url");
		app.click("gmailLink_xpath");
		app.input("email_id", "seleniumtraining10");
		app.click("nextButton_id");
		*/
		
		
		app = new AppKeywords(test);
		
		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		
		for(int rNum=2;rNum<=rows;rNum++){
			String tcid  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testUnderExecution)){
				String keyword  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String object  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String key  = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				String data = testData.get(key);
				test.log(LogStatus.INFO,tcid+" -- "+ keyword +" -- "+object+" --- "+data);
				String result="";
				if(keyword.equals("openBrowser"))
					result=app.openBrowser(data);
				else if(keyword.equals("navigate"))
					result=app.navigate(object);
				else if(keyword.equals("click"))
					result=app.click(object);
				else if(keyword.equals("moveMouseToClick"))
					result=app.moveMouseToClick(object);
				else if(keyword.equals("moveClick"))
					result=app.moveClick(object);
				else if(keyword.equals("input"))
					result=app.input(object,data);
				else if(keyword.equals("selectList"))
					result=app.dropListSelect(object,data);
				else if(keyword.equals("closeBrowser"))
					result=app.closeBrowser();
				else if(keyword.equals("verifyLoginDetails"))
					result=app.verifyLoginDetails(testData);
				else if(keyword.equals("verifyElementPresent"))
					result=app.verifyElementPresent(object);
				else if(keyword.equals("flipKartLogin"))
					result=app.flipKartLogin(testData);
				else if(keyword.equals("verifyFlipKartLogin"))
					result=app.verifyFlipKartLogin(testData.get("ExpectedResult"));
				else if(keyword.equals("verifyDBLogin"))
					result=app.verifyDBLogin(testData.get("ExpectedResult"));
				else if(keyword.equals("selectHotelDate"))
					result=app.selectHotelDate(data);
				else if(keyword.equals("selectFrDate"))
					result=app.selectFrDate(data);
				else if(keyword.equals("selectRtDate"))
					result=app.selectRtDate(data);
				else if(keyword.equals("defaultLogin"))
					result=app.defaultLogin();
				else if(keyword.equals("wait"))
					result=app.wait(key);
				else if(keyword.equals("scrollTo"))
					result=app.scrollTo(object);
				else if(keyword.equals("filterMobileAndValidate"))
					result=app.filterMobileAndValidate(testData);
				else if(keyword.equals("searchMobileAndSelect"))
					result=app.searchMobileAndSelect(testData.get(key));
				
				// central place reporting failure
				if(!result.equals(Constants.PASS)){
					app.reportFailure(result);
					Assert.fail(result);
				}
			}
		}
		
		}
	
	public AppKeywords getGenericKeyWords(){
		return app;
	}
	
	
}
