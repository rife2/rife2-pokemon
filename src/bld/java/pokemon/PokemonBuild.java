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

        precompiledTemplateTypes = List.of(HTML);

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, SONATYPE_SNAPSHOTS);
        scope(compile)
            .include(dependency("com.uwyn.rife2", "rife2", version(1,5,6)))
            .include(dependency("dev.mccue", "json", version(0,2,3)));
        scope(test)
            .include(dependency("org.jsoup", "jsoup", version(1,15,4)))
            .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,9,2)))
            .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,9,2)));
        scope(standalone)
            .include(dependency("org.eclipse.jetty", "jetty-server", version(11,0,14)))
            .include(dependency("org.eclipse.jetty", "jetty-servlet", version(11,0,14)))
            .include(dependency("org.slf4j", "slf4j-simple", version(2,0,7)));
    }

    public static class TailwindWatchHelp implements CommandHelp {
        @Override
        public String getSummary() {
            return "Runs the tailwind build in watch mode";
        }
    }

    public static class TailwindHelp implements CommandHelp {
        @Override
        public String getSummary() {
            return "Runs the tailwind build once";
        }
    }

    @BuildCommand(help = TailwindWatchHelp.class)
    public void tailwind_watch() throws IOException, InterruptedException, ExitStatusException {
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

    @BuildCommand(help = TailwindHelp.class)
    public void tailwind() throws IOException, InterruptedException, ExitStatusException {
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