package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.StackWalker.Option;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.DdsClassification;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("Integration")
@H2
public class BookDaoIntegrationTest {
    private static DataSource ds;

    BookDao bookDao;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:test");
        BookDaoIntegrationTest.ds = ds;
    }

    @BeforeEach
    void config() {
        bookDao = new BookDao(ds);
    }

    @Test
    void getExisting() {
        BookId id = BookId.of(1);

        Optional<Book> maybeBook = bookDao.get(id);

        assertThat(maybeBook).isPresent();
        assertThat(maybeBook.get().getId()).isEqualTo(id);
        assertThat(maybeBook.get().getEdition().getIsbn()).isEqualTo("9781509302000");
    }

    @Test
    void getNonExisting() {
        BookId id = BookId.of(999999);

        Optional<Book> maybeBook = bookDao.get(id);

        assertThat(maybeBook).isNotPresent();
    }

    @Test
    void existingEditions() {
        final String isbn = "9781509302000";
        final String title = "T-SQL Fundamentals";
        final String author = "Itzik Ben-Gan";

        assertThat(bookDao.find(isbn, title, author)).hasOnlyElementsOfType(BookEdition.class);
        assertThat(bookDao.find(isbn, title, author)).hasSize(1);
    }

    @Test
    void nonExistingEditions() {
        final String isbn = "9781509302001";
        final String title = "T-SQL Fundamentals";
        final String author = "Itzik Ben-Gan";

        assertThat(bookDao.find(isbn, title, author)).isEmpty();
    }

    @Test
    void getBookByClass() {
        final DdsClassification ddsClass = new DdsClassification(5, "Computer programming, programs and data");
        final DdsClassification badDdsClass = new DdsClassification(999, "test");

        assertThat(bookDao.withClass(ddsClass)).hasSize(1);
        assertThat(bookDao.withClass(ddsClass)).contains(new BookEdition("T-SQL Fundamentals", "Itzik Ben-Gan", "9781509302000"));
        assertThat(bookDao.withClass(badDdsClass)).isEmpty();
    }

    @Test
    void getLoans() {
        assertThat(bookDao.loans(UserId.of(2))).hasSize(2);
        assertThat(bookDao.loans(UserId.of(1))).isEmpty();
    }

    @Test
    void overDueLoans() {
        assertThat(bookDao.overdueLoans()).hasSize(2);
    }

    // H2 doesn't support the MySQL function used for dates in the lend() function.
    // @Test
    // void lendBook() {
    //     BookId bookId = BookId.of(3);
    //     UserId userId = UserId.of(2);

    //     assertThat(bookDao.lend(bookId, userId)).isTrue();
    // }

    @Test
    void lendAlreadyLendedBook() {
        BookId bookId = BookId.of(2);
        UserId userId = UserId.of(1);

        assertThat(bookDao.lend(bookId, userId)).isFalse();
    }

    @Test
    void returnBook() {
        BookId id = BookId.of(2);
        BookId notLoanedBook = BookId.of(1);

        assertThat(bookDao.returnBook(id)).isTrue();
        assertThat(bookDao.returnBook(notLoanedBook)).isFalse();
    }
}
