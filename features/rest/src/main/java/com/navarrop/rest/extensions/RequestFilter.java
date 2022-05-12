package com.navarrop.rest.extensions;

import com.navarrop.rest.dto.UserInfo;
import org.apache.karaf.jaas.boot.principal.GroupPrincipal;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component(property = { "osgi.jaxrs.extension=true" })
public class RequestFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        SecurityContext securityContext = null;
        try {
            securityContext = getSecurityContextFromHeader(containerRequestContext);
            if( securityContext == null ) {
                Response response = Response.
                        status(Response.Status.UNAUTHORIZED)
                        .entity(Response.Status.UNAUTHORIZED.toString())
                        .build();
                containerRequestContext.abortWith(response);
            }
            containerRequestContext.setSecurityContext(securityContext);
        } catch (LoginException ex) {
            Response response = Response.
                    status(Response.Status.UNAUTHORIZED)
                    .entity(Response.Status.UNAUTHORIZED.toString())
                    .build();
            containerRequestContext.abortWith(response);
        }
        catch (Exception ex) {
            Response response = Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ex.getMessage())
                    .build();
            containerRequestContext.abortWith(response);
        }
    }

    private SecurityContext getSecurityContextFromHeader(final ContainerRequestContext requestContext) throws LoginException {
        String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String login = "";
        String password = "";
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            login = values[0];
            password = values[1];
            logger.info("login: {}, password: {}", login, password);
            UserInfo userInfo = login(login, password);
            userInfo.setLogin(login);
            logger.info("userInfo: {}", userInfo.toJsonString());
            if( userInfo != null ) {
                return new SecurityContext() {
                    @Override
                    public UserInfo getUserPrincipal() {
                        return userInfo;
                    }

                    @Override
                    public boolean isUserInRole(String s) {
                        return userInfo.getRoles().contains(s);
                    }

                    @Override
                    public boolean isSecure() {
                        return "https".equalsIgnoreCase(httpServletRequest.getScheme());
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Token-Based-Auth-Scheme";
                    }
                };
            }
            return null;
        }

        return null;
    }

    private UserInfo login(String username, String password) throws LoginException {
        LoginContext loginContext = new LoginContext("karaf", callbacks -> {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(username);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        });
        loginContext.login();
        if (loginContext.getSubject() == null) {
            return null;
        }

        UserInfo userInfo = new UserInfo();
        LoginContext ctx = null;
        ctx = new LoginContext("karaf", callbacks -> {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(username);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }});
        ctx.login();

        List<String> roles = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        Subject subject = ctx.getSubject();
        for (Principal p : subject.getPrincipals()) {
            if ((p instanceof RolePrincipal)) {
                RolePrincipal g = (RolePrincipal) p;
                roles.add(g.getName());
            } else if ((p instanceof GroupPrincipal)) {
                GroupPrincipal g = (GroupPrincipal) p;
                groups.add(g.getName());
            }
        }
        userInfo.setRoles(roles);
        userInfo.setGroups(groups);

        return userInfo;
    }
}
