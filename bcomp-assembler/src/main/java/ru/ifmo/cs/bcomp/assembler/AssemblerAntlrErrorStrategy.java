/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 *
 * @author serge
 */
public class AssemblerAntlrErrorStrategy extends DefaultErrorStrategy {

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        //System.err.println(e.getMessage());
        if (!(e instanceof AssemblerException)) {
            super.reportError(recognizer, e);
            return;
        }
        AssemblerException ae = (AssemblerException)e;
        // if we've already reported an error and have not matched a token
        // yet successfully, don't report any errors.
        //if (inErrorRecoveryMode(recognizer)) {
            //System.err.print("[SPURIOUS] ");
        //    return; // don't report spurious errors
        //}
        beginErrorCondition(recognizer);
        recognizer.notifyErrorListeners(ae.getOffendingToken(),ae.getMessage(), ae);
    }

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
//		System.out.println("recover in "+recognizer.getRuleInvocationStack()+
//						   " index="+recognizer.getInputStream().index()+
//						   ", lastErrorIndex="+
//						   lastErrorIndex+
//						   ", states="+lastErrorStates);
            if ( lastErrorIndex==recognizer.getInputStream().index() &&
                    lastErrorStates != null &&
                    lastErrorStates.contains(recognizer.getState()) ) {
                    // uh oh, another error at same token index and previously-visited
                    // state in ATN; must be a case where LT(1) is in the recovery
                    // token set so nothing got consumed. Consume a single token
                    // at least to prevent an infinite loop; this is a failsafe.
//			System.err.println("seen error condition before index="+
//							   lastErrorIndex+", states="+lastErrorStates);
//			System.err.println("FAILSAFE consumes "+recognizer.getTokenNames()[recognizer.getInputStream().LA(1)]);
                    recognizer.consume();
            }
            lastErrorIndex = recognizer.getInputStream().index();
            if ( lastErrorStates==null ) lastErrorStates = new IntervalSet();
            lastErrorStates.add(recognizer.getState());
            IntervalSet followSet = getErrorRecoverySet(recognizer);
            consumeUntil(recognizer, followSet);
    }    
    
}
