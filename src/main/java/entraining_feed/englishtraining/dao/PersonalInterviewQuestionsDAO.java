package entraining_feed.englishtraining.dao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

@Component
@Data
@Slf4j
public class PersonalInterviewQuestionsDAO extends QuestionDAO {

    public static final String CATEGORY = "pers_interview";
    private static final String COLLECTION = "personal_interview_questions";

    @PostConstruct
    private void init() {
        questionsCollection = database.getCollection(COLLECTION);
        questionsCollection.find().iterator().forEachRemaining(document -> this.allQuestions.add(document));
        this.questionsDeque = new LinkedList<>(this.allQuestions);
    }
}

