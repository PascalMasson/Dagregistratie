package com.naugrim.zeecontainer.frame;

import java.util.Random;

public enum Dag {
	MAANDAG(1), DINSDAG(2), WOENSDAG(3), DONDERDAG(4), VRIJDAG(5), ZATERDAG(6), ZONDAG(7), ALLE(8);

	private final int dagnummer;

	Dag(int num) {
		dagnummer = num;
	}

	public int dagnummer() {
		return dagnummer;
	}

	public static Dag getDagFromDagnumber(int num) {
		switch (num) {
			case 1:
				return MAANDAG;
			case 2:
				return DINSDAG;
			case 3:
				return WOENSDAG;
			case 4:
				return DONDERDAG;
			case 5:
				return VRIJDAG;
			case 6:
				return ZATERDAG;
			case 7:
				return ZONDAG;
			default:
				return ALLE;
		}
	}

	public static Dag RandomDag() {
		// TODO Auto-generated method stub
		return getDagFromDagnumber(new Random().nextInt(5) + 1);
	}

	public String toString() {
		if (this == MAANDAG) {
			return "Maandag";
		}
		else if (this == DINSDAG) {
			return "Dinsdag";
		}
		else if (this == WOENSDAG) {
			return "Woensdag";
		}
		else if (this == DONDERDAG) {
			return "Donderdag";
		}
		else if (this == VRIJDAG) {
			return "Vrijdag";
		}
		else if (this == ZATERDAG) {
			return "Zaterdag";
		}
		else if (this == ZONDAG) {
			return "Zondag";
		}
		else if (this == ALLE) {
			return "Alle";
		}
		else {
			return "";
		}
	}

	public static Dag fromString(String s) {
		if (s.equalsIgnoreCase("maandag")) {
			return MAANDAG;
		}
		else if (s.equalsIgnoreCase("dinsdag")) {
			return DINSDAG;
		}
		else if (s.equalsIgnoreCase("woensdag")) {
			return WOENSDAG;
		}
		else if (s.equalsIgnoreCase("donderdag")) {
			return DONDERDAG;
		}
		else if (s.equalsIgnoreCase("vrijdag")) {
			return VRIJDAG;
		}
		else if (s.equalsIgnoreCase("zaterdag")) {
			return ZATERDAG;
		}
		else if (s.equalsIgnoreCase("zondag")) {
			return ZONDAG;
		}
		else if (s.equalsIgnoreCase("alle")) {
			return ALLE;
		}
		return null;
	}

	public static int getNumberFromDag(Dag dag) {
		if (dag == MAANDAG) {
			return 1;
		}
		else if (dag == DINSDAG) {
			return 2;
		}
		else if (dag == WOENSDAG) {
			return 3;
		}
		else if (dag == DONDERDAG) {
			return 4;
		}
		else if (dag == VRIJDAG) {
			return 5;
		}
		else if (dag == ZATERDAG) {
			return 6;
		}
		else if (dag == ZONDAG) {
			return 7;
		}
		return 0;
	}
}
