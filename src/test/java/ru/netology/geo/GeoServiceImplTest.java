package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

public class GeoServiceImplTest {
    GeoServiceImpl gsi = new GeoServiceImpl();

    //тестируем известные адреса (не дичь)
    public static Stream<Arguments> testByIp() {
        return Stream.of(
                Arguments.of("172.154.14.26", Country.RUSSIA),
                Arguments.of("96.154.14.26", Country.USA),
                Arguments.of(GeoServiceImpl.NEW_YORK_IP, Country.USA),
                Arguments.of(GeoServiceImpl.MOSCOW_IP, Country.RUSSIA),
                Arguments.of(GeoServiceImpl.LOCALHOST, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testByIp(String ip, Country expected) {
        //arrange
        //act
        Country result = gsi.byIp(ip).getCountry();
        //assert
        Assertions.assertEquals(expected, result);
    }

    //тестируем для IP из остального мира (дичь)
    //он отдельный от параметризованного теста выше потому,
    //что в нём ( в testByIp() ) использовался вызов метода getCountry(),
    //что на объекте null выбрасывает исключение. В случае с LOCALHOST
    //же объект Locale создавался (не null), хоть и был пустышкой по факту.
    @Test
    public void testByIpFromOtherСountries() {
        //arrange
        String ipAdress = "224.130.170.12";
        //act
        Location result  = gsi.byIp(ipAdress);
        //assert
        Assertions.assertNull(result);
    }
    //в задании про тестирование этого метода не написано,
    // но на вебинаре при разборе домашки было сказано протестить
    // его на выброс исключения
    @Test
    public void testByCoordinates() {
        //arrange
        double x, y;
        x = 1000.14;
        y = 2000.34;

        //act
        Executable action = () -> gsi.byCoordinates(x, y);

        //assert
        Assertions.assertThrowsExactly(RuntimeException.class, action);
    }
}
