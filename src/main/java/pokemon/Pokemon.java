package pokemon;

import java.util.List;

public record Pokemon(String name, String num, List<String> type, String img) {
    public String getName() { return name; }
    public String getNum()  { return num; }
    public String getImg()  { return img; }
    public String getType() { return String.join(" and ", type); }
}
