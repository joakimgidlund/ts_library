package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("Integration")
@H2
class UserDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserById() {
        final String username = "test";
        final UserId userId = UserId.of(1);

        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getNonExistingUser() {
        final UserId userId = UserId.of(100000);
        final UserId longUserId = UserId.of(Integer.MAX_VALUE);

        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        Optional<User> longUser = userDao.get(Integer.toString(longUserId.getId()));

        assertThat(maybeUser).isNotPresent();
        assertThat(longUser).isNotPresent();
    }

    @Test
    void isNameAvailable() {
        final String existingUsername = "test";
        final String nonExistingUsername = "asdadsasdasdasd";

        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable(existingUsername)).isFalse();
        assertThat(userDao.isNameAvailable(nonExistingUsername)).isTrue();
    }

    @Test
    void getLoginInfo() {
        final String username = "test";
        final UserId id = UserId.of("1");
        final String passwordHash = "$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ";

        UserDao userDao = new UserDao(ds);

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo(username);

        assertThat(loginInfo.get().getUserId()).isEqualTo(id);
        assertThat(loginInfo.get().getPasswordHash()).isEqualTo(passwordHash);
    }

    @Test
    void getNonExistingLoginInfo() {
        final String username = "nonexistingtest";

        UserDao userDao = new UserDao(ds);

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo(username);

        assertThat(loginInfo).isEmpty();
    }

    @Test
    void register() {
        final String username = "testuser";
        final String realname = "Joakim";
        final String passwordHash = "password";

        UserDao userDao = new UserDao(ds);

        boolean isRegistered = userDao.register(username, realname, passwordHash);

        Optional<LoginInfo> info = userDao.getLoginInfo(username);
        Optional<User> maybeUser = userDao.get(info.get().getUserId().toString());

        assertThat(isRegistered).isTrue();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
    }
}
