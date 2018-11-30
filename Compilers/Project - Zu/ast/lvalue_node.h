// $Id: lvalue_node.h,v 1.3 2016/04/12 16:33:51 ist179112 Exp $
#ifndef __ZU_NODE_EXPRESSION_LEFTVALUE_H__
#define __ZU_NODE_EXPRESSION_LEFTVALUE_H__


namespace zu {

  /**
   * Class for describing syntactic tree leaves for holding lvalues.
   */
  class lvalue_node: public cdk::expression_node {
	
	//CHANGED 	postfix_writter.cpp lines function(line 139) 
	//			type_checker.cpp function(line 96)
	//			xml_writer.cpp function(line 124)
      
  public:
    inline lvalue_node(int lineno) : 
        cdk::expression_node(lineno) {
    }

    
    /**
     * @param sp semantic processor visitor
     * @param level syntactic tree level
     */
    virtual void accept(basic_ast_visitor *sp, int level) {
      sp->do_lvalue_node(this, level);
    }

  };

} // zu

#endif
