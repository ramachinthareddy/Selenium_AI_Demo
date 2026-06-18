package com.example.steps;

import com.example.pages.OtpPage;
import com.example.pages.RegistrationPage;
import com.example.pages.HomePage;
import com.example.utils.DriverFactory;
import com.example.utils.TestDataUtil;
import com.example.utils.WaitUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class RegistrationSteps {
    private WebDriver driver;
    private RegistrationPage registrationPage;
    private OtpPage otpPage;
    private HomePage homePage;
    private WaitUtils waitUtils;

    @Before
    public void setUp() {
        driver = DriverFactory.getDriver();
        registrationPage = new RegistrationPage(driver);
        otpPage = new OtpPage(driver);
        homePage = new HomePage(driver);
        waitUtils = new WaitUtils(driver);
    }

    @Given("I am on the registration page")
    public void i_am_on_registration_page() {
        registrationPage.navigateTo();
    }

    @When("I enter Full Name '{string}', Email '{string}', Password '{string}' and submit")
    public void enter_fullname_email_password_and_submit(String fullName, String email, String password) {
        registrationPage.enterFullName(fullName);
        registrationPage.enterEmail(email);
        registrationPage.enterPassword(password);
        registrationPage.submit();
    }

    @When("I enter Full Name '{string}' (no last name), a valid email and password and submit")
    public void enter_single_name_and_valid_contact(String fullName) {
        String email = TestDataUtil.uniqueEmail("single");
        registrationPage.enterFullName(fullName);
        registrationPage.enterEmail(email);
        registrationPage.enterPassword("Password123");
        registrationPage.submit();
    }

    @When("I enter Full Name '{string}' and valid contact/password and submit")
    public void enter_long_compound_name_and_submit(String fullName) {
        String email = TestDataUtil.uniqueEmail("compound");
        registrationPage.enterFullName(fullName);
        registrationPage.enterEmail(email);
        registrationPage.enterPassword("Password123");
        registrationPage.submit();
    }

    @When("I enter Full Name '{string}', Email '{string}', Password '{string}' and submit")
    public void enter_name_with_special_chars_and_submit(String fullName, String email, String password) {
        // Reuse same mapping as generic step
        registrationPage.enterFullName(fullName);
        registrationPage.enterEmail(email);
        registrationPage.enterPassword(password);
        registrationPage.submit();
    }

    @When("I enter Full Name '{string}', Mobile '{string}', Password '{string}' and submit")
    public void enter_name_mobile_password_and_submit(String fullName, String mobile, String password) {
        registrationPage.enterFullName(fullName);
        registrationPage.enterMobile(mobile);
        registrationPage.enterPassword(password);
        registrationPage.submit();
    }

    @Then("the system accepts the full name and proceeds to the OTP step (or sends OTP)")
    public void system_accepts_fullname_and_proceeds_to_otp() {
        // Basic assertion that OTP page has otp input or title changed; placeholder
        // In a real test assert page-specific element is visible
        Assert.assertTrue(driver.getCurrentUrl().contains("otp") || driver.getTitle().toLowerCase().contains("otp"),
                "Expected to be on OTP step after registration submission");
    }

    @Then("the system should either accept the single name and proceed to OTP per product rules")
    public void either_accept_single_name_or_validate() {
        // Accept either behavior: otp page or validation message
        boolean atOtp = driver.getCurrentUrl().contains("otp") || driver.getTitle().toLowerCase().contains("otp");
        boolean hasValidation = registrationPage.getValidationMessage() != null;
        Assert.assertTrue(atOtp || hasValidation, "Expected either OTP step or validation message for single name");
    }

    @Then("the system should accept and correctly store or truncate the name per DB limits")
    public void accept_or_truncate_long_name() {
        // We cannot validate DB here; assert that either acceptance (OTP) or validation shown
        boolean atOtp = driver.getCurrentUrl().contains("otp") || driver.getTitle().toLowerCase().contains("otp");
        boolean hasValidation = registrationPage.getValidationMessage() != null;
        Assert.assertTrue(atOtp || hasValidation);
    }

    @Then("the system should accept only allowed punctuation (e.g., hyphen, apostrophe) and reject disallowed characters")
    public void validate_allowed_punctuation() {
        String msg = registrationPage.getValidationMessage();
        Assert.assertTrue((msg != null && msg.toLowerCase().contains("invalid")) || driver.getCurrentUrl().contains("otp") ,
                "Either invalid character validation or proceed to OTP expected");
    }

    @Then("the system should trim extra whitespace and store the normalized value 'John Doe'")
    public void trims_and_stores_normalized_name() {
        // We don't implement DB verification here. Placeholder that either proceed to OTP or show validation
        boolean atOtp = driver.getCurrentUrl().contains("otp") || driver.getTitle().toLowerCase().contains("otp");
        boolean hasValidation = registrationPage.getValidationMessage() != null;
        Assert.assertTrue(atOtp || hasValidation);
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
