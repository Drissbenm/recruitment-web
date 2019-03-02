package fr.d2factory.libraryapp.strategy;

import fr.d2factory.libraryapp.exception.DefinedTownsvilleLibraryException;
import fr.d2factory.libraryapp.member.MemberMilestones;

public class FirstYearStudentPayStrategy extends PayStrategy {

    @Override
    public double payStrategy(int numberOfDays) {
        if(numberOfDays < 0){
            throw DefinedTownsvilleLibraryException.NUMBER_OF_HOLDING_DAYS_CANT_BE_LOWER_THAN_ZERO;
        }

        double sumToPay = 0;
        float days = (float) numberOfDays;
        if(numberOfDays > MemberMilestones.FIRST_YEAR_STUDENT_FREE_PERIOD && numberOfDays<= MemberMilestones.STUDENT_TIME_LIMITE){
            sumToPay = days * MembersPricing.STUDENT_NORMAL_PRICE;
        }else if( numberOfDays > MemberMilestones.STUDENT_TIME_LIMITE ){
            sumToPay = days * MembersPricing.STUDENT_INCREASED_PRICE;
        }
        return sumToPay;
    }
}
