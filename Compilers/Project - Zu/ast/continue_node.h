// $Id: continue_node.h,v 1.1 2016/03/17 15:16:57 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_AST_CONTINUE_H__
#define __ZU_AST_CONTINUE_H__

namespace zu {

  /**
   * Class for describing continue nodes.
   */
  class continue_node: public cdk::basic_node { 

  public:
    /**
    * @param lineno source code line number for this node
    */
    inline continue_node(int lineno) :
        basic_node(lineno) {
    }

  public:
    /**
    * @param sp semantic processor visitor
    * @param level syntactic tree level
    */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_continue_node(this, level);
    }

  };

} // zu

#endif
