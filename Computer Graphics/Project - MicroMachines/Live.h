#ifndef LIVE_H_
#define LIVE_H_

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

#include "StaticObject.h"
#include "Vector3.h"
#include "Car.h"

class Live : public StaticObject {
	public:
		Live(Vector3* position);
		~Live();
		void draw(int live_number);
		
};

#endif