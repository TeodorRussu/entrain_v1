package entraining_feed.feed.mvc;

import entraining_feed.feed.env.StaticData;
import entraining_feed.feed.logic.NvgFeedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/nvg")
public class NgvController {

    @Autowired
    private NvgFeedHandler nvgFeedHandler;


    @GetMapping("")
    public String showFilterPage() {
        return "filter-page";
    }

    @GetMapping("/export")
    public String processNvgNewsFeed(@RequestParam("${dateFromParam}") String dateFrom,
                                     @RequestParam("${dateToParam}") String dateTo,
                                     @RequestParam("${paramQ}") String q, @RequestParam("email") String email) {

        StaticData.setEmailTo(email);
        nvgFeedHandler.nvgAction(dateFrom, dateTo, q);
        return "success";
    }

}