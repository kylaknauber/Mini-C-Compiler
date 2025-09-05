//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 20 "Parser.y"
import java.io.*;
//#line 19 "Parser.java"




public class Parser
             extends ParserImpl
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ASSIGN=257;
public final static short OR=258;
public final static short AND=259;
public final static short NOT=260;
public final static short EQ=261;
public final static short NE=262;
public final static short LE=263;
public final static short LT=264;
public final static short GE=265;
public final static short GT=266;
public final static short ADD=267;
public final static short SUB=268;
public final static short MUL=269;
public final static short DIV=270;
public final static short MOD=271;
public final static short IDENT=272;
public final static short NUM_LIT=273;
public final static short BOOL_LIT=274;
public final static short BOOL=275;
public final static short NUM=276;
public final static short IF=277;
public final static short ELSE=278;
public final static short WHILE=279;
public final static short PRINT=280;
public final static short RETURN=281;
public final static short BEGIN=282;
public final static short END=283;
public final static short LPAREN=284;
public final static short RPAREN=285;
public final static short LBRACKET=286;
public final static short RBRACKET=287;
public final static short SEMI=288;
public final static short COMMA=289;
public final static short NEW=290;
public final static short DOT=291;
public final static short SIZE=292;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,   22,    3,    8,    8,    9,    9,
   10,    6,    6,    7,    7,    4,    4,    5,   13,   13,
   14,   14,   14,   14,   14,   14,   15,   15,   16,   17,
   18,   19,   23,   20,   11,   11,   12,   12,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    0,   10,    1,    0,    3,    1,
    2,    1,    3,    1,    1,    2,    0,    3,    2,    0,
    1,    1,    1,    1,    1,    1,    4,    7,    3,    3,
    7,    5,    0,    5,    1,    0,    3,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    2,    3,    1,    1,    1,    4,    5,    4,    3,
};
final static short yydefred[] = {                         3,
    0,    0,   15,   14,    2,    4,    0,    0,    0,    0,
    0,   13,    0,    0,    0,   10,   11,    0,    0,   17,
    9,    0,   16,    0,   20,    0,    0,   18,    0,    0,
    0,    0,    0,   17,    6,   19,   21,   22,   23,   24,
   25,   26,    0,    0,    0,    0,    0,    0,   55,   56,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   29,   30,
   20,   27,    0,    0,    0,    0,    0,    0,    0,   60,
   53,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   41,   42,   43,    0,    0,    0,   32,   57,
    0,   59,    0,   34,    0,    0,    0,   58,   28,   31,
};
final static short yydgoto[] = {                          1,
    2,    5,    6,   22,   23,   13,    8,   14,   15,   16,
   86,   87,   27,   36,   37,   38,   39,   40,   41,   42,
   53,   25,   81,
};
final static short yysindex[] = {                         0,
    0, -267,    0,    0,    0,    0, -242, -269, -252, -236,
 -267,    0, -195, -214, -204,    0,    0, -191, -267,    0,
    0, -267,    0, -170,    0, -171,   13,    0, -255, -163,
 -162,   15,   15,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   15,   15,   15,   15,   15, -211,    0,    0,
   15, -267,  -39,  -25, -267,  -11,   45,  100,  125,  136,
   15,   15, -168,  150, -161,   15,   15,   15,   15,   15,
   15,   15,   15,   15,   15,   15,   15,   15,    0,    0,
    0,    0, -134,  212,  212, -159, -155,  175,   59,    0,
    0,   15,   72,  136,  111,  111, -264, -264, -264, -264,
 -157, -157,    0,    0,    0,  176,   15, -150,    0,    0,
   15,    0,   86,    0,    3,  212,  175,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,  143,    0,    0,    0,    0,    0, -120,    0,    0,
 -125,    0,    0,    0, -119,    0,    0,    0,    0,    0,
    0,  188,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -201,    0,    0,
    0,    0,    0,    0,  200,    0,    0,    0,    0,  -94,
 -118,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -116, -213,    0,    0,
    0,    0,  -71,  -85, -206,  -76, -239, -126, -117, -108,
 -169, -158,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -174,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  141,    0,   -1,  124,    0,    0,  159,
    0,    0,  103,  -69,    0,    0,    0,    0,    0,    0,
  -33,    0,    0,
};
final static int YYTABLESIZE=494;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
    7,   43,   74,   75,   76,   77,   78,    3,    4,   56,
   57,   58,   59,   60,  108,  109,   10,   64,   46,   46,
   24,   46,   46,   46,   46,   46,   46,   88,   89,    9,
   44,   11,   93,   94,   95,   96,   97,   98,   99,  100,
  101,  102,  103,  104,  105,   46,  120,   46,   46,   46,
   12,   44,   44,   24,   44,   44,   54,   54,  113,   54,
   54,   54,   54,   54,   54,   54,   54,   54,   54,   54,
   18,   38,   61,  115,   62,   38,   17,  117,   44,   63,
   44,   44,   44,   54,   19,   54,   54,   54,   39,   39,
   20,   39,   39,   39,   39,   39,   39,   39,   39,   40,
   40,   26,   40,   40,   40,   40,   40,   40,   40,   40,
   37,   76,   77,   78,   37,   39,   28,   39,   39,   39,
   45,   46,  107,   90,   92,  110,   40,  116,   40,   40,
   40,   47,   47,  111,   47,   47,   47,   47,   47,   47,
   48,   48,    1,   48,   48,   48,   48,   48,   48,   49,
   49,   12,   49,   49,   49,   49,   49,   49,   47,    8,
   47,   47,   47,   52,   52,    7,   36,   48,   35,   48,
   48,   48,   50,   50,   55,   65,   49,   21,   49,   49,
   49,   45,   45,  106,   45,   45,   51,    0,    0,    0,
   52,    0,   52,   52,   52,    0,    0,    0,    0,   50,
    0,   50,   50,   50,    0,    0,    0,    0,   45,    0,
   45,   45,   45,   51,    0,   51,   51,   51,   66,   67,
    0,   68,   69,   70,   71,   72,   73,   74,   75,   76,
   77,   78,   66,   67,    0,   68,   69,   70,   71,   72,
   73,   74,   75,   76,   77,   78,   66,   67,   79,   68,
   69,   70,   71,   72,   73,   74,   75,   76,   77,   78,
   66,   67,   80,   68,   69,   70,   71,   72,   73,   74,
   75,   76,   77,   78,   47,    0,   82,    0,    0,    0,
    0,    0,    0,    0,   29,    0,   48,   49,   50,   30,
  119,   31,   32,   33,   34,   35,    0,    0,   51,    0,
    0,    0,   66,   67,   52,   68,   69,   70,   71,   72,
   73,   74,   75,   76,   77,   78,   66,   67,    0,   68,
   69,   70,   71,   72,   73,   74,   75,   76,   77,   78,
   67,   83,   68,   69,   70,   71,   72,   73,   74,   75,
   76,   77,   78,   66,   67,  112,   68,   69,   70,   71,
   72,   73,   74,   75,   76,   77,   78,   66,   67,    0,
   68,   69,   70,   71,   72,   73,   74,   75,   76,   77,
   78,    0,  118,   70,   71,   72,   73,   74,   75,   76,
   77,   78,   66,   67,   84,   68,   69,   70,   71,   72,
   73,   74,   75,   76,   77,   78,   68,   69,   70,   71,
   72,   73,   74,   75,   76,   77,   78,   66,   67,   85,
   68,   69,   70,   71,   72,   73,   74,   75,   76,   77,
   78,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   66,   67,   91,   68,   69,   70,   71,   72,
   73,   74,   75,   76,   77,   78,    0,   29,    0,    0,
    0,    0,   30,    0,   31,   32,   33,   34,  114,    5,
    0,    0,    0,    0,    5,    0,    5,    5,    5,    5,
    5,   33,    0,    0,    0,    0,   33,    0,   33,   33,
   33,   33,   33,   29,    0,    0,    0,    0,   30,    0,
   31,   32,   33,   34,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    2,  257,  267,  268,  269,  270,  271,  275,  276,   43,
   44,   45,   46,   47,   84,   85,  286,   51,  258,  259,
   22,  261,  262,  263,  264,  265,  266,   61,   62,  272,
  286,  284,   66,   67,   68,   69,   70,   71,   72,   73,
   74,   75,   76,   77,   78,  285,  116,  287,  288,  289,
  287,  258,  259,   55,  261,  262,  258,  259,   92,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  285,  285,  284,  107,  286,  289,  272,  111,  285,  291,
  287,  288,  289,  285,  289,  287,  288,  289,  258,  259,
  282,  261,  262,  263,  264,  265,  266,  267,  268,  258,
  259,  272,  261,  262,  263,  264,  265,  266,  267,  268,
  285,  269,  270,  271,  289,  285,  288,  287,  288,  289,
  284,  284,  257,  292,  286,  285,  285,  278,  287,  288,
  289,  258,  259,  289,  261,  262,  263,  264,  265,  266,
  258,  259,    0,  261,  262,  263,  264,  265,  266,  258,
  259,  272,  261,  262,  263,  264,  265,  266,  285,  285,
  287,  288,  289,  258,  259,  285,  285,  285,  285,  287,
  288,  289,  258,  259,   34,   52,  285,   19,  287,  288,
  289,  258,  259,   81,  261,  262,  258,   -1,   -1,   -1,
  285,   -1,  287,  288,  289,   -1,   -1,   -1,   -1,  285,
   -1,  287,  288,  289,   -1,   -1,   -1,   -1,  285,   -1,
  287,  288,  289,  285,   -1,  287,  288,  289,  258,  259,
   -1,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,   -1,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  258,  259,  288,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  258,  259,  288,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  260,   -1,  288,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  272,   -1,  272,  273,  274,  277,
  288,  279,  280,  281,  282,  283,   -1,   -1,  284,   -1,
   -1,   -1,  258,  259,  290,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  258,  259,   -1,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  259,  287,  261,  262,  263,  264,  265,  266,  267,  268,
  269,  270,  271,  258,  259,  287,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  270,  271,  258,  259,   -1,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
  271,   -1,  287,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,  285,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  270,  271,  258,  259,  285,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  258,  259,  285,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,  272,   -1,   -1,
   -1,   -1,  277,   -1,  279,  280,  281,  282,  283,  272,
   -1,   -1,   -1,   -1,  277,   -1,  279,  280,  281,  282,
  283,  272,   -1,   -1,   -1,   -1,  277,   -1,  279,  280,
  281,  282,  283,  272,   -1,   -1,   -1,   -1,  277,   -1,
  279,  280,  281,  282,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=292;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ASSIGN","OR","AND","NOT","EQ","NE","LE","LT","GE","GT","ADD",
"SUB","MUL","DIV","MOD","IDENT","NUM_LIT","BOOL_LIT","BOOL","NUM","IF","ELSE",
"WHILE","PRINT","RETURN","BEGIN","END","LPAREN","RPAREN","LBRACKET","RBRACKET",
"SEMI","COMMA","NEW","DOT","SIZE",
};
final static String yyrule[] = {
"$accept : program",
"program : decl_list",
"decl_list : decl_list decl",
"decl_list :",
"decl : func_decl",
"$$1 :",
"func_decl : type_spec IDENT LPAREN params RPAREN BEGIN local_decls $$1 stmt_list END",
"params : param_list",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_spec IDENT",
"type_spec : prim_type",
"type_spec : prim_type LBRACKET RBRACKET",
"prim_type : NUM",
"prim_type : BOOL",
"local_decls : local_decls local_decl",
"local_decls :",
"local_decl : type_spec IDENT SEMI",
"stmt_list : stmt_list stmt",
"stmt_list :",
"stmt : assign_stmt",
"stmt : print_stmt",
"stmt : return_stmt",
"stmt : if_stmt",
"stmt : while_stmt",
"stmt : compound_stmt",
"assign_stmt : IDENT ASSIGN expr SEMI",
"assign_stmt : IDENT LBRACKET expr RBRACKET ASSIGN expr SEMI",
"print_stmt : PRINT expr SEMI",
"return_stmt : RETURN expr SEMI",
"if_stmt : IF LPAREN expr RPAREN stmt ELSE stmt",
"while_stmt : WHILE LPAREN expr RPAREN stmt",
"$$2 :",
"compound_stmt : BEGIN local_decls $$2 stmt_list END",
"args : arg_list",
"args :",
"arg_list : arg_list COMMA expr",
"arg_list : expr",
"expr : expr ADD expr",
"expr : expr SUB expr",
"expr : expr MUL expr",
"expr : expr DIV expr",
"expr : expr MOD expr",
"expr : expr EQ expr",
"expr : expr NE expr",
"expr : expr LE expr",
"expr : expr LT expr",
"expr : expr GE expr",
"expr : expr GT expr",
"expr : expr AND expr",
"expr : expr OR expr",
"expr : NOT expr",
"expr : LPAREN expr RPAREN",
"expr : IDENT",
"expr : NUM_LIT",
"expr : BOOL_LIT",
"expr : IDENT LPAREN args RPAREN",
"expr : NEW prim_type LBRACKET expr RBRACKET",
"expr : IDENT LBRACKET expr RBRACKET",
"expr : IDENT DOT SIZE",
};

//#line 158 "Parser.y"
    private Lexer lexer;
    private Token last_token;

    private int yylex () {
        int yyl_return = -1;
        try {
            yylval = new ParserVal(0);
            yyl_return = lexer.yylex();
            last_token = (Token)yylval.obj;
        }
        catch (IOException e) {
            System.out.println("IO error :"+e);
        }
        return yyl_return;
    }


    public void yyerror (String error) {
        System.out.println ("Error message for " + lexer.lineno+":"+lexer.column +" by Parser.yyerror(): " + error);
        int last_token_lineno = lexer.lineno;
        int last_token_column = lexer.column;
        System.out.println ("Error message by Parser.yyerror() at near " + last_token_lineno+":"+last_token_column + ": " + error);
    }

    public int getLineno() {
        return lexer.lineno;
    }

    public int getColumn() {
        return lexer.column;
    }

    public Parser(Reader r, boolean yydebug) {
        this.lexer   = new Lexer(r, this);
        this.yydebug = yydebug;
    }
//#line 431 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws Exception
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 52 "Parser.y"
{ Debug("program -> decl_list"                  ); yyval.obj = program____decllist(val_peek(0).obj); }
break;
case 2:
//#line 55 "Parser.y"
{ Debug("decl_list -> decl_list decl"           ); yyval.obj = decllist____decllist_decl(val_peek(1).obj,val_peek(0).obj); }
break;
case 3:
//#line 56 "Parser.y"
{ Debug("decl_list -> eps"                      ); yyval.obj = decllist____eps          (     ); }
break;
case 4:
//#line 59 "Parser.y"
{ Debug("decl -> func_decl"                     ); yyval.obj = decl____funcdecl(val_peek(0).obj); }
break;
case 5:
//#line 63 "Parser.y"
{ Debug("func_decl -> type_spec ID(params) begin local_decls"              ); yyval.obj = fundecl____typespec_ID_LPAREN_params_RPAREN_BEGIN_localdecls_8X_stmtlist_END(val_peek(6).obj,val_peek(5).obj,val_peek(4).obj,val_peek(3).obj,val_peek(2).obj,val_peek(1).obj,val_peek(0).obj          ); }
break;
case 6:
//#line 64 "Parser.y"
{ Debug("                                                    stmt_list end"); yyval.obj =      fundecl____typespec_ID_LPAREN_params_RPAREN_BEGIN_localdecls_X8_stmtlist_END(val_peek(9).obj,val_peek(8).obj,val_peek(7).obj,val_peek(6).obj,val_peek(5).obj,val_peek(4).obj,val_peek(3).obj,val_peek(2).obj,val_peek(1).obj,val_peek(0).obj); }
break;
case 7:
//#line 67 "Parser.y"
{ Debug("params -> param_list");                   yyval.obj = params____paramlist(val_peek(0).obj); }
break;
case 8:
//#line 68 "Parser.y"
{ Debug("params -> eps"                         ); yyval.obj = params____eps(); }
break;
case 9:
//#line 71 "Parser.y"
{ Debug("param_list -> param_list COMMA param");   yyval.obj = paramlist____paramlist_COMMA_param(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 10:
//#line 72 "Parser.y"
{                    yyval.obj = paramlist____param(val_peek(0).obj); }
break;
case 11:
//#line 75 "Parser.y"
{ Debug("param -> type_spec IDENT"               ); yyval.obj = param____typespec_IDENT(val_peek(1).obj,val_peek(0).obj); }
break;
case 12:
//#line 78 "Parser.y"
{ Debug("type_spec -> prim_type"                ); yyval.obj = typespec____primtype(val_peek(0).obj); }
break;
case 13:
//#line 79 "Parser.y"
{ Debug("type_spec -> prim_type LBRACKET RBRACKET"); yyval.obj = typespec____primtype_LBRACKET_RBRACKET(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 14:
//#line 82 "Parser.y"
{ Debug("prim_type -> num"                      ); yyval.obj = primtype____NUM(val_peek(0).obj); }
break;
case 15:
//#line 83 "Parser.y"
{ Debug("prim_type -> bool");                      yyval.obj = primtype____BOOL(val_peek(0).obj); }
break;
case 16:
//#line 86 "Parser.y"
{ Debug("local_decls -> local_decls local_decl");  yyval.obj = localdecls____localdecls_localdecl(val_peek(1).obj, val_peek(0).obj); }
break;
case 17:
//#line 87 "Parser.y"
{ Debug("local_decls -> eps");                     yyval.obj = localdecls____eps(); }
break;
case 18:
//#line 90 "Parser.y"
{ Debug("local_decl -> type_spec IDENT ;"       ); yyval.obj = localdecl____typespec_IDENT_SEMI(val_peek(2).obj,val_peek(1).obj,val_peek(0).obj); }
break;
case 19:
//#line 93 "Parser.y"
{ Debug("stmt_list -> stmt_list stmt"           ); yyval.obj = stmtlist____stmtlist_stmt(val_peek(1).obj,val_peek(0).obj); }
break;
case 20:
//#line 94 "Parser.y"
{ Debug("stmt_list -> eps"                      ); yyval.obj = stmtlist____eps          (     ); }
break;
case 21:
//#line 97 "Parser.y"
{ Debug("stmt -> assign_stmt"                   ); yyval.obj = stmt____assignstmt  (val_peek(0).obj); }
break;
case 22:
//#line 98 "Parser.y"
{ Debug("stmt -> print_stmt");                     yyval.obj = stmt____printstmt(val_peek(0).obj); }
break;
case 23:
//#line 99 "Parser.y"
{ Debug("stmt -> return_stmt"                   ); yyval.obj = stmt____returnstmt  (val_peek(0).obj); }
break;
case 24:
//#line 100 "Parser.y"
{ Debug("stmt -> if_stmt");                        yyval.obj = stmt____ifstmt(val_peek(0).obj); }
break;
case 25:
//#line 101 "Parser.y"
{ Debug("stmt -> while_stmt");                     yyval.obj = stmt____whilestmt(val_peek(0).obj); }
break;
case 26:
//#line 102 "Parser.y"
{ Debug("stmt -> compound_stmt");                  yyval.obj = stmt____compoundstmt(val_peek(0).obj); }
break;
case 27:
//#line 105 "Parser.y"
{ Debug("assign_stmt -> IDENT := expr ;"        ); yyval.obj = assignstmt____IDENT_ASSIGN_expr_SEMI(val_peek(3).obj,val_peek(2).obj,val_peek(1).obj,val_peek(0).obj); }
break;
case 28:
//#line 106 "Parser.y"
{ Debug("assign_stmt -> IDENT LBRACKET expr RBRACKET ASSIGN expr SEMI"); yyval.obj = assignstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_expr_SEMI(val_peek(6).obj, val_peek(5).obj, val_peek(4).obj, val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 29:
//#line 109 "Parser.y"
{ Debug("print_stmt -> PRINT expr SEMI");          yyval.obj = printstmt____PRINT_expr_SEMI(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 30:
//#line 112 "Parser.y"
{ Debug("return_stmt -> return expr ;"          ); yyval.obj = returnstmt____RETURN_expr_SEMI(val_peek(2).obj,val_peek(1).obj,val_peek(0).obj); }
break;
case 31:
//#line 115 "Parser.y"
{ Debug("if_stmt -> IF LPAREN expr RPAREN stmt ELSE stmt"); yyval.obj = ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(val_peek(6).obj, val_peek(5).obj, val_peek(4).obj, val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 32:
//#line 118 "Parser.y"
{ Debug("while_stmt -> WHILE LPAREN expr RPAREN stmt");     yyval.obj = whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(val_peek(4).obj, val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 33:
//#line 121 "Parser.y"
{ Debug("compound_stmt -> BEGIN local_decls");              yyval.obj = compoundstmt____BEGIN_localdecls(val_peek(1).obj, val_peek(0).obj); }
break;
case 34:
//#line 122 "Parser.y"
{ Debug("stmt_list end");                               yyval.obj = compoundstmt____BEGIN_localdecls_stmtlist_END(val_peek(4).obj, val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 35:
//#line 125 "Parser.y"
{ Debug("args -> arg_list");                       yyval.obj = args____arglist(val_peek(0).obj); }
break;
case 36:
//#line 126 "Parser.y"
{ Debug("args -> eps"                           ); yyval.obj = args____eps(); }
break;
case 37:
//#line 129 "Parser.y"
{ Debug("arg_list -> arg_list COMMA expr");        yyval.obj = arglist____arglist_COMMA_expr(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 38:
//#line 130 "Parser.y"
{ Debug("arg_list -> expr");                       yyval.obj = arglist____expr(val_peek(0).obj); }
break;
case 39:
//#line 133 "Parser.y"
{ Debug("expr -> expr ADD expr"                 ); yyval.obj = expr____expr_ADD_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 40:
//#line 134 "Parser.y"
{ Debug("expr -> expr SUB expr"                 ); yyval.obj = expr____expr_SUB_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 41:
//#line 135 "Parser.y"
{ Debug("expr -> expr MUL expr"                 ); yyval.obj = expr____expr_MUL_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 42:
//#line 136 "Parser.y"
{ Debug("expr -> expr DIV expr"                 ); yyval.obj = expr____expr_DIV_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 43:
//#line 137 "Parser.y"
{ Debug("expr -> expr MOD expr"                 ); yyval.obj = expr____expr_MOD_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 44:
//#line 138 "Parser.y"
{ Debug("expr -> expr EQ expr"                  ); yyval.obj = expr____expr_EQ_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 45:
//#line 139 "Parser.y"
{ Debug("expr -> expr NE expr"                  ); yyval.obj = expr____expr_NE_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 46:
//#line 140 "Parser.y"
{ Debug("expr -> expr LE expr"                  ); yyval.obj = expr____expr_LE_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 47:
//#line 141 "Parser.y"
{ Debug("expr -> expr LT expr"                  ); yyval.obj = expr____expr_LT_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 48:
//#line 142 "Parser.y"
{ Debug("expr -> expr GE expr"                  ); yyval.obj = expr____expr_GE_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 49:
//#line 143 "Parser.y"
{ Debug("expr -> expr GT expr"                  ); yyval.obj = expr____expr_GT_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 50:
//#line 144 "Parser.y"
{ Debug("expr -> expr AND expr"                 ); yyval.obj = expr____expr_AND_expr           (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 51:
//#line 145 "Parser.y"
{ Debug("expr -> expr OR expr"                  ); yyval.obj = expr____expr_OR_expr            (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 52:
//#line 146 "Parser.y"
{ Debug("expr -> NOT expr"                      ); yyval.obj = expr____NOT_expr                (val_peek(1).obj,val_peek(0).obj      ); }
break;
case 53:
//#line 147 "Parser.y"
{ Debug("expr -> LPAREN expr RPAREN"            ); yyval.obj = expr____LPAREN_expr_RPAREN      (val_peek(2).obj,val_peek(1).obj,val_peek(0).obj   ); }
break;
case 54:
//#line 148 "Parser.y"
{ Debug("expr -> IDENT"                         ); yyval.obj = expr____IDENT                   (val_peek(0).obj         ); }
break;
case 55:
//#line 149 "Parser.y"
{ Debug("expr -> NUM_LIT"                       ); yyval.obj = expr____NUMLIT                  (val_peek(0).obj         ); }
break;
case 56:
//#line 150 "Parser.y"
{ Debug("expr -> BOOL_LIT"                      ); yyval.obj = expr____BOOLLIT                 (val_peek(0).obj         ); }
break;
case 57:
//#line 151 "Parser.y"
{ Debug("expr -> IDENT LPAREN args RPAREN"      ); yyval.obj = expr____IDENT_LPAREN_args_RPAREN(val_peek(3).obj,val_peek(2).obj,val_peek(1).obj,val_peek(0).obj); }
break;
case 58:
//#line 152 "Parser.y"
{ Debug("expr -> NEW prim_type LBRACKET expr RBRACKET"); yyval.obj = expr____NEW_primtype_LBRACKET_expr_RBRACKET(val_peek(4).obj, val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 59:
//#line 153 "Parser.y"
{ Debug("expr -> IDENT LBRACKET expr RBRACKET"  ); yyval.obj = expr____IDENT_LBRACKET_expr_RBRACKET(val_peek(3).obj, val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
case 60:
//#line 154 "Parser.y"
{ Debug("expr -> IDENT DOT SIZE"                ); yyval.obj = expr____IDENT_DOT_SIZE(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj); }
break;
//#line 820 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
