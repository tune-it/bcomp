grammar Axl;

program
   : topDecl* EOF
   ;

topDecl
   : funcDecl
   | globalVar
   | orgDecl
   | wordDecl
   ;

orgDecl
   : 'org' INT ';'
   ;

wordDecl
   : 'word' addr=INT ':' wordArg (',' wordArg)* ';'
   ;

wordArg
   : '-'? INT
   | '&' ID
   ;

funcDecl
   : type ID '(' params? ')' block
   ;

params
   : param (',' param)*
   ;

param
   : type ID ('[' ']')?
   ;

globalVar
   : type initDeclarator (',' initDeclarator)* ';'
   ;

initDeclarator
   : ID ('[' size=INT? ']')? ('=' initializer)?
   ;

initializer
   : expr
   | '{' (expr (',' expr)*)? '}'
   ;

type
   : baseType '*'*
   ;

baseType
   : 'int'
   | 'uint'
   | 'char'
   | 'long'
   | 'void'
   ;

block
   : '{' statement* '}'
   ;

statement
   : block
   | localVar
   | ifStmt
   | whileStmt
   | doWhileStmt
   | forStmt
   | returnStmt
   | breakStmt
   | continueStmt
   | gotoStmt
   | labelStmt
   | exprStmt
   | ';'
   ;

localVar
   : type initDeclarator (',' initDeclarator)* ';'
   ;

ifStmt
   : 'if' '(' expr ')' statement ('else' statement)?
   ;

whileStmt
   : 'while' '(' expr ')' statement
   ;

doWhileStmt
   : 'do' statement 'while' '(' expr ')' ';'
   ;

forStmt
   : 'for' '(' forInit? ';' expr? ';' forUpdate? ')' statement
   ;

forInit
   : type initDeclarator (',' initDeclarator)*
   | expr (',' expr)*
   ;

forUpdate
   : expr (',' expr)*
   ;

returnStmt
   : 'return' expr? ';'
   ;

breakStmt
   : 'break' ';'
   ;

continueStmt
   : 'continue' ';'
   ;

gotoStmt
   : 'goto' ID ';'
   ;

labelStmt
   : ID ':'
   ;

exprStmt
   : expr ';'
   ;

expr
   : expr '(' args? ')'                                                            # callExpr
   | expr '[' expr ']'                                                             # indexExpr
   | expr op=('++' | '--')                                                         # postIncDecExpr
   | <assoc=right> op=('-' | '+' | '~' | '!' | '*' | '&' | '++' | '--') expr        # prefixExpr
   | expr op=('*' | '/' | '%') expr                                                # mulExpr
   | expr op=('+' | '-') expr                                                      # addExpr
   | expr op=('<<' | '>>') expr                                                    # shiftExpr
   | expr op=('<' | '<=' | '>' | '>=') expr                                        # relExpr
   | expr op=('==' | '!=') expr                                                    # eqExpr
   | expr '&' expr                                                                 # bitAndExpr
   | expr '^' expr                                                                 # bitXorExpr
   | expr '|' expr                                                                 # bitOrExpr
   | expr '&&' expr                                                                # logAndExpr
   | expr '||' expr                                                                # logOrExpr
   | <assoc=right> expr op=('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=' | '<<=' | '>>=') expr   # assignExpr
   | '(' expr ')'                                                                  # parenExpr
   | INT                                                                           # intLitExpr
   | CHARLIT                                                                       # charLitExpr
   | ID                                                                            # idExpr
   ;

args
   : expr (',' expr)*
   ;

INT
   : '0' [xX] [0-9a-fA-F]+
   | [0-9]+
   ;

CHARLIT
   : '\'' ( ~['\\\r\n] | '\\' . ) '\''
   ;

ID
   : [a-zA-Z_] [a-zA-Z_0-9]*
   ;

WS
   : [ \t\r\n]+ -> skip
   ;

LINE_COMMENT
   : '//' ~[\r\n]* -> skip
   ;

BLOCK_COMMENT
   : '/*' .*? '*/' -> skip
   ;
