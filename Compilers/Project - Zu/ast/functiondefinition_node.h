// $Id: functiondefinition_node.h,v 1.5 2016/04/14 22:06:01 ist178328 Exp $ -*- c++ -*-
#ifndef __ZU_FUNCTIONDEFINITIONNODE_H__
#define __ZU_FUNCTIONDEFINITIONNODE_H__

#include <cdk/ast/basic_node.h>

namespace zu {

  /**
   * Class for describing functiondeclaration nodes.
   */
  class functiondefinition_node: public cdk::basic_node {
    basic_type *_type;
    zu::identifier_node *_identifier;
    bool _public;
    bool _external;
    cdk::sequence_node *_arguments;
    cdk::expression_node *_literal;
    zu::block_node *_block; 

  public:
    inline functiondefinition_node(int lineno, basic_type *type, zu::identifier_node *identifier, bool isPublic, bool external, cdk::sequence_node *arguments, cdk::expression_node *literal, zu::block_node *block) :
        cdk::basic_node(lineno), _type(type), _identifier(identifier), _public(isPublic), _external(external), _arguments(arguments), _literal(literal), _block(block) {
    }

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
    
    inline cdk::sequence_node *arguments() {
      return _arguments;
    }

    inline cdk::expression_node *literal() {
      return _literal;
    }

    inline zu::block_node *block() {
      return _block;
    }
    
    void accept(basic_ast_visitor *sp, int level) {
      sp->do_functiondefinition_node(this, level);
    }

  };

} // zu

#endif
