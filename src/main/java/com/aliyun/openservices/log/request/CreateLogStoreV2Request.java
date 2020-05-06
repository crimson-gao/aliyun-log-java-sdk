package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.LogStore;

public class CreateLogStoreV2Request extends Request{

    private LogStore logStore;

    public CreateLogStoreV2Request(String project,LogStore logStore) {
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
