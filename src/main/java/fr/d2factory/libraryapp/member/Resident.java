package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.strategy.PayStrategy;
import fr.d2factory.libraryapp.strategy.ResidentPayStrategy;

public class Resident extends Member {

    private Resident(PayStrategy payStrategy, double wallet){
        super(payStrategy, wallet, MemberMilestones.RESIDENT_TIME_LIMITE);
    }

    public static Resident getInstanceOfResident(double wallet){
        return new Resident(new ResidentPayStrategy(), wallet);
    }

    @Override
    public void payBook(int numberOfDays) {
        double deduction = getWallet() - getPayStrategy().payStrategy(numberOfDays);
        setWallet(deduction);
    }
}
