// $Id: break_node.h,v 1.1 2016/03/16 22:18:40 ist179112 Exp $ -*- c++ -*-
#ifndef __ZU_AST_BREAK_H__
#define __ZU_AST_BREAK_H__

namespace zu {

  /**
   * Class for describing break nodes.
   */
  class break_node: public cdk::basic_node { 

  public:
    /**
    * @param lineno source code line number for this node
    */
    inline break_node(int lineno) :
        basic_node(lineno) {
    }

  public:
    /**
    * @param sp semantic processor visitor
    * @param level syntactic tree level
    */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_break_node(this, level);
    }

  };

} // zu

#endif
