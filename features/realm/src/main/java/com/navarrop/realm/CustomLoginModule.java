package com.navarrop.realm;

import org.apache.karaf.jaas.boot.principal.GroupPrincipal;
import org.apache.karaf.jaas.boot.principal.RolePolicy;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.apache.karaf.jaas.boot.principal.UserPrincipal;
import org.apache.karaf.jaas.modules.AbstractKarafLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CustomLoginModule extends AbstractKarafLoginModule
{
    private static Logger logger = LoggerFactory.getLogger(CustomLoginModule.class);

    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options)
    {
        super.initialize(subject, callbackHandler, options);
    }

    public boolean login() throws LoginException
    {
        // prepare callback objects and get the authentication information

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username: ");
        callbacks[1] = new PasswordCallback("Password: ", false);

        try {
            callbackHandler.handle(callbacks);
        }
        catch (Exception e) {
            throw new LoginException(e.getMessage());
        }

        user = ((NameCallback) callbacks[0]).getName();

        char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
        if (tmpPassword == null)
            tmpPassword = new char[0];
        String password = new String(tmpPassword);

        logger.info("Check user: '{}', password: '{}'", user, password);

        /* START OF CUSTOM AUTH */
        if( !user.equals("pierre") || !password.equals("navarro") ) {
            return false;
        }
        List<String> roles = new ArrayList<>();
        roles.add("role1");
        roles.add("role2");

        List<String> groups = new ArrayList<>();
        groups.add("group1");
        /* END OF CUSTOM AUTH */

        principals = new HashSet<>();
        UserPrincipal userPrincipal = new UserPrincipal(user);
        principals.add(userPrincipal);

        for (String role : roles)
            principals.add(new RolePrincipal(role));
        for (String group: groups)
            principals.add(new GroupPrincipal(group));
        logger.info("Add UserPrincipal: {}", userPrincipal);

        succeeded = true;

        return true;
    }

    public boolean abort() throws LoginException
    {
        return true;
    }

    public boolean logout() throws LoginException
    {
        subject.getPrincipals().removeAll(principals);
        principals.clear();
        return true;
    }
}
