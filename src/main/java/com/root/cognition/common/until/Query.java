package com.root.cognition.common.until;

import com.root.cognition.common.config.Constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页查询参数
 * @author taoya
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	/**
	 * 总数量
	 * <p>
	 * offset
	 */
	private int offset;
	/**
	 * 当前页数显示数量
	 */
	private int limit;

	public Query(Map<String, Object> params) {
		this.putAll(params);
		// 分页参数
		this.offset = Integer.parseInt(params.get("offset").toString());
		this.limit = Integer.parseInt(params.get("limit").toString());
		this.put("offset", offset);
		this.put("page", offset / limit + 1);
		this.put("limit", limit);
		this.put("delFlag", Constant.DEL_FLAG_NORMAL);
	}

	public static Map<String, Object> withDelFlag() {
		Map<String,Object> params = new HashMap<>();
		params.put("delFlag", Constant.DEL_FLAG_NORMAL);
		return params;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}