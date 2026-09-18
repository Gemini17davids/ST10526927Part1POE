package com.mycompany.main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the methods in Main.java.
 *
 * This class MUST be in the same package as Main (com.mycompany.main),
 * because the tests replace Main.input (a package-private static field)
 * so that user input can be simulated instead of typed in by hand.
 */
public class MainTest {

    private Scanner originalInput;
    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    void setUp() {
        // Remember the real Scanner and System.out so they can be restored
        originalInput = Main.input;
        originalOut = System.out;

        // Capture everything the program prints
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        // Reset the static state so every test starts clean
        Main.username = null;
        Main.password = null;
        Main.cellphone = null;
        Main.registeredUsername = null;
        Main.registeredPassword = null;
        Main.registeredCellPhone = null;
    }

    @AfterEach
    void tearDown() {
        Main.input = originalInput;
        System.setOut(originalOut);
    }

    /** Simulates the user typing the given lines (separated by \n). */
    private void provideInput(String data) {
        Main.input = new Scanner(data);
    }

    /** Everything Main printed during the test. */
    private String output() {
        return capturedOut.toString();
    }

    // ------------------------------------------------------------------
    // checkUserName
    // ------------------------------------------------------------------

    @Test
    void checkUserName_validUsername_returnsTrue() {
        assertTrue(Main.checkUserName("ab_12"));
    }

    @Test
    void checkUserName_underscoreAtStartOrEnd_returnsTrue() {
        assertTrue(Main.checkUserName("_abcd"));
        assertTrue(Main.checkUserName("abcd_"));
    }

    @Test
    void checkUserName_noUnderscore_returnsFalse() {
        assertFalse(Main.checkUserName("abcde"));
    }

    @Test
    void checkUserName_tooShort_returnsFalse() {
        assertFalse(Main.checkUserName("ab_1"));
    }

    @Test
    void checkUserName_tooLong_returnsFalse() {
        assertFalse(Main.checkUserName("abc_12"));
    }

    @Test
    void checkUserName_emptyString_returnsFalse() {
        assertFalse(Main.checkUserName(""));
    }

    // ------------------------------------------------------------------
    // checkPasswordComplexity
    // ------------------------------------------------------------------

    @Test
    void checkPasswordComplexity_validPassword_returnsTrue() {
        assertTrue(Main.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void checkPasswordComplexity_exactlyEightCharacters_returnsTrue() {
        assertTrue(Main.checkPasswordComplexity("Passw0r!"));
    }

    @Test
    void checkPasswordComplexity_tooShort_returnsFalse() {
        // 7 characters, otherwise valid
        assertFalse(Main.checkPasswordComplexity("Pas0w!d"));
    }

    @Test
    void checkPasswordComplexity_noUppercase_returnsFalse() {
        assertFalse(Main.checkPasswordComplexity("password1!"));
    }

    @Test
    void checkPasswordComplexity_noLowercase_returnsFalse() {
        assertFalse(Main.checkPasswordComplexity("PASSWORD1!"));
    }

    @Test
    void checkPasswordComplexity_noNumber_returnsFalse() {
        assertFalse(Main.checkPasswordComplexity("Password!!"));
    }

    @Test
    void checkPasswordComplexity_noSpecialCharacter_returnsFalse() {
        assertFalse(Main.checkPasswordComplexity("Password12"));
    }

    @Test
    void checkPasswordComplexity_emptyString_returnsFalse() {
        assertFalse(Main.checkPasswordComplexity(""));
    }

    // ------------------------------------------------------------------
    // checkCellPhoneNumber
    // ------------------------------------------------------------------

    @Test
    void checkCellPhoneNumber_validNumber_returnsTrue() {
        assertTrue(Main.checkCellPhoneNumber("+27831234567"));
    }

    @Test
    void checkCellPhoneNumber_missingCountryCode_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("0831234567"));
    }

    @Test
    void checkCellPhoneNumber_missingPlusSign_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("27831234567"));
    }

    @Test
    void checkCellPhoneNumber_wrongCountryCode_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("+44831234567"));
    }

    @Test
    void checkCellPhoneNumber_tooFewDigits_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("+2783123456"));
    }

    @Test
    void checkCellPhoneNumber_tooManyDigits_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("+278312345678"));
    }

    @Test
    void checkCellPhoneNumber_containsLetters_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("+27abcdefghi"));
    }

    @Test
    void checkCellPhoneNumber_containsSpaces_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber("+27 83 123 4567"));
    }

    @Test
    void checkCellPhoneNumber_emptyString_returnsFalse() {
        assertFalse(Main.checkCellPhoneNumber(""));
    }

    // ------------------------------------------------------------------
    // registerUser
    // ------------------------------------------------------------------

    @Test
    void registerUser_validDetailsFirstTry_storesRegisteredDetails() {
        provideInput("ab_12\nPassw0rd!\n+27831234567\n");

        Main.registerUser();

        assertEquals("ab_12", Main.registeredUsername);
        assertEquals("Passw0rd!", Main.registeredPassword);
        assertEquals("+27831234567", Main.registeredCellPhone);
        assertTrue(output().contains("User registered successfully"));
    }

    @Test
    void registerUser_invalidDetailsThenValid_keepsAskingUntilValid() {
        // Each field is entered wrong first, then correctly
        provideInput(
                "abc\n" + "ab_12\n"                          // username
              + "weak\n" + "Passw0rd!\n"                     // password
              + "0831234567\n" + "+27831234567\n");          // cell number

        Main.registerUser();

        // Only the valid values should have been stored
        assertEquals("ab_12", Main.registeredUsername);
        assertEquals("Passw0rd!", Main.registeredPassword);
        assertEquals("+27831234567", Main.registeredCellPhone);
        assertTrue(output().contains("User registered successfully"));
    }

    @Test
    void registerUser_invalidInputOnly_neverRegisters() {
        // Input runs out while the username is still invalid, so the loop
        // can never finish and nothing should be registered.
        provideInput("bad\nalso bad\n");

        assertThrows(NoSuchElementException.class, Main::registerUser);

        assertNull(Main.registeredUsername);
        assertFalse(output().contains("User registered successfully"));
    }

    // ------------------------------------------------------------------
    // loginUser
    // ------------------------------------------------------------------

    @Test
    void loginUser_correctDetails_returnsTrueImmediately() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";

        assertTrue(Main.loginUser("ab_12", "Passw0rd!"));
        assertFalse(output().contains("incorrect"));
    }

    @Test
    void loginUser_wrongPasswordThenCorrect_returnsTrueAfterRetry() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";
        provideInput("ab_12\nPassw0rd!\n");

        assertTrue(Main.loginUser("ab_12", "wrong"));
        assertTrue(output().contains("incorrect"));
    }

    @Test
    void loginUser_wrongUsernameThenCorrect_returnsTrueAfterRetry() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";
        provideInput("ab_12\nPassw0rd!\n");

        assertTrue(Main.loginUser("zz_99", "Passw0rd!"));
        assertTrue(output().contains("incorrect"));
    }

    @Test
    void loginUser_multipleFailuresThenCorrect_returnsTrue() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";
        provideInput("x\ny\n" + "ab_12\nnope\n" + "ab_12\nPassw0rd!\n");

        assertTrue(Main.loginUser("bad", "bad"));
    }

    @Test
    void loginUser_isCaseSensitive() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";
        provideInput("ab_12\nPassw0rd!\n");

        // Wrong case is rejected first, so the retry prompt must appear
        assertTrue(Main.loginUser("AB_12", "Passw0rd!"));
        assertTrue(output().contains("incorrect"));
    }

    @Test
    void loginUser_neverCorrect_keepsPromptingUntilInputRunsOut() {
        Main.registeredUsername = "ab_12";
        Main.registeredPassword = "Passw0rd!";
        provideInput("x\ny\n");

        // loginUser only exits on success, so when the simulated input
        // runs out the Scanner throws instead of the method returning false.
        assertThrows(NoSuchElementException.class,
                () -> Main.loginUser("wrong", "wrong"));
    }

    // ------------------------------------------------------------------
    // returnLoginStatus
    // ------------------------------------------------------------------

    @Test
    void returnLoginStatus_success_welcomesTheUser() {
        Main.username = "ab_12";

        String message = Main.returnLoginStatus(true);

        assertTrue(message.startsWith("Welcome"));
        assertTrue(message.contains("ab_12"));
    }

    @Test
    void returnLoginStatus_failure_returnsErrorMessage() {
        Main.username = "ab_12";

        String message = Main.returnLoginStatus(false);

        assertTrue(message.contains("incorrect"));
        assertFalse(message.contains("Welcome"));
    }

    // ------------------------------------------------------------------
    // main (end-to-end)
    // ------------------------------------------------------------------

    @Test
    void main_registerThenLogin_welcomesTheUser() {
        provideInput(
                "ab_12\nPassw0rd!\n+27831234567\n"   // registration
              + "ab_12\nPassw0rd!\n");               // login

        Main.main(new String[0]);

        assertTrue(output().contains("User registered successfully"));
        assertTrue(output().contains("Welcome"));
        assertTrue(output().contains("ab_12"));
    }
}
