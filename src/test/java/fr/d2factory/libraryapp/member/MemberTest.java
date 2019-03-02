package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.exception.TownsvilleLibraryException;
import fr.d2factory.libraryapp.strategy.FirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.MembersPricing;
import fr.d2factory.libraryapp.strategy.NonFirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.ResidentPayStrategy;
import org.junit.Assert;
import org.junit.Test;

public class MemberTest {

    private float wallet = 100;
    private final double delta = 0.0;

    @Test
    public void strategyPayFor1stYearStudent15FirstDays(){
        int numberOfDays = 10;
        Student firstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        firstYearStudent.payBook(numberOfDays);
        Assert.assertEquals(wallet, firstYearStudent.getWallet(), 0.0);
    }

    @Test
    public void strategyPayFor1stYearStudentKeptBookLessThan30Days(){
        int numberOfDays = 20;
        Student firstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        firstYearStudent.payBook(numberOfDays);
        Assert.assertEquals(wallet - (numberOfDays * MembersPricing.STUDENT_NORMAL_PRICE), firstYearStudent.getWallet(), delta);
    }

    @Test
    public void strategyPayFor1stYearStudentKeptBookMoreThan30Days(){
        int numberOfDays = 40;
        Student firstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        firstYearStudent.payBook(numberOfDays);
        Assert.assertEquals(wallet - (numberOfDays * MembersPricing.STUDENT_INCREASED_PRICE), firstYearStudent.getWallet(), delta);
    }

    @Test
    public void strategyPayForNon1stYearStudentKeptBookMoreThan30Days(){
        int numberOfDays = 50;
        Student nonFirstYearStudent = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        nonFirstYearStudent.payBook(numberOfDays);
        Assert.assertEquals(wallet - (numberOfDays * MembersPricing.STUDENT_INCREASED_PRICE), nonFirstYearStudent.getWallet(), delta);
    }

    @Test
    public void strategyPayForNon1stYearStudentKeptBookLessThan30Days(){
        int numberOfDays = 5;
        Student nonFirstYearStudent = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        nonFirstYearStudent.payBook(numberOfDays);

        Assert.assertEquals(wallet - (numberOfDays * 0.1), nonFirstYearStudent.getWallet(), delta);
    }

     @Test
    public void strategyPayForResidentKeptBookMoreThan60Days(){
         int numberOfDays = 100;
         Resident resident = new Resident(new ResidentPayStrategy(), wallet);
         resident.payBook(numberOfDays);
         Assert.assertEquals(wallet - (numberOfDays * MembersPricing.RESIDENT_INCREASED_PRICE), resident.getWallet(), delta);
    }

    @Test
    public void strategyPayForResidentKeptBookLessThan60Days(){
        int numberOfDays = 40;
        Resident resident = new Resident(new ResidentPayStrategy(), wallet);
        resident.payBook(numberOfDays);
        Assert.assertEquals(wallet - (numberOfDays * MembersPricing.RESIDENT_NORMAL_PRICE), resident.getWallet(), delta);
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void studentNumberOfDaysToPayCantBeLowerThanZero(){
        int numberOfDays = -1;
        Student nonFirstYearStudent = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        nonFirstYearStudent.payBook(numberOfDays);
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void firstYearstudentNumberOfDaysToPayCantBeLowerThanZero(){
        int numberOfDays = -10;
        Student nonFirstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        nonFirstYearStudent.payBook(numberOfDays);
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void ResidentNumberOfDaysToPayCantBeLowerThanZero(){
        int numberOfDays = -4;
        Student nonFirstYearStudent = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        nonFirstYearStudent.payBook(numberOfDays);
    }

}
