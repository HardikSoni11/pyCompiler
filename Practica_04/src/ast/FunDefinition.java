package ast;

import java.util.List;

public class FunDefinition implements Definition {

	private List<Statement> statements;
	private String name;
	private Type type;

	private int row = ASTNode.DEFAULT_ROW_COLUMN;
	private int column = ASTNode.DEFAULT_ROW_COLUMN;

	public FunDefinition(int i, int j, String name, Type type,List<Statement> statements) {
		super();
		this.row = i;
		this.column = j;
		this.statements = statements;
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return type;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	@Override
	public String toString() {
		return "FunDefinition [statements=" + statements + ", name=" + name + ", type=" + type + "]";
	}

	@Override
	public int getLine() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

}
