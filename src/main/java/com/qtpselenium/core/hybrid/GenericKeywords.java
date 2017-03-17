package com.qtpselenium.core.hybrid;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.qtpselenium.core.hybrid.util.Constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GenericKeywords {
	public WebDriver driver;
	public Properties prop;
	ExtentTest test;
	
	public GenericKeywords(ExtentTest test){
		this.test=test;
		
		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//project.properties");
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String openBrowser(String browserType){
		test.log(LogStatus.INFO, "Opening Browser");
		if(prop.getProperty("grid").equals("Y")){
			DesiredCapabilities cap=null;
			if(browserType.equals("Mozilla")){
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(browserType.equals("Chrome")){
				 cap = DesiredCapabilities.chrome();
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{		
				if(browserType.equals("Mozilla")){
					System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver.exe");
					driver = new FirefoxDriver();
				}else if(browserType.equals("Chrome")){
					System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");
					driver =  new ChromeDriver();
				}else if(browserType.equals("ie")){
					System.setProperty("webdriver.ie.driver", "C:\\drivers\\IEDriverServer.exe");
					driver =  new InternetExplorerDriver();
				}
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return Constants.PASS;
	}
	
	public String navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to "+ prop.getProperty(urlKey));
		driver.get(prop.getProperty(urlKey));
		return Constants.PASS;
	}
	
	public String click(String locatorKey){
		test.log(LogStatus.INFO,"Clicking on "+ prop.getProperty(locatorKey));
		WebElement e = getElement(locatorKey);
		e.click();
		return Constants.PASS;
		
	}
	
	public String moveMouseToClick(String locatorKey){
		test.log(LogStatus.INFO," Move the Mouse to Click "+ prop.getProperty(locatorKey));
		String temp = prop.getProperty("accessAccount_xpath"); 
		WebElement e = getElement(locatorKey);
	     Actions act = new Actions(driver);       // define act to move the mouse
		 act.moveToElement(e).build().perform();  // unsupported Command for FireFox
		
		try {
			  
		  WebDriverWait wait = new WebDriverWait(driver,20);
		  wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(temp)));
	      WebElement k = getElement(temp);
		  k.click();
		  
		} catch (Exception g) {
			// TODO Auto-generated catch block
			return Constants.FAIL +" - Could not click on hidden element. "+ g.getMessage();	
		}

		return Constants.PASS;
		
	}
	
	public String moveClick(String locatorKey){
		test.log(LogStatus.INFO," Move the Mouse and Click "+ prop.getProperty(locatorKey));
						
		WebDriverWait wait = new WebDriverWait(driver,10);
		try {
	      wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(locatorKey)));
	      WebElement e = getElement(locatorKey);
		  e.click();
		} catch (Exception g) {
			// TODO Auto-generated catch block
			g.printStackTrace();
		}
		return Constants.PASS;
		
	}
	
	public String input(String locatorKey, String data){
		test.log(LogStatus.INFO,"Typing in "+ prop.getProperty(locatorKey));

		WebElement e = getElement(locatorKey);
		e.sendKeys(data);
		return Constants.PASS;
	}
	
	public String dropListSelect(String locatorKey, String data){
	 	  test.log(LogStatus.INFO,"Selecting for "+ prop.getProperty(locatorKey)+" - "+data );
					 
	 	    Select dropdown = new Select(driver.findElement(By.xpath(prop.getProperty(locatorKey))));
	 	    dropdown.selectByValue(data);
	 	    return Constants.PASS;
	
	}
		
	
	public String closeBrowser() {
		test.log(LogStatus.INFO,"Closing browser");
		driver.quit();
		return Constants.PASS;
		
	}
	
	/***************************Validation keywords*********************************/
	public String verifyText(String locatorKey,String expectedText){
		WebElement e = getElement(locatorKey);
		String actualText = e.getText();
		if(actualText.equals(expectedText))
			return Constants.PASS;
		else
			return Constants.FAIL;
	}
	
	public String verifyElementPresent(String locatorKey){
		boolean result= isElementPresent(locatorKey);
		if(result)
			return Constants.PASS;
		else
			return Constants.FAIL+" - Could not find Element "+ locatorKey;
	}
	
	public String verifyElementNotPresent(String locatorKey){
		boolean result= isElementPresent(locatorKey);
		if(!result)
			return Constants.PASS;
		else
			return Constants.FAIL+" - Could find Element "+ locatorKey;
	}
	
	public String scrollTo(String xpathKey){
		int y = getElement(xpathKey).getLocation().y;
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("window.scrollTo(0,"+(y-200)+")");
		return Constants.PASS; 
	}
	public String wait(String timeout) {

	try {
		Thread.sleep(Integer.parseInt(timeout));
	} catch (Exception  e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return Constants.PASS;
	}
	
	/************************Utility Functions********************************/
	public WebElement getElement(String locatorKey){
		WebElement e = null;
		try{
			if(locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if(locatorKey.endsWith("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		}catch(Exception ex){
			reportFailure("Failure in Element Extraction - "+ locatorKey);
			Assert.fail("Failure in Element Extraction - "+ locatorKey);
		}
		
		return e;

	}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> e = null;
		
		if(locatorKey.endsWith("_id"))
			e = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElements(By.name(prop.getProperty(locatorKey)));
		
		if(e.size()==0)
			return false;
		else 
			return true;
	}
	/******************************Reporting functions******************************/
		
	public void reportFailure(String failureMessage){
		takeScreenShot();
		test.log(LogStatus.FAIL,failureMessage);
	}
	public void takeScreenShot(){
		// decide name - time stamp
		Date d = new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ","_")+".png";
		String path=Constants.SCREENSHOT_PATH+screenshotFile;
		// take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// embed screenshot into the report
		test.log(LogStatus.INFO, test.addScreenCapture(path));
	}

	
}
