package pokemon;

import rife.engine.Context;
import rife.engine.Element;

import java.util.List;
import java.util.Locale;

public final class PokemonSearch implements Element {
    private final List<Pokemon> pokemon;

    public PokemonSearch(List<Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public void process(Context c) throws Exception {
        var template = c.template("pokemon_search");

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
    }
}
