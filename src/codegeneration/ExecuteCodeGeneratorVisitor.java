package codegeneration;

import ast.Assignment;
import ast.Definition;
import ast.Expression;
import ast.FunDefinition;
import ast.IfStatement;
import ast.Invocation;
import ast.Program;
import ast.Read;
import ast.Return;
import ast.Statement;
import ast.VarDefinition;
import ast.WhileStatement;
import ast.Write;
import tipo.FunctionType;
import tipo.Type;
import tipo.VoidType;

public class ExecuteCodeGeneratorVisitor extends AbstractCodeGeneratorVisitor {
	ValueCodeGeneratorVisitor valueCgVisitor;
	AdressCodeGeneratorVisitor adressCgVisitor;

	public ExecuteCodeGeneratorVisitor(String entrada, String salida) {
		super(new CodeGenerator(entrada, salida));
		adressCgVisitor = new AdressCodeGeneratorVisitor(this.cg);
		valueCgVisitor = new ValueCodeGeneratorVisitor(this.cg, adressCgVisitor);
		adressCgVisitor.setValueVisitor(valueCgVisitor);
	}

	@Override
	public Object visit(Program program, Object o) {
		for (Definition def : program.getDefinitions()) {
			if (def instanceof VarDefinition) {
				def.accept(this, o);
			}
		}

		cg.call("main");
		cg.halt();

		for (Definition def : program.getDefinitions()) {
			if (def instanceof FunDefinition) {
				def.accept(this, o);
			}
		}

		return null;
	}

	@Override
	public Object visit(FunDefinition funDefinition, Object o) {

		cg.etiqueta(funDefinition.getName());

		int locals = 0;
		for (Statement d : funDefinition.getStatements()) {
			if (d instanceof VarDefinition) {
				locals += ((VarDefinition) d).getType().numberOfBytes();
			}
		}

		cg.enter(locals);

		for (Statement d : funDefinition.getStatements()) {
			if (!(d instanceof VarDefinition)) {
				d.accept(this, o);
			}
		}

		int params = 0;
		for (VarDefinition d : ((FunctionType) funDefinition.getType()).getParameters()) {
			params += d.getType().numberOfBytes();
		}

		Type ret = ((FunctionType) funDefinition.getType()).getReturnType();
		if (ret == VoidType.getInstance()) {
			cg.ret(0, locals, params);
		} else {
			cg.ret(ret.numberOfBytes(), locals, params);
		}

		return null;

	}

	@Override
	public Object visit(Write write, Object o) {

		write.getExpresion().accept(valueCgVisitor, o);
		cg.out(write.getExpresion().getType());

		return null;
	}

	@Override
	public Object visit(Read read, Object o) {

		read.getExpression().accept(adressCgVisitor, o);
		cg.in(read.getExpression().getType());
		cg.store(read.getExpression().getType());

		return null;
	}

	@Override
	public Object visit(Assignment assignment, Object o) {

		assignment.getLeft().accept(adressCgVisitor, o);
		assignment.getRight().accept(valueCgVisitor, o);
		// CONVERSI�N IMPL�CITA RELLENAR
		cg.store(assignment.getLeft().getType());

		return null;
	}

	@Override
	public Object visit(IfStatement ifStatement, Object o) {
		int label = cg.getLabels(2);
		ifStatement.getCondition().accept(valueCgVisitor, o);
		cg.jz(label);
		for (Statement s : ifStatement.getIfBody()) {
			s.accept(this, o);
		}
		cg.jmp(label + 1);
		cg.etiqueta(label);
		for (Statement s : ifStatement.getElseBody()) {
			s.accept(this, o);
		}
		cg.etiqueta(label + 1);

		return null;
	}

	@Override
	public Object visit(WhileStatement whileStatement, Object o) {
		int label = cg.getLabels(2);
		cg.etiqueta(label);
		whileStatement.getCondition().accept(valueCgVisitor, o);
		cg.jz(label + 1);
		for (Statement s : whileStatement.getBody()) {
			s.accept(this, o);
		}
		cg.jmp(label);
		cg.etiqueta(label + 1);

		return null;
	}

	@Override
	public Object visit(Invocation invocation, Object o) {

		for (Expression s : invocation.getArguments()) {
			s.accept(valueCgVisitor, o);
		}
		cg.call(invocation.getFuncion().getNameString());
		if (((FunctionType) invocation.getFuncion().getType()).getReturnType() != VoidType.getInstance()) {
			cg.pop(((FunctionType) invocation.getFuncion().getType()).getReturnType().suffix());
		}

		return null;

	}

	@Override
	public Object visit(Return return1, Object o) {
		return1.getExpression().accept(valueCgVisitor, o);

		FunDefinition f = (FunDefinition) o;
		// cg.convert(return1.getExpression().getType(), ((FunctionType)
		// f.getType()).getReturnType().suffix());
		cg.ret(((FunctionType) f.getType()).getReturnType().numberOfBytes(), f.localBytes(), f.paramBytes());
		return null;
	}

}
