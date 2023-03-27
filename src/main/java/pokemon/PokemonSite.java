package pokemon;

import dev.mccue.json.Json;
import dev.mccue.json.JsonDecoder;
import rife.engine.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PokemonSite extends Site {
    Route index;
    Route pokemon_list;

    @Override
    public void setup() {
        List<Pokemon> pokemon;
        try (var stream = Objects.requireNonNull(PokemonSite.class.getResourceAsStream("/pokemon.json"))) {
            var pokemonString = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            var pokemonJson = Json.readString(pokemonString);

            pokemon = JsonDecoder.array(pokemonJson, Pokemon::fromJson);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        index = get("/", c -> {
            var template = c.template("pokemon");
            for (var p : pokemon) {
                p.render(template);
                template.appendBlock("pokemon_list", "pokemon");
            }
            c.print(template);
        });

        pokemon_list = get("/pokemon_list", c -> {
            var template = c.template("pokemon_list");

            var pokemonName = c.parameter("pokemon-name", null);

            List<Pokemon> toRender;
            if (pokemonName == null || pokemonName.isBlank()) {
                toRender = pokemon;
            }
            else {
                toRender = pokemon
                        .stream()
                        .filter(p -> p.name().toLowerCase(Locale.US).startsWith(pokemonName.toLowerCase(Locale.US)))
                        .toList();
            }

            for (var p : toRender) {
                p.render(template);
                template.appendBlock("pokemon_list", "pokemon");
            }

            c.print(template);
        });

    }

    public static void main(String[] args) {
        new Server()
            .staticResourceBase("src/main/webapp")
            .start(new PokemonSite());
    }
}