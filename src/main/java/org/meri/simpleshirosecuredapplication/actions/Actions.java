package org.meri.simpleshirosecuredapplication.actions;

public enum Actions {

	MANAGE_REPAIRMANS("MANAGE_REPAIRMANS"), MANAGE_SALES("MANAGE_SALES"), MANAGE_SCIENTISTS("MANAGE_SCIENTISTS"),
	REPAIR_REFRIGERATOR("REPAIR_REFRIGERATOR"), REPAIR_FRIDGE("REPAIR_FRIDGE"), REPAIR_DOOR("REPAIR_DOOR"),
	SALE_PRODUCT("SALE_PRODUCT"), COLLECT_BONUS("COLLECT_BONUS"), MEET_CUSTOMER("MEET_CUSTOMER"),
	RESEARCH_NEW_STUFF("RESEARCH_NEW_STUFF"), WRITE_ARTICLE("WRITE_ARTICLE"), PREPARE_TALK("PREPARE_TALK");

	public String doIt() {
		return "Function " + getName() + " run succesfully.";
	}

	public String getName() {
		return name;
	}

	private Actions(String name) {
		this.name = name;
	}

	private final String name;

}
