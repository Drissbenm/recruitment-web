package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.strategy.FirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.NonFirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.PayStrategy;

public class Student extends Member {

    private Student(PayStrategy payStrategy, double wallet){
        super(payStrategy, wallet, MemberMilestones.STUDENT_TIME_LIMITE);
    }

    public static Student getInstanceOfNonFirstYearStudent(double wallet){
        return new Student(new NonFirstYearStudentPayStrategy(), wallet);
    }

    public static Student getInstanceOfFirstYearStudent(double wallet){
        return new Student(new FirstYearStudentPayStrategy(), wallet);
    }

    @Override
    public void payBook(int numberOfDays) {
        double deduction = getWallet() - getPayStrategy().payStrategy(numberOfDays);
        setWallet(deduction);
    }
}
