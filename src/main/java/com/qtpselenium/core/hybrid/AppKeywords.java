package com.qtpselenium.core.hybrid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.qtpselenium.core.hybrid.util.Constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AppKeywords extends GenericKeywords{

		
	public AppKeywords(ExtentTest test) {
		super(test);
	}

	public String verifyLoginDetails(Hashtable<String, String> testData){
		//name
		String expectedName=testData.get("Name");
		//email
		String expectedID=testData.get("Username");
		// actual
		String ActualName = getElement("gprofile_xpath").getText();
		String ActualID = getElement("gmiddis_xpath").getText();
		
		if (ActualName.equals(expectedName)) 
	      return Constants.PASS;
		else if (ActualID.equals(expectedID))  
		  return Constants.PASS;
		else
		  return Constants.FAIL+" -- verifyLoginDetail Fail";
	}
	
	public String login(String username,String password){
		
		getElement("loginLink_xpath").click();
		getElement("userName_xpath").sendKeys(username);
		getElement("password_xpath").sendKeys(password);
		getElement("loginButton_xpath").click();
		return Constants.PASS;
		
	}

	public String flipKartLogin(Hashtable<String, String> testData) {
		test.log(LogStatus.INFO, "Logging in with "+ testData.get("Username")+"/"+testData.get("Password"));
		return login(testData.get("Username"),testData.get("Password"));
	}
	
	public String defaultLogin(){
		return login(prop.getProperty("username"),prop.getProperty("password"));
		
	}

	public String verifyFlipKartLogin(String ExpectedResult) {
		test.log(LogStatus.INFO, "Validating login");
		String Text = verifyText("myAccount_xpath", ExpectedResult);
		if (!Text.equals("PASS")) {
			return "Failed - "+ "Got actual result as "+Text;
		}
		
		return Constants.PASS;
	}

	public String verifyDBLogin(String ExpectedResult) {
		test.log(LogStatus.INFO, "Validating DB login");
		String Text = verifyText("dblogout_xpath", ExpectedResult);
		if (!Text.equals("PASS")) {
			return "Failed - "+ "Got actual result as "+Text;
		}
		
		return Constants.PASS;
	}
	
	public String selectHotelDate(String date) throws ParseException{
		test.log(LogStatus.INFO, "Select Date for Hotel");
		SimpleDateFormat df =  new SimpleDateFormat("MM/dd/yyyy");
		Date dateToBeSelected = df.parse(date);
		Date currentDate = new Date();
		String monthYearDisplayed=getElement("hmonthyear_xpath").getText();
		System.out.println("From Webpage --> "+monthYearDisplayed);
		String  month =  new SimpleDateFormat("MMMM").format(dateToBeSelected);
		String year = new SimpleDateFormat("yyyy").format(dateToBeSelected);
		String day = new SimpleDateFormat("d").format(dateToBeSelected);
		String monthYearToBeSelected=month+" "+year;
		System.out.println("Desired --> "+monthYearToBeSelected);
		
		while(true){
				if(monthYearToBeSelected.equals(monthYearDisplayed)){
					//select date
					driver.findElement(By.xpath("//a[text()='"+day+"']")).click();
					System.out.println("Hotel Date --> Found and selected");
					break;
					
				}else{ // navigate to correct month and year
					
					if(dateToBeSelected.after(currentDate)){
						getElement("hnextmonth_xpath").click();
					}else{
						getElement("hprevmonth_xpath").click();
					}
				
				}
				
				 monthYearDisplayed=getElement("hmonthyear_xpath").getText();
		}
		return Constants.PASS;
	}

	
	public String selectFrDate(String date) throws ParseException{
		test.log(LogStatus.INFO, "Select <From> Date for Flight");
		SimpleDateFormat df =  new SimpleDateFormat("MM/dd/yyyy");
		Date dateToBeSelected = df.parse(date);
		Date currentDate = new Date();
		String monthYearDisplayed=getElement("fmonthyear_xpath").getText();
		System.out.println("From Webpage --> "+monthYearDisplayed);
		String  month =  new SimpleDateFormat("MMMM").format(dateToBeSelected);
		String year = new SimpleDateFormat("yyyy").format(dateToBeSelected);
		String day = new SimpleDateFormat("d").format(dateToBeSelected);
		String monthYearToBeSelected=month+" "+year;
		System.out.println("Desired --> "+monthYearToBeSelected);
		
		while(true){
				if(monthYearToBeSelected.equals(monthYearDisplayed)){
					//select date
					driver.findElement(By.xpath("//a[text()='"+day+"']")).click();
					System.out.println("From Date --> Found and selected");
					break;
					
				}else{ // navigate to correct month and year
					
					if(dateToBeSelected.after(currentDate)){
						getElement("fnextmonth_xpath").click();
					}else{
						getElement("fprevmonth_xpath").click();
					}
				
				}
				
				 monthYearDisplayed=getElement("fmonthyear_xpath").getText();
		}
		return Constants.PASS;
	}

	public String selectRtDate(String date) throws ParseException{
		test.log(LogStatus.INFO, "Select <Return> Date for Flight");
		SimpleDateFormat df =  new SimpleDateFormat("MM/dd/yyyy");
		Date dateToBeSelected = df.parse(date);
		Date currentDate = new Date();
		String monthYearDisplayed=getElement("rmonthyear_xpath").getText();
		System.out.println("From Webpage --> "+monthYearDisplayed);
		String  month =  new SimpleDateFormat("MMMM").format(dateToBeSelected);
		String year = new SimpleDateFormat("yyyy").format(dateToBeSelected);
		String day = new SimpleDateFormat("d").format(dateToBeSelected);
		String monthYearToBeSelected=month+" "+year;
		System.out.println("Desired --> "+monthYearToBeSelected);
		
		while(true){
				if(monthYearToBeSelected.equals(monthYearDisplayed)){
					//select date
					System.out.println("Day is --> "+day);
					driver.findElement(By.xpath("//a[text()='"+day+"']")).click();
					System.out.println("Return Date --> Found and selected");
					break;
					
				}else{ // navigate to correct month and year
					
					if(dateToBeSelected.after(currentDate)){
						getElement("rnextmonth_xpath").click();
					}else{
						getElement("rprevmonth_xpath").click();
					}
				
				}
				
				 monthYearDisplayed=getElement("rmonthyear_xpath").getText();
		}
		return Constants.PASS;
	}
	
	public String filterMobileAndValidate(Hashtable<String, String> testData) {
		String brandNameXPATH = testData.get("MobileCompany");
		//driver.findElement(By.xpath("//a[@title='"+brandName+"']")).click();;
		getElement(brandNameXPATH).click();
		//validate names
		
/*		for(int j=1 ; j<=5 ; j++){
			List<WebElement> mobiles = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
	    	System.out.print("Page -> "+j);
	    	System.out.println(" Total mobiles -> "+ mobiles.size());
	    	
		    for(int i=1 ; i< mobiles.size(); i++){
			 String ListName = driver.findElement(By.xpath("//*[@id='container']/div/div[2]/div[2]/div/div[2]/div[3]/div[1]/div["+i+"]/a/div[2]/div[1]/div[1]")).getText();
		     System.out.println(i+") "+ListName);
		      if(!mobiles.get(i).getText().contains("SAMSUNG")){
					return Constants.FAIL + " -  Found the mobile- " +mobiles.get(i).getText() ;
			  }
		    } 
		    if (j < 5) {
		      scrollTo("//*[@id='container']/div/div[2]/div[2]/div/div[2]/div[3]/div[1]/div[8]/a/div[2]/div[1]/div[1]");  
		      wait("5000");
		    }
		}
		
*/		
		dropListSelect("lowPrice_xpath","5000");
		wait("5000");
		dropListSelect("highPrice_xpath","10000");
		wait("5000");
		
		List<WebElement> mobiles = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
		System.out.println("The # of mobile Phones -- "+mobiles.size());
		for(int i=0;i<mobiles.size();i++){
			System.out.println(mobiles.get(i).getText());
			if(!mobiles.get(i).getText().contains("SAMSUNG")){
				return Constants.FAIL + " -  Found the mobile- " +mobiles.get(i).getText() ;

			}
		}
				
		// validate prices 
		//Rs. 5001 - Rs. 10000
		String priceRange = testData.get("PriceRange");
		String temp[] = priceRange.split(" ");
		int lowerCost = Integer.parseInt(temp[1]);
		int upperCost = Integer.parseInt(temp[4]);
		// extract all prices and check
		List<WebElement> prices = driver.findElements(By.xpath(prop.getProperty("allPrices_xpath")));
		System.out.println(" Total allPrices -> "+ prices.size());
		for(int i=0;i<prices.size();i++){
			String price=prices.get(i).getText() ;
			//System.out.println(i+") "+price);
			String temp2 = price.substring(1,price.length());
			//temp2 = price.split("\\d");
			price = temp2.replace(",", "");
  		    System.out.println(i+") "+price);
			int prodPrice = Integer.parseInt(price);
			if(prodPrice>upperCost || prodPrice<lowerCost)
				return Constants.FAIL + " Price incorrect " +prodPrice ;
			
		}
					
		return Constants.PASS;
}
	
	public String searchMobileAndSelect(String itemName) {
		boolean found=false;
		List<WebElement> mobilesBeforeScroll =null;
		List<WebElement> mobilesAfterScroll =null;
		int index=-1;
		while(!found){
			mobilesBeforeScroll = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
			int y_last=mobilesBeforeScroll.get(mobilesBeforeScroll.size()-1).getLocation().y;
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("window.scrollTo(0,"+y_last+")");
			wait("3000");
			
			 mobilesAfterScroll = driver.findElements(By.xpath(prop.getProperty("allMobiles_xpath")));
			if(mobilesAfterScroll.size() == mobilesBeforeScroll.size()){
				return "FAIL - Product not found";
			}
			
			for(int i=0;i<mobilesBeforeScroll.size();i++){
				if(mobilesBeforeScroll.get(i).getText().startsWith(itemName)){
					//found
					index=i;
					System.out.println(mobilesBeforeScroll.get(i).getText());
					found=true;
				}
			}

		}
		
		// found
		int y=mobilesBeforeScroll.get(index).getLocation().y;
		JavascriptExecutor js = (JavascriptExecutor)driver;

		js.executeScript("window.scrollTo(0,"+(y-200)+")");
		mobilesBeforeScroll.get(index).click();
		String itemText = getElement("prodHeading_xpath").getText();
		
		if(!itemText.startsWith(itemName))
			return "Fail - item name did not match " + itemText;
		
		getElement("addToCart_xpath").click();
		
		return Constants.PASS;
	}

	
	
	
}
