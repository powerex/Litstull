package org.userway.assignment.litstull.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShortenerTest {

    @Autowired
    Shortener shortener;

    @Test
    @Repeat(value = 100)
    @DisplayName("Test Should Pass When getShort() Contains 6 Symbols by Default")
    void testGetShort() {
        String receivedShort = shortener.getShort();
        assertTrue(receivedShort.matches("[A-N,P-Z,a-k,m-z,2-9]{6}"));
    }

    @Test
    @Repeat(value = 100)
    @DisplayName("Test Should Pass When getShort() No Contains Same-looking Symbols")
    void testGetShortWithoutSameLookingSymbols() {
        String receivedShort = shortener.getShort();
        assertFalse(receivedShort.matches("[O,0,l,1]"));
    }
}