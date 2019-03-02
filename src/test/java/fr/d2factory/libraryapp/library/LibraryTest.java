package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.exception.TownsvilleLibraryException;
import fr.d2factory.libraryapp.helper.TestHelper;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import fr.d2factory.libraryapp.strategy.FirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.NonFirstYearStudentPayStrategy;
import fr.d2factory.libraryapp.strategy.ResidentPayStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class LibraryTest {
    private Library library ;

    private List<Book> books;

    private double wallet = 100;

    private final double delta = 0.0;

    private TestHelper helper = new TestHelper();

    @Before
    public void setup(){
        books = helper.getBooksFromResources();
    }

    private void initLibrary() {
        BookRepository bookRepository = new BookRepository();
        bookRepository.addBooks(books);
        library = new LibraryImpl(bookRepository);
    }

    @Test
    public void memberCanBorrowABookIfBookIsAvailable(){

        // init library
        initLibrary();

        Member student = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        Book borrowedBook = library.borrowBook(bookToBorrow.getIsbn(), student);
        assertNotNull(borrowedBook);
        assertEquals(bookToBorrow, borrowedBook);
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void borrowedBookIsNoLongerAvailable(){

        initLibrary();

        Student student = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        // borrow a book the 1st time
        Book borrowedBook = library.borrowBook(bookToBorrow.getIsbn(), student);

        // borrow a book the 2nd time
        library.borrowBook(bookToBorrow.getIsbn(), student);
    }

    @Test
    public void residentsAreTaxed10CentsForEachDayTheyKeptABook(){
        // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();

        Resident resident = new Resident(new ResidentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        final int NUMBER_OF_DAYS_LEFT = 20;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


        library.borrowBook(bookToBorrow.getIsbn(), resident);
        library.returnBook(bookToBorrow, resident);


        assertEquals(wallet - NUMBER_OF_DAYS_LEFT * 0.1, resident.getWallet(), delta);
    }

    private BookRepository initLibraryWithBookRepositoryMocked() {
        BookRepository bookRepositoryMock = Mockito.mock(BookRepository.class);
        library = new LibraryImpl(bookRepositoryMock);
        return bookRepositoryMock;
    }

    @Test
    public void studentsPay10CentsTheFirst30days(){
        // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();
        Student student = new Student(new NonFirstYearStudentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        final int NUMBER_OF_DAYS_LEFT = 30;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


          library.borrowBook(bookToBorrow.getIsbn(), student);
        library.returnBook(bookToBorrow, student);

        assertEquals(wallet - NUMBER_OF_DAYS_LEFT * 0.1, student.getWallet(), delta);
    }

    @Test
    public void studentsIn1stYearAreNotTaxedForTheFirst15days(){
        // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();

        Student firstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        final int NUMBER_OF_DAYS_LEFT = 15;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


        library.borrowBook(bookToBorrow.getIsbn(), firstYearStudent);
        library.returnBook(bookToBorrow, firstYearStudent);

        assertEquals(wallet, firstYearStudent.getWallet(), delta);
    }

    @Test
    public void studentsPay15CentsForEachDayTheyKeepABookAfterTheInitial30days(){
        // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();

        Student firstYearStudent = new Student(new FirstYearStudentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        final int NUMBER_OF_DAYS_LEFT = 31;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


        library.borrowBook(bookToBorrow.getIsbn(), firstYearStudent);
        library.returnBook(bookToBorrow, firstYearStudent);

        assertEquals(wallet - (NUMBER_OF_DAYS_LEFT * 0.15), firstYearStudent.getWallet(), delta);
    }

    @Test
    public void residentsPay20CentsForEachDayTheyKeptABookAfterTheInitial60days(){
       // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();

        Resident resident = new Resident(new ResidentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        final int NUMBER_OF_DAYS_LEFT = 80;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


        library.borrowBook(bookToBorrow.getIsbn(), resident);
        library.returnBook(bookToBorrow, resident);

        assertEquals(wallet - (NUMBER_OF_DAYS_LEFT * 0.2), resident.getWallet(), delta);
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void membersCannotBorrowBookIfTheyHaveLateBooks(){
        // mock book repository
        BookRepository bookRepositoryMock = initLibraryWithBookRepositoryMocked();

        Resident resident = new Resident(new ResidentPayStrategy(), wallet);
        Book bookToBorrow = books.get(0);
        Book secondBookToBorrow = books.get(1);
        final int NUMBER_OF_DAYS_LEFT = 80;

        Mockito.when(bookRepositoryMock.findBook(bookToBorrow.getIsbn())).thenReturn(bookToBorrow);
        Mockito.when(bookRepositoryMock.returnABook(bookToBorrow)).thenReturn(LocalDate.now().plusDays(- NUMBER_OF_DAYS_LEFT));


        library.borrowBook(bookToBorrow.getIsbn(), resident);

        library.borrowBook(secondBookToBorrow.getIsbn(), resident);
    }
}
