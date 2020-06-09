package com.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

public class Dict extends Model<Dict> implements Serializable {

	private static final long serialVersionUID = -2431140186410912787L;
	private String type;
	private String k;
	private String val;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
