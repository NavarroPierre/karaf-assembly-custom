package com.navarrop.realm;

import org.apache.karaf.jaas.boot.ProxyLoginModule;
import org.apache.karaf.jaas.config.JaasRealm;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true)
public class CustomJaasRealmService implements JaasRealm
{
    public static final String REALM_NAME = "myCustomRealm";
    private static final Logger logger = LoggerFactory.getLogger(CustomJaasRealmService.class);

    private AppConfigurationEntry[] configEntries;

    @Activate
    public void start(BundleContext bc)
    {
        logger.info("STARTING");

        Map<String, Object> options = new HashMap<>();
        configEntries = new AppConfigurationEntry[1];
        configEntries[0] = new AppConfigurationEntry(ProxyLoginModule.class.getName(),
                AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT, options);

        options.put(ProxyLoginModule.PROPERTY_MODULE, CustomLoginModule.class.getName());

        long bundleId = bc.getBundle().getBundleId();
        options.put(ProxyLoginModule.PROPERTY_BUNDLE, String.valueOf(bundleId));
        options.put(BundleContext.class.getName(), bc);

        logger.info("STARTED");
    }

    @Deactivate
    public void stop() {
        logger.info("STOPPED");

    }

    @Override
    public AppConfigurationEntry[] getEntries()
    {
        return configEntries;
    }

    @Override
    public String getName()
    {
        return REALM_NAME;
    }

    @Override
    public int getRank()
    {
        return 0;
    }
}
