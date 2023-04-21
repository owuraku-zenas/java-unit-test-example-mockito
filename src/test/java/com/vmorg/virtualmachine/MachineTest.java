package com.vmorg.virtualmachine;

import com.vmorg.exceptions.InvalidHostnameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {

    @Test
    void testValidHostname() {
        assertDoesNotThrow(() -> new Desktop("host20230407001", "Zenas", 4, 30, 128, "11", "20H1"));
    }

    @Test
    void testInvalidHostnameLength()  {
        assertThrows(InvalidHostnameException.class, () -> {
            Machine testMachine = new Desktop("host202304070015", "Zenas", 4, 30, 128, "11", "20H1");
        });
    }

    @Test
    void testInvalidHostnamePrefix()  {
        assertThrows(InvalidHostnameException.class, () -> new Desktop("hest20230407001", "Zenas", 4, 30, 128, "11", "20H1"));
    }

    @Test
    void testInvalidHostnameDate() {
        assertThrows(InvalidHostnameException.class, () -> new Desktop("host20231327001", "Zenas", 4, 30, 128, "11", "20H1"));
    }

}