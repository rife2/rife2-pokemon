# rife2-pokemon

Small demo of using the [RIFE2](https://rife2.com/) framework alongside
[htmx](https://htmx.org/) and [tailwindcss](https://tailwindcss.com/)

Type in the name field and the list filters as you go, with the server sending
back only the part of the page that changed.

## Getting Started

```bash
npm i
./bld tailwind compile run
```

Go to:

[http://localhost:8080/](http://localhost:8080/)

## Running the tests

```bash
./bld compile test
```

## One route, two responses

RIFE2 has htmx support built in, so the search doesn't need a second endpoint
for the fragment. A single route serves both, and `printHtmxFragment` picks
which to send:

```java
public final Route search = get("/", () -> new PokemonSearch(pokemon));
```

```java
c.printHtmxFragment(template, "list");
```

A browser gets the whole document with the list already in it, htmx gets just
the `list` block. The block is declared as a block value with `<!--bv list-->`,
so it renders in place on the full page and stays addressable on its own.

An htmx history restoration deliberately gets the full page, since htmx replaces
the whole document when it restores, and the response carries the matching
`Vary` headers so a cache never hands one kind of response to the other. The
search box keeps its query, so a restored page and a shared link both come back
showing what was filtered on.

The app depends on nothing but RIFE2. The Pokémon data is parsed with RIFE2's
own JSON support, and both response paths are covered by the out-of-container
tests in [`PokemonTest`](src/test/java/pokemon/PokemonTest.java), which drive
the routes in memory without starting a server.

## Credits

The Pokémon data and images come from [serebii.net](https://www.serebii.net/).
Pokémon is a trademark of Nintendo, Creatures Inc. and GAME FREAK Inc. This
demo is not affiliated with them.
