package service;

import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import server.InvalidRequest;
import server.ServerException;
import service.Service;

public class ServiceTests {
    private static Service service;
    // Options: @BeforeAll, @BeforeEach, @AfterEach, @AfterAll
    // @Test


    @BeforeAll
    static void init(){
        MemoryDataAccess memoryDataAccess = new MemoryDataAccess();
        service = new Service(memoryDataAccess);
    }


    // DataAccess should use @ParameterizedTest with @ValueSource

    // Use Run with Coverage to make sure all code is run and tested
    @Test
    void registerSuccess(){
        var user = new UserData("Test Username", "Test Password", "Test Email");

        assertEquals(AuthData.class, service.register(user).getClass());
    }

    @Test
    void registerFailure(){
        var user = new UserData("Test Username", "Test Password", "Test Email");
        service.register(user);

        assertThrows(InvalidRequest.class, () -> service.register(user));
    }

    @Test
    void clear(){
        assertTrue(true);
        // First value is the expected value, second is what we're checking
        assertEquals(true, 1==1);
    }
}
