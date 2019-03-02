package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.strategy.PayStrategy;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 * My abstract product class
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */

    public Member(PayStrategy strategy, double wallet, int delai){
        this.payStrategy = strategy;
        this.wallet = wallet;
        this.delai = delai;
    }

    private double wallet;

    private int delai;

    private PayStrategy payStrategy;

    public int getDelai() {
        return delai;
    }

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public PayStrategy getPayStrategy() {
        return payStrategy;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
