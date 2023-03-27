package pokemon;

import org.junit.jupiter.api.Test;
import rife.test.MockConversation;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {
    @Test
    void verifyRoot() {
        var m = new MockConversation(new PokemonSite());
        assertEquals(m.doRequest("/").getStatus(), 200);
    }
}
