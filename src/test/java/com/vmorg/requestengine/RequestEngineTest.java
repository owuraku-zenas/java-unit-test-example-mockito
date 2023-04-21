package com.vmorg.requestengine;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.exceptions.InvalidHostnameException;
import com.vmorg.exceptions.MachineNotCreatedException;
import com.vmorg.exceptions.UserNotEntitledException;
import com.vmorg.virtualmachine.Desktop;
import com.vmorg.virtualmachine.Machine;
import com.vmorg.virtualmachine.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestEngineTest {
    private RequestEngine requestEngineTest;
    private Desktop desktop;
    private Desktop desktop2;
    private Server server;
    private Server server2;

    @Mock
    private AuthorisingService authorisingService;

    @Mock
    SystemBuildService systemBuildService;



    @BeforeEach
    void setUp() throws InvalidHostnameException {

        authorisingService = mock(AuthorisingService.class);
        systemBuildService = mock(SystemBuildService.class);
        requestEngineTest = new RequestEngine(authorisingService, systemBuildService);
        desktop = new Desktop("host20230407001", "Zenas", 4, 30, 128, "11", "20H1");
        desktop2 = new Desktop("host20230407004", "Zenas", 4, 30, 128, "11", "20H1");
        server = new Server("host20230407002", "Zenas", 4, 30, 128, "Ubuntu", 85, "JIHIH", "Email Team");
        server2 = new Server("host20230407003", "Zenas", 4, 30, 128, "Ubuntu", 85, "JIHIH", "Email Team");
    }

    // Build limit test
    // TODO: Come back to this. Use a look to create 999 successful builds
    @Test
    void testMaxLimitOfBuildsForDay() throws MachineNotCreatedException, UserNotEntitledException {
        requestEngineTest.setTotalSuccessfulBuildsForDay(999);
        assertThrows(MachineNotCreatedException.class, () -> requestEngineTest.createNewRequest(desktop));
    }

    // Machine not created exception test
    // Unauthorised user test
    @Test
    void testFailedAuthorization()  {
        when(authorisingService.isAuthorised(anyString())).thenReturn(false);
        assertThrows(UserNotEntitledException.class, () -> requestEngineTest.createNewRequest(desktop));
    }

    // Authorised user test
    @Test
    void testSuccessfulAuthorisation() {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn("host20230407001");
        assertDoesNotThrow(()->requestEngineTest.createNewRequest(desktop));
    }

    // Successful build
    @Test
    void testSuccessfulBuild() {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn("host20230407001");
        assertDoesNotThrow(() -> requestEngineTest.createNewRequest(desktop));
    }

    // Failed build
    @Test
    void testFailedBuild() throws MachineNotCreatedException, UserNotEntitledException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn("");
        assertThrows(MachineNotCreatedException.class,()-> requestEngineTest.createNewRequest(desktop));
    }


//    Test wherether user has been added to the map
    @Test
    void testUserAddedToBuildByUserForDay() throws UserNotEntitledException, MachineNotCreatedException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn(server.getRequestorName());
        requestEngineTest.createNewRequest(server);
        Map<String, Map<String, Integer>> users = requestEngineTest.totalBuildsByUserForDay();
        assertTrue(users.containsKey(server.getRequestorName()));
    }


//    Test whether user's first Server build has been added to his list of builds in the map
    @Test
    void testUserFirstServerBuildAddedToBuildByUserForDay() throws UserNotEntitledException, MachineNotCreatedException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn(server.getRequestorName());
        requestEngineTest.createNewRequest(server);
        Map<String, Map<String, Integer>> users = requestEngineTest.totalBuildsByUserForDay();

        assertEquals(users.get(server.getRequestorName()).get(server.getMachineType()), 1);
    }

//    Test whether user Server build has been added to his list of builds in the map
    @Test
    void testUserServerBuildAddedToBuildByUserForDay() throws UserNotEntitledException, MachineNotCreatedException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn(server.getRequestorName());
        requestEngineTest.createNewRequest(server);
        int servers = requestEngineTest.totalBuildsByUserForDay().get(server.getRequestorName()).get(server.getMachineType());
        requestEngineTest.createNewRequest(server2);

        assertEquals(requestEngineTest.totalBuildsByUserForDay().get(server2.getRequestorName()).get(server2.getMachineType()), ++servers);
    }

//    Test whether user's first Desktop build has been added to his list of builds in the map
    @Test
    void testUserFirstDesktopBuildAddedToBuildByUserForDay() throws UserNotEntitledException, MachineNotCreatedException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn(desktop.getRequestorName());
        requestEngineTest.createNewRequest(desktop);
        Map<String, Map<String, Integer>> users = requestEngineTest.totalBuildsByUserForDay();

        assertEquals(users.get(desktop.getRequestorName()).get(desktop.getMachineType()), 1);
    }

//    Test whether user Desktop build has been added to his Desktop build list
    @Test
    void testUserDesktopBuildAddedToBuildByUserForDay() throws UserNotEntitledException, MachineNotCreatedException {
        when(authorisingService.isAuthorised(anyString())).thenReturn(true);
        when(systemBuildService.createNewMachine(any(Machine.class))).thenReturn(desktop.getRequestorName());
        requestEngineTest.createNewRequest(desktop);
        int desktops = requestEngineTest.totalBuildsByUserForDay().get(desktop.getRequestorName()).get(desktop.getMachineType());
        requestEngineTest.createNewRequest(desktop2);

        assertEquals(requestEngineTest.totalBuildsByUserForDay().get(desktop2.getRequestorName()).get(desktop2.getMachineType()), ++desktops);
    }
}