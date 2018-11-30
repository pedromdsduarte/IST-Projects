%option c++ yyclass="zu_scanner" outfile="zu_scanner.cpp"
%option stack noyywrap yylineno 8bit debug
%{ 
/* $Id: zu_scanner.l,v 1.17 2016/05/19 17:45:30 ist178328 Exp $ */
// make relevant includes before including the parser's tab file
#include <string>
#include <cdk/ast/sequence_node.h>
#include <cdk/ast/expression_node.h>
#include "zu_scanner.h"
#include "zu_parser.tab.h"

// don't change this
#define yyerror LexerError
%}
%x X_STRING X_COMMENT X_STR_SPECIAL
%%

"//".*$                 ; /* ignore comments */

"/*"                   yy_push_state(X_COMMENT);
<X_COMMENT>"/*"        yy_push_state(X_COMMENT);
<X_COMMENT>"*/"        yy_pop_state();
<X_COMMENT>.|\n        ;

">="                   return tGE; 
"<="                   return tLE; 
"=="                   return tEQ; 
"!="                   return tNE; 
 /*">"                    return tGT;*/ 
 /*"<"                    return tLT;*/

"@"                    return tREAD; 
"><"                   return tBREAK;
"<>"                   return tCONTINUE;
"!!"                   return tPRINTLN;
"!!!"                  return tRETURN;

[A-Za-z_][A-Za-z0-9_]*  yylval.s = new std::string(yytext); return tIDENTIFIER;

\"                     yy_push_state(X_STRING); yylval.s = new std::string("");
<X_STRING>\"           yy_pop_state(); return tSTRING;
<X_STRING>\\           yy_push_state(X_STR_SPECIAL);
<X_STRING>.            *yylval.s += yytext;
<X_STRING>\n           yyerror("newline in string");

<X_STR_SPECIAL>n			yy_pop_state(); *yylval.s += std::string(1,10); 
<X_STR_SPECIAL>r			yy_pop_state(); *yylval.s += std::string(1,13); 
<X_STR_SPECIAL>t			yy_pop_state(); *yylval.s += std::string(1,9); 
<X_STR_SPECIAL>[\"\\]			yy_pop_state(); *yylval.s += std::string(1,(int)*yytext); 
<X_STR_SPECIAL>[0-9a-fA-F][0-9a-fA-F]?	{yy_pop_state(); *yylval.s += std::string(1,strtol(yytext, nullptr, 16));}
<X_STR_SPECIAL>.|\n			yyerror("Invalid special character");

0|([1-9][0-9]*)        yylval.i = strtol(yytext, nullptr, 10); return tINTEGER;
0x[0-9a-fA-F]+         yylval.i = strtol(yytext, nullptr, 16); return tINTEGER;
    /*([0-9]+\.[0-9]+)([eE][+-]?[0-9]+)? yylval.r = strtod(yytext, nullptr); return tREAL;*/
(([0-9]*\.[0-9]+)|([0-9]+\.[0-9]*)|([0-9]+))([eE][-]?[0-9]+)?       {yylval.r = strtod(yytext, nullptr); return tREAL;}
    /*0[0-9]+                yyerror("Invalid number");*/


[-()<>=+*/?%;{}\.!#$,~|:[\]&]       return *yytext;


[ \n\r\t]+              ; /* ignore whitespace */

.                      {
			std::cout << "Found character '" << yytext << "' on line " << yylineno << std::endl;
			yyerror("Unknown character");
			}

%%
// Very, very dirty hack: flex is a mess generating C++ scanners.
int zu_scanner::yywrap() { return 1; }
