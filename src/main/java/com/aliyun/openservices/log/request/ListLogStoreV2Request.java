/*
 * Copyright (C) Alibaba Cloud Computing All rights reserved.
 */
package com.aliyun.openservices.log.request;

import com.aliyun.openservices.log.common.Consts;

/**
 * The request used to list log store from sls server
 * @author sls_dev
 *
 */
public class ListLogStoreV2Request extends Request {

	private int offset;
	private int size;
	private String telemetryType;

	public ListLogStoreV2Request(String project, int offset, int size, String telemetryType)
	{
		super(project);
		this.offset = offset;
		this.size = size;
		this.telemetryType =telemetryType;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTelemetryType() {
		return telemetryType;
	}

	public void setTelemetryType(String telemetryType) {
		this.telemetryType = telemetryType;
	}
}
