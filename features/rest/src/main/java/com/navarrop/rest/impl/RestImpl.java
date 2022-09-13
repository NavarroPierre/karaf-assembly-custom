package com.navarrop.rest.impl;

import com.navarrop.rest.api.RestApi;
import com.navarrop.rest.dto.UserInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.core.SecurityContext;
import java.util.Date;

@Component(service = RestApi.class, property = { "osgi.jaxrs.resource=true" }, immediate = true)
public class RestImpl implements RestApi {

    private static Logger logger = LoggerFactory.getLogger(RestImpl.class);

    @Activate
    public void start() {
        logger.info("STARTED");
    }

    @Deactivate
    public void stop() {
        logger.info("STOPPED");
    }

    @Override
    public long getTimestamp() {
        logger.info("getTimestamp()");
        return new Date().getTime();
    }

    @Override
    public String echo(String message) {
        logger.info("echo({})", message);
        return message;
    }

    @Override
    public String user(SecurityContext securityContext, String message) {
        String user = ((UserInfo) securityContext.getUserPrincipal()).toJsonString();
        logger.info("user({})", user);
        return user;
    }
}
