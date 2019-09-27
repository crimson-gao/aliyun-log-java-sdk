package com.aliyun.openservices.log.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for VersionInfo.
 */
public class VersionInfoUtilsTest {
    @Test
    public void getVersion() {
        String userAgent = VersionInfoUtils.getDefaultUserAgent();
        Assert.assertTrue(userAgent.startsWith("aliyun-log-sdk-java-0.6.39"));
    }
}
