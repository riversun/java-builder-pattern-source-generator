package org.riversun.dp.builder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generator implementation for "Effective Java" sytyle
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class SourceGenEffectiveJava implements SourceGeneratable {

    final BuilderPattern mParent;

    SourceGenEffectiveJava(BuilderPattern parent) {
        mParent = parent;
    }

    @Override
    public String generate() {

        String template;

        try {
            template = FileUtil.getTextFromResourceFile("effective_java_template.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        String result = template

                .replace("//IMPORTS//", createImports())
                .replace("//PACKAGE_NAME//", mParent.packageName)
                .replace("//CLASS_NAME//", mParent.className)
                .replace("//CLASS_FIELDS//", createChunkField("\t"))
                .replace("//BUILDER_FIELDS//", createChunkFieldBuilder("\t\t"))
                .replace("//BUILDER_CONSTRUCTOR//", createChunkBuilderConstructor(""))
                .replace("//BUILDER_INITIALIZERS//", createChunkBuilderInitializer("\t\t\t"))
                .replace("//BUILDER_SETTERS//", createChunkBuilderSetters("\t\t"))
                .replace("//NULL_FIELD_CHECKER//", createChunkBuilderNullChecker())
                .replace("//CLASS_INITIALIZERS//", createChunkClassInitializer("\t\t"));

        return result;

    }

    String createImports() {

        StringBuilder sb = new StringBuilder();
        for (Variable v : mParent.variables) {
            if (v.type.startsWith("List")) {
                sb.append("import java.util.ArrayList;\n");
                sb.append("import java.util.List;\n");
                break;
            }
        }
        return sb.toString();
    }

    String createChunkBuilderNullChecker() {

        StringBuilder sb = new StringBuilder();

        for (Variable v : mParent.variables) {
            if (Character.isUpperCase(v.type.charAt(0))) {
                sb.append(String.format("%s == null || ", v.name));
            }
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();

    }

    String createChunkBuilderSetters(String indent) {

        StringBuilder sb = new StringBuilder();

        for (Variable v : mParent.variables) {

            // 1st line
            sb.append(indent);
            sb.append(String.format("public Builder %s(%s %s){", v.name, v.type, v.name));
            sb.append("\n");

            // 2nd line
            sb.append(indent);
            sb.append("\t");
            sb.append(String.format("this.%s = %s;", v.name, v.name));
            sb.append("\n");

            // 3rd line
            sb.append(indent);
            sb.append("\t");
            sb.append(String.format("return Builder.this;"));
            sb.append("\n");
            // 4th line
            sb.append(indent);
            sb.append(String.format("}"));
            sb.append("\n");
            sb.append("\n");

            if (v.type.startsWith("List")) {
                // 1st line
                sb.append(indent);
                sb.append(String.format("public Builder add%s(%s %s){",
                        capitalizeFirstLetter(v.singleName),
                        extractTextEnclosedInBrackets(v.type, "Object"), v.singleName));
                sb.append("\n");

                // 2nd line
                sb.append(indent);
                sb.append("\t");
                sb.append(String.format("this.%s.add(%s);", v.name, v.singleName));
                sb.append("\n");

                // 3rd line
                sb.append(indent);
                sb.append("\t");
                sb.append(String.format("return Builder.this;"));
                sb.append("\n");
                // 4th line
                sb.append(indent);
                sb.append(String.format("}"));
                sb.append("\n");
                sb.append("\n");
            }

        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    String createChunkClassInitializer(String indent) {

        StringBuilder sb = new StringBuilder();

        for (Variable v : mParent.variables) {
            sb.append(indent);
            sb.append(String.format("this.%s = builder.%s; ", v.name, v.name));
            sb.append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    String createChunkBuilderInitializer(String indent) {

        StringBuilder sb = new StringBuilder();
        for (Variable v : mParent.variables) {
            sb.append(indent);
            sb.append(String.format("this.%s = %s; ", v.name, v.name));
            sb.append("\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    String createChunkBuilderConstructor(String indent) {

        StringBuilder sb = new StringBuilder();
        for (Variable v : mParent.variables) {
            sb.append(indent);
            sb.append(String.format("%s %s, ", v.type, v.name));

        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    String createChunkField(String indent) {

        StringBuilder sb = new StringBuilder();
        for (Variable v : mParent.variables) {
            sb.append(indent);
            sb.append(String.format("private %s %s", v.type, v.name));
            sb.append(";");
            sb.append("\n");
        }
        return sb.toString();
    }

    String createChunkFieldBuilder(String indent) {

        StringBuilder sb = new StringBuilder();
        for (Variable v : mParent.variables) {
            sb.append(indent);
            sb.append(String.format("private %s %s", v.type, v.name));
            if (v.type.startsWith("List")) {
                sb.append(String.format(" = new ArrayList<%s>()", extractTextEnclosedInBrackets(v.type, "Object")));
            }
            sb.append(";");
            sb.append("\n");
        }
        return sb.toString();
    }

    String extractTextEnclosedInBrackets(String str, String textWhenNull) {
        final String regex = "\\<(.+?)\\>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            // return 1st match
            return matcher.group(1);
        }
        return textWhenNull;
    }

    String capitalizeFirstLetter(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1).toLowerCase());
        return sb.toString();

    }
}