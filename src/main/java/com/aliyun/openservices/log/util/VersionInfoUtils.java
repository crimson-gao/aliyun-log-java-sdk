package com.aliyun.openservices.log.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author jiashuangkai
 * @version 1.0
 * @since 2019-09-26 16:21
 */
public class VersionInfoUtils {
    private static final String VERSION_INFO_FILE = "versioninfo.properties";
    private static final String USER_AGENT_PREFIX = "aliyun-sdk-java";

    private static String version = null;

    private static String defaultUserAgent = null;

    public static String getVersion() {
        if (version == null) {
            initializeVersion();
        }
        return version;
    }

    public static String getDefaultUserAgent() {
        if (defaultUserAgent == null) {
            defaultUserAgent = getVersion()+"/" + System.getProperty("java.version");
        }
        return defaultUserAgent;
    }

    private static void initializeVersion() {
        InputStream inputStream = VersionInfoUtils.class.getClassLoader().getResourceAsStream(VERSION_INFO_FILE);
        Properties versionInfoProperties = new Properties();
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException(VERSION_INFO_FILE + " not found on classpath");
            }

            versionInfoProperties.load(inputStream);
            version = versionInfoProperties.getProperty("version");
        } catch (Exception e) {
            System.out.println("Unable to load version information for the running SDK: " + e.getMessage());
            version = "unknown-version";
        }
    }
}
