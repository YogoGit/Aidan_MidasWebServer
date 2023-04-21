package com.midaswebserver.midasweb.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class HashServiceImplTest {

    @Autowired
    private HashServiceImp hashService;

    @Test
    public void goodMashMatchTest1(){
        String rawPass = "Password";
        String hashedPass1 = hashService.getHash(rawPass);
        String hashedPass2 = hashService.getHash(rawPass);
        assertFalse("Hash results not equal", hashedPass1.equals(hashedPass2));
        assertTrue("MatchCheck inputs aren't the same", hashService.checkMatch(rawPass, hashedPass1));
    }

    @Test
    public void goodMashMatchTest2(){
        String rawPass1 = "Password";
        String rawPass2 = "Password";
        String hashedPass1 = hashService.getHash(rawPass1);
        assertTrue("MatchCheck, inputs aren't the same", hashService.checkMatch(rawPass2, hashedPass1));
    }
    @Test
    public void badMashMatchTest(){
        String rawPass1 = "Password";
        String rawPass2 = "Password ";
        String hashedPass1 = hashService.getHash(rawPass1);
        assertFalse("MatchCheck, inputs are the same", hashService.checkMatch(rawPass2, hashedPass1));
    }
}
