package org.userway.assignment.litstull.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.data.repository.LinkDataRepository;
import org.userway.assignment.litstull.service.exceptions.NotFoundShortException;
import org.userway.assignment.litstull.utils.Shortener;

import java.util.Optional;

@Slf4j
@Service
public class LinkDataService {

    @Autowired
    LinkDataRepository linkDataRepository;

    @Autowired
    Shortener shortener;

    public boolean isExist(String shortLink) {
        return linkDataRepository.findByLink(shortLink).isPresent();
    }

    public LinkData saveGeneratedShortByOrigin(String origin) {
        String[] parts = origin.split("://");
        return linkDataRepository.save(
                new LinkData(
                        origin,
                        parts[0] + "://" + shortener.getShort()));
    }

    public LinkData getByShort(String shortLink) {
        return linkDataRepository.findByLink(shortLink).orElse(new LinkData("error", ""));
    }

    public LinkData getLinkDataByOrigin(String origin) throws NotFoundShortException {
        Optional<LinkData> linkDataOptional = linkDataRepository.findFirstByOrigin(origin);
        if (linkDataOptional.isPresent()) {
            return linkDataOptional.get();
        } else {
            try {
                return saveGeneratedShortByOrigin(origin);
            } catch (Exception exception) {
                log.warn(String.format("getShort(): Can't create short link for %s.\nException: %s", origin, exception.getMessage()));
                throw new NotFoundShortException();
            }
        }
    }
}
