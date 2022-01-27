package com.cake.auth;

public enum RolePermission {
	BOOK_READ("book:read"),
	BOOK_WRITE("book:write"),
	BOOK_DELETE("book:delete"),
	USER_READ("user:read"),
	USER_WRITE("user:write"),
	USER_DELETE("user:delete"),
	STAT_READ("stat:read");
	
	private final String permission;

	private RolePermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
	
	
}
