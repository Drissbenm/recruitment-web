package fr.d2factory.libraryapp.book;

import fr.d2factory.libraryapp.exception.DefinedTownsvilleLibraryException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();

    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    // don't throw exception let the process go
    public void addBooks(List<Book> books) {

        if (availableBooks.isEmpty() && borrowedBooks.isEmpty()) {

            books.forEach(book -> availableBooks.put(book.getIsbn(), book));

        } else {

            Map<ISBN, Book> booksNotBorrowed = books.stream()
                    .filter(book -> borrowedBooks.get(book) == null && availableBooks.get(book.getIsbn()) == null)
                    .collect(Collectors.toMap(Book::getIsbn, b -> b));

            if (! booksNotBorrowed.isEmpty()) {
                availableBooks.putAll(booksNotBorrowed);
            }

        }
    }

        public Book findBook ( ISBN isbnCode){
            return availableBooks.get(isbnCode);
        }

        public void saveBookBorrow (Book book, LocalDate borrowedAt){

            Book availableBook = availableBooks.get(book.getIsbn());
            if (availableBook == null) {
                throw DefinedTownsvilleLibraryException.BOOK_UNVAILABLE_EXCEPTION;
            }

            borrowedBooks.putIfAbsent(availableBook, borrowedAt);
            availableBooks.remove(availableBook.getIsbn());
        }

        public LocalDate findBorrowedBookDate (Book book){
            return borrowedBooks.get(book);
        }

        public LocalDate returnABook (Book book) {
            LocalDate borrowedAt = borrowedBooks.get(book);
            if(borrowedAt == null) {
                throw DefinedTownsvilleLibraryException.BOOK_NOT_BORROWED_EXCEPTION;
            }
            availableBooks.putIfAbsent(book.getIsbn(), book);
            borrowedBooks.remove(book);
            return borrowedAt;
        }

        protected Map<ISBN, Book> getAvailableBooks () {
            return availableBooks;
        }

        protected Map<Book, LocalDate> getBorrowedBooks () {
            return borrowedBooks;
        }
    }
