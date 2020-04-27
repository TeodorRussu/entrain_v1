package entraining_feed.englishtraining.mapper;

import entraining_feed.englishtraining.data.QuestionObject;
import entraining_feed.englishtraining.util.TextNormalizer;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    @Autowired
    TextNormalizer textNormalizer;

    public QuestionObject mapDocumentToQuestionObject(Document document) {

        return QuestionObject.builder()
                .id(document.getObjectId("_id").toHexString())
                .situation(document.getString("situation"))
                .hint(document.getString("hint"))
                .build();
    }
}
