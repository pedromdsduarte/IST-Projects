// $Id: declaration_node.h,v 1.9 2016/04/14 22:50:34 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_DECLARATIONNODE_H__
#define __ZU_DECLARATIONNODE_H__

#include <cdk/ast/basic_node.h>
#include <cdk/ast/expression_node.h>
#include <cdk/basic_type.h>
#include "ast/identifier_node.h"
#include <string>

namespace zu {

  /**
   * Class for describing declaration nodes.
   */
  class declaration_node: public cdk::basic_node {
    basic_type *_type;
    zu::identifier_node *_identifier;
    std::string *_name;
    bool _public;
    bool _external;
    cdk::expression_node *_value;

    

  public:
    /*inline declaration_node(int lineno, basic_type *type, zu::identifier_node *identifier) :
        basic_node(lineno), _type(type), _identifier(identifier) {
    }*/
    
    inline declaration_node(int lineno, basic_type *type, zu::identifier_node *identifier, bool isPublic, bool external) :
        basic_node(lineno), _type(type), _identifier(identifier), _public(isPublic), _external(external) {
    }
    
    inline declaration_node(int lineno, basic_type *type, zu::identifier_node *identifier, bool isPublic, bool external, cdk::expression_node *value) :
        basic_node(lineno), _type(type), _identifier(identifier), _public(isPublic), _external(external), _value(value) {
    }

    
    /*inline declaration_node(int lineno, basic_type *type, std::string *name, bool isPublic, bool external, cdk::expression_node *value) :
        basic_node(lineno), _type(type), _name(name), _public(isPublic), _external(external), _value(value) {
    }
    
    inline declaration_node(int lineno, basic_type *type, std::string *name, bool isPublic, bool external) :
        basic_node(lineno), _type(type), _name(name), _public(isPublic), _external(external) {
    }*/
    
    
  public:
    inline basic_type *type() {
      return _type;
    }
    
    inline zu::identifier_node *identifier() {
      return _identifier;
    }
    
    inline bool isPublic() {
      return _public;
    }
    
    inline bool external() {
      return _external;
    }
    
    inline std::string *name() {
        return _name;
    }
    
    inline cdk::expression_node *value() {
        return _value;
    }
    
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_declaration_node(this, level);
    }

  };

} // zu

#endif
