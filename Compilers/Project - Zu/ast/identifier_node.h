// $Id: identifier_node.h,v 1.4 2016/04/14 16:23:49 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_AST_EXPRESSION_IDENTIFIER_H__
#define __ZU_AST_EXPRESSION_IDENTIFIER_H__

#include <ast/lvalue_node.h>
#include <string>

namespace zu {

  /**
   * Class for describing syntactic tree leaves for holding identifier
   * values.
   */

  
  class identifier_node : public zu::lvalue_node {
      
        
    std::string _name;
      
  public:
      
    inline identifier_node(int lineno, std::string  *name) :
        lvalue_node(lineno), _name(*name) {
        
    }
      
    /*inline identifier_node(int lineno, const char *s) :
        simple_value_node<std::string>(lineno, s) {
    }
    inline identifier_node(int lineno, const std::string &s) :
        simple_value_node<std::string>(lineno, s) {
    }
    inline identifier_node(int lineno, const std::string *s) :
        simple_value_node<std::string>(lineno, *s) {
    }
    */
    
    inline std::string name() {
        return _name;
    }
    
    /**
     * @param sp semantic processor visitor
     * @param level syntactic tree level
     */
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_identifier_node(this, level);
    }

  };

} // cdk

#endif
