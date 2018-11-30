// $Id: type_checker.cpp,v 1.46 2016/05/19 23:39:26 ist178328 Exp $ -*- c++ -*-
#include <string>
#include "targets/type_checker.h"
#include "ast/all.h"  // automatically generated

#define ASSERT_UNSPEC \
    { if (node->type() != nullptr && \
          node->type()->name() != basic_type::TYPE_UNSPEC) return; }

//---------------------------------------------------------------------------

void zu::type_checker::do_integer_node(cdk::integer_node * const node, int lvl) {
  ASSERT_UNSPEC;
  node->type(new basic_type(4, basic_type::TYPE_INT));
}

void zu::type_checker::do_string_node(cdk::string_node * const node, int lvl) {
  ASSERT_UNSPEC;
  node->type(new basic_type(4, basic_type::TYPE_STRING));
}

void zu::type_checker::do_double_node(cdk::double_node * const node, int lvl) {
  ASSERT_UNSPEC;
  node->type(new basic_type(4, basic_type::TYPE_DOUBLE));
}



//---------------------------------------------------------------------------

inline void zu::type_checker::processUnaryExpression(cdk::unary_expression_node * const node, int lvl) {
  node->argument()->accept(this, lvl + 2);
  if (node->argument()->type()->name() != basic_type::TYPE_INT)
    throw std::string("wrong type in argument of unary expression");

  // in Zu, expressions are always int
  node->type(new basic_type(4, basic_type::TYPE_INT));
}

void zu::type_checker::do_neg_node(cdk::neg_node * const node, int lvl) {
  processUnaryExpression(node, lvl);
}

//---------------------------------------------------------------------------

inline void zu::type_checker::processBinaryExpression(cdk::binary_expression_node * const node, int lvl) {
  ASSERT_UNSPEC;
  std::cout << "Im here\n";
  node->left()->accept(this, lvl + 2);
  if (node->left()->type()->name() != basic_type::TYPE_INT)
    throw std::string("wrong type in left argument of binary expression");
  node->right()->accept(this, lvl + 2);
  if (node->right()->type()->name() != basic_type::TYPE_INT)
    throw std::string("wrong type in right argument of binary expression");

  // in Zu, expressions are always int
  node->type(new basic_type(4, basic_type::TYPE_INT));
}

inline void zu::type_checker::processBinaryIntRealExpression(cdk::binary_expression_node * const node, int lvl){
    //FEITO
    ASSERT_UNSPEC;
    
    node->left()->accept(this, lvl+2);
    if ((node->left()->type()->name() != basic_type::TYPE_INT) 
       && (node->left()->type()->name() != basic_type::TYPE_DOUBLE))
        throw std::string("wrong type in left argument of binary expression");
    
    node->right()->accept(this, lvl+2);
    if ((node->right()->type()->name() != basic_type::TYPE_INT) 
       && (node->right()->type()->name() != basic_type::TYPE_DOUBLE))
        throw std::string("wrong type in right argument of binary expression");
        
    if ((node->right()->type()->name() == basic_type::TYPE_DOUBLE)
        && (node->left()->type()->name() == basic_type::TYPE_DOUBLE))
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    else
        node->type(new basic_type(4, basic_type::TYPE_INT));
}

inline void zu::type_checker::processBinaryLogicalIntRealExpression(cdk::binary_expression_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    
    node->left()->accept(this, lvl+2);
    if ((node->left()->type()->name() != basic_type::TYPE_INT) 
       && (node->left()->type()->name() != basic_type::TYPE_DOUBLE))
        throw std::string("wrong type in left argument of logical binary expression");
    
    node->right()->accept(this, lvl+2);
    if ((node->right()->type()->name() != basic_type::TYPE_INT) 
       && (node->right()->type()->name() != basic_type::TYPE_DOUBLE))
        throw std::string("wrong type in right argument of logical binary expression");
        
    node->type(new basic_type(4, basic_type::TYPE_INT));
}

inline void zu::type_checker::processBinaryLogicalIRPExpression(cdk::binary_expression_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    node->left()->accept(this, lvl + 2);
    if( (node->left()->type()->name() != basic_type::TYPE_INT) &&
        (node->left()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->left()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in left argument of equal expression (must be int, double or pointer)");
    
    node->right()->accept(this, lvl + 2);
    if( (node->right()->type()->name() != basic_type::TYPE_INT) &&
        (node->right()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in right argument of equal expression (must be int, double or pointer)");
    
    node->type(new basic_type(4, basic_type::TYPE_INT));
}


    
/***********************************************************************************/


void zu::type_checker::do_add_node(cdk::add_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    node->left()->accept(this, lvl + 2);

    if( (node->left()->type()->name() != basic_type::TYPE_INT) &&
        (node->left()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->left()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in left argument of add expression (must be int, double or pointer)");
    
    node->right()->accept(this, lvl + 2);
    if( (node->right()->type()->name() != basic_type::TYPE_INT) &&
        (node->right()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in right argument of add expression (must be int, double or pointer)");
    
    // INT + INT
    if( (node->left()->type()->name() == basic_type::TYPE_INT) &&
        (node->right()->type()->name() == basic_type::TYPE_INT))
        node->type(new basic_type(4, basic_type::TYPE_INT));
    
    // DOUBLE + DOUBLE
    else if( (node->left()->type()->name() == basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() == basic_type::TYPE_DOUBLE))
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    
    // DOUBLE + INT  || INT  + DOUBLE
    else if( ((node->left()->type()->name() == basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() == basic_type::TYPE_INT)) ||
        ((node->left()->type()->name() == basic_type::TYPE_INT) &&
        (node->right()->type()->name() == basic_type::TYPE_DOUBLE))  )
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    
    // POINTER + INT || INT + POINTER
    else if( ((node->left()->type()->name() == basic_type::TYPE_POINTER) &&
        (node->right()->type()->name() == basic_type::TYPE_INT)) ||
        ((node->left()->type()->name() == basic_type::TYPE_INT) &&
        (node->right()->type()->name() == basic_type::TYPE_POINTER)) )
        node->type(new basic_type(4, basic_type::TYPE_POINTER));
    
    else
        throw std::string("cannot add left argument with right argument");
}
void zu::type_checker::do_sub_node(cdk::sub_node * const node, int lvl) {
    ASSERT_UNSPEC;
    node->left()->accept(this, lvl + 2);
    if( (node->left()->type()->name() != basic_type::TYPE_INT) &&
        (node->left()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->left()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in left argument of add expression (must be int, double or pointer)");
    
    node->right()->accept(this, lvl + 2);
    if( (node->right()->type()->name() != basic_type::TYPE_INT) &&
        (node->right()->type()->name() != basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() != basic_type::TYPE_POINTER))
        throw std::string("wrong type in right argument of add expression (must be int, double or pointer)");
    
    // INT - INT
    if( (node->left()->type()->name() == basic_type::TYPE_INT) &&
        (node->right()->type()->name() == basic_type::TYPE_INT))
        node->type(new basic_type(4, basic_type::TYPE_INT));
    
    // DOUBLE - DOUBLE
    else if( (node->left()->type()->name() == basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() == basic_type::TYPE_DOUBLE))
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    
    // DOUBLE - INT  || INT  - DOUBLE
    else if( ((node->left()->type()->name() == basic_type::TYPE_DOUBLE) &&
        (node->right()->type()->name() == basic_type::TYPE_INT)) ||
        ((node->left()->type()->name() == basic_type::TYPE_INT) &&
        (node->right()->type()->name() == basic_type::TYPE_DOUBLE))  )
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    
    // POINTER - INT || INT - POINTER
    else if((node->left()->type()->name() == basic_type::TYPE_POINTER) &&
        (node->right()->type()->name() == basic_type::TYPE_INT))
        node->type(new basic_type(4, basic_type::TYPE_INT));
    
    else
        throw std::string("cannot sub left argument with right argument");
}
void zu::type_checker::do_mul_node(cdk::mul_node * const node, int lvl) {
    //FEITO
  processBinaryIntRealExpression(node, lvl);
}
void zu::type_checker::do_div_node(cdk::div_node * const node, int lvl) {
    //FEITO
  processBinaryIntRealExpression(node, lvl);
}
void zu::type_checker::do_mod_node(cdk::mod_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    
    node->left()->accept(this, lvl+2);
    if (node->left()->type()->name() != basic_type::TYPE_INT)
        throw std::string("wrong type in left argument of binary expression");
    
    node->right()->accept(this, lvl+2);
    if (node->right()->type()->name() != basic_type::TYPE_INT)
        throw std::string("wrong type in right argument of binary expression");
        
    node->type(new basic_type(4, basic_type::TYPE_INT));
}

/***********************************************************************************/

void zu::type_checker::do_lt_node(cdk::lt_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIntRealExpression(node, lvl);
}
void zu::type_checker::do_le_node(cdk::le_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIntRealExpression(node, lvl);
}
void zu::type_checker::do_ge_node(cdk::ge_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIntRealExpression(node, lvl);
}
void zu::type_checker::do_gt_node(cdk::gt_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIntRealExpression(node, lvl);
}

/***********************************************************************************/

void zu::type_checker::do_ne_node(cdk::ne_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIRPExpression(node, lvl);
}
void zu::type_checker::do_eq_node(cdk::eq_node * const node, int lvl) {
    //FEITO
    processBinaryLogicalIRPExpression(node, lvl);
}

/***********************************************************************************/

//---------------------------------------------------------------------------

void zu::type_checker::do_rvalue_node(zu::rvalue_node * const node, int lvl) {
  node->lvalue()->accept(this, lvl);
  node->type(node->lvalue()->type());
}

//---------------------------------------------------------------------------

void zu::type_checker::do_lvalue_node(zu::lvalue_node * const node, int lvl) {
    ASSERT_UNSPEC;
    const std::string &id = node->name();
    std::shared_ptr<zu::symbol> symbol = _symtab.find(id);
    if(symbol.get() == NULL)
        throw node->name() + " not declared";
    
    if(symbol.get()->type()->name() == basic_type::TYPE_INT){
        node->type(new basic_type(4, basic_type::TYPE_INT));
    }else if(symbol.get()->type()->name() == basic_type::TYPE_DOUBLE){
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
    }else if(symbol.get()->type()->name() == basic_type::TYPE_POINTER){
        node->type(new basic_type(4, basic_type::TYPE_POINTER));
    }else if(symbol.get()->type()->name() == basic_type::TYPE_STRING){
        node->type(new basic_type(4, basic_type::TYPE_STRING));
    }else{
        throw std::string("lvalue type is not correct");
    }
}

//---------------------------------------------------------------------------

void zu::type_checker::do_assignment_node(zu::assignment_node * const node, int lvl) {
  //TODO: Finished?
  ASSERT_UNSPEC;

  // DAVID: horrible hack!
  // (this is caused by Zu not having explicit variable declarations)
 /* const std::string &id = node->lvalue()->value();
  if (!_symtab.find(id)) {
    _symtab.insert(id, std::make_shared<zu::symbol>(new basic_type(4, basic_type::TYPE_INT), id, -1)); // put in the symbol table
  }
*/

    node->lvalue()->accept(this, lvl+2);
    node->rvalue()->accept(this, lvl+2);


    
    //Tipos invalidos
    if((node->lvalue()->type()->name() == basic_type::TYPE_VOID)
	|| (node->rvalue()->type()->name() == basic_type::TYPE_VOID)
	|| (node->lvalue()->type()->name() == basic_type::TYPE_ERROR)
	|| (node->rvalue()->type()->name() == basic_type::TYPE_ERROR)
	|| (node->lvalue()->type()->name() == basic_type::TYPE_UNSPEC)) {
		node->type(new basic_type(0, basic_type::TYPE_ERROR));
		return;
    }
    
    //Para read e alloc
    else if (node->rvalue()->type()->name() == basic_type::TYPE_UNSPEC)
        node->type(node->lvalue()->type());
    
    //Conversao: [double] d = 2; 
    else if((node->lvalue()->type()->name() == basic_type::TYPE_DOUBLE)
	 && (node->rvalue()->type()->name() == basic_type::TYPE_INT)) {
            node->type(node->lvalue()->type());
    }

    //Ponteiros = 0
    else if((node->lvalue()->type()->name() == basic_type::TYPE_POINTER)
	 && (node->rvalue()->type()->name() == basic_type::TYPE_INT)
	 && (((cdk::simple_value_node<int>*)node->rvalue())->value() == 0)) {
		node->type(node->lvalue()->type());
    }
    
    //Right values podem nao estar especificados?
    else if(node->rvalue()->type()->name() == basic_type::TYPE_UNSPEC)
        node->type(node->lvalue()->type());
    
    //Tipos iguais
    else if (node->lvalue()->type()->name() == node->rvalue()->type()->name())
       node->type(node->lvalue()->type());
    else {
        node->type(new basic_type(0, basic_type::TYPE_ERROR));
	return;
    }
}

//---------------------------------------------------------------------------

void zu::type_checker::do_evaluation_node(zu::evaluation_node * const node, int lvl) {
    //FEITO
  node->argument()->accept(this, lvl + 2);
}

void zu::type_checker::do_print_node(zu::print_node * const node, int lvl) {
    //FEITO
    node->argument()->accept(this, lvl + 2);
    if (node->argument()->type()->name() == basic_type::TYPE_POINTER)
        throw std::string("cannot print pointers");
    /*else
        node->type(node->argument()->type());*/
        
}

//---------------------------------------------------------------------------

void zu::type_checker::do_read_node(zu::read_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    node->type(new basic_type(0, basic_type::TYPE_UNSPEC));
}

//---------------------------------------------------------------------------
/*
void zu::type_checker::do_while_node(zu::while_node * const node, int lvl) {
  node->condition()->accept(this, lvl + 4);
}
*/
//---------------------------------------------------------------------------

void zu::type_checker::do_if_node(zu::if_node * const node, int lvl) {
    //FEITO
  node->condition()->accept(this, lvl + 4);
  node->block()->accept(this, lvl+4);
}

void zu::type_checker::do_if_else_node(zu::if_else_node * const node, int lvl) {
    //FEITO
  node->condition()->accept(this, lvl + 4);
  node->thenblock()->accept(this, lvl + 4);
  node->elseblock()->accept(this, lvl + 4);
}

void zu::type_checker::do_and_node(zu::and_node * const node, int lvl) {
    processBinaryExpression(node, lvl);
}

void zu::type_checker::do_not_node(zu::not_node * const node, int lvl) {

    processUnaryExpression(node, lvl);

}

void zu::type_checker::do_or_node(zu::or_node * const node, int lvl) {
    processBinaryExpression(node, lvl);
}

void zu::type_checker::do_block_node(zu::block_node * const node, int lvl) {
    //FEITO
    node->declarations()->accept(this, lvl+2);
    node->instructions()->accept(this, lvl+2);
}

void zu::type_checker::do_for_node(zu::for_node * const node, int lvl) {
    //FEITO
    node->init()->accept(this , lvl + 2);
    node->condition()->accept(this , lvl + 2);
    node->increment()->accept(this , lvl + 2);
    node->block()->accept(this , lvl + 2);
}

void zu::type_checker::do_break_node(zu::break_node * const node, int lvl) {
    //FEITO
}
void zu::type_checker::do_return_node(zu::return_node * const node, int lvl) {
    //FEITO
}
void zu::type_checker::do_continue_node(zu::continue_node * const node, int lvl) {
    //FEITO
}
void zu::type_checker::do_functioninvocation_node(zu::functioninvocation_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    const std::string &id = node->identifier()->name();
    std::shared_ptr<zu::symbol> symbol = _symtab.find(id);
    if(symbol.get() == NULL)
        throw node->name() + " not declared";
    node->type(new basic_type(symbol.get()->type()->size(), symbol.get()->type()->name()));
    
    if(node->args() != NULL)
        node->args()->accept(this, lvl);
    
    
}
void zu::type_checker::do_functiondeclaration_node(zu::functiondeclaration_node * const node, int lvl) {
   //FIXME
}
void zu::type_checker::do_functiondefinition_node(zu::functiondefinition_node * const node, int lvl) {
   //XXX
}
void zu::type_checker::do_declaration_node(zu::declaration_node * const node, int lvl){
    //Probably not done...
    ASSERT_UNSPEC;
    if(node->value() != NULL) {
        node->value()->accept(this, lvl+1);
    }
    
}

void zu::type_checker::do_address_node(zu::address_node * const node, int lvl) {

    //FEITO
    node->argument()->accept(this, lvl + 2);
    node->type(new basic_type(4, basic_type::TYPE_POINTER));
    
}

void zu::type_checker::do_index_node(zu::index_node * const node, int lvl) {
    //FEITO +/-
    node->pointer()->accept(this, lvl + 2);
    if( node->pointer()->type()->name() != basic_type::TYPE_POINTER)
        throw std::string("pointer is not type of pointer!");
    node->argument()->accept(this, lvl + 2);
    if( node->argument()->type()->name() != basic_type::TYPE_INT)
        throw std::string("type of index must be integer");

    //NEEDS SYMBOL_TABLE
    node->type(node->type());
}
void zu::type_checker::do_identity_node(zu::identity_node * const node, int lvl) {
    //FEITO
    node->argument()->accept(this, lvl + 2);
    if( (node->argument()->type()->name() != basic_type::TYPE_INT) &&
        (node->argument()->type()->name() != basic_type::TYPE_DOUBLE) )
        throw std::string("type of identity expression must be integer or real");
    
    else if (node->argument()->type()->name() == basic_type::TYPE_INT)
        node->type(new basic_type(4, basic_type::TYPE_INT));
    else if (node->argument()->type()->name() == basic_type::TYPE_DOUBLE)
        node->type(new basic_type(8, basic_type::TYPE_DOUBLE));
}

void zu::type_checker::do_alloc_node(zu::alloc_node * const node, int lvl) {
    //FEITO
    ASSERT_UNSPEC;
    node->argument()->accept(this, lvl);
    if (node->argument()->type()->name() != basic_type::TYPE_INT)
        throw std::string("argument of allocation must be of type integer");
    node->type(new basic_type(4, basic_type::TYPE_POINTER));
}

void zu::type_checker::do_identifier_node(zu::identifier_node * const node, int lvl) {
   //FEITO
    std::shared_ptr<zu::symbol> symbol = _symtab.find(node->name());
    if (symbol.get() == NULL)
        throw node->name() + " was not declared";
    if ((symbol.get()->type()->name() == basic_type::TYPE_INT) ||
        (symbol.get()->type()->name() == basic_type::TYPE_DOUBLE) ||
        (symbol.get()->type()->name() == basic_type::TYPE_STRING) ||
        (symbol.get()->type()->name() == basic_type::TYPE_POINTER))
        node->type(symbol.get()->type());
    else
        throw std::string("wrong type in left value");
}

void zu::type_checker::do_sequence_node(cdk::sequence_node * const node, int lvl) {
    //FEITO
    for (size_t i = 0; i < node->size(); i++)
          node->node(i)->accept(this, lvl);
}