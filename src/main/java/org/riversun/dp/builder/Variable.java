package org.riversun.dp.builder;

/**
 * Model class of Variable
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class Variable {

    public String type;
    public String name;
    public String singleName;

    /**
     * 
     * @param type
     *            variable type
     * @param name
     *            variable name
     */
    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
        this.singleName = name;
    }

    /**
     * In the case of "List<>", variable name for singular and plural can be
     * specified
     * 
     * @param type
     * @param plural
     * @param singular
     */
    public Variable(String type, String plural, String singular) {
        this.type = type;
        this.name = plural;
        this.singleName = singular;
    }

}