package org.mycompany.feign.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	// current annotation sets the whole JSON string on the relations property
	@JsonProperty("_embedded")
	private Set<Relation> relations;
	
}