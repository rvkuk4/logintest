package com.logintest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginTest {

	private AppiumDriver<MobileElement> driver;

	@BeforeClass
    @org.testng.annotations.Parameters({"platformName", "platformVersion", "deviceName", "browserName"})
    public void setUp(String platformName, String platformVersion, String deviceName, String browserName) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("browserName", browserName);
        
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        
        if (platformName.equalsIgnoreCase("iOS")) {
            driver = new IOSDriver(url, capabilities);
            
        } else if (platformName.equalsIgnoreCase("Android")) {
            driver = new AndroidDriver(url, capabilities);
        }
    }

    @Test
    public void testLogin() {
        // Navigate to the login page
        driver.get("https://www.wwgoa.com/login");

        // Enter username
        MobileElement username = driver.findElement(By.name("username"));
        username.sendKeys("tester1@simplestream.com");
        Assert.assertEquals(username.getText(),"tester1@simplestream.com");

        // Enter password
        MobileElement password = driver.findElement(By.name("password"));
  	    password.sendKeys("TestLogin");
  	    Assert.assertEquals(password.getText(),"TestLogin");

        // Click the login button
        MobileElement loginButton = driver.findElement(By.xpath("//*[@text='Sign in']"));
        loginButton.click();

        // To Wait for the login process to complete
        try {
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        /* Handling errors during login */
	  	  if(username.getText() == null)  {
	  		  System.out.println("The username field is required.");
	  	  } else if(password.getText() == null) {
	  		  System.out.println("The password field is required.");
	  	  } else if(username.getText() == "tester1@simplestream.com" && password.getText() != "TestLogin") {
	  		  System.out.println("Error: The password you entered for the email address" + username.getText() + "is incorrect.");
	  	  } else if(username.getText() != "tester1@simplestream.com" && password.getText() != "TestLogin") {
	  		  System.out.println("Unknown email address. Check again or try your username.");
	  	  } else {
	  		  System.out.println("Unknown error occured during login, please try again");
	  	  }

        // Validate successful login
        MobileElement signoutButton = driver.findElement(By.xpath("//*[@text='Sign Out']"));
        assert signoutButton.isDisplayed() : "Login failed!";
        
        String expected_button_after_login = "Signout";
        try {
            Assert.assertEquals(signoutButton.getText(), expected_button_after_login);
        } catch (AssertionError e) {
            System.out.println("Login Not successful");
            throw e;
        }
        System.out.println("Login successful");
    }

	@AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
