package outout.dao;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import outout.model.Suggestion;
import outout.view.RestaurantSuggestion;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.Principal;
import java.util.List;

@Repository
public class SuggestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(final Suggestion s) {
        entityManager.persist(s);
    }

    public List<Suggestion> getSuggestionsByRestaurantName(final RestaurantSuggestion r) {
        Query q = entityManager.createQuery("select s from Suggestion s where s.suggestion = :suggestion " +
                "and trunc(s.suggestedDate) = trunc(:suggestedDate)");
        q.setParameter("suggestion", r.getRestaurant());
        q.setParameter("suggestedDate", DateTime.now().toDate());
        return q.getResultList();
    }

    public List<Suggestion> getSuggestionsByUser(final Principal p) {
        Query q = entityManager.createQuery("select s from Suggestion s where trunc(s.suggestedDate) = trunc(:suggestedDate) " +
                "and s.suggestedBy = :username");
        q.setParameter("suggestedDate", DateTime.now().toDate());
        q.setParameter("username", p.getName());
        return q.getResultList();
    }
}
