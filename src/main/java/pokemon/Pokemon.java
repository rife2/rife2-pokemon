package pokemon;


import dev.mccue.json.Json;
import dev.mccue.json.JsonDecoder;
import rife.template.Template;

import java.util.List;

public record Pokemon(
        String name,
        String number,
        List<String> type,
        String image
) {
    static Pokemon fromJson(Json json) {
        var name = JsonDecoder.field(json, "name", JsonDecoder::string);
        var number = JsonDecoder.field(json, "num", JsonDecoder::string);
        var type = JsonDecoder.field(json, "type", JsonDecoder.array(JsonDecoder::string));
        var image = JsonDecoder.field(json, "img", JsonDecoder::string);

        return new Pokemon(
                name,
                number,
                type,
                image
        );
    }

    void render(Template template) {
        template.setValue("pokemon_name", this.name());
        template.setValue("pokemon_number", this.number());
        template.setValue("pokemon_image", this.image());

        if (this.type().size() == 1) {
            template.setValue("pokemon_type", this.type().get(0));
        }
        else {
            template.setValue("pokemon_type", this.type().get(0) + " and " + this.type().get(1));
        }
    }
}
