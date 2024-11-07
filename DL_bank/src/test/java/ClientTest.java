

import java.org.example.Client;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class ClientTest {
    @Test
    void testClientInitialization() {
        Client client = new Client(0,"Alice", "Smith", "alice.smith@example.com", "5554443333", "789 Elm St",new ArrayList<>());

        assertEquals("Alice", client.getFirstName());
        assertEquals("Smith", client.getLastName());
        assertEquals("alice.smith@example.com", client.getEmail());
    }
}