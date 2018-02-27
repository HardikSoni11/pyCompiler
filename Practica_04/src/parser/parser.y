%{
// * Declaraciones de c�digo Java
// * Se sit�an al comienzo del archivo generado
// * El package lo a�ade yacc si utilizamos la opci�n -Jpackage
import scanner.Scanner;
import java.io.Reader;

import ast.*;
import java.util.*;

%}

// * Declaraciones Yacc
%token INT_CONSTANT
%token INPUT
%token PRINT
%token DEF
%token WHILE
%token IF
%token ELSE
%token INT
%token DOUBLE
%token CHAR
%token STRUCT
%token RETURN
%token VOID
%token ID
%token REAL_CONSTANT
%token CHAR_CONSTANT
%token GREATER
%token SMALLER
%token EQUALS
%token NEGATION
%token MAIN
%token OR
%token AND


%right '='
%left OR AND

%left EQUALS NEGATION SMALLER '<' GREATER '>'

%left '-' '+'
%left '*' '/' '%'

%nonassoc CAST
%right UNARIO
%nonassoc '!'
%left '.'
%nonassoc '[' ']'
%nonassoc '(' ')'

%nonassoc CUERPO
%nonassoc ELSE



%%
// * Gram�tica y acciones Yacc

programa : definiciones DEF MAIN '(' ')'':'VOID '{' body '}';		{ ast = new Program(0,0,(List<Definition>) $1);}

definiciones: definiciones definicion 								
	 | /* empty */
	 ;


definicion: def ';'													
			| funcion										
			;

// *********  FUNCIONES  *********

funcion: DEF ID '(' params ')' ':' retorno '{' body '}'; //	************** NIPU	{ $$ = new FunDefinition(scanner.getLine(),scanner.getColumn(), (String) $1,(Type) $7);}

retorno: tipo | VOID ; 											{ $$ = VoidType.getInstance();}



body: defs
	| sentencias
	| defs sentencias
	|
	;


params:  /* empty */											 //HAY QUE DEVOLVER ALGO?
		| param													{$$ = $1;}
		;

param: par														{ List<VarDefinition> par = new ArrayList<VarDefinition>();par.add((VarDefinition)$1);$$=par;}
	| param ',' par												{ List<VarDefinition> pars = (List<VarDefinition>) $1;pars.add((VarDefinition)$3);$$=pars;}

par:  ID ':' tipo;												{ $$ = new VarDefinition(scanner.getLine(),scanner.getColumn(),(String) $1, (Type) $3);}


// *********  DEFINICIONES  *********

defs: def ';'													{ $$ = $1;}
	| defs def ';'												{ List<Definition> defs = (List<Definition>)$1; List<VarDefinition> def = (List<VarDefinition>) $2; for(VarDefinition var:def){defs.add(var);}$$=defs;}
	;
				
	
def: ids ':' tipo												{ List<String> ids = (List<String>) $1; List<VarDefinition> def = new ArrayList<VarDefinition>();for(String id:ids){def.add(new VarDefinition(scanner.getLine(),scanner.getColumn(),id, (Type) $3));}$$=def;}


ids: ID															{ List<String> ids = new ArrayList<String>(); ids.add((String) $1); $$=ids;}
	| ids ',' ID												{ List<String> ids = (List<String>) $1; ids.add((String) $3); $$=ids;}
	;
								   
tipo: INT 														{ $$ = IntType.getInstance();}
	| DOUBLE 													{ $$ = RealType.getInstance();}
	| CHAR														{ $$ = CharType.getInstance();}
	|'['INT_CONSTANT']' tipo									{ $$ = new ArrayType(scanner.getLine(),scanner.getColumn(),(int) $2, (Type) $4);} 
	| STRUCT '{' campos '}'										{ $$ = new RecordType(scanner.getLine(),scanner.getColumn(),(List<RecordField>)$3);}
	;


campos: campo
		|campos campo 
		;
		
campo:ids ':' tipo ';';	

// *********  SENTENCIAS  *********

sentencias: sentencia
		| sentencias sentencia
		;


sentencia: PRINT list ';'										
		| INPUT list ';'	
		| RETURN expresion ';'									{ $$ = new Return(scanner.getLine(),scanner.getColumn(),(Expression) $2);}
		| condicionalSimple
		| condicionalComplejo
		| while
		| asignacion ';'
		| invocacion ';'
		;
	

expresion: ID 
		| INT_CONSTANT
		| CHAR_CONSTANT
		| REAL_CONSTANT
		| '(' expresion ')'
		| expresion '[' expresion ']'
		|  expresion '.' ID
		| '(' tipo ')' expresion  %prec CAST
		| '-' expresion %prec UNARIO
		| '!' expresion
		|  expresion '*' expresion	 							{ $$ = new Arithmetic(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		|  expresion '/' expresion	 							{ $$ = new Arithmetic(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		|  expresion '%' expresion	 							{ $$ = new Arithmetic(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		|  expresion '+' expresion	 							{ $$ = new Arithmetic(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		|  expresion '-' expresion	 							{ $$ = new Arithmetic(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		| expresion '>' expresion	 							{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion GREATER expresion 							{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion '<' expresion								{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion SMALLER expresion							{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion NEGATION expresion							{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion EQUALS expresion							{ $$ = new Comparison(scanner.getLine(),scanner.getColumn(),(Expression) $1,(String) $2,(Expression)$3);}
		| expresion AND expresion								{ $$ = new Logical(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		| expresion OR expresion								{ $$ = new Logical(scanner.getLine(),scanner.getColumn(),(Arithmetic) $1,(String)$2,(Arithmetic)$3);}
		| ID '(' args ')'
		;
		
		
list: expresion													{ List<Expression> exp = new ArrayList<Expression>();exp.add((Expression)$1);$$=exp;}
	| list ',' expresion										{ List<Expression> exps = (List<Expression>) $1;exps.add((Expression)$1);$$=exps;}
	;
	
asignacion: expresion '=' expresion ;

invocacion: ID '(' args ')'


// *********  WHILE  *********

while: WHILE expresion ':' '{' sentencias '}' ;					{ $$ = new WhileStatement(scanner.getLine(),scanner.getColumn(),(List<Statement>) $5,(Expression) $2);}

// *********  IF-ELSE  *********


condicionalSimple: IF expresion ':' cuerpo %prec CUERPO; 		{ $$ = new IfStatement(scanner.getLine(),scanner.getColumn(),(List<Statement>) $4,null,(Expression) $2);}
condicionalComplejo: IF expresion ':' cuerpo else;				{ $$ = new IfStatement(scanner.getLine(),scanner.getColumn(),(List<Statement>) $4,(List<Statement>) $5,(Expression) $2);}

else: ELSE cuerpo ;												{ $$=$2;}

cuerpo: sentencia												{ $$=$1;}
		| '{' sentencias '}'   									{ $$=$2;}
		;
		
				
// *********  INVOCACI�N DE FUNCIONES  *********

args:  /* empty */
		| arg													{$$=$1;}
		;

arg: expresion													{ List<Expression> exp = new ArrayList<Expression>();exp.add((Expression)$1);$$=exp;}
	| arg ',' expresion											{ List<Expression> exps = (List<Expression>) $1;exps.add((Expression)$1);$$=exps;}


		         
%%
// * C�digo Java
// * Se crea una clase "Parser", lo que aqu� ubiquemos ser�:
//	- Atributos, si son variables
//	- M�todos, si son funciones
//   de la clase "Parser"

// * Estamos obligados a implementar:
//	int yylex()
//	void yyerror(String)

// * Referencia al analizador l�xico
private Scanner scanner;


private ASTNode ast;

public ASTNode getAST(){
	return ast;
}


// * Llamada al analizador l�xico
private int yylex () {
    int token=0;
    try { 
		token=scanner.yylex(); 	
		this.yylval = scanner.getYylval();
    } catch(Throwable e) {
	    System.err.println ("Lexical error at line " + scanner.getLine() + " and column "+scanner.getColumn()+":\n\t"+e); 
    }
    return token;
}

// * Manejo de Errores Sint�cticos
public void yyerror (String error) {
    System.err.println ("Syntactical error at line " + scanner.getLine() + " and column "+scanner.getColumn()+":\n\t"+error);
}

// * Constructor del Sint�ctico
public Parser(Scanner scanner) {
	this.scanner = scanner;
}
