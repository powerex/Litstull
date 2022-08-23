package org.userway.assignment.litstull.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.userway.assignment.litstull.service.LinkDataService;

import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Shortener {

    @Autowired
    LinkDataService linkDataService;

    // exclude same-looking characters 1 & l, O & 0
    private static final String alphabet = "ABCDEFGHIJKLNPQRSTVWXYZabcdefgijkmnopqrstvwxyz23456789";

    // after this count of tries to get short link, increase length of character in short link
    private static final int LOOP_TRIES = 10000;

    private static final int LINK_LENGTH = 6;

    /**
     * Concat a specified number of characters (default value = {@link Shortener#LINK_LENGTH})
     * from the {@link Shortener#alphabet}
     *
     * @return String
     */
    public String getShort() {
        int length = LINK_LENGTH;
        boolean codeGen;
        String newShort;
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
            codeGen = linkDataService.isExist(newShort);
        } while (codeGen);
        return newShort;
    }

}
