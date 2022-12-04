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
fragment RSSH : ('щ' | 'Щ');
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
ORG: ( O R G ) | ( RN RA RCH ); // НАЧ
WORD: ( W O R D ) | ( RS RL RO RV RO ); // СЛОВО
END: ( E N D ) | ( RK RO RN ); // КОН
DUP: ( D U P ) | ( D U P L I C A T E ) | ( RP RO RV RT ); // ПОВТ

/*
* opcodes
*/

AND: ( A N D ) | ( RI );      //И
OR: ( O R ) | ( RI RL RI );   //ИЛИ
ADD: ( A D D ) | ( RP RL RYU RS );       // ПЛЮС
ADC: ( A D C ) | ( RP RL RYU RS RS );    // ПЛЮСС
SUB: ( S U B ) | ( RM RI RN RU RS );     // МИНУС
CMP: ( C M P ) | ( RS RR RA RV );        // СРАВ
LOOP: ( L O O P ) | ( RV RZ RA RD );     //ВЗАД
LD: ( L D ) | ( RN RYA RM );             //НЯМ
SWAM: ( S W A M ) | ( RO RB RM RE RN );  // ОБМЕН
JUMP: ( J U M P ) | ( RP RR RII RG );    //ПРЫГ
CALL: ( C A L L ) | ( RV RZH RU RKH );   // ВЖУХ
ST: ( S T ) | ( RT RSSIGN RF RU );       //ТЬФУ

NOP: ( N O P ) | ( RP RR RO RP ); // ПРОП
HLT: ( H L T ) | ( RS RT RO RP ); // СТОП
CLA: ( C L A ) | ( RCH RI RS RT RSSIGN ); // ЧИСТЬ
NOT: ( N O T ) | ( C M A ) | ( C O M ) | ( RN RE ) | ( RS RB RA )| ( RN RE RT RSSIGN );  // НЕТЬ
CLC: ( C L C ) | ( RCH RI RS RT RTSC );  // ЧИСТЦ
CMC: ( C M C ) | ( RI RN RV RTSC );  // ИНВЦ
ROL: ( R O L ) | ( RTSC RL RE RV );  // ЦЛЕВ
ROR: ( R O R ) | ( RTSC RP RR RA RV ); // ЦПРАВ
ASL: ( A S L ) | ( RA RL RE RV ); // АЛЕВ
ASR: ( A S R ) | ( RA RP RR RA RV );  // АПРАВ
SXTB: ( S X T B ) | ( RR RA RS RSH );  // РАСШ
SWAB: ( S W A B ) | ( RN RA RO RB RO RR RO RT );  // НАОБОРОТ
INC: ( I N C ) | ( RU RV RE RL );  // УВЕЛ
DEC: ( D E C ) | ( RU RM RE RN );  // УМЕН
NEG: ( N E G ) | ( RO RT RR RI RTSC );  // ОТРИЦ
POP: ( P O P ) | ( RV RII RN RSSIGN ); // ВЫНЬ
POPF: ( P O P F ) | ( RV RII RN RSSIGN RF );  // ВЫНЬФ
RET: ( R E T )  | ( RV RO RZ RV RR ); // ВОЗВР
IRET: ( I R E T ) | ( RV RO RZ RV RR RP);  // ВОЗВРП
PUSH: ( P U S H ) | ( RS RU RN RSSIGN ); // СУНЬ
PUSHF: ( P U S H F ) | ( RS RU RN RSSIGN RF); // СУНЬФ
SWAP: ( S W A P ) | ( RM RE RN RSSIGN ); // МЕНЬ

BEQ: ( B E Q ) | ( B Z S ) | ( RB RYA RK RA ); // БЯКА
BNE: ( B N E ) | ( B Z C ) | ( RB RN RE RK RA ); // БНЕКА
BMI: ( B M I ) | ( B N S ) | ( RB RM RI RN RU RS ); // БМИНУС 
BPL: ( B P L ) | ( B N C ) | ( RB RP RL RU RS ); // БПЛЮС
BCS: ( B C S ) | ( B H I S ) | ( RB RE RTSC ); // БЕЦ
BCC: ( B C C ) | ( B L O ) | ( RB RN RE RTSC ); // БНЕЦ
BVS: ( B V S ) | ( RB RO RV RE RR ); // БОВЕР
BVC: ( B V C ) | ( RB RN RE RO RV RE RR ); // БНЕОВЕР
BLT: ( B L T ) | ( RB RM RE RN RSSIGN ); // БМЕНЬ
BGE: ( B G E ) | ( RB RN RE RM RE RN RSSIGN ); // БНЕМЕНЬ
BR: ( B R ) ;    //syntetic insturction, jump with direct relative addressing mode


DI: ( D I ) | ( RN RI RZ RYA ); // НИЗЯ
EI: ( E I ) | ( RL RSSIGN RZ RYA ); // ЛЬЗЯ
IN: ( I N ) | ( RV RV RO RD ); // ВВОД
OUT: ( O U T ) | ( RV RII RV RO RD ); // ВЫВОД
INT: ( I N T ) | ( RP RR RE RR ); // ПРЕР

SP: ( S P ) | ( RS RU ); // СУ
IP: ( I P ) | ( RI RU ); // ИУ

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
