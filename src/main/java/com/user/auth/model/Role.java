package com.user.auth.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long ROLE_ID;

    @Column
    private String name;

    @Column
    private String description;
    
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="roles")
	private Collection<DAOUser> users=new ArrayList<DAOUser>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<DAOUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<DAOUser> users) {
		this.users = users;
	}
    
    

}
