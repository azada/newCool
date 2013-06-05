
/**
 * Created with IntelliJ IDEA.
 * User: pedram
 * Date: 4/10/13
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
package cool;
import beaver.Symbol;
import beaver.Scanner;
import cool.parser.Terminals;

%%

%{




  int commentStartLine = 0;
  String stringValue = "";
  int stringStartLine = 0;
  int stringStartCol = 0;
  int mStringStartCol = 0;


  private static int lineNum = 1;
  void incrLineNum() {
     lineNum++;
  }
  public int getLineNum() {
     return lineNum;
  }
  Symbol getTerm(int token) {
    //return new Symbol((short)token, yychar, yychar + yytext().length(),
    //	       		      yyline+1, yycolumn, null);
     //yyend = zzMarkedPos;
     //zzStartRead
    // System.out.println("yychar = " + yychar);
     //System.out.println("yycolumn = " + yycolumn);
      // System.out.println("yyline = " + yyline);
     int pos = Symbol.makePosition(2, 5);
          int endPos = Symbol.makePosition(3,6);

          //return new Symbol((short)token, pos, endPos, yytext().length(), null);

          return new Symbol((short)token, yyline+1, yycolumn+1, yytext().length(), null);
     }

  Symbol getTerm(int token, Object val) {
  //                   System.out.println("yychar = " + yychar);
   //                       System.out.println("yycolumn = " + yycolumn);
    //                        System.out.println("yyline = " + yyline);
    //return new Symbol((short)token, yychar, yychar + yytext().length(), yytext().length(), null );
    int pos = Symbol.makePosition(2, 5);
         int endPos = Symbol.makePosition(3,6);
         if (token == Terminals.STRING) {
          //   System.out.println("yycolumn = " + stringStartCol);
              int len = ((String) val).length() + 2;
            //  System.out.println("len = " + len);

            return new Symbol((short)token, stringStartLine, mStringStartCol + stringStartCol + len, yytext().length(), val);
         }
         return new Symbol((short)token, yyline+1, yycolumn+1, yytext().length(), val);
    //return new Symbol((short)token, yyline, yyline + yytext().length(), -1, null );
 //   return null;
  }





  void error(String msg) {
    String full_msg = "Error at line " + (yyline+1) + " column " + (yycolumn - yytext().length()) + ": " + msg;
    MyCoolError.error(full_msg);

  }
   void error(String msg, String value) {
       String full_msg = "Error at line " + (yyline+1) + " column " + (yycolumn - value.length() + 1) + ": " + msg;
       MyCoolError.error(full_msg);

     }
  void error(String msg, int col) {
         String full_msg = "Error at line " + (yyline+1) + " column " + col + ": " + msg;
         MyCoolError.error(full_msg);

       }

%}

%class MyCoolScanner
%extends Scanner
%function nextToken
%type Symbol
%yylexthrow Scanner.Exception
%eofval{
	return new Symbol(Terminals.EOF, "end-of-file");
%eofval}
%line
%column
%char

%xstate STRING
%xstate MSTRING

%xstate COMMENT


SPACE = [ \n\t]+
//ENDOFLINE = [\n]+
ONELINECOMMENT = "//".*[\n]


%%

//{ENDOFLINE} { incrLineNum(); }
{SPACE}    { }

{ONELINECOMMENT}  {}

"/*" { yybegin(COMMENT); commentStartLine = yyline + 1; }
<COMMENT> {
  "*/"    { yybegin(YYINITIAL);  }
  .        { }
 [^\*]+   {  }
 \n       {  }
  <<EOF>> { error("Comment started at line " + commentStartLine + " has no closing match"); yybegin(YYINITIAL);}
}


<<EOF>>    { return getTerm(Terminals.EOF);}



"+"		{ return getTerm( Terminals.PLUS); }
"-"		{ return getTerm( Terminals.MINUS); }
"*"		{ return getTerm( Terminals.TIMES); }
"/"     { return getTerm( Terminals.DIV ); }


"{"	   { return getTerm( Terminals.LBRACE ); }
"}"	   { return getTerm( Terminals.RBRACE ); }
"("	   { return getTerm( Terminals.LPAREN ); }
")"	   { return getTerm( Terminals.RPAREN ); }

";"	   { return getTerm( Terminals.SEMI ); }
"."	   { return getTerm( Terminals.DOT );  }

"=="	   { return getTerm( Terminals.EQUALS );  }
"<"	   { return getTerm( Terminals.LT );  }
"<="	   { return getTerm( Terminals.LE); }
"!"    { return getTerm( Terminals.NOT); }

"="	   { return getTerm( Terminals.ASSIGN); }



":" 	{ return getTerm( Terminals.COLON); }
","		{ return getTerm( Terminals.COMMA); }
";"     { return getTerm( Terminals.SEMI); }

"=>"    { return getTerm( Terminals.ARROW); }
"."     { return getTerm( Terminals.DOT); }




"class"	    { return getTerm( Terminals.CLASS ); }
"extends"	    { return getTerm( Terminals.EXTENDS ); }
"var"       {return getTerm(Terminals.VAR); }
"override"  {return getTerm(Terminals.OVERRIDE); }
"def"       {return getTerm(Terminals.DEF); }
"native"    {return getTerm(Terminals.NATIVE); }
"null"      {return getTerm(Terminals.NULL); }
"this"      {return getTerm(Terminals.THIS); }
"super"	    { return getTerm( Terminals.SUPER ); }

"if"	    { return getTerm( Terminals.IF ); }
"else"	    { return getTerm( Terminals.ELSE ); }


"while"	    { return getTerm( Terminals.WHILE ); }
"case"	    { return getTerm( Terminals.CASE ); }
"match"     { return getTerm( Terminals.MATCH); }
"new"	    { return getTerm( Terminals.NEW ); }

"false"     { return getTerm(Terminals.BOOLEAN, new Boolean(false));}
"true"      { return getTerm(Terminals.BOOLEAN, new Boolean(true));}

"abstract" { error("abstract is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"catch"    { error("catch is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"do"       { error("do is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"final" { error("final is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"finally" { error("finally is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"for" { error("for is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"implicit" { error("implicit is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"import" { error("import is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"lazy" { error("lazy is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"object" { error("object is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"forSome" { error("forSome is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"package" { error("package is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"private" { error("private is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"protected" { error("protected is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"requires" { error("requires is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"return" { error("return is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"sealed" { error("sealed is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"throw" { error("throw is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"trait" { error("trait is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"try" { error("try is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"type" { error("type is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"val" { error("val is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"with" { error("with is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"yield" { error("yield is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}
"Cool" { error("Cool is a reserved word", ""); return getTerm( Terminals.ID, yytext() );}

[a-z][_a-zA-Z0-9]*  { return getTerm( Terminals.ID, yytext()); }
[A-Z][_a-zA-Z0-9]*  { return getTerm( Terminals.TYPE, yytext()); }


[0-9]+	{ return getTerm( Terminals.INTEGER, yytext() ); }

"\"\"\"" { yybegin(MSTRING); mStringStartCol = -2; stringValue = ""; stringStartLine = yyline + 1; stringStartCol = yycolumn + 1; }
<MSTRING> {
	[^\n\"\\]+		{ stringValue += yytext(); }
	"\\0"			{ stringValue += "\0"; }
	"\\b"			{ stringValue += "\b"; }
	"\\f"			{ stringValue += "\f"; }
	"\\t"			{ stringValue += "\t"; }
	"\\n"			{ stringValue += "\n"; }
	"\\r"			{ stringValue += "\r"; }
    "\\\""          { stringValue += "\""; }

	"\n"			{ stringValue += "\n";}
//	"\"\""			{ error("Missing one \"", yycolumn + 1);yybegin(YYINITIAL);  }
//	"\""			{ error("Missing two \"s", yycolumn + 1);yybegin(YYINITIAL); }
	"\\"			{ stringValue += "\\" ;}
	"\"\"\""	    { yybegin(YYINITIAL); return getTerm(Terminals.STRING, stringValue); }
    "\\\n"          {}
}



"\"" { yybegin(STRING); stringValue = ""; mStringStartCol = 0; stringStartLine = yyline + 1; stringStartCol = yycolumn + 1; }
<STRING> {
	[^\n\"\\]+		{ stringValue += yytext(); }
	"\\0"			{ stringValue += "\0"; }
	"\\b"			{ stringValue += "\b"; }
	"\\f"			{ stringValue += "\f"; }
	"\\t"			{ stringValue += "\t"; }
	"\\n"			{ stringValue += "\n"; }
	"\\r"			{ stringValue += "\r"; }
    "\\\""          { stringValue += "\""; }

	"\n"			{ error("Newline is not allowd in string or you didn't close the string", stringValue); yybegin(YYINITIAL);  }
	"\\"			{ stringValue += "\\" ;}
	"\""			{ yybegin(YYINITIAL); return getTerm(Terminals.STRING, stringValue); }
    "\\\n"          {}
}


.   { error("Unknown symbol '" + yytext() + "' ", yycolumn+1); }

