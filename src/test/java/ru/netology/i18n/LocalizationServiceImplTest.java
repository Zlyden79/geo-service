package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import java.util.stream.Stream;

public class LocalizationServiceImplTest {
    LocalizationServiceImpl lsi = new LocalizationServiceImpl();

    public static Stream<Arguments> testLocale() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.GERMANY, "Welcome"),
                Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.BRAZIL, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testLocale(Country country, String expected){
        //arrange

        //act
        String result = lsi.locale(country);
        //assert
        Assertions.assertEquals(expected, result);
    }
}
