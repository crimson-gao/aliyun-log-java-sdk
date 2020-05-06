package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.LogStore;

public class UpdateLogStoreV2Request extends Request{

    private LogStore logStore;

    public UpdateLogStoreV2Request(String project, LogStore logStore) {
        super(project);
        this.logStore = logStore;
    }

    public LogStore getLogStore() {
        return logStore;
    }

    public void setLogStore(LogStore logStore) {
        this.logStore = logStore;
    }
}
