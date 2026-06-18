package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;
    private By welcomeBanner = By.cssSelector(".welcome-banner");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isAtHomePage() {
        try {
            return driver.findElement(welcomeBanner).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
