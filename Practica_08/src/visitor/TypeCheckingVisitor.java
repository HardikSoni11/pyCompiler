package visitor;

import ast.Arithmetic;
import ast.Assignment;
import ast.Cast;
import ast.CharLiteral;
import ast.Comparison;
import ast.Expression;
import ast.FieldAccess;
import ast.Indexing;
import ast.IntLiteral;
import ast.Invocation;
import ast.Logical;
import ast.Read;
import ast.RealLiteral;
import ast.UnaryMinus;
import ast.UnaryNot;
import ast.Variable;
import tipo.ErrorType;

public class TypeCheckingVisitor extends AbstractVisitor {

	@Override
	public Object visit(Variable v, Object object) {
		v.setLValue(true);
		return null;
	}

	@Override
	public Object visit(Assignment a, Object o) {
		a.getLeft().accept(this, o);
		a.getRight().accept(this, o);
		if (!a.getLeft().getLValue()) {
			new ErrorType(a.getLeft(), "ERROR: Se esperaba un Lvalue en: " + a.getLeft());
		}
		return null;
	}

	@Override
	public Object visit(Arithmetic a, Object object) {
		a.getLeft().accept(this, object);
		a.getRight().accept(this, object);
		a.setLValue(false);
		return null;
	}

	@Override
	public Object visit(Cast cast, Object o) {
		cast.getExp().accept(this, o);
		cast.getCastType().accept(this, o);
		cast.setLValue(false);
		return null;
	}

	@Override
	public Object visit(Comparison comparison, Object o) {
		comparison.getLeft().accept(this, o);
		comparison.getRight().accept(this, o);
		comparison.setLValue(false);
		return null;
	}

	@Override
	public Object visit(CharLiteral charLiteral, Object o) {
		charLiteral.setLValue(false);
		return null;
	}

	@Override
	public Object visit(Read read, Object o) {
		read.getExpression().accept(this, o);
		if (!read.getExpression().getLValue()) {
			new ErrorType(read.getExpression(), "ERROR: Se esperaba un Lvalue en: " + read.getExpression());
		}
		return null;
	}

	@Override
	public Object visit(FieldAccess fieldAccess, Object o) {
		fieldAccess.getExp().accept(this, o);
		if (fieldAccess.getExp().getLValue()) {
			fieldAccess.setLValue(true);
		}
		return null;
	}

	@Override
	public Object visit(Indexing indexing, Object o) {
		indexing.getRight().accept(this, o);
		indexing.getLeft().accept(this, o);
		if (!indexing.getLeft().getLValue()) {
			new ErrorType(indexing, "Se esperaba un Lvalue en: " + indexing.getLeft());
		} else {
			indexing.setLValue(true);
		}

		return true;
	}

	@Override
	public Object visit(IntLiteral intLiteral, Object o) {
		intLiteral.setLValue(false);
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
		invocation.setLValue(false);
		return null;
	}

	@Override
	public Object visit(Logical logical, Object o) {
		logical.getLeft().accept(this, o);
		logical.getRight().accept(this, o);
		logical.setLValue(false);
		return null;
	}

	@Override
	public Object visit(UnaryNot negation, Object o) {
		negation.getOperand().accept(this, o);
		negation.setLValue(false);
		return null;
	}

	@Override
	public Object visit(RealLiteral realLiteral, Object o) {
		realLiteral.setLValue(false);
		return null;
	}

	@Override
	public Object visit(UnaryMinus unaryMinus, Object o) {
		unaryMinus.getOperand().accept(this, o);
		unaryMinus.setLValue(false);
		return null;
	}

}
