package com.navarrop.rest.impl;

import com.navarrop.rest.api.RestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.component.annotations.Component;

import java.util.Date;

@Component(service = RestApi.class, property = { "osgi.jaxrs.resource=true" }, immediate = true)
public class RestImpl implements RestApi {

    private static Logger logger = LoggerFactory.getLogger(RestImpl.class);

    public long getTimestamp() {
        logger.info("getTimestamp()");
        return new Date().getTime();
    }

    public String echo(String message) {
        logger.info("echo({})", message);
        return message;
    }
}
