// $Id: print_node.h,v 1.3 2016/04/14 22:56:51 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_PRINTNODE_H__
#define __ZU_PRINTNODE_H__

#include <cdk/ast/expression_node.h>

namespace zu {

  /**
   * Class for describing print nodes.
   */
  class print_node: public cdk::expression_node {
    cdk::expression_node *_argument;
    bool _newline;

  public:
    inline print_node(int lineno, cdk::expression_node *argument, bool newline) :
        cdk::expression_node(lineno), _argument(argument), _newline(newline) {
    }

  public:
    inline cdk::expression_node *argument() {
      return _argument;
    }
    
    inline bool newline() {
        return _newline;
    }

    void accept(basic_ast_visitor *sp, int level) {
      sp->do_print_node(this, level);
    }

  };

} // zu

#endif
