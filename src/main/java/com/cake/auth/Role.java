package com.cake.auth;

import java.util.Set;
 import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum Role {
	CUSTOMER(Sets.newHashSet()),
	EMPLOYEE(Sets.newHashSet(
			RolePermission.BOOK_READ, 
			RolePermission.BOOK_WRITE,
			RolePermission.USER_READ,
			RolePermission.USER_WRITE)),
	BUSSINESS(Sets.newHashSet(
			RolePermission.STAT_READ)),
	ADMIN(Sets.newHashSet(
			RolePermission.BOOK_READ, 
			RolePermission.BOOK_WRITE,
			RolePermission.BOOK_DELETE,
			RolePermission.USER_READ,
			RolePermission.USER_WRITE,
			RolePermission.USER_DELETE,
			RolePermission.STAT_READ));
	
	private final Set<RolePermission> permissions;

	
	
	Role(Set<RolePermission> permissions) {
		this.permissions = permissions;
	}


	public Set<RolePermission> getPermissions() {
		return permissions;
	}
	
	//cast the permissions to Set<SimpleGrantedAuthority>
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
		Set<SimpleGrantedAuthority> permissions=
		getPermissions().stream()
			.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
			.collect(Collectors.toSet());
		permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
		return permissions;
	}
}
