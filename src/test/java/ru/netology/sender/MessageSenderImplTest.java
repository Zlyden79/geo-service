package ru.netology.sender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ru.netology.sender.MessageSenderImpl.IP_ADDRESS_HEADER;

class MessageSenderImplTest {
    GeoService gs;
    LocalizationService ls;
    MessageSenderImpl msi;

    public static Stream<Arguments> testSend() {
        Map<String, String> headers1, headers2, headers3, headers4;
        //Москва
        headers1 = new HashMap<String, String>();
        headers1.put(IP_ADDRESS_HEADER, GeoServiceImpl.MOSCOW_IP);
        //Нью-Йорк
        headers2 = new HashMap<String, String>();
        headers2.put(IP_ADDRESS_HEADER, GeoServiceImpl.NEW_YORK_IP);
        //ЛОКАЛОСТ
        headers3 = new HashMap<String, String>();
        headers3.put(IP_ADDRESS_HEADER, GeoServiceImpl.LOCALHOST);
        //Любой не российский и не американский IP
        headers4 = new HashMap<String, String>();
        headers4.put(IP_ADDRESS_HEADER, "216.54.32.15");


        return Stream.of(
                Arguments.of(headers1, "Добро пожаловать"),
                Arguments.of(headers2, "Welcome"),
                Arguments.of(headers3, "Welcome"),
                Arguments.of(headers4, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testSend(Map<String, String> headers, String expected) {
        //arrange
        String ip = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        //Логика в тесте, какую заглушку применять
        //Если Россия - то заглушки русские
        if (ip.startsWith("172.")) {
            gs = Mockito.mock(GeoService.class);
            Mockito.when(gs.byIp(Mockito.anyString()))
                    .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
            ls = Mockito.mock(LocalizationService.class);
            Mockito.when(ls.locale(Mockito.any(Country.class))).
                    thenReturn("Добро пожаловать");
            //Иначе - заглушки американские
        } else {
            gs = Mockito.mock(GeoService.class);
            Mockito.when(gs.byIp(Mockito.anyString()))
                    .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
            ls = Mockito.mock(LocalizationService.class);
            Mockito.when(ls.locale(Mockito.any(Country.class))).
                    thenReturn("Welcome");
        }
        msi = new MessageSenderImpl(gs, ls);
        //act
        String result = msi.send(headers);
        //assert
        Assertions.assertEquals(expected, result);
    }

    @AfterEach
    public void cleanUp() {
        GeoService gs = null;
        LocalizationService ls = null;
        MessageSenderImpl msi = null;
    }
}
/*
    //тестируем RUSSIA
    @Test
    void testSendRussia() {

        //arrange
        gs = Mockito.mock(GeoService.class);
        Mockito.when(gs.byIp(Mockito.anyString()))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

        ls = Mockito.mock(LocalizationService.class);
        Mockito.when(ls.locale(Mockito.any(Country.class))).
                thenReturn("Добро пожаловать");

        msi = new MessageSenderImpl(gs, ls);

        //arrange
        String expected = "Добро пожаловать";
        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, GeoServiceImpl.MOSCOW_IP);

        //act
        String result = msi.send(headers);
        //assert
        Assertions.assertEquals(expected, result);
    }


    //тестируем USA
    @Test
    void testSendUSA() {

        //arrange
        gs = Mockito.mock(GeoService.class);
        Mockito.when(gs.byIp(Mockito.anyString()))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        ls = Mockito.mock(LocalizationService.class);
        Mockito.when(ls.locale(Mockito.any(Country.class))).
                thenReturn("Welcome");

        msi = new MessageSenderImpl(gs, ls);

        String expected = "Welcome";
        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, GeoServiceImpl.NEW_YORK_IP);

        //act
        String result = msi.send(headers);
        //assert
        Assertions.assertEquals(expected, result);
    }
*/

