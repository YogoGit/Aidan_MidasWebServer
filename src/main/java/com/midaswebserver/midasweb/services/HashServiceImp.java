package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @version 0.0.1
 * implements custom implementation of hash used primarily in password hashing
 * TODO: move hashing of passwords to the user service
 * @Author Aidan Scott
 * @since 0.0.1
 */
@Service
public class HashServiceImp implements HashService {
    private static final Logger log = LoggerFactory.getLogger(HashServiceImp.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public HashServiceImp() {
    }

    /**
     * Hashes a password using java hashing (bad)
     *
     * @param rawPass raw unhashed password
     * @return hashed password
     */
    @Override
    public String getHash(String rawPass) {
        return encoder.encode(rawPass);
    }

    /**
     * Checks to see if the two input passwords match
     *
     * @param rawPass
     * @param hashedPass
     * @return true if match, false if !match
     */
    @Override
    public Boolean checkMatch(String rawPass, String hashedPass) {
        return encoder.matches(rawPass, hashedPass);
    }

}
