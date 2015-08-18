package outout.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import outout.dao.SuggestionDao;
import outout.model.Suggestion;
import outout.view.RestaurantSuggestion;

import java.security.Principal;
import java.util.List;

@Service
public class SuggestionService {

    @Autowired
    SuggestionDao suggestionDao;


    public void createSuggestion(final RestaurantSuggestion r, final Principal p) {
        Suggestion s = new Suggestion();
        s.setSuggestedBy(p.getName());
        s.setSuggestion(r.getRestaurant());
        s.setSuggestedDate(DateTime.now().toDate());
        suggestionDao.persist(s);
    }

    public List<Suggestion> getSuggestionsByRestaurantName(final RestaurantSuggestion r) {
        return suggestionDao.getSuggestionsByRestaurantName(r);
    }

    public List<Suggestion> getSuggestionsByUser(final Principal p) {
        return suggestionDao.getSuggestionsByUser(p);
    }
}
