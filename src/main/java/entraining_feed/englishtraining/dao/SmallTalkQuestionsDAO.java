package entraining_feed.englishtraining.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

@Data
@Component
public class SmallTalkQuestionsDAO extends QuestionDAO {
    public static final String CATEGORY = "smalltalk";
    private static final String COLLECTION = "questions";

    @PostConstruct
    private void init() {
        questionsCollection = database.getCollection(COLLECTION);
        questionsCollection.find().iterator().forEachRemaining(document -> this.allQuestions.add(document));
        this.questionsDeque = new LinkedList<>(this.allQuestions);
    }
}
