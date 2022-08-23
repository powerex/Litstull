package org.userway.assignment.litstull.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.data.repository.LinkDataRepository;
import org.userway.assignment.litstull.service.exceptions.NotFoundShortException;
import org.userway.assignment.litstull.utils.Shortener;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkDataServiceTest {

    @Autowired
    LinkDataService linkDataService;

    @MockBean
    LinkDataRepository linkDataRepository;
    @MockBean
    Shortener shortener;


    @Test
    @DisplayName("Test Should Return True If Link Exists In Repository")
    void isExist() {
        Mockito.when(linkDataRepository.findByLink("some_existing_link"))
                .thenReturn(Optional.of(new LinkData("origin", "some_existing_link")));
        assertTrue(linkDataService.isExist("some_existing_link"));
    }

    @Test
    @DisplayName("Test Should Return False If Link No Exists In Repository")
    void isNoExist() {
        Mockito.when(linkDataRepository.findByLink("some_no_existing_link"))
                .thenReturn(Optional.empty());
        assertFalse(linkDataService.isExist("some_no_existing_link"));
    }

    @Test
    void saveGeneratedShortByOrigin() {
        LinkData linkData = new LinkData("https://origin", "https://qwerty");
        Mockito.when(linkDataRepository.save(linkData)).thenReturn(linkData);
        Mockito.when(shortener.getShort()).thenReturn("qwerty");
        assertEquals(linkData, linkDataService.saveGeneratedShortByOrigin("https://origin"));
    }

    @Test
    @DisplayName("Test Should Return Right LinkData From Repository")
    void getByShort() {
        LinkData linkData = new LinkData("origin", "some_existing_link");
        Mockito.when(linkDataRepository.findByLink("some_existing_link"))
                .thenReturn(Optional.of(linkData));
        assertEquals(linkData, linkDataService.getByShort("some_existing_link"));
    }

    @Test
    @DisplayName("Test Should Return Error LinkData From Repository")
    void getByShortNoExist() {
        LinkData linkData = new LinkData("error", "");
        Mockito.when(linkDataRepository.findByLink("some_no_existing_link"))
                .thenReturn(Optional.empty());
        assertEquals(linkData, linkDataService.getByShort("some_no_existing_link"));
    }

    @Test
    @DisplayName("Test Should Return Right Origin LinkData From Repository")
    void getLinkDataByOrigin() throws NotFoundShortException {
        LinkData linkData = new LinkData("some_existing_link", "shorts");
        Mockito.when(linkDataRepository.findFirstByOrigin("some_existing_link"))
                .thenReturn(Optional.of(linkData));
        assertEquals(linkData, linkDataService.getLinkDataByOrigin("some_existing_link"));
    }
}