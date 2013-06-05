package cool.parser;

/**
 * This class lists terminals used by the
 * grammar specified in "cool.grammar".
 */
public class Terminals {
	static public final short ARROW = 38;
	static public final short ASSIGN = 30;
	static public final short BOOLEAN = 21;
	static public final short CASE = 7;
	static public final short CLASS = 36;
	static public final short COLON = 33;
	static public final short COMMA = 4;
	static public final short DEF = 32;
	static public final short DIV = 24;
	static public final short DOT = 25;
	static public final short ELSE = 8;
	static public final short EOF = 0;
	static public final short EQUALS = 13;
	static public final short EXTENDS = 35;
	static public final short ID = 10;
	static public final short IF = 27;
	static public final short INTEGER = 19;
	static public final short LBRACE = 5;
	static public final short LE = 11;
	static public final short LPAREN = 9;
	static public final short LT = 12;
	static public final short MATCH = 26;
	static public final short MINUS = 3;
	static public final short NATIVE = 37;
	static public final short NEW = 15;
	static public final short NOT = 18;
	static public final short NULL = 14;
	static public final short OVERRIDE = 34;
	static public final short PLUS = 22;
	static public final short RBRACE = 1;
	static public final short RPAREN = 2;
	static public final short SEMI = 6;
	static public final short STRING = 20;
	static public final short STUB = 39;
	static public final short SUPER = 17;
	static public final short THIS = 16;
	static public final short TIMES = 23;
	static public final short TYPE = 31;
	static public final short VAR = 29;
	static public final short WHILE = 28;

	static public final String[] NAMES = {
		"EOF",
		"RBRACE",
		"RPAREN",
		"MINUS",
		"COMMA",
		"LBRACE",
		"SEMI",
		"CASE",
		"ELSE",
		"LPAREN",
		"ID",
		"LE",
		"LT",
		"EQUALS",
		"NULL",
		"NEW",
		"THIS",
		"SUPER",
		"NOT",
		"INTEGER",
		"STRING",
		"BOOLEAN",
		"PLUS",
		"TIMES",
		"DIV",
		"DOT",
		"MATCH",
		"IF",
		"WHILE",
		"VAR",
		"ASSIGN",
		"TYPE",
		"DEF",
		"COLON",
		"OVERRIDE",
		"EXTENDS",
		"CLASS",
		"NATIVE",
		"ARROW",
		"STUB"
	};
}
