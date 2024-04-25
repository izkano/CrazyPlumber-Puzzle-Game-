package model;

public enum GameMode {
	CLASSIC (0),
	TIMER (1),
	LIMITED (2),
	BUILDER (3),
	ONLINE (4);

	private final int value;

	GameMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}