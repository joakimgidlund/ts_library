package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class RealNameTest {
    @ParameterizedTest
    @ValueSource(strings = {"bull", "test testerman", "horrible", "drat", "darn", "stupid", "butt butt butt"})
    void wordInBadList(String source) {
        assertThat(RealName.validate(source)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"this should be fine", "another good one", "myname", "1231231", "[{$[£]}]"})
    void validWords(String source) {
        assertThat(RealName.validate(source)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void nullOrEmptyInput(String source) {
        // The documentation doesn't mention empty names and the code marks them as valid.
        assertThat(RealName.validate(source)).isTrue();     
    }
}
