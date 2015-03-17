package com.codepath.chirrupy.misc;

public enum ActivityRequestCodes {
	None(0),
	Compose(100),
	Profile(200);

	private int value = 0;

	ActivityRequestCodes(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
