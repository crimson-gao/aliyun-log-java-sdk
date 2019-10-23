package com.aliyun.openservices.log.common;

import com.aliyun.openservices.log.util.JsonUtils;
import net.sf.json.JSONObject;

public class AliyunMaxComputeSource extends DataSource {
	private String accessKeyId;
	private String accessKeySecret;
	private String endpoint;
	private String tunnelEndpoint;
	private String project;
	private String table;
	private String partitionSpec;

	public AliyunMaxComputeSource() {
		super(DataSourceType.ALIYUN_MAX_COMPUTE);
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getTunnelEndpoint() {
		return tunnelEndpoint;
	}

	public void setTunnelEndpoint(String tunnelEndpoint) {
		this.tunnelEndpoint = tunnelEndpoint;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPartitionSpec() {
		return partitionSpec;
	}

	public void setPartitionSpec(String partitionSpec) {
		this.partitionSpec = partitionSpec;
	}

	@Override
	public void deserialize(JSONObject jsonObject) {
		super.deserialize(jsonObject);
		accessKeyId = JsonUtils.readOptionalString(jsonObject, "accessKeyId");
		accessKeySecret = JsonUtils.readOptionalString(jsonObject, "accessKeySecret");
		endpoint = JsonUtils.readOptionalString(jsonObject, "endpoint");
		tunnelEndpoint = JsonUtils.readOptionalString(jsonObject, "tunnelEndpoint");
		project = JsonUtils.readOptionalString(jsonObject, "project");
		table = JsonUtils.readOptionalString(jsonObject, "table");
		partitionSpec = JsonUtils.readOptionalString(jsonObject, "partitionSpec");
	}
}
