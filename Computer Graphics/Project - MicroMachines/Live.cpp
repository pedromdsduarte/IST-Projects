#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#include <math.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#include <GL\GL.h>
#include <GL\GLU.h>
#endif

#include "Live.h"

Live::Live(Vector3* position) {
	_position = position;
}

Live::~Live() {

}

void Live::draw(int live_number) {
	glPushMatrix();
		glBegin(GL_POLYGON);
			glVertex3f(0.0f, 0.0f, 0.0f);
			glVertex3f(0.0f, 1.0f, 0.0f);
			glVertex3f(1.0f, 1.0f, 0.0f);
			glVertex3f(1.0f, 0.0f, 0.0f);
		glEnd();
	glPopMatrix();
}



