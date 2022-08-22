package org.userway.assignment.litstull.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.data.repository.LinkDataRepository;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;
import org.userway.assignment.litstull.service.exceptions.NotFoundOriginException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ShortererServiceTest {

    @Autowired
    ShortenerService shortenerService;
    @Autowired
    LinkDataRepository linkDataRepository;

    @Test
    @DisplayName("Test Should Return Right LinkData")
    void getOrigin() throws NotFoundOriginException {
        linkDataRepository.save(new LinkData("origin", "short"));

        assertEquals(
                "origin",
                shortenerService.getOrigin("short").getOrigin()
        );

        linkDataRepository.deleteAll();
    }

    @Test
    @DisplayName("Test Should Throw NotFoundException")
    void getOriginNotFound() {

        NotFoundOriginException thrown = assertThrows(
                NotFoundOriginException.class,
                () -> shortenerService.getOrigin("https://google.com"),
                "Expected getOrigin() to throw NotFoundOriginException");

    }

    @Test
    @DisplayName("Test Should Throw Exception When Origin Link Not Valid")
    void testGetShortValid() {
        BadRequestLink thrown = assertThrows(
                BadRequestLink.class,
                () -> shortenerService.getShort("http://invalid"),
                "Expected getShort() to throw BadRequestLink");
    }
}