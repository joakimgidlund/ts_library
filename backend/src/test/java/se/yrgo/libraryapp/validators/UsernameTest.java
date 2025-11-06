package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class UsernameTest {
    @ParameterizedTest
    @ValueSource(strings = {"bosse", "And-ers", "joakim_gidlund", "@tester", "Quad", "...."})
    void correctUsername() {
        assertThat(Username.validate("bosse")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"name with space", "!notallowed", "!!)(!(/(&¤/&¤#)))"})
    void badCharactersInUsername(String username) {
        assertThat(Username.validate(username)).isFalse();
    }

    @Test
    void shortUsername() {
        assertThat(Username.validate("fin")).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullOrEmptyUsername(String username) {
        assertThat(Username.validate(username)).isFalse();
    }
}
