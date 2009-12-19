package com.mysema.query.collections.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 */
@Immutable
public class EvaluatorFactory {

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorFactory.class);
    
    public static final EvaluatorFactory DEFAULT = new EvaluatorFactory(ColQueryTemplates.DEFAULT);
    
    private final ColQueryTemplates templates;
    
    protected EvaluatorFactory(ColQueryTemplates templates){
        this.templates = templates;
    }
    
    public <T> Evaluator<T> create(List<? extends Expr<?>> sources, final Expr<T> projection) {
        ColQuerySerializer serializer = new ColQuerySerializer(templates);
        serializer.handle(projection);
        Map<Object,String> constantToLabel = serializer.getConstantToLabel();
        final String javaSource = serializer.toString();
        final Object[] constArray = constantToLabel.keySet().toArray();        
        final Class<?>[] types = new Class<?>[constArray.length + sources.size()];
        final String[] names = new String[constArray.length + sources.size()];
        for (int i = 0; i < constArray.length; i++) {
            if (List.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = List.class;
            }else if (Set.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = Set.class;
            }else if (Collection.class.isAssignableFrom(constArray[i].getClass())){
                types[i] = Collection.class;
            }else{
                types[i] = constArray[i].getClass();    
            }            
            names[i] = constantToLabel.get(constArray[i]);
        }

        int off = constArray.length;
        for (int i = 0; i < sources.size(); i++) {
            types[off + i] = sources.get(i).getType();
            names[off + i] = sources.get(i).toString();
        }

        if (logger.isInfoEnabled()) {
            logger.info(javaSource + " " + Arrays.asList(names) + " " + Arrays.asList(types));
        }

        try {
            // REPLACE START
            final ExpressionEvaluator evaluator = new ExpressionEvaluator(javaSource, projection.getType(), names, types);
            // REPLACE END
            
            return new Evaluator<T>(){
                @SuppressWarnings("unchecked")
                @Override
                public T evaluate(Object... args) {
                    try {
                        args = combine(constArray.length + args.length, constArray, args);
                        return (T) evaluator.evaluate(args);
                    } catch (InvocationTargetException e) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Caught exception when evaluating '").append(javaSource);
                        builder.append("' with arguments ");
                        for (int i = 0; i < args.length; i++){
                            builder.append(names[i]).append(" = ").append(args[i]);
                            if (i < args.length -1){
                                builder.append(", ");
                            }
                        }
                        throw new RuntimeException(builder.toString(), e);
                    }
                }
                
            };
            
        } catch (CompileException e) {
            throw new RuntimeException(e.getMessage() + " with source " + javaSource, e);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage() + " with source " + javaSource, e);
        } catch (ScanException e) {
            throw new RuntimeException(e.getMessage() + " with source " + javaSource, e);
        }
    }
    
    private static Object[] combine(int size, Object[]... arrays) {
        int offset = 0;
        Object[] target = new Object[size];
        for (Object[] arr : arrays) {
            System.arraycopy(arr, 0, target, offset, arr.length);
            offset += arr.length;
        }
        return target;
    }

    
}
