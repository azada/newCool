package cool.parser;

/**
 * This class lists terminals used by the
 * grammar specified in "cool.grammar".
 */
public class Terminals {
	static public final short EOF = 0;
	static public final short LPAREN = 1;
	static public final short ID = 2;
	static public final short LBRACE = 3;
	static public final short MINUS = 4;
	static public final short NULL = 5;
	static public final short NEW = 6;
	static public final short THIS = 7;
	static public final short SUPER = 8;
	static public final short NOT = 9;
	static public final short INTEGER = 10;
	static public final short STRING = 11;
	static public final short BOOLEAN = 12;
	static public final short RBRACE = 13;
	static public final short IF = 14;
	static public final short WHILE = 15;
	static public final short TYPE = 16;
	static public final short SEMI = 17;
	static public final short RPAREN = 18;
	static public final short COLON = 19;
	static public final short DOT = 20;
	static public final short ASSIGN = 21;
	static public final short VAR = 22;
	static public final short CASE = 23;
	static public final short COMMA = 24;
	static public final short DEF = 25;
	static public final short NATIVE = 26;
	static public final short TIMES = 27;
	static public final short DIV = 28;
	static public final short EQUALS = 29;
	static public final short CLASS = 30;
	static public final short OVERRIDE = 31;
	static public final short ARROW = 32;
	static public final short PLUS = 33;
	static public final short EXTENDS = 34;
	static public final short MATCH = 35;
	static public final short ELSE = 36;
	static public final short LE = 37;
	static public final short LT = 38;
	static public final short STUB = 39;

	static public final String[] NAMES = {
		"EOF",
		"LPAREN",
		"ID",
		"LBRACE",
		"MINUS",
		"NULL",
		"NEW",
		"THIS",
		"SUPER",
		"NOT",
		"INTEGER",
		"STRING",
		"BOOLEAN",
		"RBRACE",
		"IF",
		"WHILE",
		"TYPE",
		"SEMI",
		"RPAREN",
		"COLON",
		"DOT",
		"ASSIGN",
		"VAR",
		"CASE",
		"COMMA",
		"DEF",
		"NATIVE",
		"TIMES",
		"DIV",
		"EQUALS",
		"CLASS",
		"OVERRIDE",
		"ARROW",
		"PLUS",
		"EXTENDS",
		"MATCH",
		"ELSE",
		"LE",
		"LT",
		"STUB"
	};
}
