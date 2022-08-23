package org.userway.assignment.litstull.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.userway.assignment.litstull.model.ShortLinkResponseDto;
import org.userway.assignment.litstull.service.ShortenerService;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ShortenerController.class)
class ShortererControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ShortenerService shortenerService;

    @Test
    @DisplayName("Test Should Pass With Status OK")
    void getShortUrl() throws Exception {
        ShortLinkResponseDto resp = new ShortLinkResponseDto("httpd://qwerty");
        Mockito.when(shortenerService.getShort("https://google.com")).thenReturn(resp);
        String url = "/litstull/v1/cut?originUrl=https://google.com";
        mockMvc.perform(post(url)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test Should Pass With Status BAD_REQUEST")
    void getShortUrlBad() throws Exception {
        Mockito.when(shortenerService.getShort("https://com")).thenThrow(new BadRequestLink());
        String url = "/litstull/v1/cut?originUrl=https://com";
        mockMvc.perform(post(url)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test Should Pass With Redirection Anyway")
    void getOriginUrl() throws Exception {
        Mockito.when(shortenerService.getOrigin("https://no")).thenReturn("error");
        String url = "/litstull/v1/restore?shortLink=https://no";
        mockMvc.perform(get(url)).andExpect(status().is3xxRedirection());
    }
}