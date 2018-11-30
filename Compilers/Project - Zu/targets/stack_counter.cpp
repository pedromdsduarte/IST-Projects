// $Id: stack_counter.cpp,v 1.2 2016/05/18 11:27:56 ist179112 Exp $ -*- c++ -*-
#include <string>
#include <sstream>
#include "targets/type_checker.h"
#include "targets/stack_counter.h"
#include "ast/all.h"  // all.h is automatically generated

//---------------------------------------------------------------------------
//     THIS IS THE VISITOR'S DEFINITION
//---------------------------------------------------------------------------

void zu::stack_counter::do_sequence_node(cdk::sequence_node * const node, int lvl) {
  for (size_t i = 0; i < node->size(); i++) {
    node->node(i)->accept(this, lvl);
  }
}

//---------------------------------------------------------------------------

void zu::stack_counter::do_integer_node(cdk::integer_node * const node, int lvl) {

    
}

void zu::stack_counter::do_string_node(cdk::string_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_neg_node(cdk::neg_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_add_node(cdk::add_node * const node, int lvl) {

}
void zu::stack_counter::do_sub_node(cdk::sub_node * const node, int lvl) {

}
void zu::stack_counter::do_mul_node(cdk::mul_node * const node, int lvl) {

}
void zu::stack_counter::do_div_node(cdk::div_node * const node, int lvl) {

}
void zu::stack_counter::do_mod_node(cdk::mod_node * const node, int lvl) {

}
void zu::stack_counter::do_lt_node(cdk::lt_node * const node, int lvl) {

}
void zu::stack_counter::do_le_node(cdk::le_node * const node, int lvl) {

}
void zu::stack_counter::do_ge_node(cdk::ge_node * const node, int lvl) {

}
void zu::stack_counter::do_gt_node(cdk::gt_node * const node, int lvl) {

}
void zu::stack_counter::do_ne_node(cdk::ne_node * const node, int lvl) {

}
void zu::stack_counter::do_eq_node(cdk::eq_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_rvalue_node(zu::rvalue_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_lvalue_node(zu::lvalue_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_assignment_node(zu::assignment_node * const node, int lvl) {

}


void zu::stack_counter::do_evaluation_node(zu::evaluation_node * const node, int lvl) {

}

void zu::stack_counter::do_print_node(zu::print_node * const node, int lvl) {

}

//---------------------------------------------------------------------------

void zu::stack_counter::do_read_node(zu::read_node * const node, int lvl) {

}

void zu::stack_counter::do_if_node(zu::if_node * const node, int lvl) {
    if(node->block() != NULL) {
        node->block()->accept(this, lvl+2);
    }
}

//---------------------------------------------------------------------------

void zu::stack_counter::do_if_else_node(zu::if_else_node * const node, int lvl) {
    if(node->thenblock() != NULL) {
        node->thenblock()->accept(this, lvl+2);
    }
    if(node->elseblock() != NULL) {
        node->elseblock()->accept(this, lvl+2);
    }
}

//---------------------------------------------------------------------------

void zu::stack_counter::do_and_node(zu::and_node * const node, int lvl) {
    //FIXME
}

void zu::stack_counter::do_not_node(zu::not_node * const node, int lvl) {
  //FIXME
}

void zu::stack_counter::do_or_node(zu::or_node * const node, int lvl) {
    //FIXME
}

void zu::stack_counter::do_block_node(zu::block_node * const node, int lvl) {
    if(node->declarations() != NULL) {
        node->declarations()->accept(this, lvl+2);
    }
    if(node->instructions() != NULL) {
	// We could have nested blocks with declarations
	// We must account for all of them
	node->instructions()->accept(this, lvl+2);
    }
}

void zu::stack_counter::do_for_node(zu::for_node * const node, int lvl) {
    //em principio, so e preciso visitar init e block...
    if(node->init() != NULL) {
        node->init()->accept(this, lvl+2);
    }
    if(node->block() != NULL) {
        node->block()->accept(this, lvl+2);
    }
}

void zu::stack_counter::do_break_node(zu::break_node * const node, int lvl) {
    //XXX
}
void zu::stack_counter::do_return_node(zu::return_node * const node, int lvl) {
    //XXX
}

void zu::stack_counter::do_continue_node(zu::continue_node * const node, int lvl) {
    //FIXME
}
void zu::stack_counter::do_functioninvocation_node(zu::functioninvocation_node * const node, int lvl) {
    // If function call has arguments
	if(node->args() != NULL) {
		node->args()->accept(this, lvl+2);
	}
	
}

void zu::stack_counter::do_functiondeclaration_node(zu::functiondeclaration_node * const node, int lvl) {
    //FIXME
}

void zu::stack_counter::do_functiondefinition_node(zu::functiondefinition_node * const node, int lvl) {
    
        
    
        // space required for the return variable
	if(node->type()->name() != basic_type::TYPE_VOID) {
		_size += node->type()->size();
	}
        


        node->block()->accept(this, lvl+2);

}


void zu::stack_counter::do_address_node(zu::address_node * const node, int lvl) {
    //FIXME
}

void zu::stack_counter::do_index_node(zu::index_node * const node, int lvl) {
    //XXX
}
void zu::stack_counter::do_identity_node(zu::identity_node * const node, int lvl) {
    //XXX
}

void zu::stack_counter::do_alloc_node(zu::alloc_node * const node, int lvl) {
    //FIXME
}


void zu::stack_counter::do_declaration_node(zu::declaration_node * const node, int lvl){
    //std::cout << "[@@@ STACK_COUNTER @@@] IM HERE: " << node->name() << std::endl;
    _size += node->type()->size();
}

void zu::stack_counter::do_identifier_node(zu::identifier_node * const node, int lvl){
    //XXX
}
