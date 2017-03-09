package org.mycompany.feign.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relation {
	
	public static enum Type {
		CUSTOMER, CONTACT_PERSON, EMPLOYEE;
    }
	
	private Type type;
	
	private Contact from;
	
	private Contact to;
}