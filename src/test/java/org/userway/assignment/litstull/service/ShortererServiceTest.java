package org.userway.assignment.litstull.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShortererServiceTest {

    @Autowired
    ShortenerService shortenerService;
    @MockBean
    LinkDataService linkDataService;

    @Test
    @DisplayName("Test Should Throw Exception When Origin Link Not Valid")
    void testGetShortValid() {
        BadRequestLink thrown = assertThrows(
                BadRequestLink.class,
                () -> shortenerService.getShort("http://invalid"),
                "Expected getShort() to throw BadRequestLink");
    }

    private Method getIsValidLink() throws NoSuchMethodException {
        Method method = ShortenerService.class.getDeclaredMethod("isValidLink", String.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @DisplayName("Test Should Correct Validate Url Links")
    void validLink() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertTrue((Boolean) getIsValidLink().invoke(shortenerService,"https://google.com"));
        assertTrue((Boolean) getIsValidLink().invoke(shortenerService,"http://google.com"));
        assertTrue((Boolean) getIsValidLink().invoke(shortenerService,"https://urk.net"));
        assertTrue((Boolean) getIsValidLink().invoke(shortenerService,"https://www.dou.ua"));
        assertTrue((Boolean) getIsValidLink().invoke(shortenerService,"http://urk.net"));
        assertFalse((Boolean) getIsValidLink().invoke(shortenerService,"https://com"));
        assertFalse((Boolean) getIsValidLink().invoke(shortenerService,"com"));
    }

    @Test
    @DisplayName("Test Should Return Right Origin")
    void getOrigin() {
        Mockito.when(linkDataService.getByShort("https://shorts")).thenReturn(
                new LinkData("origin", "https://shorts")
        );
        assertEquals("origin", shortenerService.getOrigin("https://shorts"));
    }

}