// $Id: read_node.h,v 1.2 2016/04/11 14:12:14 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_READNODE_H__
#define __ZU_READNODE_H__


namespace zu {

  /**
   * Class for describing read nodes.
   */
  class read_node: public cdk::expression_node {

  public:
    inline read_node(int lineno) :
        cdk::expression_node(lineno) {
    }

  public:
  
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_read_node(this, level);
    }

  };

} // zu

#endif
