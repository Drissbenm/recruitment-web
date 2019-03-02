package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.strategy.PayStrategy;

public class Resident extends Member {

    public Resident(PayStrategy payStrategy, double wallet){
        super(payStrategy, wallet, MemberMilestones.RESIDENT_TIME_LIMITE);
    }

    @Override
    public void payBook(int numberOfDays) {
        double deduction = getWallet() - getPayStrategy().payStrategy(numberOfDays);
        setWallet(deduction);
    }
}
