package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class UtilsTest {
    @ParameterizedTest
    @ValueSource(strings = {"l33t sp4ak", "7rou61e", "g4m3r g4m3r g4m3r", "9u357"})
    void leetSpeakConversion(String value) {
        assertThat(Utils.cleanAndUnLeet(value).replaceAll("\\W+", "")).isAlphabetic();
    }

    @Test
    void leetSpeakSpecialCharacters() {
        assertThat(Utils.cleanAndUnLeet("l@7e")).isEqualTo("lte");
    }

    @ParameterizedTest
    @ValueSource(strings = {"112!hgahsf", "test test test test", "l33t sp34k", "@anders"})
    void removeNonAlphabetic(String value) {
        assertThat(Utils.onlyLettersAndWhitespace(value).replaceAll("\\W+", "")).isAlphabetic();
    }

    @Test
    void specialCharactersOnlyReturnsEmpty() {
        assertThat(Utils.onlyLettersAndWhitespace("@!#%(/)/&(/&(/%)))")).isEmpty();
        assertThat(Utils.cleanAndUnLeet(")/&(/!)/)(/)")).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullAndEmptyUtils(String source) {
        assertThat(Utils.cleanAndUnLeet(source).replaceAll("\\W+", "")).isEmpty();
        assertThat(Utils.onlyLettersAndWhitespace(source).replaceAll("\\W+", "")).isEmpty();
    }
}
