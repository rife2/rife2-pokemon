package pokemon;

import rife.bld.BuildCommand;
import rife.bld.WebProject;
import rife.bld.operations.exceptions.ExitStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;
import static rife.bld.operations.TemplateType.*;

public class PokemonBuild extends WebProject {
    public PokemonBuild() {
        pkg = "pokemon";
        name = "Pokemon";
        mainClass = "pokemon.PokemonSite";
        uberJarMainClass = "pokemon.PokemonSiteUber";
        version = version(2,0,0);

        downloadSources = true;
        autoDownloadPurge = true;
        javaRelease = 17;

        repositories = List.of(MAVEN_CENTRAL);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", version(1,10,0)));
        scope(test)
            .include(bom("org.junit", "junit-bom", version(6,1,3)))
            .include(dependency("org.jsoup", "jsoup", version(1,23,2)))
            .include(dependency("org.junit.jupiter", "junit-jupiter"))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone"));
        scope(standalone)
            .include(bom("org.eclipse.jetty.ee10", "jetty-ee10-bom", version(12,1,13)))
            .include(dependency("org.eclipse.jetty.ee10", "jetty-ee10-servlet"))
            .include(dependency("org.slf4j", "slf4j-simple", version(2,0,19)));

        precompileOperation()
            .templateTypes(HTML);
    }

    @BuildCommand(summary = "Runs the tailwind build once")
    public void tailwind() throws Exception {
        runTailwind();
    }

    @BuildCommand(value = "tailwind-watch", summary = "Runs the tailwind build in watch mode")
    public void tailwindWatch() throws Exception {
        runTailwind("--watch");
    }

    private void runTailwind(String... options) throws Exception {
        var command = new ArrayList<String>();
        // npx is a batch script on Windows, which ProcessBuilder can't launch directly
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).startsWith("windows")) {
            command.addAll(List.of("cmd", "/c"));
        }
        command.addAll(List.of("npx", "tailwindcss",
            "-i", "./src/main/css/tailwind.css",
            "-o", "./src/main/webapp/css/tailwind.css"));
        command.addAll(List.of(options));

        var status = new ProcessBuilder(command).inheritIO().start().waitFor();
        if (status != 0) {
            throw new ExitStatusException(status);
        }
    }

    public static void main(String[] args) {
        new PokemonBuild().start(args);
    }
}