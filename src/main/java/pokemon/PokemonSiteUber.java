package pokemon;

import rife.engine.Server;

public class PokemonSiteUber extends PokemonSite {
    public static void main(String[] args) {
        new Server()
            .staticUberJarResourceBase("webapp")
            .start(new PokemonSiteUber());
    }
}