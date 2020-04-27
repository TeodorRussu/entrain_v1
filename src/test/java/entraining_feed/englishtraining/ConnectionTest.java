package entraining_feed.englishtraining;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ConnectionTest {

    @Autowired
    Environment environment;
    MongoDatabase database;

    @Before
    public void setup() throws IOException {
        MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:admin@mflix-dpbje.mongodb.net/test?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("sample_mflix");
    }

    @Test
    public void testMoviesCount() {
        long expected = 23539;

        MongoCollection<Document> moviesCollection = database.getCollection("movies");

        Assert.assertEquals("Check your connection string", expected, moviesCollection.countDocuments());

        Assertions.assertNotNull(database);
    }
}