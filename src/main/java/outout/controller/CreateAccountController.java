package outout.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import outout.model.User;
import outout.service.AccountService;
import outout.view.AccountCreationResult;
import outout.view.AccountCredentials;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account/create")
public class CreateAccountController {


    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResponseEntity<AccountCreationResult> createAccount(@RequestBody AccountCredentials ac) {
        if(isUsernameAndPasswordValid(ac)) {
            return getValidAccountCreationResultResponseEntity(ac);
        }
        else {
            return getInvalidAccountCreationResultResponseEntity(ac);
        }
    }

    private ResponseEntity<AccountCreationResult> getInvalidAccountCreationResultResponseEntity(final AccountCredentials ac) {
        AccountCreationResult acr = new AccountCreationResult();
        List<String> errors = new ArrayList<>();
        if(StringUtils.isEmpty(ac.getUsername()) || ac.getUsername().length() < 5) {
            errors.add("Username must be at least 5 characters");
        }
        if(StringUtils.isEmpty(ac.getPassword()) || ac.getPassword().length() < 10) {
            errors.add("Password must be at least 10 characters");
        }
        if(accountService.getIsUserExists(ac)) {
            errors.add("Username already in use.  Please enter another username.");
        }
        acr.setErrors(errors);
        return new ResponseEntity<>(acr, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<AccountCreationResult> getValidAccountCreationResultResponseEntity(final AccountCredentials ac) {
        accountService.createUser(ac);
        AccountCreationResult acr = new AccountCreationResult();
        acr.setSuccessful(true);
        return new ResponseEntity<>(acr, HttpStatus.OK);
    }

    private boolean isUsernameAndPasswordValid(final AccountCredentials ac) {
        return !StringUtils.isEmpty(ac.getUsername()) && ac.getUsername().length() >= 5
                && !StringUtils.isEmpty(ac.getPassword()) && ac.getPassword().length() >= 10
                && !accountService.getIsUserExists(ac);
    }

}
