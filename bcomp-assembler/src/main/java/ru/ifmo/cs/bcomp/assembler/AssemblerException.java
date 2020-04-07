/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author serge
 */
public class AssemblerException extends RecognitionException {


    public AssemblerException(Parser recognizer) { // LL(1) error
        super(recognizer, recognizer.getInputStream(), recognizer.getContext());
        this.setOffendingToken(recognizer.getCurrentToken());
    }


    public AssemblerException(String msg, Parser recognizer, ParserRuleContext ctx) {
        super(msg, recognizer, recognizer.getInputStream(), ctx);
        //System.out.println(msg+recognizer.getCurrentToken());
        this.setOffendingToken(recognizer.getCurrentToken());
    }
    
    public AssemblerException(String msg, Parser recognizer) {
        super(msg, recognizer, recognizer.getInputStream(), recognizer.getContext());
        this.setOffendingToken(recognizer.getCurrentToken());
    }
    
    public ParserRuleContext getParserRuleContext() {
        return (ParserRuleContext)getCtx();
    }

    
    
}
