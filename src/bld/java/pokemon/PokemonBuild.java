package pokemon;

import rife.bld.BuildCommand;
import rife.bld.CommandHelp;
import rife.bld.WebProject;
import rife.bld.operations.exceptions.ExitStatusException;

import java.io.IOException;
import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.TemplateType.*;

public class PokemonBuild extends WebProject {
    public PokemonBuild() {
        pkg = "pokemon";
        name = "Pokemon";
        mainClass = "pokemon.PokemonSite";
        uberJarMainClass = "pokemon.PokemonSiteUber";
        version = version(0,1,0);

        downloadSources = true;
        autoDownloadPurge = true;

        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", version(1,9,1)))
            .include(dependency("dev.mccue", "json", version("2024.11.20")));
        scope(test)
            .include(dependency("org.jsoup", "jsoup", version(1,18,3)))
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,11,4)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,11,4)));
        scope(standalone)
            .include(dependency("org.eclipse.jetty.ee10", "jetty-ee10", version(12,0,16)))
            .include(dependency("org.eclipse.jetty.ee10", "jetty-ee10-servlet", version(12,0,16)))
            .include(dependency("org.slf4j", "slf4j-simple", version(2,0,16)));

        precompileOperation()
            .templateTypes(HTML);
    }

    @BuildCommand(summary = "Runs the tailwind build in watch mode")
    public void tailwind_watch() throws Exception {
        int status = new ProcessBuilder(
                "npx",
                "tailwindcss",
                "-i",
                "./src/main/css/tailwind.css",
                "-o",
                "./src/main/webapp/css/tailwind.css",
                "--watch"
        )
                .inheritIO()
                .start()
                .waitFor();

        if (status != 0) {
            throw new ExitStatusException(status);
        }
    }

    @BuildCommand(summary = "Runs the tailwind build once")
    public void tailwind() throws Exception {
        int status = new ProcessBuilder(
                "npx",
                "tailwindcss",
                "-i",
                "./src/main/css/tailwind.css",
                "-o",
                "./src/main/webapp/css/tailwind.css"
        )
                .inheritIO()
                .start()
                .waitFor();
        if (status != 0) {
            throw new ExitStatusException(status);
        }
    }

    public static void main(String[] args) {
        new PokemonBuild().start(args);
    }
}