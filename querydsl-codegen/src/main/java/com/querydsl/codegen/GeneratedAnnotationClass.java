package com.querydsl.codegen;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratedAnnotationClass {

    public static final class Context {
        private final String generatorClassName;

        private Context(String generatorClassName) {
            this.generatorClassName = generatorClassName;
        }

        public String replacePlaceholders(String input) {
            String next = input;

            next = next.replaceAll("\\{\\{queryDSL.generator.className}}", generatorClassName);
            next = next.replaceAll("\\{\\{queryDSL.timestamp}}", String.valueOf(new Date().getTime()));

            return next;
        }

        public static Context of(String generatorClassName) {
            return new Context(generatorClassName);
        }
    }


    public static final String DEFAULT_CONSTRUCTOR_ARGS = "\"{{queryDSL.generator.className}}\"";

    private static final Pattern JAVA_CLASS_NAME_PATTERN = Pattern.compile(
            "^((\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)(\\.\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)*?)\\.(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)\\s*(\\((.*)\\))?"
    );

    private final String template;
    private final String packageName;
    private final String simpleClassName;
    private final String fullyQualifiedClassName;
    private final String constructorArgs;

    public GeneratedAnnotationClass(String template) {
        this.template = template;

        Matcher matcher = parseClassNameFrom(template);
        this.packageName = matcher.group(1);
        this.simpleClassName = matcher.group(4);
        this.fullyQualifiedClassName = this.packageName + "." + this.simpleClassName;
        this.constructorArgs = matcher.group(6) != null
                ? matcher.group(6)
                : DEFAULT_CONSTRUCTOR_ARGS;
    }

    private Matcher parseClassNameFrom(String template) {
        Matcher matcher = JAVA_CLASS_NAME_PATTERN.matcher(template);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Cannot parse template: " + template);
        }
        return matcher;
    }

    public String getTemplate() {
        return template;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleClassName;
    }

    public String getName() {
        return fullyQualifiedClassName;
    }

    public String getConstructorArgs() {
        return constructorArgs;
    }

    public String buildAnnotation(Context context) {
        String liveTemplate = simpleClassName + buildAnnotationCtor(context);
        return liveTemplate;
    }

    private String buildAnnotationCtor(Context context) {
        String args = buildAnnotationArgs(context);

        String result = args.isEmpty()
                ? ""
                : "(" + args + ")";

        return result;
    }

    public String buildAnnotationArgs(Context context) {
        String result = context.replacePlaceholders(getConstructorArgs());
        return result;
    }

}
