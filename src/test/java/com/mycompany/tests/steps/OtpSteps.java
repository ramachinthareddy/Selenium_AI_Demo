package com.mycompany.tests.steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.mycompany.tests.pages.OtpVerificationPage;
import com.mycompany.tests.utils.DriverFactory;
import com.mycompany.tests.utils.SmsSimulator;
import com.mycompany.tests.utils.EmailTestInbox;
import com.mycompany.tests.utils.TestUserStore;

public class OtpSteps {

    private WebDriver driver;
    private OtpVerificationPage otpPage;
    private SmsSimulator smsSimulator;
    private EmailTestInbox emailInbox;
    private TestUserStore userStore;

    public OtpSteps() {
        this.driver = DriverFactory.getDriver();
        this.otpPage = new OtpVerificationPage(driver);
        this.smsSimulator = new SmsSimulator();
        this.emailInbox = new EmailTestInbox();
        this.userStore = new TestUserStore();
    }

    @Given("the user is on the OTP verification page")
    public void user_on_otp_page() {
        DriverFactory.navigateTo("/verify-otp");
        otpPage.waitForPageLoad();
    }

    @When("the user submits a valid OTP")
    public void user_submits_valid_otp() {
        String otp = userStore.getLastOtp();
        otpPage.enterOtp(otp);
        otpPage.submit();
    }

    @When("the user submits an invalid OTP")
    public void user_submits_invalid_otp() {
        otpPage.enterOtp("000000");
        otpPage.submit();
    }

    @Then("the user should be successfully verified and redirected to the dashboard")
    public void user_verified_and_redirected() {
        boolean verified = otpPage.isVerified();
        Assert.assertTrue(verified, "Expected user to be verified");
        Assert.assertTrue(DriverFactory.getCurrentUrl().contains("/dashboard"), "Expected redirect to dashboard");
    }

    @Then("the system should display an OTP invalid error")
    public void otp_invalid_error() {
        String msg = otpPage.getOtpErrorMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("invalid"), "Expected invalid OTP message");
    }

    @When("the user requests a resend of the OTP")
    public void user_requests_otp_resend() {
        otpPage.clickResend();
    }

    @Then("a new OTP should be generated and sent to the user via email or SMS")
    public void new_otp_sent() {
        String contact = userStore.getLastContact();
        String newOtp = null;
        if (contact.contains("@")) {
            newOtp = emailInbox.waitForOtpFor(contact, 30);
        } else {
            newOtp = smsSimulator.waitForOtpFor(contact, 30);
        }
        Assert.assertNotNull(newOtp, "Expected new OTP to be received");
        Assert.assertNotEquals(newOtp, userStore.getLastOtp(), "Expected a different OTP to have been generated");
        userStore.setLastOtp(newOtp);
    }

    @When("the user submits OTP more than the allowed attempts")
    public void user_submits_otp_too_many_times() {
        for (int i = 0; i < otpPage.getMaxAttempts() + 1; i++) {
            otpPage.enterOtp("111111");
            otpPage.submit();
        }
    }

    @Then("the account should be locked from verifying for a cooldown period and an explanatory message displayed")
    public void account_locked_after_too_many_attempts() {
        Assert.assertTrue(otpPage.isLocked(), "Expected account to be locked");
        String msg = otpPage.getLockMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("locked"), "Expected lock explanatory message");
    }

    @Then("the user should still be able to request a new OTP after the cooldown")
    public void user_can_request_new_otp_after_cooldown() {
        otpPage.waitForCooldownToExpire();
        otpPage.clickResend();
        String contact = userStore.getLastContact();
        String otp = contact.contains("@") ? emailInbox.waitForOtpFor(contact, 30) : smsSimulator.waitForOtpFor(contact, 30);
        Assert.assertNotNull(otp, "Expected OTP after cooldown");
    }

}
