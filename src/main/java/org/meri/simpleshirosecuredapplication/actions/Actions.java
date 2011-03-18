package org.meri.simpleshirosecuredapplication.actions;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;

public enum Actions {

	MANAGE_REPAIRMEN("MANAGE_REPAIRMEN", "functions:manage:repairmen"), MANAGE_SALES("MANAGE_SALES", "functions:manage:sales"), MANAGE_SCIENTISTS("MANAGE_SCIENTISTS", "functions:manage:scientists"),
	REPAIR_REFRIGERATOR("REPAIR_REFRIGERATOR", "functions:repair:refrigerator"), REPAIR_FRIDGE("REPAIR_FRIDGE", "functions:repair:bridge"), REPAIR_DOOR("REPAIR_DOOR", "functions:repair:door"),
	SALE_PRODUCT("SALE_PRODUCT", "functions:sale:product"), COLLECT_BONUS("COLLECT_BONUS", "functions:sale:collectbonus"), MEET_CUSTOMER("MEET_CUSTOMER", "functions:sale:meetcustomer"),
	RESEARCH_NEW_STUFF("RESEARCH_NEW_STUFF", "functions:science:research"), WRITE_ARTICLE("WRITE_ARTICLE", "functions:science:writearticle"), PREPARE_TALK("PREPARE_TALK", "functions:science:preparetalk");

	public String doIt() {
		String neededPermission = getNeededPermission();
		if (SecurityUtils.getSubject().isPermitted(neededPermission))
			return "Function " + getName() + " run succesfully.";
		
		throw new UnauthorizedException("Logged user does not have " + neededPermission + " permission");
	}

	private String getNeededPermission() {
	  return permission;
  }

	public String getName() {
		return name;
	}

	private Actions(String name, String permission) {
		this.name = name;
		this.permission = permission;
	}

	private final String name;
	private final String permission;

}
