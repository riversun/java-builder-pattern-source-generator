# Overview
A library that automatically generates the source code of the "builder pattern"

It is licensed under [MIT](https://opensource.org/licenses/MIT).

# How to use

## Dependency

Add dependency.

```xml
<dependency>
	<groupId>org.riversun</groupId>
	<artifactId>builder-pattern-generator</artifactId>
	<version>1.0.0</version>
</dependency>
```


## Example1:Generate "Effective Java" style builder pattern source code. 


```java
package examples;

import org.riversun.dp.builder.BuilderPattern;
import org.riversun.dp.builder.Variable;

public class Example1 {

    public static void main(String[] args) {

        String sourceCode = new BuilderPattern.Builder()
                .packageName("com.example")
                .className("Person")
                // specify variable type and name
                .addVariable(new Variable("String", "name"))
                .addVariable(new Variable("String", "address"))
                // In the case of "List<>", name of singular and plural can be specified
                .addVariable(new Variable("List<String>", "hobbies", "hobby"))
                .build()
                .get();

        System.out.println(sourceCode);

    }
}
```

**Result**

You can get following source code.
If you specify a variable of type "List<>", the method named "add[Variable]" is also automatically generated.

```java
package com.example;

import java.util.ArrayList;
import java.util.List;


public class Person {

	private String name;
	private String address;
	private List<String> hobbies;

	public static class Builder {
	
		private String name;
		private String address;
		private List<String> hobbies = new ArrayList<String>();

		public Builder() {	
		}
		
		Builder(String name, String address, List<String> hobbies) {	
			this.name = name; 
			this.address = address; 
			this.hobbies = hobbies; 			
		}
		
		public Builder name(String name){
			this.name = name;
			return Builder.this;
		}

		public Builder address(String address){
			this.address = address;
			return Builder.this;
		}

		public Builder hobbies(List<String> hobbies){
			this.hobbies = hobbies;
			return Builder.this;
		}

		public Builder addHobby(String hobby){
			this.hobbies.add(hobby);
			return Builder.this;
		}

		public Person build() {
		
			if (name == null || address == null || hobbies == null) {
				throw new NullPointerException();
			}
			
			return new Person(this);
		}
	}

	private Person(Builder builder) {
		this.name = builder.name; 
		this.address = builder.address; 
		this.hobbies = builder.hobbies; 	
	}

	public void doSomething() {
		// do something
	}
}
```

## Example2:Save generated code to directory.


```java
package examples;

import java.io.File;

import org.riversun.dp.builder.BuilderPattern;
import org.riversun.dp.builder.Variable;

public class Example2 {

    public static void main(String[] args) {

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

    }
}
```


