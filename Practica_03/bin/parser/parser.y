%{
// * Declaraciones de c�digo Java
// * Se sit�an al comienzo del archivo generado
// * El package lo a�ade yacc si utilizamos la opci�n -Jpackage
import scanner.Scanner;
import java.io.Reader;
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

%left '+'
%left '*'

%%
// * Gram�tica y acciones Yacc


programa : prog 
		 | prog programa;

prog: variables 
	 | funcion
	 ;

// *********  FUNCIONES  *********

funcion: DEF ID '(' params ')' ':' retorno '{' definiciones '}';

retorno:  tipo 
		|  VOID
		;

params: param ',' params;
		| param
		|
		;

param: ID ':' tipo;



// *********  DEFINICIONES  *********

definiciones: definicion
			| definicion definiciones
			;
			
definicion: variables
			| array 
			; 
			

//  *********  VARIABLES  *********
		
variables :  variable ':' tipo ';';
	
variable: ID 
		| ID ',' variable;
			
   

				   
//  *********  ARRAYS  *********

array:  ID ':' dimension tipo ';'

dimension: dim 
		| dim dimension ;

dim: '['INT_CONSTANT']'	;	   
				   
tipo: INT 
	| DOUBLE 
	| CHAR
	;
         
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
