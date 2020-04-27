package entraining_feed.feed.logic;

import entraining_feed.feed.action.AngiNewsFeed;
import entraining_feed.feed.config.YamlConfig;
import entraining_feed.feed.email.EmailSender;
import entraining_feed.feed.env.StaticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class AngiFeedHandler {

    @Autowired
    EmailSender emailSender;
    @Autowired
    ApplicationContext springContext;
    @Autowired
    private YamlConfig yamlConfig;

    public AngiNewsFeed nvgCreate(String url, LocalDate selectedDate) {
        AngiNewsFeed angiNewsFeed = springContext.getBean(AngiNewsFeed.class);
        angiNewsFeed.setUrl(url);
        angiNewsFeed.setSelectedDate(selectedDate);
        return angiNewsFeed;
    }

    public void nvgAction(String dateFrom) {
        LocalDate selectedDate = LocalDate.parse(dateFrom, DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            String url = yamlConfig.getAngiNewsPath();
            AngiNewsFeed feed = nvgCreate(url, selectedDate);
            feed.action();
            feed.toExcel();
            emailSender.sendEmailWithAttachment(StaticData.getEmailTo(), yamlConfig.getAngiEmailSubject(),
                    yamlConfig.getAngiEmailContent(), yamlConfig.getAngiExcelFilename(),
                    yamlConfig.getExcelPathRoot() + yamlConfig.getAngiExcelFilename());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
