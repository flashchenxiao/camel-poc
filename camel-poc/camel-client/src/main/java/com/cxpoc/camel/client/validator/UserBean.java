package com.cxpoc.camel.client.validator;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

/**
 * User entity
 *
 */
@Component
public class UserBean {

	@NotNull(message = "ID不能为空", groups = PutUserValidation.class)
	@DecimalMin(value = "1", message = "ID不能小于1", groups = PutUserValidation.class)
	@Max(value = 4, message = "ID不能大于4", groups = PutUserValidation.class)
	private Integer id;

	@Size(min = 1, max = 10, message = "NAME长度为1~10位", groups = PutUserValidation.class)
	@NotNull(message = "NAME不能为空", groups = PutUserValidation.class)
	private String name;

	public UserBean() {
	}

	public UserBean(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
