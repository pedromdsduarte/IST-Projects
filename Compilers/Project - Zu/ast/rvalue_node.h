// $Id: rvalue_node.h,v 1.2 2016/04/14 16:23:49 ist178328 Exp $
#ifndef __ZU_RVALUE_NODE_H__
#define __ZU_RVALUE_NODE_H__

#include <cdk/ast/expression_node.h>
#include "ast/lvalue_node.h"

namespace zu {

  /**
   * Class for describing rvalue nodes.
   */
  class rvalue_node: public cdk::expression_node {
    lvalue_node *_lvalue;

  public:
    inline rvalue_node(int lineno, zu::lvalue_node *lvalue) :
        cdk::expression_node(lineno), _lvalue(lvalue) {
    }

  public:
    inline zu::lvalue_node *lvalue() {
      return _lvalue;
    }

    void accept(basic_ast_visitor *sp, int level) {
      sp->do_rvalue_node(this, level);
    }

  };

} // zu

#endif
