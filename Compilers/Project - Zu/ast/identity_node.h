// $Id: identity_node.h,v 1.1 2016/03/17 21:04:09 ist179112 Exp $ -*- c++ -*-
#ifndef __ZU_AST_EXPRESSION_IDENTITY_H__
#define __ZU_AST_EXPRESSION_IDENTITY_H__

#include <cdk/ast/unary_expression_node.h>

namespace zu {

  /**
   * Class for describing the identity operator
   */
  class identity_node : public cdk::unary_expression_node {
  public:
    inline identity_node(int lineno, expression_node *arg) :
        unary_expression_node(lineno, arg) {
    }

    /**
     * @param sp semantic processor visitor
     * @param level syntactic tree level
     */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_identity_node(this, level);
    }

  };

} // zu

#endif
