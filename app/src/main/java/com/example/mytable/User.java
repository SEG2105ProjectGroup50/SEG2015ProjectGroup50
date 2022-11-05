package com.example.mytable;
import java.time.LocalDate;
import java.util.regex.*;

public class User {
    protected String email, password, firstName, lastName, userType;
    private boolean suspendedStatus;
    private long suspensionDate;
    protected boolean loginStatus;


    public User() { // default constructor
        loginStatus = false;
        this.suspendedStatus = false;
        this.suspensionDate = 0;
    }

    public boolean verifyLogin() {
        return(loginStatus);
    }

    public boolean userLogin() { // allows user to login
        return true; // not implemented yet
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean setFirstName(String firstName) {
        if(firstName.equals("")) {
            return(false);
        } else {
            this.firstName = firstName;
            return(true);
        }
    }

    public String getLastName() {
        return lastName;
    }

    public boolean setLastName(String lastName) {
        if(lastName.equals("")) {
            return(false);
        } else {
            this.lastName = lastName;
            return(true);
        }
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getEmail() { // returns user Email
        return(email);
    }

    public boolean setEmail(String email) { // sets user Email
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // regex for email validation from https://howtodoinjava.com/java/regex/java-regex-validate-email-address/

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        if(matcher.matches()) { // returns true if email is valid, false if not
            this.email = email;
            return(true);
        } else {
            return(false);
        }
    }

    public String getPassword() { // returns user Password
        return(password);
    }

    public boolean setPassword(String password) { // sets user Email
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$"; // regex for password validation from https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) { // returns true if password is valid, false if not
            this.password = password;
            return (true);
        } else {
            return (false);
        }

    }

    public String getUserType(){
        return userType;
    }

    public void setUserType(String type){
        this.userType = type;
    }

    public boolean getsuspendedStatus(){
        return suspendedStatus;
    }

    public void setSuspendedStatus(Boolean suspendedStatus){
        this.suspendedStatus = suspendedStatus;
    }

    public long getSuspensionDate(){
        return suspensionDate;
    }

    public void setSuspensionDate(long suspensionDate){
        this.suspensionDate = suspensionDate;
    }

}
