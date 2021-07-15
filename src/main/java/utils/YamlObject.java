package utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YamlObject {
    private String id;
    private String label;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String link;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String depends;

    public YamlObject(String id, String label) {
        this.id = id;
        this.label = label;
    }
}
