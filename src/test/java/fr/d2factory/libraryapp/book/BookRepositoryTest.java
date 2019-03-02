package fr.d2factory.libraryapp.book;


import fr.d2factory.libraryapp.exception.TownsvilleLibraryException;
import fr.d2factory.libraryapp.helper.TestHelper;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BookRepositoryTest {
    private BookRepository bookRepository;
    private List<Book> books;
    private ISBN existingIsbn;
    private TestHelper helper = new TestHelper();

    @Before
    public void init() {
        bookRepository = new BookRepository();
        books = helper.getBooks();
        existingIsbn = new ISBN(1113);
        bookRepository.addBooks(books);
    }

    @Test
    public void addAvailableBooks() {
        assertEquals(books.size(), bookRepository.getAvailableBooks().values().size());
    }

    @Test
    public void addAvailableBooksThatExistInMapOfBorrowedBooks() {
        Book borrowedBook1 = books.get(0);
        Book borrowedBook2 = books.get(2);

        bookRepository.saveBookBorrow(borrowedBook1, LocalDate.now());
        bookRepository.saveBookBorrow(borrowedBook2, LocalDate.now());

        // books are borrowed
        assertEquals(2, bookRepository.getBorrowedBooks().values().size());

        // books are no more available
        assertNull(bookRepository.findBook(borrowedBook1.getIsbn()));
        assertNull(bookRepository.findBook(borrowedBook2.getIsbn()));

        bookRepository.addBooks(books);
        assertEquals(1, bookRepository.getAvailableBooks().values().size());
    }

    @Test
    public void addAvailableBooksThatExistInMapOfAvaibleBooks() {
        Book redundantBook = new Book("Le prince v2", "Machiavel", existingIsbn);
        books.add(redundantBook);
        bookRepository.addBooks(books);
        assertEquals(books.size() - 1, bookRepository.getAvailableBooks().values().size());
    }

    @Test
    public void findAvailableBook() {
        Book bookFound = bookRepository.findBook(existingIsbn);
        assertNotNull(bookFound);
        assertEquals(books.get(2), bookFound);
    }

    @Test
    public void saveBookBorrow() {
        LocalDate now = LocalDate.now();
        Book bookToBorrow = books.get(2);
        bookRepository.saveBookBorrow(bookToBorrow, now);
        assertEquals(books.size() - 1, bookRepository.getAvailableBooks().values().size());
        Collection<LocalDate> localDates = bookRepository.getBorrowedBooks().values();
        assertTrue(!localDates.isEmpty());
        assertEquals(1, localDates.size());
        // verify that localDate is stored correctly
        assertTrue(localDates.contains(now));
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void bookToBorrowNotAvailable() {
        LocalDate now = LocalDate.now();
        Book bookToBorrow = books.get(2);
        // borrow a book the 1st time
        bookRepository.saveBookBorrow(bookToBorrow, now);
        assertEquals(books.size() - 1, bookRepository.getAvailableBooks().values().size());
        Collection<LocalDate> localDates = bookRepository.getBorrowedBooks().values();
        assertTrue(!localDates.isEmpty());
        assertEquals(1, localDates.size());
        // verify that localDate is stored correctly
        assertTrue(localDates.contains(now));

        // borrow the same book 2nd time
        bookRepository.saveBookBorrow(bookToBorrow, now);
    }

    @Test
    public void findBorrowedBookDate() {
        LocalDate now = LocalDate.now();
        Book bookToBorrow = books.get(2);
        // borrow a book
        bookRepository.saveBookBorrow(bookToBorrow, now);
        assertEquals(now, bookRepository.findBorrowedBookDate(bookToBorrow));
    }

    @Test(expected = TownsvilleLibraryException.class)
    public void returnNotBorrowedBook() {
        Book notBorrowedBook = books.get(2);
        bookRepository.returnABook(notBorrowedBook);
    }

    @Test
    public void returnABook() {
        LocalDate now = LocalDate.now();
        Book bookToBorrow = books.get(2);
        // borrow a book the 1st time
        bookRepository.saveBookBorrow(bookToBorrow, now);
        // return a book
        bookRepository.returnABook(bookToBorrow);
        assertNull(bookRepository.getBorrowedBooks().get(bookToBorrow));
        assertEquals(bookRepository.getAvailableBooks().get(bookToBorrow.getIsbn()), bookToBorrow);
    }
}
