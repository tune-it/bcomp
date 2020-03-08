/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.bcomp.assembler;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

/**
 *
 * @author serge
 */
public class AssemblerAntlrErrorStrategy extends DefaultErrorStrategy {

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        if (!(e instanceof AssemblerException)) {
            super.reportError(recognizer, e);
            return;
        }
        AssemblerException ae = (AssemblerException)e;
        // if we've already reported an error and have not matched a token
        // yet successfully, don't report any errors.
        if (inErrorRecoveryMode(recognizer)) {
            //System.err.print("[SPURIOUS] ");
            return; // don't report spurious errors
        }
        beginErrorCondition(recognizer);
        recognizer.notifyErrorListeners(ae.getOffendingToken(),ae.getMessage(), ae);
    }

}
