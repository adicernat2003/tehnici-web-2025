package model;

public class BankAccount {
    private int id;
    private String accountNumber;
    private String accountOwner;

    public BankAccount(int id, String accountNumber, String accountOwner) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountOwner = accountOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountOwner='" + accountOwner + '\'' +
                '}';
    }
}
