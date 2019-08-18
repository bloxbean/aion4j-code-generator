grammar ABI;

abi_content
    : version contract_name clinit (methodDeclaration)* EOF;

version: TEXT (DOT TEXT)?;
contract_name: TEXT (DOT TEXT)*;

clinit: clinitKeyword LPAREN paramType? (COMMA paramType)* RPAREN;

methodDeclaration
	:	MODIFIERS+ returnType methodName LPAREN paramType? (',' paramType)* RPAREN
	;

MODIFIERS:
    | 'public'
    | 'static'
    ;

returnType: TEXT (ARRAY)?;

methodName: TEXT;

parameterListBody
    : LPAREN RPAREN
    | LPAREN TEXT (COMMA TEXT)* RPAREN
    ;

parameterList
    : (TEXT COMMA?)
    ;

paramType: TEXT (ARRAY)?;


clinitKeyword : TEXT COLON;


TEXT: LetterOrDigit+;

//TEXT: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '/' | '\\' | '*' | '.' | '@' | ' ')+;
        //( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '/'| '\\' | ':')* ;

//TEXT : ( ~('='|'\n') )*;


TYPE
    :  'byte'
    |  'boolean'
    |  'char'
    |  'short'
    |   'int'
    |   'float'
    |   'long'
    |   'double'
    |   'Address'
    |   'String'
    |   'BigInteger'
       ;
//
ARRAY
    : ONEDARRAY
    | TWODARRAY
    ;

ONEDARRAY
    : '[]'
    ;

TWODARRAY
    : '[][]'
    ;
//
//
IDENTIFIER:         Letter LetterOrDigit*;
//
//
//TEXT   : ~[,\n\r"]+ ;
//
//
LPAREN:             '(';
RPAREN:             ')';
COMMA:              ',';
DOT:                '.';
COLON:              ':';

LINE_COMMENT : ';' ~('\n'|'\r')*  ->  channel(HIDDEN);

WS  :   (('\r')? '\n' |  ' ' | '\t')+  -> skip;

fragment Digits
    : [0-9] ([0-9_]* [0-9])?
    ;
fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment Letter
    : [a-zA-Z$_] // these are the "java letters" below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;
