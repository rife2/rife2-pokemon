package pokemon;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rife.test.MockConversation;
import rife.test.MockResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokemonTest {
    @Test
    void verifyRoot() {
        var m = new MockConversation(new PokemonSite());
        assertEquals(m.doRequest("/").getStatus(), 200);
    }

    record PokemonElement(String name, String number, String type) {}

    private void verifySearchResults(List<PokemonElement> expected, MockResponse searchResults) {

        var results = searchResults.getParsedHtml().getDocument()
                .getElementsByAttributeValue("data-testid", "pokemon_list_item");


        assertEquals(expected.size(), results.size());

        for (int i = 0; i < results.size(); i++) {

            assertEquals(
                    expected.get(i).name,
                    results.get(i).getElementsByAttributeValue("data-testid", "pokemon_name").text()
            );
            assertEquals(
                    expected.get(i).number,
                    results.get(i).getElementsByAttributeValue("data-testid", "pokemon_number").text()
            );
            assertEquals(
                    expected.get(i).type,
                    results.get(i).getElementsByAttributeValue("data-testid", "pokemon_type").text()
            );
        }

    }
    @Test
    void searchForB() {
        var expected = List.of(
                new PokemonElement("Bulbasaur", "001", "Grass and Poison"),
                new PokemonElement("Blastoise", "009", "Water"),
                new PokemonElement("Butterfree", "012", "Bug and Flying"),
                new PokemonElement("Beedrill", "015", "Bug and Poison"),
                new PokemonElement("Bellsprout", "069", "Grass and Poison")
        );


        var m = new MockConversation(new PokemonSite());
        verifySearchResults(
                expected,
                m.doRequest("/pokemon_search?pokemon-name=b")
        );
    }

    @Test
    void searchForBul() {
        var expected = List.of(
                new PokemonElement("Bulbasaur", "001", "Grass and Poison")
        );


        var m = new MockConversation(new PokemonSite());
        verifySearchResults(
                expected,
                m.doRequest("/pokemon_search?pokemon-name=bul")
        );
    }

    @Test
    void searchForMew() {
        var expected = List.of(
                new PokemonElement("Mewtwo", "150", "Psychic"),
                new PokemonElement("Mew", "151", "Psychic")
        );


        var m = new MockConversation(new PokemonSite());
        verifySearchResults(
                expected,
                m.doRequest("/pokemon_search?pokemon-name=mew")
        );
    }
}
