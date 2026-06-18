package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// Page Object for Registration page. Keep locators maintainable and centralized.
public class RegistrationPage {
    private WebDriver driver;

    // NOTE: Update locators to match actual application from master branch.
    private By fullNameInput = By.id("fullName");
    private By emailInput = By.id("email");
    private By mobileInput = By.id("mobile");
    private By passwordInput = By.id("password");
    private By submitBtn = By.cssSelector("button[type='submit']");
    private By validationMessage = By.cssSelector(".validation-message");
    private By alreadyRegisteredMessage = By.cssSelector(".error-already-registered");
    private By passwordToggle = By.cssSelector(".password-toggle");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        // Base URL should come from config in real repo
        driver.get("https://example.com/register");
    }

    public void enterFullName(String name) {
        WebElement el = driver.findElement(fullNameInput);
        el.clear();
        el.sendKeys(name);
    }

    public void enterEmail(String email) {
        WebElement el = driver.findElement(emailInput);
        el.clear();
        el.sendKeys(email);
    }

    public void enterMobile(String mobile) {
        WebElement el = driver.findElement(mobileInput);
        el.clear();
        el.sendKeys(mobile);
    }

    public void enterPassword(String pwd) {
        WebElement el = driver.findElement(passwordInput);
        el.clear();
        el.sendKeys(pwd);
    }

    public void submit() {
        driver.findElement(submitBtn).click();
    }

    public String getValidationMessage() {
        try {
            return driver.findElement(validationMessage).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isAlreadyRegisteredShown() {
        try {
            return driver.findElement(alreadyRegisteredMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void togglePasswordVisibility() {
        driver.findElement(passwordToggle).click();
    }
}
