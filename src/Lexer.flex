/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// Name: Kyla Knauber
// Course: CMPSC 470
// Part 2 Due: April 30, 2025
// Homework 5: Semantic Checker

%%

%class Lexer
%byaccj

%{

  public Parser   parser;
  public int      lineno;
  public int      column;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;
  }

%}

num          = [0-9]+("."[0-9]+)?
identifier   = [a-zA-Z][a-zA-Z0-9_]*
bool         = "true"|"false"
newline      = \n
whitespace   = [ \t\r]+
linecomment  = "%%".*
blockcomment = "%("[^]*")%"

%%

"return"                            { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.RETURN     ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.PRINT      ; }
"if"                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.IF         ; }
"else"                              { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.ELSE       ; }
"while"                             { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.WHILE      ; }
"{"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.BEGIN      ; }
"}"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.END        ; }
"("                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.RPAREN     ; }
"["                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.LBRACKET   ; }
"]"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.RBRACKET   ; }
"num"                               { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.NUM        ; }
"bool"                              { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.BOOL       ; }
"new"                               { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.NEW        ; }
"size"                              { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.SIZE       ; }
"<-"                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.ASSIGN     ; }
"+"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.ADD        ; }
"-"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.SUB        ; }
"*"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.MUL        ; }
"/"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.DIV        ; }
"%"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.MOD        ; }
"and"                               { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.AND        ; }
"or"                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.OR         ; }
"not"                               { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.NOT        ; }
"<"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.LT         ; }
">"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.GT         ; }
"<="                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.LE         ; }
">="                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.GE         ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.EQ         ; }
"<>"                                { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.NE         ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.SEMI       ; }
","                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.COMMA      ; }
"."                                 { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.DOT        ; }
{bool}                              { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.BOOL_LIT   ; }
{num}                               { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.NUM_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext(), lineno, column)); column += yytext().length(); return Parser.IDENT      ; }
{linecomment}                       { column += yytext().length(); }
{newline}                           { lineno++; column = 1; }
{whitespace}                        { column += yytext().length(); }
{blockcomment}                      { for(int i = 0; i < yytext().length(); i++) {
                                        if (yytext().charAt(i) == '\n') {
                                            lineno++;
                                            column = 1;
                                        } else {
                                            column++;
                                        }
                                       }
                                    }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
