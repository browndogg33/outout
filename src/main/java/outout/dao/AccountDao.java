package outout.dao;

import org.springframework.stereotype.Repository;
import outout.model.User;
import outout.view.AccountCredentials;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean getIsUserExists(final AccountCredentials ac) {
        Query query = entityManager.createQuery("select count(u) from User u where u.username = :username");
        query.setParameter("username", ac.getUsername());
        int count = ((Number) query.getSingleResult()).intValue();
        return count > 0;
    }

    public void persist(final User user) {
        entityManager.persist(user);
    }

    public User getUser(final AccountCredentials ac) {
        Query q = entityManager.createQuery("select u from User u where u.username = :username");
        q.setParameter("username", ac.getUsername());
        q.setMaxResults(1);
        List<User> uList = q.getResultList();
        return uList.isEmpty() ? null : uList.get(0);
    }
}
