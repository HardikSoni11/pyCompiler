package visitor;

import ast.Arithmetic;
import ast.Assignment;
import ast.Cast;
import ast.CharLiteral;
import ast.Comparison;
import ast.Definition;
import ast.Expression;
import ast.FieldAccess;
import ast.FunDefinition;
import ast.IfStatement;
import ast.AlterAssigVal;
import ast.AlterVal;
import ast.Indexing;
import ast.IntLiteral;
import ast.Invocation;
import ast.Logical;
import ast.Program;
import ast.Read;
import ast.RealLiteral;
import ast.RecordField;
import ast.Return;
import ast.Statement;
import ast.UnaryMinus;
import ast.UnaryNot;
import ast.VarDefinition;
import ast.Variable;
import ast.WhileStatement;
import ast.Write;
import tipo.ArrayType;
import tipo.CharType;
import tipo.ErrorType;
import tipo.FunctionType;
import tipo.IntType;
import tipo.RealType;
import tipo.RecordType;
import tipo.VoidType;

public class AbstractVisitor implements Visitor {

	@Override
	public Object visit(Variable v, Object object) {
		return null;
	}

	@Override
	public Object visit(Arithmetic a, Object object) {
		a.getLeft().accept(this, object);
		a.getRight().accept(this, object);
		return null;
	}

	@Override
	public Object visit(Assignment a, Object o) {
		a.getLeft().accept(this, o);
		a.getRight().accept(this, o);
		return null;
	}

	@Override
	public Object visit(Cast cast, Object o) {
		cast.getExp().accept(this, o);
		cast.getCastType().accept(this, o);
		return null;
	}

	@Override
	public Object visit(CharLiteral charLiteral, Object o) {
		return null;
	}

	@Override
	public Object visit(Comparison comparison, Object o) {
		comparison.getLeft().accept(this, o);
		comparison.getRight().accept(this, o);
		return null;
	}

	@Override
	public Object visit(FieldAccess fieldAccess, Object o) {
		fieldAccess.getExp().accept(this, o);
		return null;
	}

	@Override
	public Object visit(FunDefinition funDefinition, Object o) {
		funDefinition.getType().accept(this, o);
		if (funDefinition.getStatements() != null) {
			for (Statement statement : funDefinition.getStatements()) {
				statement.accept(this, o);
			}
		}
		return null;

	}

	@Override
	public Object visit(IfStatement ifStatement, Object o) {
		ifStatement.getCondition().accept(this, o);
		if (ifStatement.getIfBody() != null) {
			for (Statement statement : ifStatement.getIfBody()) {
				statement.accept(this, o);
			}
		}

		if (ifStatement.getElseBody() != null) {
			for (Statement statement : ifStatement.getElseBody()) {
				statement.accept(this, o);
			}
		}
		return null;
	}

	@Override
	public Object visit(Indexing indexing, Object o) {
		indexing.getRight().accept(this, o);
		indexing.getLeft().accept(this, o);
		return true;
	}

	@Override
	public Object visit(IntLiteral intLiteral, Object o) {
		return true;
	}

	@Override
	public Object visit(Invocation invocation, Object o) {
		invocation.getFuncion().accept(this, o);
		if (invocation.getArguments() != null) {
			for (Expression e : invocation.getArguments()) {
				e.accept(this, o);
			}
		}
		return null;
	}

	@Override
	public Object visit(Logical logical, Object o) {
		logical.getLeft().accept(this, o);
		logical.getRight().accept(this, o);
		return null;
	}

	@Override
	public Object visit(UnaryNot negation, Object o) {
		negation.getOperand().accept(this, o);
		return null;
	}

	@Override
	public Object visit(Program program, Object o) {
		for (Definition def : program.getDefinitions()) {
			def.accept(this, o);
		}
		return null;
	}

	@Override
	public Object visit(Read read, Object o) {
		read.getExpression().accept(this, o);
		return null;
	}

	@Override
	public Object visit(RealLiteral realLiteral, Object o) {
		return null;
	}

	@Override
	public Object visit(RecordField recordField, Object o) {
		recordField.getType().accept(this, o);
		return null;
	}

	@Override
	public Object visit(RecordType recordType, Object o) {
		for (RecordField r : recordType.getFields()) {
			r.accept(this, o);
		}
		return null;
	}

	@Override
	public Object visit(Return return1, Object o) {
		return1.getExpression().accept(this, o);
		return null;
	}

	@Override
	public Object visit(UnaryMinus unaryMinus, Object o) {
		unaryMinus.getOperand().accept(this, o);
		return null;
	}

	@Override
	public Object visit(VarDefinition varDefinition, Object o) {
		varDefinition.getType().accept(this, o);
		return null;
	}

	@Override
	public Object visit(WhileStatement whileStatement, Object o) {
		whileStatement.getCondition().accept(this, o);
		for (Statement s : whileStatement.getBody()) {
			s.accept(this, o);
		}
		return null;
	}

	@Override
	public Object visit(Write write, Object o) {
		write.getExpresion().accept(this, o);
		return null;
	}

	@Override
	public Object visit(ArrayType arrayType, Object o) {
		arrayType.getOf().accept(this, o);
		return null;
	}

	@Override
	public Object visit(CharType charType, Object o) {
		return null;
	}

	@Override
	public Object visit(ErrorType errorType, Object o) {
		return null;
	}

	@Override
	public Object visit(FunctionType functionType, Object o) {
		functionType.getReturnType().accept(this, o);
		for (Statement s : functionType.getParameters()) {
			s.accept(this, o);
		}
		return null;
	}

	@Override
	public Object visit(IntType intType, Object o) {
		return null;
	}

	@Override
	public Object visit(RealType realType, Object o) {
		return null;
	}

	@Override
	public Object visit(VoidType voidType, Object o) {
		return null;
	}

	@Override
	public Object visit(AlterVal i, Object o) {
		i.getExpr().accept(this, o);
		return null;
	}

	@Override
	public Object visit(AlterAssigVal a, Object o) {
		a.getLeft().accept(this, o);
		a.getRight().accept(this, o);
		return null;
	}

}
