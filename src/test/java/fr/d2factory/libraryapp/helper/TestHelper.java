package fr.d2factory.libraryapp.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.ISBN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    private List<Book> books;

    public List<Book> getBooks() {
        books = new ArrayList<>();
        Book book1 = new Book("Harry Potter", "J.K. Rowling", new ISBN(1111));
        Book book2 = new Book("Around the world in 80 days", "J. Vernes", new ISBN(1112));
        Book book3 = new Book("Le prince", "Machiavel", new ISBN(1113));
        books.add(book1);
        books.add(book2);
        books.add(book3);
        return books;
    }

    public List<Book> getBooksFromResources() {
        List<Book> books = new ArrayList<>();;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File resourcesFile = new File("src/test/resources/books.json");
            books = objectMapper.readValue( resourcesFile, new TypeReference<List<Book>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
}
