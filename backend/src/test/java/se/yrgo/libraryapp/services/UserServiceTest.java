package se.yrgo.libraryapp.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceTest {
    @Mock
    private UserDao userDao;

    private PasswordEncoder encoder;
    private UserService userService;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void config() {
        this.encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        this.userService = new UserService(userDao, encoder);
    }

    @Test
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        assertThat(userService.validate(username, password)).isEqualTo(Optional.of(id));
    }

    @Test
    void register() {
        final String username = "testuser";
        final String realname = "Ian O'Toole";
        final String password = "password";

        when(userDao.register(username, "Ian O\\Toole", password)).thenReturn(true);

        assertThat(userService.register(username, realname, password)).isTrue();
    }

    @Test
    void unableToRegister() {
        final String username = "testuser";
        final String realname = "Joakim Gidlund";
        final String password = "password";

        when(userDao.register(username, realname, password)).thenReturn(false);

        assertThat(userService.register(username, realname, password)).isFalse();
    }

    @Test
    void isNameAvailable() {
        final String username = "testuser";

        when(userDao.isNameAvailable(username)).thenReturn(true);
        assertThat(userService.isNameAvailable(username)).isTrue();
    }

    @Test
    void nameNotAvailable() {
        final String username = "testuser";

        when(userDao.isNameAvailable(username)).thenReturn(false);

        assertThat(userService.isNameAvailable(username)).isFalse();
    }

    @Test
    void shortOrNullName() {
        final String shortName = "as";
        final String nullName = null;
        final String emptyName = "           ";

        assertThat(userService.isNameAvailable(shortName)).isFalse();
        assertThat(userService.isNameAvailable(nullName)).isFalse();
        assertThat(userService.isNameAvailable(emptyName)).isFalse();
    }

    @Test
    void passwordLength() {
        final String username = "testuser";
        final String realName = "Joakim";
        final String shortPassword = "asd";
        final String longPassword = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        assertThat(userService.register(username, realName, shortPassword)).isFalse();
        assertThat(userService.register(username, realName, longPassword)).isFalse();
    }
}
