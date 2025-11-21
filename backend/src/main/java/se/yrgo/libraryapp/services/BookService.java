package se.yrgo.libraryapp.services;

import java.util.Set;

import javax.inject.Inject;

import se.yrgo.libraryapp.dao.BookDao;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.DdsClassification;

public class BookService {
    private BookDao bookDao;

    @Inject
    BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Set<BookEdition> find(String isbn, String title, String author) {
        if ((isbn == null || isbn.trim().length() == 0)
                && (title == null || title.trim().length() == 0)
                && (author == null || author.trim().length() == 0)) {
            return Set.of();
        }

        return bookDao.find(isbn, title, author);
    }

    public Set<BookEdition> withClass(DdsClassification classification) {
        return bookDao.withClass(classification);
    }
}
