// $Id: xml_writer.cpp,v 1.30 2016/05/16 16:38:47 ist179112 Exp $ -*- c++ -*-
#include <string>
#include "targets/xml_writer.h"
#include "targets/type_checker.h"
#include "ast/all.h"  // automatically generated

//---------------------------------------------------------------------------

void zu::xml_writer::do_sequence_node(cdk::sequence_node * const node, int lvl) {
  if(node->node(0) == nullptr)
    os() << std::string(lvl, ' ') << "<sequence_node size='0'>" << std::endl;
  else{
  os() << std::string(lvl, ' ') << "<sequence_node size='" << node->size() << "'>" << std::endl;
  for (size_t i = 0; i < node->size(); i++)
    node->node(i)->accept(this, lvl + 2);
  }closeTag(node, lvl);
  
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_integer_node(cdk::integer_node * const node, int lvl) {
  processSimple(node, lvl);
}

void zu::xml_writer::do_string_node(cdk::string_node * const node, int lvl) {
  processSimple(node, lvl);
}

//---------------------------------------------------------------------------

inline void zu::xml_writer::processUnaryExpression(cdk::unary_expression_node * const node, int lvl) {
 // CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
  node->argument()->accept(this, lvl + 2);
  closeTag(node, lvl);
}

void zu::xml_writer::do_neg_node(cdk::neg_node * const node, int lvl) {
  processUnaryExpression(node, lvl);
}

void zu::xml_writer::do_not_node(zu::not_node * const node, int lvl) {
  processUnaryExpression(node, lvl);
}

void zu::xml_writer::do_and_node(zu::and_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}

void zu::xml_writer::do_or_node(zu::or_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_for_node(zu::for_node * const node, int lvl) {

  openTag(node, lvl);
    
    openTag("init", lvl + 2);
        if(node->init() == nullptr)
            os() << std::string(lvl+2 , ' ') << "default" << std::endl;
        else
            node->init()->accept(this, lvl + 4);
    closeTag("init", lvl + 2);
    
    openTag("condition", lvl + 2);
        if(node->init() == nullptr)
            os() << std::string(lvl+2 , ' ') << "default" << std::endl;
        else
            node->condition()->accept(this, lvl + 4);
    closeTag("condition", lvl + 2);
    
    openTag("increment", lvl + 2);
        if(node->init() == nullptr)
            os() << std::string(lvl+2 , ' ') << "default" << std::endl;
        else
            node->increment()->accept(this, lvl + 4);
    closeTag("increment", lvl + 2);
    
    if(node->block() == nullptr){
        openTag("block='empty'",lvl + 2);
        closeTag("block", lvl + 2);         
    }else{
        node->block()->accept(this, lvl + 2);
    }
         
  closeTag(node, lvl);
}

void zu::xml_writer::do_break_node(zu::break_node * const node, int lvl) {
    openTag("break", lvl);
        node->accept(this,lvl + 2);
    closeTag("break", lvl);
}

void zu::xml_writer::do_return_node(zu::return_node * const node, int lvl) {
    openTag("return", lvl);
        node->accept(this,lvl + 2);
    closeTag("return", lvl);
}

void zu::xml_writer::do_functioninvocation_node(zu::functioninvocation_node * const node, int lvl) {
    //FIXME
    openTag("function_invocation", lvl);
    
   /* openTag("identifier", lvl+2);
    node->identifier()->accept(this, lvl+4);
    closeTag("identifier", lvl+2);
    */
    
   node->args()->accept(this, lvl + 2);
   
   closeTag("function_invocation", lvl);
}

void zu::xml_writer::do_continue_node(zu::continue_node * const node, int lvl) {
    openTag("continue",lvl);
        node->accept(this,lvl + 2);
    closeTag("continue", lvl);
}
//---------------------------------------------------------------------------

inline void zu::xml_writer::processBinaryExpression(cdk::binary_expression_node * const node, int lvl) {
 // CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
  node->left()->accept(this, lvl + 2);
  node->right()->accept(this, lvl + 2);
  closeTag(node, lvl);
}

void zu::xml_writer::do_add_node(cdk::add_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_sub_node(cdk::sub_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_mul_node(cdk::mul_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_div_node(cdk::div_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_mod_node(cdk::mod_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_lt_node(cdk::lt_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_le_node(cdk::le_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_ge_node(cdk::ge_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_gt_node(cdk::gt_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_ne_node(cdk::ne_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}
void zu::xml_writer::do_eq_node(cdk::eq_node * const node, int lvl) {
  processBinaryExpression(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_rvalue_node(zu::rvalue_node * const node, int lvl) {
 // CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
    node->lvalue()->accept(this, lvl + 4);
  closeTag(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_lvalue_node(zu::lvalue_node * const node, int lvl) {
//  CHECK_TYPES(_compiler, _symtab, node);
 // processSimple(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_assignment_node(zu::assignment_node * const node, int lvl) {
 // CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
    node->lvalue()->accept(this, lvl + 2);
    openTag("rvalue", lvl + 2);
     node->rvalue()->accept(this, lvl + 4);
    closeTag("rvalue", lvl + 2);
  closeTag(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_block_node(zu::block_node * const node, int lvl) {
  openTag(node,lvl);
	if(node->declarations() == nullptr){
		openTag("declarations = 'empty'",lvl+2);
		closeTag("declarations",lvl+2);
	}else{
		openTag("declarations",lvl+2);
			node->declarations()->accept(this, lvl + 4);
		closeTag("declarations",lvl+2);
	}
	if(node->instructions() == nullptr){
		openTag("instructions = 'empty'",lvl+2);
		closeTag("instructions",lvl+2);
	}else{
		openTag("instructions",lvl+2);
		node->instructions()->accept(this, lvl + 4);	
		closeTag("instructions",lvl+2);
	}
  closeTag(node,lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_functiondeclaration_node(zu::functiondeclaration_node * const node, int lvl) {
 /* openTag(node,lvl);
	//node->type();
	node->identifier()->accept(this, lvl+2);
	node->arguments()->accept(this, lvl+2);
	node->literal()->accept(this, lvl+2);
  closeTag(node,lvl);*/
}

void zu::xml_writer::do_functiondefinition_node(zu::functiondefinition_node *const node, int lvl) {
  openTag(node,lvl);
    os() << std::string(lvl + 2, ' ') << "<type> " << type_string(node->type()) << " </type>" << std::endl;
	
	node->identifier()->accept(this, lvl+2);
	if(node->arguments() == nullptr){
		openTag("args ='empty'",lvl+2);
		closeTag("args",lvl+2);
	}else{
		openTag("args",lvl+2);
			node->arguments()->accept(this,lvl+4);
		closeTag("args",lvl+2);
	}
	node->literal()->accept(this,lvl+2);
	
	if(node->block() == nullptr){
		openTag("block='empty'",lvl+2);
		closeTag("block",lvl+2);
	}else{
		node->block()->accept(this,lvl+4);
        }
  closeTag(node,lvl);
}

void zu::xml_writer::do_declaration_node(zu::declaration_node * const node, int lvl){
  openTag(node,lvl);
	os() << std::string(lvl + 2, ' ') << "<type> " << type_string(node->type()) << " </type>" << std::endl;
	node->identifier()->accept(this, lvl + 2);
	os() << std::string(lvl + 2, ' ') << "<name> " << node->name() << " </name>" << std::endl;
	if(node->value() == nullptr)
		;
	else{
		openTag("value",lvl + 2);
			node->value()->accept(this,lvl+4);
		closeTag("value",lvl + 2);
	}
  closeTag(node,lvl);
}

//---------------------------------------------------------------------------
/*
void zu::xml_writer::do_program_node(zu::program_node * const node, int lvl) {
  openTag(node, lvl);
  node->statements()->accept(this, lvl + 4);
  closeTag(node, lvl);
}
*/
//---------------------------------------------------------------------------

void zu::xml_writer::do_evaluation_node(zu::evaluation_node * const node, int lvl) {
  //CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
  node->argument()->accept(this, lvl + 2);
  closeTag(node, lvl);
}

void zu::xml_writer::do_print_node(zu::print_node * const node, int lvl) {
  //CHECK_TYPES(_compiler, _symtab, node);
  openTag(node, lvl);
  node->argument()->accept(this, lvl + 2);
  closeTag(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_read_node(zu::read_node * const node, int lvl) {
  openTag("read", lvl);
	node->accept(this, lvl + 2);
  closeTag("read", lvl);
}

//---------------------------------------------------------------------------
/*
void zu::xml_writer::do_while_node(zu::while_node * const node, int lvl) {
  openTag(node, lvl);
  openTag("condition", lvl + 2);
  node->condition()->accept(this, lvl + 4);
  closeTag("condition", lvl + 2);
  openTag("block", lvl + 2);
  node->block()->accept(this, lvl + 4);
  closeTag("block", lvl + 2);
  closeTag(node, lvl);
}
*/
//---------------------------------------------------------------------------

void zu::xml_writer::do_if_node(zu::if_node * const node, int lvl) {
  openTag(node, lvl);
    openTag("condition", lvl + 2);
        node->condition()->accept(this, lvl + 4);
    closeTag("condition", lvl + 2);
    openTag("then", lvl + 2);
        if(node->block() == nullptr){
            openTag("block='empty'",lvl+4);
	    closeTag("block",lvl+4);
	}else{    
            node->block()->accept(this, lvl + 4);
        }
    closeTag("then", lvl + 2);
  closeTag(node, lvl);
}

void zu::xml_writer::do_if_else_node(zu::if_else_node * const node, int lvl) {
  openTag(node, lvl);
    openTag("condition", lvl + 2);
        node->condition()->accept(this, lvl + 4);
    closeTag("condition", lvl + 2);
    openTag("then", lvl + 2);
        if(node->thenblock() == nullptr){
		openTag("block='empty'",lvl+4);
		closeTag("block",lvl+4);
	}else{
            node->thenblock()->accept(this, lvl + 4);
        }
    closeTag("then", lvl + 2);
    openTag("else", lvl + 2);
        if(node->elseblock() == nullptr){
            openTag("block='empty'",lvl+4);
            closeTag("block",lvl+4);
	}else{
            node->elseblock()->accept(this, lvl + 4);
        }
    closeTag("else", lvl + 2);
  closeTag(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_address_node(zu::address_node * const node, int lvl) {
	processUnaryExpression(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_index_node(zu::index_node * const node, int lvl) {
  openTag(node, lvl);
	node->pointer()->accept(this, lvl + 2);
	node->argument()->accept(this, lvl + 2);
  closeTag(node, lvl);
}

void zu::xml_writer::do_identity_node(zu::identity_node * const node, int lvl) {
	processUnaryExpression(node, lvl);
}

//---------------------------------------------------------------------------

void zu::xml_writer::do_alloc_node(zu::alloc_node * const node, int lvl) {
	processUnaryExpression(node, lvl);
}


void zu::xml_writer::do_identifier_node(zu::identifier_node * const node, int lvl) {
  openTag(node, lvl);
	os() << std::string(lvl+2 , ' ') << "<identifier name='" << node->name() << "'>" << std::endl;
  closeTag(node,lvl);
}

