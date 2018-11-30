#ifndef STATICOBJECT_H_
#define STATICOBJECT_H_

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

#include "GameObject.h"

class StaticObject : public GameObject {
	private:

	public:
		StaticObject();
		~StaticObject();
		void update();
};


#endif