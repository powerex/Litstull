package org.userway.assignment.litstull.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.data.repository.LinkDataRepository;
import org.userway.assignment.litstull.model.ShortLinkResponseDto;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;
import org.userway.assignment.litstull.service.exceptions.NotFoundOriginException;
import org.userway.assignment.litstull.service.exceptions.NotFoundShortException;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShortererService {

    @Autowired
    LinkDataRepository linkDataRepository;

    // exclude same-looking characters 1 & l, O & 0
    private static final String alphabet = "ABCDEFGHIJKLNPQRSTVWXYZabcdefgijkmnopqrstvwxyz23456789";

    // after this count of tries to get short link, increase length of character in short link
    private static final int LOOP_TRIES = 10000;

    private static final int LINK_LENGTH = 6;

    @Cacheable(value = "shortslinks", key = "#origin")
    public ShortLinkResponseDto getShort(String origin) throws NotFoundShortException, BadRequestLink {
        if (isValidLink(origin)) {
            String[] parts = origin.split("://");
            Optional<LinkData> linkDataOptional = linkDataRepository.findFirstByOrigin(origin);
            if (linkDataOptional.isPresent()) {
                return new ShortLinkResponseDto(linkDataOptional.get().getLink());
            } else {
                try {
                    LinkData linkData = linkDataRepository.save(
                            new LinkData(
                                    origin,
                                    parts[0] + "://" + getShort()));
                    return new ShortLinkResponseDto(linkData.getLink());
                } catch (Exception exception) {
                    log.warn(String.format("getShort(): Can't create short link for %s.\nException: %s", origin, exception.getMessage()));
                    throw new NotFoundShortException();
                }
            }
        } else {
            throw new BadRequestLink();
        }
    }

    @Cacheable(value = "originslinks", key = "#shortLink")
    public LinkData getOrigin(String shortLink) throws NotFoundOriginException {
        Optional<LinkData> linkDataOptional = linkDataRepository.findByLink(shortLink);
        return linkDataOptional.orElseThrow(NotFoundOriginException::new);
    }

    /**
     * Concat a specified number of characters (default value = {@link ShortererService#LINK_LENGTH})
     * from the {@link ShortererService#alphabet}
     *
     * @return String
     */
    private String getShort() {
        int length = LINK_LENGTH;
        boolean codeGen;
        String newShort = "";
        int loopTries = 0;

        do {
            if (loopTries > LOOP_TRIES) {
                loopTries = 0;
                log.info(String.format("Increase length of shorts to %d", ++length));
            }
            ++loopTries;
            newShort = new Random().ints(length, 0, alphabet.length())
                    .mapToObj(i -> String.valueOf(alphabet.charAt(i)))
                    .collect(Collectors.joining());
            codeGen = linkDataRepository.findByLink(newShort).isPresent();
        } while (codeGen);
        return newShort;
    }

    public void deleteById(String id) {
        linkDataRepository.deleteById(id);
    }

    private boolean isValidLink(String link) {
        return link.matches("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
    }

}
