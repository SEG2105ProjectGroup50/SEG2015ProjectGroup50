package com.example.mytable;

import android.content.Context;

public class Client extends User{
    private Address clientAddress;
    private int cardCVC, clientRating;
    private long cardNumber;
    private String cardHolderName, cardExpirationDateMonth, cardExpirationDateYear, cardExpirationDate;

    public Client() {
        this.userType = "Client";
    }

    public Client(Context context) {

    }

    public Client(Address clientAddress, int cardCVC, long cardNumber, String cardHolderName, String cardExpirationDate) {
        this.clientAddress = clientAddress;
        this.cardCVC = cardCVC;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardExpirationDate = cardExpirationDate;
        this.userType = "Client";
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public boolean setCardNumber(String cardNumber) {
        if(cardNumber.length() != 16) {
            return(false);
        } else {
            this.cardNumber = Long.parseLong(cardNumber);
            return(true);
        }
    }

    public int getCardCVC() {
        return cardCVC;
    }

    public boolean setCardCVC(String cardCVC) {
        if(cardCVC.length() != 3) {
            return(false);
        } else {
            this.cardCVC = Integer.parseInt(cardCVC);
            return(true);
        }
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public boolean setCardHolderName(String cardHolderName) {
        if(cardHolderName.equals("")) {
            return(false);
        } else {
            this.cardHolderName = cardHolderName;
            return(true);
        }

    }

    public String getCardExpirationDate() {
        cardExpirationDate = cardExpirationDateMonth + "/" + cardExpirationDateYear;
        return(cardExpirationDate);
    }

    public boolean setCardExpirationMonth(String cardExpirationDateMonth) {
        if (cardExpirationDateMonth.equals("")) {
            return(false);
        } else if(Integer.parseInt(cardExpirationDateMonth) > 12) {
            return(false);
        } else {
            this.cardExpirationDateMonth = cardExpirationDateMonth;
            return(true);
        }
    }

    public boolean setCardExpirationYear(String cardExpirationDateYear) {
        if (cardExpirationDateYear.equals("")) {
            return(false);
        } else if(Integer.parseInt(cardExpirationDateYear) < 22) {
            return(false);
        } else {
            this.cardExpirationDateYear = cardExpirationDateYear;
            return(true);
        }
    }

    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }
}
