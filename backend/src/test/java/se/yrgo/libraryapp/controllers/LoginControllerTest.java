package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.CredentialException;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.jooby.Context;
import io.jooby.internal.ContextAsServiceInitializer;
import io.jooby.internal.ReadOnlyContext;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class LoginControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private RoleDao roleDao;
    @Mock
    private SessionDao sessionDao;

    @Test
    void successfulLogin() {
        Context context = mock(Context.class);
        String sessionCookie = "testcookie";
        LoginData loginData = new LoginData("test", "password");

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);
        UserId id = UserId.of(1);
        List<Role> userRoles = List.of(Role.ADMIN, Role.USER);

        when(userService.validate(loginData.getUsername(), loginData.getPassword())).thenReturn(Optional.of(id));
        when(roleDao.get(id)).thenReturn(List.of(Role.ADMIN, Role.USER));
        when(sessionDao.create(id)).thenReturn(new UUID(0, 0));

        assertThat(loginController.login(context, sessionCookie, loginData)).isEqualTo(userRoles);
    }

    @Test
    void unknownUserLogin() {
        Context context = mock(Context.class);
        String sessionCookie = "testcookie";
        LoginData loginData = new LoginData("test", "password");

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        when(userService.validate(loginData.getUsername(), loginData.getPassword())).thenReturn(Optional.empty());

        assertThat(loginController.login(context, sessionCookie, loginData)).isEmpty();
    }

    @Test
    void userAlreadyLoggedIn() {
        UUID uuid = UUID.randomUUID();
        Context context = mock(Context.class);
        String sessionCookie = uuid.toString();
        LoginData loginData = new LoginData("test", "password");

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        when(sessionDao.validate(uuid)).thenReturn(UserId.of(1));

        assertThat(loginController.login(context, sessionCookie, loginData)).isEmpty();
    }

    @Test
    void isLoggedIn() {
        final UUID uuid = UUID.randomUUID();
        UserId userId = UserId.of(1);
        List<Role> roles = List.of(Role.ADMIN, Role.USER);

        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        when(sessionDao.validate(uuid)).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(roles);

        assertThat(loginController.isLoggedIn(uuid.toString())).isEqualTo(roles);
    }

    @Test
    void isNotLoggedIn() {
        LoginController loginController = new LoginController(userService, roleDao, sessionDao);

        assertThat(loginController.isLoggedIn("")).isEmpty();;
    }
}
