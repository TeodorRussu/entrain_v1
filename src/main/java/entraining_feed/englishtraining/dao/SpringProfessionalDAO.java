package entraining_feed.englishtraining.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;


@Data
@Component
public class SpringProfessionalDAO extends QuestionDAO {
    public static final String CATEGORY = "spring_professional";

    @PostConstruct
    private void init() {
        questionsCollection = database.getCollection(CATEGORY);
        questionsCollection.find().iterator().forEachRemaining(document -> this.allQuestions.add(document));
        this.questionsDeque = new LinkedList<>(this.allQuestions);
    }
}
