// Name: Kyla Knauber
// Course: CMPSC 470
// Part 2 Due: April 30, 2025
// Homework 5: Semantic Checker

public class Token
{
    public String lexeme;
    public int lineno;
    public int column;
    public Token(String lexeme, int lineno, int column)
    {
        this.lexeme = lexeme;
        this.lineno = lineno;
        this.column += column;
    }
}
