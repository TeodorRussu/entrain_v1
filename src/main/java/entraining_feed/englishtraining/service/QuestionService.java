package entraining_feed.englishtraining.service;


import entraining_feed.englishtraining.dao.*;
import entraining_feed.englishtraining.data.QuestionObject;
import entraining_feed.englishtraining.mapper.QuestionMapper;
import entraining_feed.englishtraining.util.TextNormalizer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@AllArgsConstructor
public class QuestionService {

    @Autowired

    private SmallTalkQuestionsDAO smallTalkQuestionsDAO;
    @Autowired
    private PersonalInterviewQuestionsDAO personalInterviewQuestionsDAO;
    @Autowired
    private TechnicalQuestionsDAO technicalQuestionsDAO;
    @Autowired
    private SpringProfessionalDAO springQuestionsDAO;
    @Autowired
    private QuestionMapper mapper;
    @Autowired
    TextNormalizer textNormalizer;

    private List<String> categories;

    @PostConstruct
    private void init() {
        categories = new ArrayList<>();
        categories.add(PersonalInterviewQuestionsDAO.CATEGORY);
        categories.add(SmallTalkQuestionsDAO.CATEGORY);
        categories.add(TechnicalQuestionsDAO.CATEGORY);
        categories.add(SpringProfessionalDAO.CATEGORY);
    }

    public QuestionObject getQuestion(String category) {

        if (category == null) {
            category = SmallTalkQuestionsDAO.CATEGORY;
        }

        QuestionDAO dao = getDaoByCategory(category);

        Document document = dao.getQuestion();
        QuestionObject questionObject = mapper.mapDocumentToQuestionObject(document);
        questionObject.setCategory(category);
        return questionObject;
    }

    private QuestionDAO getDaoByCategory(String category) {
        switch (category) {
            case PersonalInterviewQuestionsDAO.CATEGORY:
                return personalInterviewQuestionsDAO;
            case TechnicalQuestionsDAO.CATEGORY:
                return technicalQuestionsDAO;
            case SpringProfessionalDAO.CATEGORY:
                return springQuestionsDAO;
            default:
                return smallTalkQuestionsDAO;
        }
    }

    public QuestionObject getQuestion(String id, String category) {
        QuestionDAO dao = getDaoByCategory(category);
        Document document = dao.getQuestionById(id);
        QuestionObject questionObject = mapper.mapDocumentToQuestionObject(document);
        questionObject.setCategory(category);
        return questionObject;
    }

    public QuestionObject updateObject(String id, String situation, String hint, String category) {
        QuestionDAO dao = getDaoByCategory(category);
        hint = textNormalizer.normalizeHintTextBeforePersisting(hint);
        dao.updateDocument(id, situation, hint);
        return getQuestion(id, category);
    }

    public QuestionObject createObject(String situation, String hint, String category) {
        QuestionDAO dao = getDaoByCategory(category);
        hint = textNormalizer.normalizeHintTextBeforePersisting(hint);
        Document document = dao.insertDocument(situation, hint);

        QuestionObject questionObject = mapper.mapDocumentToQuestionObject(document);
        questionObject.setCategory(category);
        return questionObject;
    }

    public void deleteQuestion(String id, String category) {
        QuestionDAO dao = getDaoByCategory(category);
        dao.deleteQuestion(id);
    }
}
