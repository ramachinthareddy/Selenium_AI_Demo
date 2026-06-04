package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    private WebDriver driver;

    private By addressField = By.id("address");
    private By cardNumber = By.id("cardNumber");
    private By expiry = By.id("expiry");
    private By cvv = By.id("cvv");
    private By placeOrderBtn = By.cssSelector("button.place-order");
    private By successMsg = By.cssSelector(".order-success");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterAddress(String address) {
        driver.findElement(addressField).clear();
        driver.findElement(addressField).sendKeys(address);
    }

    public void enterCard(String number, String exp, String cvvCode) {
        driver.findElement(cardNumber).clear();
        driver.findElement(cardNumber).sendKeys(number);
        driver.findElement(expiry).clear();
        driver.findElement(expiry).sendKeys(exp);
        driver.findElement(cvv).clear();
        driver.findElement(cvv).sendKeys(cvvCode);
    }

    public void placeOrder() {
        driver.findElement(placeOrderBtn).click();
    }

    public boolean isOrderSuccess() {
        try { return driver.findElement(successMsg).isDisplayed(); }
        catch (Exception e) { return false; }
    }
}
