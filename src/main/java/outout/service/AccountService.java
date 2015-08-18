package outout.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import outout.dao.AccountDao;
import outout.model.User;
import outout.view.AccountCredentials;

import javax.persistence.Query;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    private PasswordEncoder encoder;


    public boolean getIsUserExists(final AccountCredentials ac) {
        return accountDao.getIsUserExists(ac);
    }

    public void createUser(final AccountCredentials ac) {
        User user = new User();
        user.setUsername(ac.getUsername());
        user.setPassword(encoder.encode(ac.getPassword()));
        accountDao.persist(user);
    }

    public boolean isValidUser(final AccountCredentials ac) {
        User user = accountDao.getUser(ac);
        return user != null && encoder.matches(ac.getPassword(), user.getPassword());
    }
}
