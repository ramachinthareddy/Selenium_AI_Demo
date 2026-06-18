package com.mycompany.tests.steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.mycompany.tests.pages.RegistrationPage;
import com.mycompany.tests.pages.OtpVerificationPage;
import com.mycompany.tests.utils.DriverFactory;
import com.mycompany.tests.utils.EmailTestInbox;
import com.mycompany.tests.utils.SmsSimulator;
import com.mycompany.tests.utils.TestUserStore;

/**
 * Step definitions for user registration related scenarios.
 * NOTE: Page objects and helper classes referenced here must be implemented in your repo.
 */
public class RegistrationSteps {

    private WebDriver driver;
    private RegistrationPage registrationPage;
    private OtpVerificationPage otpPage;
    private EmailTestInbox emailInbox;
    private SmsSimulator smsSimulator;
    private TestUserStore userStore;

    public RegistrationSteps() {
        this.driver = DriverFactory.getDriver();
        this.registrationPage = new RegistrationPage(driver);
        this.otpPage = new OtpVerificationPage(driver);
        this.emailInbox = new EmailTestInbox(); // test harness helper to capture emails
        this.smsSimulator = new SmsSimulator(); // test harness helper for SMS capture
        this.userStore = new TestUserStore(); // simple in-memory store used by tests if needed
    }

    @Given("a new, unauthenticated user is on the registration page and the test email inbox is monitored")
    public void user_on_registration_page_with_monitored_email() {
        DriverFactory.navigateTo("/register");
        registrationPage.waitForPageLoad();
        emailInbox.startMonitoring();
    }

    @Given("a new, unauthenticated user is on the registration page and the test SMS simulator is available")
    public void user_on_registration_page_with_sms_simulator() {
        DriverFactory.navigateTo("/register");
        registrationPage.waitForPageLoad();
        smsSimulator.ensureAvailable();
    }

    @Given("a new user is on the registration page")
    public void new_user_on_registration_page() {
        DriverFactory.navigateTo("/register");
        registrationPage.waitForPageLoad();
    }

    @Given("a user is creating an account on the registration page")
    public void user_creating_account_on_registration_page() {
        DriverFactory.navigateTo("/register");
        registrationPage.waitForPageLoad();
    }

    @Given("a user is on the registration page and the acceptance criteria require both first and last name")
    public void user_on_registration_fullname_required() {
        DriverFactory.navigateTo("/register");
        registrationPage.waitForPageLoad();
        registrationPage.setRequireFullName(true); // optional test config hook
    }

    @Given("there is an existing account associated with the test contact (for example {string})")
    public void existing_account_associated_with_contact(String contact) {
        // Create or ensure an account exists via test API client or DB fixture
        userStore.ensureUserExists(contact);
    }

    @When("the user enters {string} as first name, {string} as last name, {string} as email and {string} as password and submits the registration form")
    public void user_registers_with_email(String firstName, String lastName, String email, String password) {
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterEmail(email);
        registrationPage.enterPassword(password);
        registrationPage.submit();
        userStore.setLastContact(email);
    }

    @When("the user enters {string} as first name, {string} as last name, {string} as mobile and {string} as password and submits the registration form")
    public void user_registers_with_mobile(String firstName, String lastName, String mobile, String password) {
        registrationPage.enterFirstName(firstName);
        registrationPage.enterLastName(lastName);
        registrationPage.enterMobile(mobile);
        registrationPage.enterPassword(password);
        registrationPage.submit();
        userStore.setLastContact(mobile);
    }

    @When("the user submits the registration form with blank or missing first name and/or last name but with a valid email and password")
    public void user_submits_with_missing_names() {
        registrationPage.clearFirstName();
        registrationPage.clearLastName();
        registrationPage.enterEmail("valid+missingname@example.com");
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @When("the user submits the registration form without entering an email or a mobile number")
    public void user_submits_without_contact() {
        registrationPage.enterFirstName("Test");
        registrationPage.enterLastName("User");
        registrationPage.clearEmail();
        registrationPage.clearMobile();
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @When("the user enters an invalid email format (for example {string}) and submits the registration form")
    public void user_enters_invalid_email(String badEmail) {
        registrationPage.enterFirstName("Bob");
        registrationPage.enterLastName("Marley");
        registrationPage.enterEmail(badEmail);
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @When("the user enters a mobile number with invalid characters/format (for example {string}) and submits the registration form")
    public void user_enters_invalid_mobile(String badMobile) {
        registrationPage.enterFirstName("Test");
        registrationPage.enterLastName("User");
        registrationPage.enterMobile(badMobile);
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @When("the user enters a password with length less than 6 characters (for example {string}) and submits the registration form")
    public void user_enters_short_password(String shortPassword) {
        registrationPage.enterFirstName("Short");
        registrationPage.enterLastName("Pwd");
        registrationPage.enterEmail("shortpwd@example.com");
        registrationPage.enterPassword(shortPassword);
        registrationPage.submit();
    }

    @When("the user enters a password exactly 6 characters long that meets any other password requirements (for example {string}) and submits the registration form")
    public void user_enters_exact_six(String exactSix) {
        registrationPage.enterFirstName("Exact");
        registrationPage.enterLastName("Six");
        registrationPage.enterEmail("exactsix@example.com");
        registrationPage.enterPassword(exactSix);
        registrationPage.submit();
        userStore.setLastContact("exactsix@example.com");
    }

    @When("the user provides only a single-word name token (for example {string}) and submits the registration form")
    public void user_provides_single_word_name(String singleName) {
        registrationPage.enterFirstName(singleName);
        registrationPage.clearLastName();
        registrationPage.enterEmail("single@example.com");
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @When("the user enters an unusually long but valid first and last name (for example a combined length up to the system's allowed limit) and submits the registration form")
    public void user_enters_very_long_name() {
        String longFirst = new String(new char[100]).replace('\0', 'A');
        String longLast = new String(new char[100]).replace('\0', 'B');
        registrationPage.enterFirstName(longFirst);
        registrationPage.enterLastName(longLast);
        registrationPage.enterEmail("longname@example.com");
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @Then("an account should be created in a pending-verification state")
    public void account_created_pending_verification() {
        boolean isPending = registrationPage.isRegistrationPending(); // or query API
        Assert.assertTrue(isPending, "Expected account to be pending verification");
    }

    @Then("an OTP should be generated and sent to the monitored test email")
    public void otp_sent_to_test_email() {
        String contact = userStore.getLastContact();
        String otp = emailInbox.waitForOtpFor(contact, 30); // waits up to 30s
        Assert.assertNotNull(otp, "Expected OTP mail to be received");
        userStore.setLastOtp(otp);
    }

    @Then("an OTP should be generated and sent to the provided mobile number via the SMS simulator")
    public void otp_sent_to_mobile() {
        String contact = userStore.getLastContact();
        String otp = smsSimulator.waitForOtpFor(contact, 30);
        Assert.assertNotNull(otp, "Expected OTP SMS to be received");
        userStore.setLastOtp(otp);
    }

    @Then("the system should display a name-required validation error")
    public void name_required_validation_error() {
        String msg = registrationPage.getNameValidationMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("required"), "Expected name required validation");
    }

    @Then("the system should prevent account creation")
    public void prevent_account_creation() {
        boolean created = registrationPage.isAccountCreated();
        Assert.assertFalse(created, "Account should not be created");
    }

    @Then("the system should display a validation error requiring at least one contact (email or mobile)")
    public void contact_required_validation_error() {
        String msg = registrationPage.getContactValidationMessage();
        Assert.assertTrue(msg != null && (msg.toLowerCase().contains("email") || msg.toLowerCase().contains("mobile")),
                          "Expected contact required validation");
    }

    @Then("the system should show an invalid-email format error")
    public void invalid_email_format_error() {
        String msg = registrationPage.getEmailValidationMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("invalid"), "Expected invalid email message");
    }

    @Then("the system should show a mobile-format validation error")
    public void mobile_format_validation_error() {
        String msg = registrationPage.getMobileValidationMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("format"), "Expected mobile format message");
    }

    @Then("the system should reject the password and display a \"minimum 6 characters\" validation error")
    public void password_min_length_error() {
        String msg = registrationPage.getPasswordValidationMessage();
        Assert.assertTrue(msg != null && msg.toLowerCase().contains("minimum") && msg.contains("6"),
                          "Expected minimum 6 characters validation");
    }

    @Then("the system should accept the password")
    public void system_accepts_password() {
        Assert.assertFalse(registrationPage.isPasswordRejected(), "Password should be accepted");
    }

    @Then("if the name length is within allowed limits the system should accept the name and create an account pending OTP verification")
    public void name_length_within_limits_accept() {
        boolean accepted = registrationPage.isRegistrationPending();
        // If the registration page reports pending, test passes for accept path
        Assert.assertTrue(accepted, "Expected accepted name and pending verification");
    }

    @Then("if the name length exceeds allowed limits the system should show a clear length-limit error and prevent account creation")
    public void name_length_exceed_error() {
        String msg = registrationPage.getNameLengthValidationMessage();
        if (msg != null && !msg.isEmpty()) {
            Assert.assertTrue(msg.toLowerCase().contains("limit") || msg.toLowerCase().contains("maximum"),
                              "Expected name length limit validation");
        } else {
            // fallback check that account not created
            Assert.assertFalse(registrationPage.isAccountCreated(), "Account should not be created if name exceeds limits");
        }
    }

    @Then("the system should display an error that the email or mobile is already in use")
    public void duplicate_contact_error() {
        String msg = registrationPage.getDuplicateContactMessage();
        Assert.assertTrue(msg != null && (msg.toLowerCase().contains("already") || msg.toLowerCase().contains("in use")),
                          "Expected duplicate contact error");
    }

    @Then("the system should suggest signing in or using account recovery instead of creating a new account")
    public void suggest_signin_or_recovery() {
        String suggestion = registrationPage.getPostDuplicateSuggestion();
        Assert.assertTrue(suggestion != null && (suggestion.toLowerCase().contains("sign in") || suggestion.toLowerCase().contains("recover")),
                          "Expected suggestion to sign in or recover account");
    }

    @Then("the registration should complete successfully and the account should be activated")
    public void registration_completes_and_activated() {
        boolean active = registrationPage.isAccountActive();
        Assert.assertTrue(active, "Expected account to be active");
    }

    @Then("the user account should have no default address or payment methods stored")
    public void account_has_no_default_address_or_payment() {
        Assert.assertFalse(registrationPage.hasDefaultAddress(), "Expected no default address");
        Assert.assertFalse(registrationPage.hasDefaultPaymentMethod(), "Expected no default payment methods");
    }

    @When("a tester submits malicious payloads in input fields (for example full name {string} or email {string}) and submits the registration form")
    public void tester_submits_malicious_payloads(String maliciousName, String maliciousEmail) {
        registrationPage.enterFirstName(maliciousName);
        registrationPage.enterLastName("Exploit");
        registrationPage.enterEmail(maliciousEmail);
        registrationPage.enterPassword("Abc123!");
        registrationPage.submit();
    }

    @Then("the application should sanitise or reject the malicious input and not execute scripts or show stack traces")
    public void application_handles_malicious_input() {
        // Simple checks - adapt to