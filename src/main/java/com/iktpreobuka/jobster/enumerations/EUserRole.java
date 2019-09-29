package com.iktpreobuka.jobster.enumerations;

public enum EUserRole /* implements GrantedAuthority */ {
	ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT, ROLE_STUDENT
	
	/* ROLE_ADMIN(Code.ROLE_ADMIN),
	ROLE_TEACHER(Code.ROLE_TEACHER),
	ROLE_PARENT(Code.ROLE_PARENT),
	ROLE_STUDENT(Code.ROLE_STUDENT);
	
	private final String authority;

    EUserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public class Code {
		public static final String ROLE_ADMIN = "ROLE_ADMIN";
		public static final String ROLE_TEACHER = "ROLE_TEACHER";
		public static final String ROLE_PARENT = "ROLE_PARENT";
		public static final String ROLE_STUDENT = "ROLE_STUDENT";
	}
    
    private Long id;
    private String name;

    // ********************************************************************
    
    
    EUserRole(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static EUserRole from(Long id) {
        for(EUserRole position: values()) {
            if(position.getId().equals(id))
                return position;
        }

        throw new IllegalArgumentException("Cannot get position for ID " + id);
    }

    public static EUserRole from(String name) {
        for(EUserRole position: values()) {
            if(position.getName().toUpperCase().equals(name.toUpperCase()))
                return position;
        }

        throw new IllegalArgumentException("No such position " + name);
    } */
	
}
