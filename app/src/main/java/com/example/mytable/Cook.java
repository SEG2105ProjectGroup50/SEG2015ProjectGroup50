package com.example.mytable;

public class Cook extends User{
    private String voidChequeFileName, cookDescription;
    private int mealsSold, cookRating;
    private Address cookAddress;

    public Cook() {
        this.userType = "Cook";
    }

    public Cook(String voidChequeFileName, String cookDescription, int mealsSold, int cookRating, Address cookAddress) {
        this.voidChequeFileName = voidChequeFileName;
        this.cookDescription = cookDescription;
        this.mealsSold = mealsSold;
        this.cookRating = cookRating;
        this.cookAddress = cookAddress;
        this.userType = "Cook";
    }

    public String getVoidChequeFileName() {
        return voidChequeFileName;
    }

    public void setVoidChequeFileName(String voidChequeFileName) {
        this.voidChequeFileName = voidChequeFileName;
    }

    public String getCookDescription() {
        return cookDescription;
    }

    public void setCookDescription(String cookDescription) {
        this.cookDescription = cookDescription;
    }

    public int getMealsSold() {
        return mealsSold;
    }

    public void setMealsSold(int mealsSold) {
        this.mealsSold = mealsSold;
    }

    public int getCookRating() {
        return cookRating;
    }

    public void setCookRating(int cookRating) {
        this.cookRating = cookRating;
    }

    public Address getCookAddress() {
        return cookAddress;
    }

    public void setCookAddress(Address cookAddress) {
        this.cookAddress = cookAddress;
    }
}
