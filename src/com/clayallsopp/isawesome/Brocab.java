package com.clayallsopp.isawesome;

import java.io.Serializable;

public class Brocab implements Serializable {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = -8403439264570933221L;
	String term;
    String author;
    String description;

    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
