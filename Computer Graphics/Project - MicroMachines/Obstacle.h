#ifndef OBSTACLE_H_
#define OBSTACLE_H_

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

#include "DynamicObject.h"

class Obstacle : public DynamicObject {
	private:

	public:
		Obstacle();
		~Obstacle();
		void draw();
		virtual void update(double delta_t) = 0;
};

#endif 
