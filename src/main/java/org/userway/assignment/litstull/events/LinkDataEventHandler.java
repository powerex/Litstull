package org.userway.assignment.litstull.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;
import org.userway.assignment.litstull.data.domain.LinkData;
import org.userway.assignment.litstull.data.repository.LinkDataRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class LinkDataEventHandler extends AbstractMongoEventListener<LinkData> {

    @Autowired
    CacheManager cacheManager;
    @Autowired
    LinkDataRepository linkDataRepository;

    public void onBeforeDelete(BeforeDeleteEvent<LinkData> event) {
        String id = String.valueOf(event.getSource().get("_id"));
        Optional<LinkData> linkData = linkDataRepository.findById(id);
        linkData.ifPresent(data -> Objects.requireNonNull(cacheManager.getCache("originslinks")).evictIfPresent(data.getLink()));
        linkData.ifPresent(data -> Objects.requireNonNull(cacheManager.getCache("shortslinks")).evictIfPresent(data.getOrigin()));
    }

}
