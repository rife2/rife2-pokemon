package pokemon;

import dev.mccue.json.Json;
import dev.mccue.json.JsonDecoder;
import rife.engine.*;
import rife.resources.ResourceFinderClasspath;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PokemonSite extends Site {
    Route pokemon_index;
    Route pokemon_search;

    @Override
    public void setup() {
        List<Pokemon> pokemon;
        try (var stream = ResourceFinderClasspath.instance().getResource("/pokemon.json").openStream()) {
            var pokemonString = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            var pokemonJson = Json.readString(pokemonString);

            pokemon = JsonDecoder.array(pokemonJson, Pokemon::fromJson);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        pokemon_index = get("/", () -> new PokemonIndex(pokemon));
        pokemon_search = get("/pokemon_search", () -> new PokemonSearch(pokemon));
    }

    public static void main(String[] args) {
        new Server()
            .staticResourceBase("src/main/webapp")
            .start(new PokemonSite());
    }
}