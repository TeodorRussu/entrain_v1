package entraining_feed.englishtraining.mvc;

import entraining_feed.englishtraining.data.QuestionObject;
import entraining_feed.englishtraining.service.QuestionService;
import entraining_feed.englishtraining.util.TextNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.EmptyStackException;

@Controller
@RequestMapping(path = "/en")
public class ClientHandler {

    public static final String EMPTY_STRING = "";
    public static final String QUESTION_PAGE = "question_page";
    private static final String SITUATION = "situation";
    private static final String HINT = "hint";
    private static final String ID = "id";
    private static final String LOADED_CATEGORY = "loadedCategory";
    private static final String CATEGORIES = "categories";
    private static final String CATEGORY = "category";

    @Autowired
    private QuestionService service;
    @Autowired
    private TextNormalizer textNormalizer;
    private QuestionObject questionObject;

    @GetMapping()
    public String loadPage(Model model, @RequestParam(value = CATEGORY, required = false) String category) {

        try {
            questionObject = service.getQuestion(category);
            questionObject.setHint(textNormalizer.normalizeTextForDisplay(questionObject.getHint()));
        } catch (EmptyStackException exception) {
            prepareModeLForEmptyCategory(model, category);
            return QUESTION_PAGE;
        }
        questionObjectToModel(model, questionObject.getCategory());
        return QUESTION_PAGE;
    }

    @GetMapping(path = "/edit")
    public String loadQuestion(Model model, @RequestParam(ID) String id, @RequestParam(value = LOADED_CATEGORY) String loadedCategory) {
        questionObject = service.getQuestion(id, loadedCategory);
        questionObjectToModel(model, loadedCategory);
        return "edit_question";
    }

    @GetMapping(path = "/new")
    public String loadNewQuestionForm(Model model, @RequestParam(value = CATEGORY) String loadedCategory) {
        model.addAttribute(SITUATION, EMPTY_STRING);
        model.addAttribute(HINT, EMPTY_STRING);
        model.addAttribute(ID, EMPTY_STRING);
        model.addAttribute(LOADED_CATEGORY, loadedCategory);
        model.addAttribute(CATEGORIES, service.getCategories());
        return "edit_question";
    }

    @PostMapping(path = "/edit")
    public String editQuestion(Model model, @RequestParam String id,
                               @RequestParam(SITUATION) String situation,
                               @RequestParam(HINT) String hint,
                               @RequestParam String category) {

        if (id.equals(EMPTY_STRING)) {
            questionObject = service.createObject(situation, hint, category);
        } else {
            questionObject = service.updateObject(id, situation, hint, category);
        }
        questionObject.setHint(textNormalizer.normalizeTextForDisplay(questionObject.getHint()));
        questionObjectToModel(model, questionObject.getCategory());
        return QUESTION_PAGE;
    }

    @GetMapping(path = "/delete")
    public String deleteQuestion(Model model, @RequestParam(ID) String id, @RequestParam String category) {
        service.deleteQuestion(id, category);
        return loadPage(model, category);
    }

    private void questionObjectToModel(Model model, String category) {
        model.addAttribute(SITUATION, questionObject.getSituation());
        model.addAttribute(HINT, questionObject.getHint());
        model.addAttribute(ID, questionObject.getId());
        model.addAttribute(LOADED_CATEGORY, category);
        model.addAttribute(CATEGORIES, service.getCategories());
    }

    private void prepareModeLForEmptyCategory(Model model, String category) {
        model.addAttribute(SITUATION, "The category is empty!");
        model.addAttribute(HINT, "The category is empty!");
        model.addAttribute(ID, EMPTY_STRING);
        model.addAttribute(LOADED_CATEGORY, category);
        model.addAttribute(CATEGORIES, service.getCategories());
    }
}
