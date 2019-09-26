package com.aliyun.openservices.log.util;

import org.junit.Test;

/**
 * @author jiashuangkai
 * @version 1.0
 * @since 2019-09-26 16:45
 */
public class VersionInfoUtilsTest {
    @Test
    public void getVersion() {
        String userAgent = VersionInfoUtils.getDefaultUserAgent();
        System.out.println(userAgent);
    }
}
