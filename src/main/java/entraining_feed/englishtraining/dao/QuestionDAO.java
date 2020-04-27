package entraining_feed.englishtraining.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.mongodb.client.model.Filters.eq;

@Component
@Scope(scopeName = "prototype")
@Data
@Slf4j
public class QuestionDAO {

    public static final String SPRING_MONGODB_URI = "spring.mongodb.uri";
    public static final String SPRING_MONGODB_DATABASE = "spring.mongodb.database";

    @Autowired
    protected Environment environment;

    protected MongoClientURI uri;
    protected MongoClient mongoClient;
    protected MongoDatabase database;

    protected MongoCollection<Document> questionsCollection;
    protected Set<Document> allQuestions = new HashSet<>();
    protected Deque<Document> questionsDeque;

    @PostConstruct
    private void initDBConnection() {
        uri = new MongoClientURI(environment.getProperty(SPRING_MONGODB_URI));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase(environment.getProperty(SPRING_MONGODB_DATABASE));
    }

    public Document getQuestion() {
        int random = ThreadLocalRandom.current().nextInt(2);
        Document toReturn = random % 2 == 0 ? getQuestionsDeque().pollFirst() : getQuestionsDeque().pollLast();

        if (toReturn == null) {
            getAllQuestions().clear();
            getQuestionsCollection().find().iterator().forEachRemaining(document -> allQuestions.add(document));
            setQuestionsDeque(new LinkedList<>(getAllQuestions()));
            if (questionsDeque.isEmpty()) {
                throw new EmptyStackException();
            }
            toReturn = getQuestion();
        }

        return toReturn;
    }

    public Document getQuestionById(String id) {
        Bson queryFilter;
        queryFilter = Filters.in("_id", new ObjectId(id));
        return getQuestionsCollection().find(queryFilter).first();

    }

    public void updateDocument(String id, String situation, String hint) {

        Document filter = new Document();
        filter.append("_id", new ObjectId(id));

        Bson update = new Document("$set",
                new Document()
                        .append("situation", situation)
                        .append("hint", hint));
        UpdateOptions options = new UpdateOptions().upsert(true);

        try {
            getQuestionsCollection().withWriteConcern(WriteConcern.MAJORITY).updateOne(filter, update, options);
        } catch (MongoWriteException exception) {
            log.debug(exception.getMessage());
        }
    }

    public Document insertDocument(String situation, String hint) {
        Document document = new Document()
                .append("situation", situation)
                .append("hint", hint);
        getQuestionsCollection().insertOne(document);
        String id = document.getObjectId("_id").toHexString();
        return getQuestionById(id);
    }

    public void deleteQuestion(String id) {
        Document filter = new Document();
        filter.append("_id", new ObjectId(id));
        getQuestionsCollection().deleteOne(eq("_id", new ObjectId(id)));
        log.info(String.format("question with id %s removed", id));
    }
}
