package pokemon;

import org.junit.jupiter.api.Test;
import rife.test.MockConversation;
import rife.test.MockRequest;
import rife.test.MockResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PokemonTest {
    @Test
    void verifyRoot() {
        var m = new MockConversation(new PokemonSite());
        assertEquals(200, m.doRequest("/").getStatus());
    }

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

    private MockResponse search(MockConversation m, String name) {
        return m.doRequest("/?pokemon-name=" + name, new MockRequest().htmx());
    }

    @Test
    void searchMatchesAnywhereInTheName() {
        var expected = List.of(
                new PokemonElement("Bulbasaur", "001", "Grass and Poison"),
                new PokemonElement("Ivysaur", "002", "Grass and Poison"),
                new PokemonElement("Venusaur", "003", "Grass and Poison")
        );

        var m = new MockConversation(new PokemonSite());
        verifySearchResults(expected, search(m, "saur"));
    }

    @Test
    void searchForZar() {
        var expected = List.of(
                new PokemonElement("Charizard", "006", "Fire and Flying")
        );

        var m = new MockConversation(new PokemonSite());
        verifySearchResults(expected, search(m, "zar"));
    }

    @Test
    void searchForBul() {
        var expected = List.of(
                new PokemonElement("Bulbasaur", "001", "Grass and Poison")
        );

        var m = new MockConversation(new PokemonSite());
        verifySearchResults(expected, search(m, "bul"));
    }

    @Test
    void searchForMew() {
        var expected = List.of(
                new PokemonElement("Mewtwo", "150", "Psychic"),
                new PokemonElement("Mew", "151", "Psychic")
        );

        var m = new MockConversation(new PokemonSite());
        verifySearchResults(expected, search(m, "mew"));
    }

    @Test
    void htmxGetsTheFragmentAndTheBrowserGetsThePage() {
        var m = new MockConversation(new PokemonSite());

        var fragment = search(m, "mew").getText().trim();
        assertTrue(fragment.startsWith("<ul"), "htmx receives only the list block");
        assertTrue(fragment.contains("pokemon_list_item"));

        var page = m.doRequest("/?pokemon-name=mew").getText();
        assertTrue(page.contains("<html"), "a browser receives the whole document");
        assertTrue(page.contains("pokemon_list_item"), "with the list already filled in");
    }

    @Test
    void theResponseVariesOnTheHtmxHeaders() {
        var m = new MockConversation(new PokemonSite());
        var vary = m.doRequest("/").getHeader("Vary");
        assertTrue(vary != null && vary.contains("HX-Request"),
                "so a cache never serves the fragment to a browser");
    }

    @Test
    void aHistoryRestorationGetsTheFullPage() {
        var m = new MockConversation(new PokemonSite());
        var restore = m.doRequest("/?pokemon-name=mew",
                new MockRequest().htmx().header("HX-History-Restore-Request", "true"));
        assertTrue(restore.getText().contains("<html"),
                "htmx replaces the whole document when it restores");
    }

    @Test
    void namesWithSymbolsSurviveTheJsonRoundTrip() {
        var m = new MockConversation(new PokemonSite());
        var text = search(m, "nidoran").getText();
        assertTrue(text.contains("&#9792;") || text.contains("♀"),
                "the file is UTF-8 whatever the platform default is");
    }

    @Test
    void searchWithNoMatchesExplainsItself() {
        var m = new MockConversation(new PokemonSite());
        var text = search(m, "zzz").getText();
        assertTrue(text.contains("pokemon_empty"));
        assertFalse(text.contains("pokemon_list_item"));
    }

    @Test
    void theSearchIsCaseInsensitive() {
        var m = new MockConversation(new PokemonSite());
        assertEquals(search(m, "mew").getText(), search(m, "MEW").getText());
    }

    @Test
    void theBoxKeepsWhatWasSearchedFor() {
        var m = new MockConversation(new PokemonSite());
        assertTrue(m.doRequest("/?pokemon-name=mew").getText().contains("value=\"mew\""));
    }

    @Test
    void theBoxIsEmptyWithoutAQuery() {
        var m = new MockConversation(new PokemonSite());
        var text = m.doRequest("/").getText();
        assertTrue(text.contains("value=\"\""), "an unfilled param tag must not print itself");
        assertFalse(text.contains("param:pokemon-name"));
    }

    record PokemonElement(String name, String number, String type) {
    }
}
