package pokemon;

import rife.engine.Context;
import rife.engine.Element;
import rife.engine.annotations.Parameter;

import java.util.List;
import java.util.Locale;

public class PokemonSearch implements Element {
    private final List<Pokemon> pokemon;

    @Parameter("pokemon-name") String search = "";

    public PokemonSearch(List<Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

    public void process(Context c) {
        var template = c.template("pokemon_index");
        var name = search.toLowerCase(Locale.ROOT);

        var matches = pokemon.stream()
            .filter(p -> name.isBlank() || p.name().toLowerCase(Locale.ROOT).contains(name))
            .toList();

        if (matches.isEmpty()) {
            template.appendBlock("rows", "empty");
        } else {
            for (var p : matches) {
                template.setBean(p, "pokemon_");
                template.appendBlock("rows", "pokemon");
            }
        }

        // a browser gets the whole page, htmx gets just the "list" block
        c.printHtmxFragment(template, "list");
    }
}
