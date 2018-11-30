// $Id: return_node.h,v 1.3 2016/03/17 16:15:38 ist179112 Exp $ -*- c++ -*-
#ifndef __ZU_AST_RETURNNODE_H__
#define __ZU_AST_RETURNNODE_H__

#include <cdk/ast/expression_node.h>

namespace zu {

  /**
   * Class for describing return nodes.
   */
  class return_node: public cdk::basic_node {

  public:
    /**
    * @param lineno source code line number for this node
    * @param return value of return
    */
    inline return_node(int lineno) :
        basic_node(lineno) {
    }

  public:
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_return_node(this, level);
    }

  };

} // zu

#endif
