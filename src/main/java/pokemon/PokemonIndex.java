package pokemon;

import rife.engine.Context;
import rife.engine.Element;

import java.util.List;

public final class PokemonIndex implements Element {
    private final List<Pokemon> pokemon;

    public PokemonIndex(List<Pokemon> pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public void process(Context c) throws Exception {
        var template = c.template("pokemon_index");
        for (var p : pokemon) {
            p.render(template);
            template.appendBlock("pokemon_list", "pokemon");
        }
        c.print(template);
    }
}
