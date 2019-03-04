package fr.d2factory.libraryapp.proxy;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.helper.TestHelper;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.library.LibraryImpl;
import fr.d2factory.libraryapp.member.Resident;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SecurityHandlerTest {

    private Library secureLibrary;
    private BookRepository bookRepository;
    private List<Book> books;
    private TestHelper helper = new TestHelper();

    @Before
    public void setUp(){

        books = helper.getBooksFromResources();
        bookRepository = new BookRepository();
        bookRepository.addBooks(books);
        secureLibrary =  SecurityHandler.getLibraryInstance(new LibraryImpl(bookRepository));
    }

    @Test
    public void memberWithFilledWalletCanBorrowBook(){
        Resident resident = Resident.getInstanceOfResident(100);
        Book bookToBorrow = books.get(0);
        Assert.assertNotNull(secureLibrary.borrowBook(bookToBorrow.getIsbn(), resident));
    }

    @Test(expected = RuntimeException.class)
    public void memberWithEmptyWalletCantBorrowBook(){
        Resident resident = Resident.getInstanceOfResident(0);
        Book bookToBorrow = books.get(0);
        secureLibrary.borrowBook(bookToBorrow.getIsbn(), resident);
    }
}
