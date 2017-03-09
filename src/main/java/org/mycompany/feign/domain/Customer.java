package org.mycompany.feign.domain;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
	
	public static enum Type {
        PERSON, INSTITUTION
    }
	
	private String displayName;
	
	private Integer rating;

	private Type type;
	
	// FIXME: mapping of _embedded relations not working yet
	// @JsonProperty("_embedded") - this annotation sets the whole _embedded JSON on the relations property
	private Set<Relation> relations;
	
}