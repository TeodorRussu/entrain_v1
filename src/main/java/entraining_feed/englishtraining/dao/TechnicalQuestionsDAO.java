package entraining_feed.englishtraining.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

@Component
@Data
@Slf4j
public class TechnicalQuestionsDAO extends QuestionDAO {
    public static final String CATEGORY = "technical_questions";

    @PostConstruct
    private void init() {
        questionsCollection = database.getCollection(CATEGORY);
        questionsCollection.find().iterator().forEachRemaining(document -> this.allQuestions.add(document));
        this.questionsDeque = new LinkedList<>(this.allQuestions);
    }
}
