package entraining_feed.englishtraining.util;

import org.springframework.stereotype.Component;

@Component
public class TextNormalizer {
    public String normalizeTextForDisplay(String hint) {
        if (hint.contains("<br>")) {
            return hint;
        }
        hint = hint.replace("\r\n", "<br>");
        return hint;
    }

    public String normalizeHintTextBeforePersisting(String hint) {
        hint = hint.replace("\uF075", "-");
        hint = hint.replace("<br>", "\r\n");
        return hint;
    }
}
