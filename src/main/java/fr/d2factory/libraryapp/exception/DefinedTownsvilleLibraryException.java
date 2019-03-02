package fr.d2factory.libraryapp.exception;

public interface DefinedTownsvilleLibraryException {

    public final TownsvilleLibraryException BOOK_UNVAILABLE_EXCEPTION = new TownsvilleLibraryException("BOOK_UNVAILABLE_EXCEPTION");
    public final TownsvilleLibraryException BOOK_NOT_BORROWED_EXCEPTION = new TownsvilleLibraryException("BOOK_UNVAILABLE_EXCEPTION");
    public final TownsvilleLibraryException HAS_LATE_BOOK_EXCEPTION = new TownsvilleLibraryException("HAS_LATE_BOOK_EXCEPTION");
    public final TownsvilleLibraryException EMPTY_WALLOW_EXCEPTION = new TownsvilleLibraryException("EMPTY_WALLOW_EXCEPTION");
    public final TownsvilleLibraryException NUMBER_OF_HOLDING_DAYS_CANT_BE_LOWER_THAN_ZERO = new TownsvilleLibraryException("NUMBER_OF_HOLDING_DAYS_CANT_BE_LOWER_THAN_ZERO");
}
