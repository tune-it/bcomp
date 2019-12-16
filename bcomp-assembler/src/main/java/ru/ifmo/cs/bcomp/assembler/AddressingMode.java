/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

/**
 *
 * @author serge
 */
public class AddressingMode {
    // contains direct numbers: addresses, displacement, direct load numbers
    public volatile int number = MemoryWord.UNDEFINED;
    public volatile String reference = null;
    public AddressingType addressation;
    
    public enum AddressingType {DIRECT_ABSOLUTE, INDIRECT, POST_INCREMENT, PRE_DECREMENT,
                                DISPLACEMENT_SP, DIRECT_RELATIVE, DIRECT_LOAD};
        
    @Override
    public String toString() {
        String s = "";
        if(addressation==null) return s;
        switch (addressation) {
            case DIRECT_ABSOLUTE: 
                if (number != MemoryWord.UNDEFINED ) {s += "0d"+number; break;}
                if (reference != null ) {s = '$'+reference; break;}
                s="$undef"; break;
            case INDIRECT:
                if (reference != null ) {s = '(' + reference + ')'; break;}
                s="(undef)"; break;
            case POST_INCREMENT:
                if (reference != null ) {s = '(' + reference + ")+"; break;}
                s="(undef)+"; break;
            case PRE_DECREMENT:
                if (reference != null ) {s = "-(" + reference + ')'; break;}
                s="-(undef)"; break;
            case DISPLACEMENT_SP: 
                if (number != MemoryWord.UNDEFINED ) {s = "&"+number; break;}
                s="&undef"; break;
            case DIRECT_RELATIVE:
                if (reference != null ) {s = reference; break;}
                s="undef"; break;
            case DIRECT_LOAD: 
                if (number != MemoryWord.UNDEFINED ) {s = "#"+number; break;}
                s="#undef"; break;
            default:
                s="UNDEF";
        }
        return s;
    }
    
}
