// $Id: symbol.h,v 1.4 2016/05/19 15:19:45 ist179112 Exp $ -*- c++ -*-
#ifndef __ZU_SEMANTICS_SYMBOL_H__
#define __ZU_SEMANTICS_SYMBOL_H__

#include <string>
#include <cdk/basic_type.h>

namespace zu {

    class symbol {
      /*const*/ basic_type *_type;
      std::string _name;
      long _value; // hack!
      int _offset;
      bool _public;
      bool _external;
      bool _function;
    public:
      // inline symbol(/*const*/ basic_type *type, const std::string &name, long value) :
          //  _type(type), _name(name), _value(value) {
        //}
        
        inline symbol(basic_type *type, const std::string &name, int offset) : 
            _type(type), _name(name), _offset(offset){
        }

        virtual ~symbol() {
            delete _type;
        }

        inline /*const*/ basic_type *type() /*const */{
            return _type;
        }
        inline /*const*/ std::string &name() /*const */{
            return _name;
        }
        inline long value() /*const*/ {
            return _value;
        }
        inline long value(long v) {
            return _value = v;
        }
      
        inline int offset() const { 
            return _offset;
        }
    
        inline void offset(int o) { 
            _offset = o; 
        
        }
        
        inline bool isPublic() const {
            return _public;
        }
        
        inline void isPublic(bool b){
            _public = b;
        }
        
        inline bool external() const {
            return _external;
        }
        
        inline void external(bool b){
            _external = b;
        }
    
        inline bool function() const {
            return _function;
        }
        
        inline void function(bool b){
            _function = b;
        }
    };

} // zu

#endif
