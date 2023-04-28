
package ru.ifmo.cs.bcomp.assembler;

/**
 *
 * @author serge
 */

public class Label {
    public final static int UNDEFINED = -1; 

    public String name;
    public volatile int address = UNDEFINED;
    public boolean referenced = false;
    public Label parent = null;

    @Override
    public String toString() {
        return "Label{" + "name=" + name + ", addr=" + (address != UNDEFINED ? address :"UNDEF") + '}';
    }

    public String getFullName() {
        if (parent == null)
            return name;
        return parent.name + name;
    }
    
}
