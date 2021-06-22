package app.dto.platform;

import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"error", "timestamp", "total", "offset", "perPage", "data"})
public class LanguageResponse extends PaginationResponse {

    @JsonProperty("data")
    private List<LanguageDto> languages;


    public LanguageResponse(int offset, int perPage) {
        this.languages = new ArrayList<>();
        this.setOffset(offset);
        this.setPerPage(perPage);
    }


    public void addLanguage(LanguageDto language) {
        if (languages == null) {
            languages = new ArrayList<>();
        }
        languages.add(language);
    }

    public List<LanguageDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageDto> languages) {
        this.languages = languages;
    }

    @Override
    public String toString() {
        return "LanguageResponse{" +
                "languages=" + languages +
                '}';
    }
}
