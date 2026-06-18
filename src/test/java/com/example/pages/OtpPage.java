package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OtpPage {
    private WebDriver driver;

    private By otpInput = By.id("otp");
    private By submitBtn = By.cssSelector("button.otp-submit");
    private By resendBtn = By.cssSelector("button.resend-otp");
    private By errorMsg = By.cssSelector(".otp-error");

    public OtpPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterOtp(String code) {
        driver.findElement(otpInput).clear();
        driver.findElement(otpInput).sendKeys(code);
    }

    public void submitOtp() {
        driver.findElement(submitBtn).click();
    }

    public void resendOtp() {
        driver.findElement(resendBtn).click();
    }

    public String getErrorMessage() {
        try { return driver.findElement(errorMsg).getText(); }
        catch (Exception e) { return null; }
    }
}
