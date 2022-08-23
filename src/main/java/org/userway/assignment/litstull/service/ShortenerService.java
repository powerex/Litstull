package org.userway.assignment.litstull.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.model.ShortLinkResponseDto;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;
import org.userway.assignment.litstull.service.exceptions.NotFoundShortException;
import org.userway.assignment.litstull.utils.Shortener;

import java.util.Objects;

@Slf4j
@Service
public class ShortenerService {

    @Autowired
    Shortener shortener;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    LinkDataService linkDataService;

    @Cacheable(value = "shortslinks", key = "#origin")
    public ShortLinkResponseDto getShort(String origin) throws NotFoundShortException, BadRequestLink {
        if (isValidLink(origin)) {
            try {
                LinkData linkData = linkDataService.getLinkDataByOrigin(origin);
                return new ShortLinkResponseDto(linkData.getLink());
            } catch (Exception exception) {
                log.warn(String.format("getShort(): Can't create short link for %s.\nException: %s", origin, exception.getMessage()));
                throw new NotFoundShortException();
            }
        } else {
            throw new BadRequestLink();
        }
    }


    @Cacheable(value = "originslinks", key = "#shortLink")
    public String getOrigin(String shortLink) {
        LinkData linkData = linkDataService.getByShort(shortLink);
        return linkData.getOrigin();
    }

    private boolean isValidLink(String link) {
        return link.matches("[(http(s)?)://(www.)?a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)");
    }

//TODO delete before push
    public void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("originslinks")).clear();
        Objects.requireNonNull(cacheManager.getCache("shortslinks")).clear();
    }

}
