package com.dto;

import com.entity.User;
import lombok.Data;

import java.util.List;
@Data
public class UserDto extends User {

	private static final long serialVersionUID = -184009306207076712L;

    private String phidden;
	private String chidden;

	private List<Integer> hobbyIds;
	private List<Integer> permissionIds;

	private String hobby;

	private String dhidden;

	private String address;
	private String zt;


}
