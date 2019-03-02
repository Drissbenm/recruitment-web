package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.exception.DefinedTownsvilleLibraryException;
import fr.d2factory.libraryapp.exception.TownsvilleLibraryException;
import fr.d2factory.libraryapp.member.Member;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class LibraryImpl implements Library {

    private BookRepository bookRepository;

    private Map<Member, Set<Book>> booksBorrowedByMember = new HashMap<>();

    public LibraryImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book borrowBook(ISBN isbnCode, Member member) throws TownsvilleLibraryException {
        Book bookAvailable = bookRepository.findBook(isbnCode);

        if (bookAvailable == null) {
            throw DefinedTownsvilleLibraryException.BOOK_UNVAILABLE_EXCEPTION;
        }

        Set<Book> membersBorrowedBooks = booksBorrowedByMember.getOrDefault(member, new HashSet<>());
        if (CollectionUtils.isEmpty(membersBorrowedBooks)) {
            newMemberBorrowBook(member, bookAvailable);
        } else {
            existingMemberBorrowBook(member, bookAvailable, membersBorrowedBooks);
        }
        return bookAvailable;
    }

    @Override
    public void returnBook(Book book, Member member) {
        LocalDate borrowedAt = bookRepository.returnABook(book);
        booksBorrowedByMember.computeIfPresent(member, (m, books) -> {
            if (CollectionUtils.isNotEmpty(books)) {
                books.remove(book);
            }
            return books;
        });
        member.payBook((int) borrowedAt.until(LocalDate.now(), ChronoUnit.DAYS));
    }

    private void existingMemberBorrowBook(Member member, Book bookAvailable, Set<Book> membersBorrowedBooks) {
        boolean hasLateBook = doMemberHasLateBook(member, membersBorrowedBooks);
        if (hasLateBook) {
            throw DefinedTownsvilleLibraryException.HAS_LATE_BOOK_EXCEPTION;
        }
        newMemberBorrowBook(member, bookAvailable);
    }

    private boolean doMemberHasLateBook(Member member, Set<Book> membersBorrowedBooks) {
        LocalDate now = LocalDate.now();
        return membersBorrowedBooks.stream()
                .map(bookRepository::findBorrowedBookDate)
                .filter(Objects::nonNull)
                .anyMatch(borrowedAt -> borrowedAt.until(now).getDays() > member.getDelai());
    }

    private void newMemberBorrowBook(Member member, Book bookAvailable) {
        booksBorrowedByMember.computeIfAbsent(member, books -> new HashSet<>()).add(bookAvailable);
        bookRepository.saveBookBorrow(bookAvailable, LocalDate.now());
    }
}
