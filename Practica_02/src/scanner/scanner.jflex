// ************  C�digo a incluir ********************

package scanner;
import parser.Parser;

%%
// ************  Opciones ********************
// % debug // * Opci�n para depurar
%byaccj
%class Scanner
%public
%unicode
%line
%column

%{
// ************  Atributos y m�todos ********************

// * Para acceder al n�mero de l�nea (yyline es package)
public int getLine() { 
	// * Flex empieza en cero
	return yyline+1;
}

// * Para acceder al n�mero de columna (yycolumn es package)
public int getColumn() { 
	// * Flex empieza en cero
	return yycolumn+1;
}

// * Valor semantico del token
private Object yylval;
public Object getYylval() {
	return this.yylval;
}

%}

// ************  Patrones (macros) ********************
ConstanteEntera = [0-9]*
Rubbish = [ \t\n\r]
Comment = #~


%%
// ************  Acciones ********************

// * Constante Entera

{Rubbish}			{}  
{Comment}			{} 

{ConstanteEntera}	{ this.yylval = new Integer(yytext());
         			  return Parser.INT_CONSTANT;  }
  			  
         		
		  
// * Cualquier otro car�cter
.			{ System.err.println ("Lexical error at line " 	+ this.getLine() + " and column "+getColumn()+":\n\tUnknow character \'"+ yycharat(0)+"\'."); }		
				
			
			