package org.userway.assignment.litstull.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.userway.assignment.litstull.data.domain.LinkData;

import java.util.Optional;

@Repository
public interface LinkDataRepository extends MongoRepository<LinkData, String> {

    Optional<LinkData> findByLink(String link);
    Optional<LinkData> findFirstByOrigin(String origin);

}
