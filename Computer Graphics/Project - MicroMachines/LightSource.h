#ifndef LIGHTSOURCE_H_
#define LIGHTSOURCE_H_

#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#endif

#include "Vector4.h"

class LightSource {
	protected:
        Vector4 *_ambient;
        Vector4 *_diffuse;
        Vector4 *_specular;
        Vector4 *_position;
        Vector4 *_direction;
        GLfloat _cutoff;
        GLfloat _exponent;
		GLfloat _const_att;
		GLfloat _linear_att;
		GLfloat _quad_att;
		GLenum _att_type;
		bool _state;
        GLenum _num;
    

	public:
        LightSource();
        LightSource(GLenum number);
		~LightSource();
		bool getState();
		void setState(bool state);
		GLenum getNum();
		void setPosition(Vector4 *position);
		void setDirection(Vector4 *direction);
		void setCutOff(float cut_off);
		void setExponent(float exponent);
		void setAmbient(Vector4 *ambient);
		void setDiffuse(Vector4 *difuse);
		void setSpecular(Vector4 *specular);
		void setAttenuation(float cons, float linear, float quad);
		void draw();
		//void SetSpotLights_On_Off(bool value);
		void shine();

};


#endif