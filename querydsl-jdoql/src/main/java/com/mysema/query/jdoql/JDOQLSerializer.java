/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.List;

import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.alias.AToPath;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.Op;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * 
 * @author tiwe
 *
 */
public class JDOQLSerializer extends BaseSerializer<JDOQLSerializer> {

	private PEntity<?> candidatePath;
	
	public JDOQLSerializer(JDOQLOps ops, PEntity<?> candidate) {
		super(ops);
		this.candidatePath = candidate;
	}
	
	@Override
	protected void visit(ASimple<?> expr) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void visit(AToPath expr) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void visit(EConstant<?> expr) {
		boolean wrap = expr.getConstant().getClass().isArray();
		if (wrap){
			append("(");
		}			
		append("a");
		if (!constants.contains(expr.getConstant())) {
			constants.add(expr.getConstant());
			append(Integer.toString(constants.size()));
		} else {
			append(Integer.toString(constants.indexOf(expr.getConstant()) + 1));
		}
		if (wrap){
			append(")");
		}			
	}

	// FIXME
	private void visitCast(Op<?> operator, Expr<?> source, Class<?> targetType) {
		append("CAST(").handle(source);
		append(" AS ");
		append(targetType.getSimpleName().toLowerCase()).append(")");

	}

	@Override
	protected void visitOperation(Class<?> type, Op<?> operator, List<Expr<?>> args) {
		if (operator.equals(Ops.ISTYPEOF)){
			handle(args.get(0)).append(" instanceof ");
			append(((EConstant<Class<?>>)args.get(1)).getConstant().getName());
		}else if (operator.equals(Ops.STRING_CAST)) {
			visitCast(operator, args.get(0), String.class);
		} else if (operator.equals(Ops.NUMCAST)) {
			visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args.get(1)).getConstant());
		} else {
			super.visitOperation(type, operator, args);
		}
	}
	
	@Override
	protected void visit(Path<?> path) {
		if (path.equals(candidatePath)){
			append("this");
		}else{
			super.visit(path);
		}
	}

}
