package com.aliyun.openservices.log.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.log.common.TagResources;

import java.util.List;
import java.util.Map;

public class ListTagResourcesResponse extends Response {
	private static final long serialVersionUID = -2784242482549088661L;
	private String tagList;
	private List<TagResources> tagResources;

	public ListTagResourcesResponse(Map<String, String> headers, String tagList) {
		super(headers);
		this.tagList = tagList;
	}

	public ListTagResourcesResponse(Map<String, String> headers, String tagList, List<TagResources> tagResources) {
		super(headers);
		this.tagList = tagList;
		this.tagResources = tagResources;
	}

	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	public List<TagResources> getTagResources() {
		return tagResources;
	}

	public void setTagResources(List<TagResources> tagResources) {
		this.tagResources = tagResources;
	}
}
