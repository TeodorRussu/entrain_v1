package entraining_feed.englishtraining.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QuestionObject {
    private String id;
    private String situation;
    private String hint;
    private String category;
}
