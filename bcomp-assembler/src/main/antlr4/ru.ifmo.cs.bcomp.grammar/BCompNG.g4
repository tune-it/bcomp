grammar BCompNG;

prog
   : (line? EOL)*
   ;

line
   : comment
   | instructionLine
   | directive
   | lbl
   ;

instructionLine
   : lbl? instruction comment?
   ;

instruction
   : addr operand
   | nonaddr
   | branch label
   | io dev
   ;

directive
   : org orgAddress comment?
   | wordDirective
   | end comment?
   ;

orgAddress
   : address
   ;

wordDirective
   : lbl? word wordArguments comment?
   ;

wordArguments
   : wordArgument ( ',' wordArgument )*
   ;

wordArgument
   : number
   | '$' label
   | dupArgument
   | '?'
   ;

dupArgument
   : count dup '(' wordArgument ')'
   ;

count
   : number
   ;

lbl
   : label ':'
   ;

label
   : name
   ;

dev
   : number
   ;

operand
   : directAbsolute
   | indirect
   | postIncrement
   | preDecrement
   | displacementSP
   | directRelative
   | directLoad
   ;

directAbsolute
   : address
   | '$' label
   ;

indirect
   : '(' label ')'
   ;

postIncrement
   : '(' label ')' '+'
   ;

preDecrement
   : '-' '(' label ')'
   ;

displacementSP
   : '&' number
   | '(' sp '+' number ')'
   ;

directRelative
   : label
   ;

directLoad
   : '#' number
   ;

address
   : number
   ;

string
   : STRING
   ;

name
   : NAME
   ;

number
   : DECIMAL
   | HEX
   ;

comment
   : COMMENT
   ;

addr: AND | OR | ADD | ADC | SUB | CMP | LOOP | LD | SWAM | JUMP | CALL | ST;
nonaddr: NOP | HLT | CLA | NOT | CLC | CMC | ROL | ROR | ASL | ASR | SXTB | SWAB |
         INC | DEC | NEG | POP | POPF | RET | IRET | PUSH | PUSHF | SWAP |
         EI  | DI;
branch: BEQ | BNE | BMI | BPL | BCS | BCC | BVS | BVC | BLT | BGE | BR;
io:  IN | OUT | INT;

sp: SP;
ip: IP;

org: ORG;
word: WORD;
dup: DUP;
end: END;

fragment A : ('a' | 'A'); 
fragment B : ('b' | 'B');
fragment C : ('c' | 'C');
fragment D : ('d' | 'D');
fragment E : ('e' | 'E');
fragment F : ('f' | 'F');
fragment G : ('g' | 'G');
fragment H : ('h' | 'H');
fragment I : ('i' | 'I');
fragment J : ('j' | 'J');
fragment K : ('k' | 'K');
fragment L : ('l' | 'L');
fragment M : ('m' | 'M');
fragment N : ('n' | 'N');
fragment O : ('o' | 'O');
fragment P : ('p' | 'P');
fragment Q : ('q' | 'Q');
fragment R : ('r' | 'R');
fragment S : ('s' | 'S');
fragment T : ('t' | 'T');
fragment U : ('u' | 'U');
fragment V : ('v' | 'V');
fragment W : ('w' | 'W');
fragment X : ('x' | 'X');
fragment Y : ('y' | 'Y');
fragment Z : ('z' | 'Z');

fragment RA : ('а' | 'А'); 
fragment RB : ('б' | 'Б');
fragment RV : ('в' | 'В');
fragment RG : ('г' | 'Г');
fragment RD : ('д' | 'Д');
fragment RE : ('е' | 'Е');
fragment RYO : ('ё' | 'Ё');
fragment RZH : ('ж' | 'Ж');
fragment RZ : ('з' | 'З');
fragment RI : ('и' | 'И');
fragment RK : ('к' | 'К');
fragment RL : ('л' | 'Л');
fragment RM : ('м' | 'М');
fragment RN : ('н' | 'Н');
fragment RO : ('о' | 'О');
fragment RP : ('п' | 'П');
fragment RR : ('р' | 'Р');
fragment RS : ('с' | 'С');
fragment RT : ('т' | 'Т');
fragment RU : ('у' | 'У');
fragment RF : ('ф' | 'Ф');
fragment RKH : ('х' | 'Х');
fragment RTSC : ('ц' | 'Ц');
fragment RCH : ('ч' | 'Ч');
fragment RSH : ('ш' | 'Ш');
fragment RHSIGN : ('ъ' | 'Ъ');
fragment RII : ('ы' | 'Ы');
fragment RSSIGN : ('ь' | 'Ь');
fragment RYE : ('э' | 'Э');
fragment RYU : ('ю' | 'Ю');
fragment RYA : ('я' | 'Я');

fragment HEXDIGIT : [0-9a-fA-F] ;
fragment DECDIGIT : [0-9] ;
fragment P0X : '0' X ;
fragment P0D : '0' D ;

/*
* Assembler Instruction
*/
ORG: O R G;
WORD: W O R D;
END: E N D;
DUP: ( D U P ) | ( D U P L I C A T E );

/*
* opcodes
*/

AND: ( A N D ) | ( RI );
OR: ( O R ) | ( RI RL RI );
ADD: ( A D D ) | ( RP RL RYU RS );
ADC: ( A D C ) | ( RP RL RYU RS RS );
SUB: ( S U B ) | ( RM RI RN RU RS );
CMP: ( C M P ) | ( RS RR RA RV );
LOOP: ( L O O P ) | ( RK RR RU RG );
LD: ( L D ) | ( RN RYA RM );
SWAM: ( S W A M );
JUMP: ( J U M P ) | ( RP RR RII RG );
CALL: ( C A L L ) | ( RV RZH RU RKH );
ST: ( S T ) | ( RT RSSIGN RF RU );

NOP: ( N O P );
HLT: ( H L T );
CLA: ( C L A );
NOT: ( N O T ) | ( C M A ) | ( C O M ) | ( RN RE ) | ( RS RB RA );
CLC: ( C L C ) ;
CMC: ( C M C ) ;
ROL: ( R O L ) ;
ROR: ( R O R ) ;
ASL: ( A S L ) ;
ASR: ( A S R ) ;
SXTB: ( S X T B ) ; 
SWAB: ( S W A B ) ;
INC: ( I N C ) ; 
DEC: ( D E C ) ;
NEG: ( N E G ) ;
POP: ( P O P ) ;
POPF: ( P O P F ) ;
RET: ( R E T ) ;
IRET: ( I R E T ) ;
PUSH: ( P U S H ) ;
PUSHF: ( P U S H F );
SWAP: ( S W A P ) ;

BEQ: ( B E Q ) | ( B Z S );
BNE: ( B N E ) | ( B Z C );
BMI: ( B M I ) | ( B N S ); 
BPL: ( B P L ) | ( B N C );
BCS: ( B C S ) | ( B L O );
BCC: ( B C C ) | ( B H I S );
BVS: ( B V C ) ;
BVC: ( B V C ) ;
BLT: ( B L T ) ;
BGE: ( B G E ) ;
BR: ( B R ) ;    //syntetic insturction, jump with direct relative addressing mode


DI: ( D I ) ;
EI: ( E I ) ;
IN: ( I N ) ;
OUT: ( O U T ) ;
INT: ( I N T ) ;

SP: ( S P ) ;
IP: ( I P ) ;

NAME
   : [a-zA-Zа-яА-Я_] [a-zA-Zа-яА-Я_0-9."]*
   ;

DECIMAL
   : ( P0D? DECDIGIT+ )
   | ( '-' P0D? DECDIGIT+ )
   | ( P0D? '-' DECDIGIT+ )
   ;

HEX
   : HEXDIGIT+ | (P0X HEXDIGIT+ ) | ( HEXDIGIT+ H ) 
   | ( '-' HEXDIGIT+ ) | ( '-' P0X HEXDIGIT+ ) | (P0X '-' HEXDIGIT+ ) | ( '-' HEXDIGIT+ H ) 
   ;

COMMENT
   : ';' ~ [\r\n]* -> skip
   ;


STRING
   : '"' ~ ["]* '"'
   ;


EOL
   : [\r\n] +
   ;


WS
   : [ \t] -> skip
   ;