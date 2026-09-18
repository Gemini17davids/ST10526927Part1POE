package com.mycompany.main;

// import Scanner so we can get a user input
import java.util.Scanner;

// Create the main class
public class Main {
    
    // Create Scanner for user input
    static Scanner input = new Scanner(System.in);
    
    static String username;
    static String password;
    static String cellphone;
    
    // Stores the registered useername
    static String registeredUsername;
    
    // Stores the registered Password
    static String registeredPassword;
    
    // Stores the registered Cellphone number
    static String registeredCellPhone;
    
    public static boolean checkUserName(String username){
        
        // usernbame must have 5 Characters
       // and must contain an underscore
       if(username.length()== 5 && username.contains("_")){
         return true;
        } else {
        return false;
       }
    }
    
    // Create the password checking method
    public static boolean checkPasswordComplexity(String password){
        
        // Password must have 8 characters long, contain a capital letter
        // Contain a number and a spacial character
        if(password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        }else{
        
        return false;
    }
    }
    
    // Create the cellphone checking method
    public static boolean checkCellPhoneNumber(String cellphone){
        
        // Phone Number must have a country code (+27)
        if(cellphone.matches("\\+27[0-9]{9}$")){
            return true;
        } else{
        return false;
    }
    }
    
    // Create the registration method
    public static void registerUser(){
        
        // registration code will be added here
        while (true){
        System.out.print("Enter your Username: ");
        username = input.nextLine();
        // check username
        boolean usernameCorrect = checkUserName(username);
        if(usernameCorrect){
            break;
        } else{
            System.out.print("Username is incorerect formatted. Please try again");
        }
        }
        
        // infite loop for password
        while (true){
        System.out.print("Enter your Password: ");
        password = input.nextLine();
        // check username
       boolean passwordCorrect = checkPasswordComplexity(password);
        if(passwordCorrect){
            break;
        } else{
            System.out.print("Password is incorerect formatted. Please try again");
        }
        }
        
        // infite loop for cell phone number
        while (true){
        System.out.print("Enter your Cell Phone Number (+27): ");
        cellphone = input.nextLine();
        // check username
        boolean cellPhoneCorrect = checkCellPhoneNumber(cellphone);
        if(cellPhoneCorrect){
            break;
        } else{
            System.out.print("Cell Phone number is incorerect formatted. Please try again");
        }
      }
        
        // save the valid username
        registeredUsername = username;
        // save the valid password
        registeredPassword = password;
        // save the valid cellphone number
        registeredCellPhone = cellphone;
        // display success messege
        System.out.println("User registered successfully");
    }
    // Creates the log in method
    public static boolean loginUser(String username, String password){
        
        
        while (true){
        // Compare entered details with registered details
        if(username.equals(registeredUsername)
                && password.equals(registeredPassword)) {
            return true;
        } else {
            System.out.println("Username or password incorrect, please, please try again");
            // Re-promt the user for username
            System.out.print("Enter your username: ");
            username = input.nextLine();
            // Re-promt the user for password
            System.out.print("Enter your password: ");
            password = input.nextLine();
        }
        }
    }
    // Creates the login status method
    public static String returnLoginStatus(boolean loginSuccessful){
        
        // Check if login was successful
        if(loginSuccessful){
        
        return "Welcome, it is greate to see you again" + username;
        } else {
            return "Username or password incorrect, pleasew try again";
        }
    
    }
    
     // Main method where the program starts
    public static void main(String[] args) {
        
        // call registration method
        registerUser();
        
        System.out.println("");
        
        // aske user to login
        System.out.print("Enter your username to login: ");
            String loginUsername = input.nextLine();
            
        System.out.print("Enter your password to login: ");
            String loginPassword = input.nextLine();
            
            System.out.println("");
            
        // call login method
        boolean loginSuccessful = loginUser(loginUsername, loginPassword);
        // call login status method
        System.out.println(returnLoginStatus(loginSuccessful));
        
       
    }
}
