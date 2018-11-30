// $Id: for_node.h,v 1.3 2016/03/17 15:36:35 ist179112 Exp $ -*- c++ -*-
#ifndef __ZU_AST_BASIC_FOR_H__
#define __ZU_AST_BASIC_FOR_H__

#include <cdk/ast/expression_node.h>

namespace zu {

  /**
   * Class for describing for-cycle nodes.
   */
  class for_node: public cdk::basic_node {
    cdk::sequence_node *_init;
    cdk::sequence_node *_condition;
    cdk::sequence_node *_increment;
    cdk::basic_node *_block;

  public:
    /**
     * @param lineno source code line number for this node
     * @param init declaration of var
     * @param condition do til condition verified
     * @param increment var declareted
    */
    inline for_node(int lineno, cdk::sequence_node *init, cdk::sequence_node *condition, cdk::sequence_node *increment, cdk::basic_node *block) :
        basic_node(lineno), _init(init), _condition(condition), _increment(increment), _block(block) {
    }

  public:
    inline cdk::sequence_node *condition() {
      return _condition;
    }
    inline cdk::basic_node *block() {
      return _block;
    }
    inline cdk::sequence_node *init() {
      return _init;
    } 
    inline cdk::sequence_node *increment(){
      return _increment;
    }
    /**
    * @param sp semantic processor visitor
    * @param level syntactic tree level
    */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_for_node(this, level);
    }

  };

} // zu

#endif
