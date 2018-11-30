%{
// $Id: zu_parser.y,v 1.36 2016/05/19 23:45:47 ist179112 Exp $
//-- don't change *any* of these: if you do, you'll break the compiler.
#include <cdk/compiler.h>
#include "ast/all.h"
#define LINE       compiler->scanner()->lineno()
#define yylex()    compiler->scanner()->scan()
#define yyerror(s) compiler->scanner()->error(s)
#define YYPARSE_PARAM_TYPE std::shared_ptr<cdk::compiler>
#define YYPARSE_PARAM      compiler
//-- don't change *any* of these --- END!
%}

%union {
  int                   i;	/* integer value */
  double                r;
  std::string          *s;	/* symbol name or string literal */
  cdk::basic_node      *node;	/* node pointer */
  cdk::sequence_node   *sequence;
  cdk::expression_node *expression; /* expression nodes */
  zu::lvalue_node      *lvalue;
  basic_type           *type;
  zu::block_node       *block;
};

%token <i> tINTEGER
%token <s> tIDENTIFIER tSTRING
%token <r> tREAL;
%token tREAD tBREAK tCONTINUE tRETURN tPRINTLN

/*
%right '='
%left '|'
%left '&'
%nonassoc '~'
%left tEQ tNE
%left tGE tLE tGT tLT
%left '+' '-'
%left '*' '/' '%'
%nonassoc tUNARY
%nonassoc '(' '['
%nonassoc '>' '<'
%nonassoc '{' '}'
%nonassoc '#'
%nonassoc '?'
%nonassoc ':'
*/


%nonassoc xIF 
%nonassoc ':'

%right '='
%left '|'
%left '&'
%nonassoc '~'
%left tEQ tNE
%left tGE tLE '>' '<'
%left '+' '-'
%left '*' '/' '%'
%nonassoc tUNARY
%nonassoc '(' ')' '[' ']'

/*%nonassoc '>' '<'
%nonassoc '{' '}'
%nonassoc '#'
%nonassoc '?'
%nonassoc ':'*/


%type <node> stmt decl iteration condition function var file vardecl arg
%type <sequence> list vars seqdecl exprs varseqdecl fargs args 
%type <expression> expr literal
%type <lvalue> lval
%type <type> type
%type <block> block

%type <s> lstring

%{
//-- The rules below will be included in yyparse, the main parsing function.
%}
%%

/*program	: tBEGIN list tEND { compiler->ast(new zu::program_node(LINE, $2)); }
	      ;*/
file : seqdecl          { compiler->ast(new cdk::sequence_node(LINE, $1)); }
     |                  { compiler->ast(new cdk::sequence_node(LINE, nullptr));}
     ;
     
seqdecl: decl           { $$ = new cdk::sequence_node(LINE, $1); }
     | seqdecl decl     { $$ = new cdk::sequence_node(LINE, $2, $1); }
     ;

decl : vardecl          { $$ = $1;}
     | function         { $$ = $1;}
     ;
     
vardecl: var ';'        { $$ = $1;} //FIXME ,?
     ;

vars : var                  { $$ = new cdk::sequence_node(LINE, $1); }
     | vars ',' var         { $$ = new cdk::sequence_node(LINE, $3, $1); }
     ;

varseqdecl: vardecl         { $$ = new cdk::sequence_node(LINE, $1); }
     | varseqdecl vardecl   { $$ = new cdk::sequence_node(LINE, $2, $1); }
     ;
     

var  : type tIDENTIFIER              { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false);     }
     | type tIDENTIFIER '=' expr     { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false, $4); }
     | type tIDENTIFIER '!'          { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false);      }
     | type tIDENTIFIER '?'          { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true);      } 
     | type tIDENTIFIER '!' '=' expr { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false, $5);  }
     | type tIDENTIFIER '?' '=' expr { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true, $5);  }
     ;
     
fargs: args { $$ = $1; }
     | /* epsilon */    { $$ = nullptr;}
     ;
     

     
args : arg                      { $$ = new cdk::sequence_node(LINE, $1); }
     | args ',' arg             { $$ = new cdk::sequence_node(LINE, $3, $1); }
     ;

arg  : type tIDENTIFIER              { $$ = new zu::declaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false); }
     /*| literal                       { $$ = $1; }*/
     /*| tIDENTIFIER                   { $$ = new zu::identifier_node(LINE, $1); }*/
     | expr                          { $$ = $1; }
     ;
     
function : type tIDENTIFIER '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false, $4, nullptr); }
     | type tIDENTIFIER '!' '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false, $5, nullptr); }
     | type tIDENTIFIER '?' '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true, $5, nullptr); }
     | '!'  tIDENTIFIER     '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, false, $4, nullptr); }
     | '!'  tIDENTIFIER '!' '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), true, false, $5, nullptr); }
     | '!'  tIDENTIFIER '?' '(' fargs ')'                    { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, true, $5, nullptr); }
     
     | type tIDENTIFIER '(' fargs ')' block                  { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false, $4, nullptr, $6); }
     | type tIDENTIFIER '!' '(' fargs ')'  block             { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false, $5, nullptr, $7); }
     | type tIDENTIFIER '?' '(' fargs ')'  block             { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true, $5, nullptr, $7); }
     | '!'  tIDENTIFIER     '(' fargs ')'  block             { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, false, $4, nullptr, $6); }
     | '!'  tIDENTIFIER '!' '(' fargs ')'  block             { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), true, false, $5, nullptr, $7); }
     | '!'  tIDENTIFIER '?' '(' fargs ')'  block             { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, true, $5, nullptr, $7); }
     
     | type tIDENTIFIER     '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false, $4, $7); }
     | type tIDENTIFIER '!' '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false, $5, $8); }
     | type tIDENTIFIER '?' '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true, $5, $8); }
     | '!'  tIDENTIFIER     '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, false, $4, $7); }
     | '!'  tIDENTIFIER '!' '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), true, false, $5, $8); }
     | '!'  tIDENTIFIER '?' '(' fargs ')' '=' literal        { $$ = new zu::functiondeclaration_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, true, $5, $8); }
     
     | type tIDENTIFIER     '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), false, false, $4, $7, $8); }
     | type tIDENTIFIER '!' '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), true, false, $5, $8, $9); }
     | type tIDENTIFIER '?' '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, $1, new zu::identifier_node(LINE, $2), false, true, $5, $8, $9); }
     | '!'  tIDENTIFIER     '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, false, $4, $7, $8); }
     | '!'  tIDENTIFIER '!' '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), true, false, $5, $8, $9); }
     | '!'  tIDENTIFIER '?' '(' fargs ')' '=' literal block  { $$ = new zu::functiondefinition_node(LINE, new basic_type(0, basic_type::TYPE_VOID), new zu::identifier_node(LINE, $2), false, true, $5, $8, $9); }
     ;

type : '#'              { $$ = new basic_type(4, basic_type::TYPE_INT); }
     | '%'              { $$ = new basic_type(8, basic_type::TYPE_DOUBLE); }
     | '$'              { $$ = new basic_type(4, basic_type::TYPE_STRING); }
     | '<' type '>'     { basic_type *p = new basic_type(4, basic_type::TYPE_POINTER);
                            p->_subtype = $2;
                            ; $$ = p;}
     ;


block: '{' '}'                  { $$ = new zu::block_node(LINE, nullptr, nullptr); }
     | '{' varseqdecl '}'       { $$ = new zu::block_node(LINE, $2, nullptr); }
     | '{' list    '}'          { $$ = new zu::block_node(LINE, nullptr, $2); }
     | '{' varseqdecl list '}'  { $$ = new zu::block_node(LINE, $2, $3); }
     ;
     
list : stmt         { $$ = new cdk::sequence_node(LINE, $1); }
     | list stmt    { $$ = new cdk::sequence_node(LINE, $2, $1); }
     ;

stmt : expr ';'                         { $$ = new zu::evaluation_node(LINE, $1); }
     | expr tPRINTLN                    { $$ = new zu::print_node(LINE, $1, true); }
     | expr '!'                         { $$ = new zu::print_node(LINE, $1, false); }
     
/*     | stmt tPRINTLN                    { $$ = new zu::print_node(LINE, $1, true); }
     | stmt '!'                         { $$ = new zu::print_node(LINE, $1, false); }*/
     
     | tBREAK                           { $$ = new zu::break_node(LINE); }
     | tCONTINUE                        { $$ = new zu::continue_node(LINE); }
     | tRETURN                          { $$ = new zu::return_node(LINE); }
     | condition                        { $$ = $1; }
     | iteration                        { $$ = $1; }
     | block                            { $$ = $1; }
     //| /* epsilon */                    { $$ = nullptr; }
     /*| tIDENTIFIER '(' fargs ')' ';' { $$ = new zu::functioninvocation_node(LINE, new zu::identifier_node(LINE, $1), $3); }*/
     ;

condition : '[' expr ']' '#' stmt %prec xIF              { $$ = new zu::if_node(LINE, $2, $5); }
     | '[' expr ']' '?' stmt  %prec xIF         { $$ = new zu::if_node(LINE, $2, $5); }
     | '[' expr ']' '?' stmt ':' stmt           { $$ = new zu::if_else_node(LINE, $2, $5,$7); }
     ;

iteration : '[' vars ';' exprs ';' exprs ']' stmt   { $$ = new zu::for_node(LINE, $2, $4, $6, $8); }
     |  '['          ';' exprs ';' exprs ']' stmt   { $$ = new zu::for_node(LINE, nullptr, $3, $5, $7); }
     |  '['          ';'       ';' exprs ']' stmt   { $$ = new zu::for_node(LINE, nullptr, nullptr, $4, $6); }
     |  '['          ';' exprs ';'       ']' stmt   { $$ = new zu::for_node(LINE, nullptr, $3, nullptr, $6); }
     |  '['     vars ';' exprs ';'       ']' stmt   { $$ = new zu::for_node(LINE, $2, $4, nullptr, $7); }
     |  '['     exprs';' exprs ';' exprs ']' stmt   { $$ = new zu::for_node(LINE, $2, $4, $6, $8); }
     |  '['     exprs';'       ';'       ']' stmt   { $$ = new zu::for_node(LINE, $2, nullptr, nullptr, $6); }
     |  '['     vars ';'       ';'       ']' stmt   { $$ = new zu::for_node(LINE, $2, nullptr, nullptr, $6); }
     |  '['          ';'       ';'       ']' stmt   { $$ = new zu::for_node(LINE, nullptr, nullptr, nullptr, $5); }
     ;
     
exprs: expr             { $$ = new cdk::sequence_node(LINE, $1); }
     | expr ',' exprs   { $$ = new cdk::sequence_node(LINE, $1, $3); }
     ;


     
expr : literal                      { $$ = $1; }     
     |'-' expr %prec tUNARY         { $$ = new cdk::neg_node(LINE, $2); }
     | expr '+' expr	            { $$ = new cdk::add_node(LINE, $1, $3); }
     | expr '-' expr	            { $$ = new cdk::sub_node(LINE, $1, $3); }
     | expr '*' expr	            { $$ = new cdk::mul_node(LINE, $1, $3); }
     | expr '/' expr	            { $$ = new cdk::div_node(LINE, $1, $3); }
     | expr '%' expr	            { $$ = new cdk::mod_node(LINE, $1, $3); }
     | expr '<' expr	            { $$ = new cdk::lt_node(LINE, $1, $3); }
     | expr '>' expr	            { $$ = new cdk::gt_node(LINE, $1, $3); }
     | expr tGE expr	            { $$ = new cdk::ge_node(LINE, $1, $3); }
     | expr tLE expr                { $$ = new cdk::le_node(LINE, $1, $3); }
     | expr tNE expr	            { $$ = new cdk::ne_node(LINE, $1, $3); }
     | expr tEQ expr	            { $$ = new cdk::eq_node(LINE, $1, $3); }
     | expr '|' expr                { $$ = new zu::or_node(LINE, $1, $3);  }
     | expr '&' expr                { $$ = new zu::and_node(LINE, $1, $3); }
     | '~' expr                     { $$ = new zu::not_node(LINE, $2);     }
     | '+' expr %prec tUNARY        { $$ = new zu::identity_node(LINE, $2);}
     | lval '?'         { $$ = new zu::address_node(LINE, $1); }
     //resto das expressoes
     | '(' expr ')'                 { $$ = $2; }
     
     | '[' expr ']'               { $$ = new zu::alloc_node(LINE, $2); } 
     | lval                         { $$ = new zu::rvalue_node(LINE, $1); }  //FIXME*/
     | lval '=' expr                { $$ = new zu::assignment_node(LINE, $1, $3); }
     | tREAD                        { $$ = new zu::read_node(LINE); }
     
     | tIDENTIFIER '(' fargs ')'    { $$ = new zu::functioninvocation_node(LINE, new zu::identifier_node(LINE, $1), $3); }
     
     /*| expr '[' expr ']'            { $$ = new zu::index_node(LINE, $1, $3);  }*/
     ;
     
literal: tINTEGER               { $$ = new cdk::integer_node(LINE, $1); }
     | tREAL                    { $$ = new cdk::double_node(LINE, $1); }
     | lstring                  { $$ = new cdk::string_node(LINE, $1);  /*delete $1;*/ }
     ;

lval : tIDENTIFIER             { $$ = new zu::identifier_node(LINE, $1); }  //$$ = new zu::lvalue_node(LINE, $1); 
     | expr '['expr ']'        { $$ = new zu::index_node(LINE, $1, $3); }
     //| tIDENTIFIER '['expr ']'        { $$ = new zu::index_node(LINE, $1, $3); }
     ;

     
lstring : tSTRING			{ $$ = $1; }
        | lstring tSTRING		{ $$ = new std::string(*$1 + *$2); delete $1; delete $2; }
        ; 

%%
