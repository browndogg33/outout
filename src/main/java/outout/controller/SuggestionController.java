package outout.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import outout.model.Suggestion;
import outout.service.SuggestionService;
import outout.view.RestaurantSuggestion;
import outout.view.RestaurantSuggestions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/suggestion")
public class SuggestionController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    SuggestionService suggestionService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> suggest(@RequestBody RestaurantSuggestion r, Principal p) {
        List<Suggestion> suggestionListByRestaurantName = suggestionService.getSuggestionsByRestaurantName(r);
        List<Suggestion> suggestionListSuggestedByUser = suggestionService.getSuggestionsByUser(p);
        if(isSuggestionNotBeenMade(suggestionListByRestaurantName)
                && isUserMadeLessThanMaxSuggestions(suggestionListSuggestedByUser)) {
            suggestionService.createSuggestion(r, p);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private boolean isSuggestionNotBeenMade(final List<Suggestion> suggestionListByRestaurantName) {
        return suggestionListByRestaurantName.size() == 0;
    }

    private boolean isUserMadeLessThanMaxSuggestions(final List<Suggestion> suggestionListSuggestedByUser) {
        return suggestionListSuggestedByUser.size() < 2;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public ResponseEntity<RestaurantSuggestions> suggestionsForToday() {
        RestaurantSuggestions r = new RestaurantSuggestions();
        Query q = em.createQuery("select s from Suggestion s where trunc(s.suggestedDate) = trunc(:suggestedDate)");
        q.setParameter("suggestedDate", DateTime.now().toDate());
        List<Suggestion> sList = q.getResultList();
        r.setRestaurantSuggestions(new ArrayList<>(sList.size()));
        for(Suggestion s: sList) {
            RestaurantSuggestion rs = new RestaurantSuggestion();
            rs.setRestaurant(s.getSuggestion());
            r.getRestaurantSuggestions().add(rs);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


}
