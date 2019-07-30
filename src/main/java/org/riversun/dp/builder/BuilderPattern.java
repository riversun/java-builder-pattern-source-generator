package org.riversun.dp.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate Java builder pattern source code
 * 
 * Now supported only "Effective Java" builder pattern
 * 
 * Typical Usage
 * <code>
 *
  //Generate source code and save it.
  new BuilderPattern.Builder()
        .packageName("com.example")
        .className("Person")
        // specify variable type and name
        .addVariable(new Variable("String", "name"))
        .addVariable(new Variable("String", "address"))
        // In the case of "List<>", name of singular and plural can be specified
        .addVariable(new Variable("List<String>", "hobbies", "hobby"))
        .build()
        .save(new File("c:/temp"));
  
  //Generace source code as get it as String      
  String sourceCode = new BuilderPattern.Builder()
        .packageName("com.example")
        .className("Person")
        .addVariable(new Variable("String", "name"))
        .addVariable(new Variable("String", "address"))
        .addVariable(new Variable("List<String>", "hobbies", "hobby"))
        .build()
        .get();        
 * </code>
 * 
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class BuilderPattern {

    List<Variable> variables;
    String packageName;
    String className;
    String type;

    public static class Builder {

        List<Variable> variables = new ArrayList<Variable>();
        String packageName = "com.example";
        String className = "Example";
        String type = "EffectiveJava";

        public Builder() {
        }

        Builder(List<Variable> variables, String packageName, String className, String type) {
            this.variables = variables;
            this.packageName = packageName;
            this.className = className;
            this.type = type;
        }

        public Builder variables(List<Variable> variables) {
            this.variables = variables;
            return Builder.this;
        }

        public Builder addVariable(Variable variable) {
            this.variables.add(variable);
            return Builder.this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return Builder.this;
        }

        public Builder className(String className) {
            this.className = className;
            return Builder.this;
        }

        public Builder type(String type) {
            this.type = type;
            return Builder.this;
        }

        public BuilderPattern build() {

            if (variables == null || packageName == null || className == null || type == null) {
                throw new NullPointerException();
            }

            return new BuilderPattern(this);
        }
    }

    private BuilderPattern(Builder builder) {
        this.variables = builder.variables;
        this.packageName = builder.packageName;
        this.className = builder.className;
        this.type = builder.type;
    }

    /**
     * Returns generated source code as String
     * 
     * @return
     */
    public String get() {

        SourceGeneratable impl;

        if (type != null && type.equals("EffectiveJava")) {
            impl = new SourceGenEffectiveJava(this);
        } else {
            return "Only `EffectiveJava` type is supported now.";
        }

        return impl.generate();

    }

    /**
     * Save generated source code to specified dir
     * 
     * @param dir
     */
    public void save(File dir) {

        if (!dir.exists()) {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        }
        FileUtil.writeText(new File(dir, this.className + ".java"), get(), "UTF-8", false);
    }

}
