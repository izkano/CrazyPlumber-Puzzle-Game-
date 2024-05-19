package model;

public enum GameMode {
	CLASSIC (0),
	RANDOM(1),
	TIMER (2),
	LIMITED (3),
	BUILDER (4),
	ONLINE (5),
	TESTING(6),
	PLAYBUILD(7);

	private final int value;

	GameMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}