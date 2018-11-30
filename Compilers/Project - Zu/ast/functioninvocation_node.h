// $Id: functioninvocation_node.h,v 1.3 2016/04/14 22:50:34 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_FUNCTIONINVOCATIONNODE_H__
#define __ZU_FUNCTIONINVOCATIONNODE_H__

#include <cdk/ast/expression_node.h>
//#include <ast/identifier_node.h>

namespace zu {

  /**
   * Class for describing invocation function nodes.
   */
  class functioninvocation_node: public cdk::expression_node {
    zu::identifier_node *_identifier;
    cdk::sequence_node *_args;

  public:
    inline functioninvocation_node(int lineno, zu::identifier_node *identifier, cdk::sequence_node *args) :
        expression_node(lineno), _identifier(identifier), _args(args) {
    }

  public:
  inline zu::identifier_node *identifier() {
      return _identifier;
    }

    inline cdk::sequence_node *args() {
      return _args;
    }

    void accept(basic_ast_visitor *sp, int level) {
      sp->do_functioninvocation_node(this, level);
    }

  };

} // zu

#endif
