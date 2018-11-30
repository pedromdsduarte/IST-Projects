// $Id: address_node.h,v 1.1 2016/03/17 19:39:54 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_AST_EXPRESSION_ADDRESS_H__
#define __ZU_AST_EXPRESSION_ADDRESS_H__

#include <cdk/ast/unary_expression_node.h>

namespace zu {

  /**
   * Class for describing the addressing operator
   */
  class address_node : public cdk::unary_expression_node {
  public:
    inline address_node(int lineno, expression_node *leftv) :
        unary_expression_node(lineno, leftv) {
    }

    /**
     * @param sp semantic processor visitor
     * @param level syntactic tree level
     */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_address_node(this, level);
    }

  };

} // zu

#endif
