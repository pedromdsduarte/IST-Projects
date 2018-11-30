// $Id: index_node.h,v 1.4 2016/04/15 00:22:20 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_INDEXNODE_H__
#define __ZU_INDEXNODE_H__

#include <cdk/ast/expression_node.h>
#include "ast/lvalue_node.h"
namespace zu {

  /**
   * Class for describing the index operator
   */
  class index_node : public zu::lvalue_node {
  public:
    cdk::expression_node *_pointer; 
    cdk::expression_node *_argument;
    /**
     * @param lineno source code line number for this node
     * @param pointer
     * @param argument is the index
     */
    inline index_node(int lineno, cdk::expression_node *pointer, cdk::expression_node *argument) :
        zu::lvalue_node(lineno), _pointer(pointer), _argument(argument) {
    }

  public:
    inline cdk::expression_node *pointer(){
      return _pointer;
    }

    inline cdk::expression_node *argument(){
      return _argument;
    }
    /**
     * @param sp semantic processor visitor
     * @param level syntactic tree level
     */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_index_node(this, level);
    }

  };

} // zu

#endif
