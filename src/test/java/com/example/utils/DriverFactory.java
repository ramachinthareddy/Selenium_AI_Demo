package com.example.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// Simple Driver factory. In real repo read config from properties/master branch config.
public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (tlDriver.get() == null) {
            // Basic Chrome options for headful debugging. Adapt with desired capabilities.
            System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            tlDriver.set(new ChromeDriver(options));
        }
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}
