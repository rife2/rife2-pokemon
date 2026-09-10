package pokemon;

import rife.engine.*;
import rife.engine.exceptions.EngineException;
import rife.json.Json;
import rife.resources.ResourceFinderClasspath;
import rife.resources.exceptions.ResourceFinderErrorException;

import java.util.List;

public class PokemonSite extends Site {
    final List<Pokemon> pokemon = loadPokemon();

    // public so the template can reach it through the route: tag
    public final Route search = get("/", () -> new PokemonSearch(pokemon));

    static List<Pokemon> loadPokemon() {
        try {
            // the Nidoran names carry ♀ and ♂, the platform default isn't always UTF-8
            var json = ResourceFinderClasspath.instance().getContent("pokemon.json", "UTF-8");
            return List.copyOf(Json.toBeanList(Json.parseArray(json), Pokemon.class));
        } catch (ResourceFinderErrorException e) {
            throw new EngineException(e);
        }
    }

    public static void main(String[] args) {
        new Server()
            .staticResourceBase("src/main/webapp")
            .start(new PokemonSite());
    }
}
