package com.midaswebserver.midasweb.services;

import com.midaswebserver.midasweb.models.User.Symbol;
import com.midaswebserver.midasweb.models.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@Transactional
@SpringBootTest
public class UserServiceImpTest {

    @Autowired
    private UserService userService;

    /**
     * arguably we test the validateUniqueUsername using these userAdd tests
     */
    //good test
    @Test
    public void goodAddOneUser1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("Test User Couldn't be added", userService.add(testUser));
        assertNotNull("User was not found in database", userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void goodAddOneUser2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("Test User Couldn't be added", userService.add(testUser));
        assertNotNull("User was not found in database", userService.getUserByUsername(testUser.getUsername()));
    }

    /**
     * Checking if more than one user can be added to the database
     */
    @Test
    public void goodUserAddTwoUsers() {
        final User testUser1 = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        final User testUser2 = new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        assertTrue("TestUser2 wasn't added in add method", userService.add(testUser1));
        assertTrue("TestUser3 wasn't added in add method", userService.add(testUser2));
        assertTrue("TestUser2 was added but not found", testUser1 == userService.getUserByUsername(testUser1.getUsername()));
        assertTrue("TestUser3 was added but not found", testUser2 == userService.getUserByUsername(testUser2.getUsername()));
    }

    /**
     * if two users of the same first name are added, should return false
     */
    @Test
    public void badUserAddSameUserTwice1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("testUser wasn't added first time", userService.add(testUser));
        assertFalse("testUser was added second time", userService.add(testUser));
        assertTrue("testUser not expected", testUser == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void badUserAddSameUserTwice2() {
        final User testUser = new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        assertTrue("testUser wasn't added first time", userService.add(testUser));
        assertFalse("testUser was added second time", userService.add(testUser));
        assertTrue("TestUser not expected", testUser == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void crazyUserAddNull() {
        assertFalse("User was added", userService.add(null));
    }

    @Test
    public void crazyUserAddBlank() {
        assertFalse("User was added", userService.add(new User()));
    }

    @Test
    public void goodDeleteUserAndCheck1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertTrue("Added validation failed", testUser == userService.getUserByUsername(testUser.getUsername()));
        assertTrue("TestUser wasn't deleted", userService.delete(testUser));
        assertTrue("Deletion validation failed", null == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void goodDeleteUserAndCheck2() {
        final User testUser = new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertTrue("Added validation failed", testUser == userService.getUserByUsername(testUser.getUsername()));
        assertTrue("TestUser wasn't deleted", userService.delete(testUser));
        assertTrue("Deletion validation failed", null == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void goodAddTwoUsersDeleteOne3() {
        final User testUser1 = new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        final User testUser2 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser1 wasn't added", userService.add(testUser1));
        assertTrue("TestUser2 wasn't added", userService.add(testUser2));
        assertTrue("TestUser1 Added validation failed", testUser1 == userService.getUserByUsername(testUser1.getUsername()));
        assertTrue("TestUser1 Added validation failed", testUser2 == userService.getUserByUsername(testUser2.getUsername()));
        assertTrue("TestUser1 Deletion validation failed", userService.delete(testUser1));
        assertTrue("TestUser1 wasn't deleted", null == userService.getUserByUsername(testUser1.getUsername()));
        assertTrue("TestUser2 returned not expected response", testUser2 == userService.getUserByUsername(testUser2.getUsername()));
    }

    @Test
    public void BadAddTwoUsersDeleteWrongOne3() {
        final User testUser1 = new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        final User testUser2 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser1 wasn't added", userService.add(testUser1));
        assertTrue("TestUser2 wasn't added", userService.add(testUser2));
        assertTrue("TestUser1 Added validation failed", testUser1 == userService.getUserByUsername(testUser1.getUsername()));
        assertTrue("TestUser1 Added validation failed", testUser2 == userService.getUserByUsername(testUser2.getUsername()));
        assertTrue("TestUser1 Deletion validation failed", userService.delete(testUser1));
        assertFalse("TestUser2 was deleted", null == userService.getUserByUsername(testUser2.getUsername()));
        assertFalse("TestUser1 returned not expected response", testUser1 == userService.getUserByUsername(testUser1.getUsername()));
    }

    @Test
    public void badDeleteUnknownUserUser1() {
        final User testUser1 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final User testUser2 = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser1 wasn't added", userService.add(testUser1));
        assertFalse("TestUser2 Deletion validation failed", userService.delete(testUser2));
        assertFalse("TestUser1 was deleted, should have not been deleted", null == userService.getUserByUsername(testUser1.getUsername()));
    }

    @Test
    public void badDeleteUnknownUser2() {
        final User testUser1 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final User testUser2 = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser2 wasn't added", userService.add(testUser2));
        assertFalse("TestUser1 Deletion failed", userService.delete(testUser1));
        assertFalse("TestUser2 was deleted, should have not been deleted", null == userService.getUserByUsername(testUser2.getUsername()));
    }

    @Test
    public void crazyDeleteUserWithNull1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertFalse("A User was deleted", userService.delete(null));
        assertFalse("TestUser was deleted", null == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void goodGetUserByID1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertFalse("Returned user was not expected", null == userService.getUserById(testUser.getId()));
    }

    @Test
    public void goodGetUserByID2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertFalse("TestUser wasn't returned", null == userService.getUserById(testUser.getId()));
    }

    @Test
    public void badGetUserByID1() {
        final User testUser1 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final User testUser2 = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser1 wasn't added", userService.add(testUser1));
        assertTrue("User was returned", null == userService.getUserById(testUser2.getId()));
    }

    @Test
    public void badGetUserByID2() {
        final User testUser1 = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final User testUser2 = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser2 wasn't added", userService.add(testUser2));
        assertTrue("Returned User was the same then added user", null == userService.getUserById(testUser1.getId()));
    }

    @Test
    public void crazyGetUserByID1() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser2 wasn't added", userService.add(testUser));
        assertTrue("Returned User wasn't null", null == userService.getUserById(null));
    }

    @Test
    public void goodGetUserByName1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertTrue("Failed to return testUser", testUser == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void goodGetUserByName2() {
        final User testUser= new User
                ("LordCabbage", "CabbageMan", "pleaseImSafe@friendly.edu", "23414443333");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertTrue("Failed to return testUser", testUser == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void badGetUserByName1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertFalse("User was found where they shouldn't have been", testUser == userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    public void crazyGetUserByName1() {
        assertTrue("User was found with input value null", null == userService.getUserByUsername(null));
    }

    /**
     * Checking if data can be updated within the database. This requires getting the object from the database with the
     * specific Id, then changing its data, then using that updated object in the update method
     */
    @Test //Test if username can be updated (good)
    public void goodUpdateUserTest1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        String newUsername = "MadAtlas2";
        testUser.setUsername(newUsername);
        userService.update(testUser);
        //retrieving the user and seeing if the name was saved
        User changedUser = userService.getUserByUsername(newUsername);
        assertTrue("Username wasnt changed", newUsername.equals(changedUser.getUsername()));
        userService.delete(changedUser);
    }

    @Test
    public void goodUpdateUserTest2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        String newUsername = "MadAtlas3";
        User user = userService.getUserByUsername(testUser.getUsername());
        user.setUsername(newUsername);
        userService.update(user);
        //retrieving the user and seeing if the name was saved
        User changedUser = userService.getUserByUsername(newUsername);
        assertTrue("Username wasnt changed", newUsername.equals(changedUser.getUsername()));
        userService.delete(changedUser);
    }

    @Test
    public void goodUpdateUserTest3() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        String newPhonenumber = "4066604455";
        testUser.setPhoneNumber(newPhonenumber);
        userService.update(testUser);
        //retrieving the user and seeing if the name was saved
        User changedUser = userService.getUserByUsername(testUser.getUsername());
        assertTrue("Phone number wasn't changed", newPhonenumber.equals(changedUser.getPhoneNumber()));
    }

    @Test
    public void badUpdateUserTest1() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        Long newId = (long) 40666;
        testUser.setId(newId);
        assertFalse("User was updated, when they shouldn't have", userService.update(testUser));
    }

    @Test
    public void crazyUpdateUserTest1() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        assertFalse("User was updated, when null was passed", userService.update(null));
    }

    /**
     * There are a couple bugs related to updating settings.
     * User must be added to the setting object, this shouldn't be the case
     * validation must be included to make sure that invalid information isn't used to update
     */
    @Test
    public void goodUpdateSymbolTest1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        String ticker = "EUC";
        Symbol symbol = new Symbol(ticker);
        symbol.setUserId(testUser);//I shouldnt have to add the user to the symbol
        testUser.addSymbol(symbol);
        assertTrue("User couldn't be updated", userService.update(testUser));
        //retrieving the user and seeing if the name was saved
        User changedUser = userService.getUserByUsername(testUser.getUsername());
        Symbol[] setArray = changedUser.getSymbol().toArray(new Symbol[changedUser.getSymbol().size()]);
        List<Symbol> setList = new ArrayList<>(changedUser.getSymbol());
        assertTrue("Couldnt delete changeduser", userService.delete(changedUser));
        assertTrue("Returned ticker and set ticker wasn't the same", setList.get(0).getTicker().equals(ticker));
    }

    @Test
    public void goodUpdateSymbolTest2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        assertTrue("TestUser wasn't added", userService.add(testUser));
        //sets updates username
        String ticker = "EUC";
        Symbol symbol = new Symbol(ticker);
        symbol.setUserId(testUser);//I shouldnt have to add the user to the symbol
        testUser.addSymbol(symbol);
        assertTrue("User couldn't be updated", userService.update(testUser));
        //retrieving the user and seeing if the name was saved
        User changedUser = userService.getUserByUsername(testUser.getUsername());
        Symbol[] setArray = changedUser.getSymbol().toArray(new Symbol[changedUser.getSymbol().size()]);
        List<Symbol> setList = new ArrayList<>(changedUser.getSymbol());
        assertTrue("Couldnt delete changeduser", userService.delete(changedUser));
        assertTrue("Returned ticker and set ticker wasn't the same", setList.get(0).getTicker().equals(ticker));
    }

    @Test
    public void goodAddSymbolInvalidSymbol1() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        final String symbol = "UECCC";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertFalse("Input symbol was valid, should have been invalid", userService.addSymbolToUser(testUser, symbol));
    }

    @Test
    public void goodAddSymbolInvalidSymbol2() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final String symbol = "APPLE";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertFalse("Input symbol was valid, should have been invalid", userService.addSymbolToUser(testUser, symbol));
    }

    @Test
    public void goodAddSymbolValidSymbol1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final String symbol = "AAPL";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertTrue("Input symbol was invalid, should have been valid", userService.addSymbolToUser(testUser, symbol));
    }

    @Test
    public void goodAddSymbolValidSymbol2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        final String symbol = "UEC";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertTrue("Input symbol was invalid, should have been valid", userService.addSymbolToUser(testUser, symbol));
    }

    @Test
    public void goodAddSymbolUserNotExist1() {
        final String symbol = "UEC";
        assertFalse("Input symbol was valid, should have been invalid", userService.addSymbolToUser(null, symbol));
    }

    @Test
    public void goodAddSymbolUserNotExist2() {
        final String symbol = "AAPL";
        assertFalse("Input symbol was valid, should have been invalid", userService.addSymbolToUser(null, symbol));
    }

    @Test
    public void goodAddSymbolPreviouslySaved1() {
        final User testUser = new User
                ("CabbageMan", "password", "riblueliany@docomo.ne.jp", "4065556666");
        final String symbol = "AAPL";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertTrue("Input symbol and or user was invalid, should have been valid", userService.addSymbolToUser(testUser, symbol));
        assertTrue("Input symbol and or user was invalid, second add should have returned true", userService.addSymbolToUser(testUser, symbol));

        List<String> dupedTickers = new ArrayList<>();
        for (Symbol symbolSaved : testUser.getSymbol()) {
            if (symbolSaved.getTicker().equalsIgnoreCase(symbol)) {
                dupedTickers.add(symbolSaved.getTicker());
            }
        }
        assertTrue("Ticker was duplicated, found more than one instance", dupedTickers.size() == 1);
    }

    @Test
    public void goodAddSymbolPreviouslySaved2() {
        final User testUser = new User
                ("MonkeyKing", "PassW0rd", "一个@阿育王。印度", "861065529988");
        final String symbol = "UEC";
        assertTrue("User wasn't added to database", userService.add(testUser));
        assertTrue("Input symbol and or user was invalid, should have been invalid", userService.addSymbolToUser(testUser, symbol));
        assertTrue("Input symbol and or user was invalid, second add should have returned true", userService.addSymbolToUser(testUser, symbol));

        List<String> dupedTickers = new ArrayList<>();
        for (Symbol symbolSaved : testUser.getSymbol()) {
            if (symbolSaved.getTicker().equalsIgnoreCase(symbol)) {
                dupedTickers.add(symbolSaved.getTicker());
            }
        }
        assertTrue("Ticker was duplicated, non-one amount of instances found", dupedTickers.size() == 1);
    }

    @Test
    public void crazyNullInputs() {
        final User testUser = new User();
        assertFalse("Input symbol was invalid, should have been valid", userService.addSymbolToUser(testUser, null));
    }
}
