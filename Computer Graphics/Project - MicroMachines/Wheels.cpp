

#include "Wheels.h"
void drawWheel(){
	glPushMatrix();
	glPushMatrix();
	glPushMatrix();
	glNormal3f(0.0, -1.0, 0.0);
	// Up half
	glVertex3f(-0.25f, 0, 0.0f);
	glVertex3f(0.0f, 0, 0.0f);
	glVertex3f(-0.125f,0,.2165f);

	glVertex3f(	0.0f, 0, 0.0f);
	glVertex3f(0.125f, 0,.2165f);
	glVertex3f(-0.125f, 0,.2165f);

	glVertex3f(0,0,0);
	glVertex3f(0.25,0,0);
	glVertex3f(0.125,0,0.2165);

	// Down half
	glNormal3f(0.0, -1.0, 0.0);
	glVertex3f(-0.125f,0,-.2165f);
	glVertex3f(0.0f, 0, 0.0f);
	glVertex3f(-0.25f, 0.0f, 0.0f);

	glVertex3f(-0.125f, 0.0f,-.2165f);
	glVertex3f(0.125f, 0.0f,-.2165f);
	glVertex3f(	0.0f, 0.0f, 0.0f);

	glVertex3f(0.125,0,-0.2165);
	glVertex3f(0.25,0,0);
	glVertex3f(0,0,0);

	glEnd();
	glBegin(GL_QUADS);
	glNormal3f(-1, 0,1);
	glVertex3f(-.125,0,.2165);
	glNormal3f(1, 0,1);
	glVertex3f(.125,0,.2165);
	glNormal3f(1, 1,1);
	glVertex3f(.125,.125,.2165);
	glNormal3f(-1, 1,1);
	glVertex3f(-.125,.125,.2165);

	glNormal3f(-1, 0, 0);
	glVertex3f(-.25,0,0);
	glNormal3f(-1, 0, 1);
	glVertex3f(-.125,0,.2165);
	glNormal3f(-1, 1, 1);
	glVertex3f(-.125,.125,.2165);
	glNormal3f(-1, 1, 0);
	glVertex3f(-.25,.125,0);

	glNormal3f(1, 0, 0);
	glVertex3f(.25,0,0);
	glNormal3f(1, 1, 0);
	glVertex3f(.25,.125,0);
	glNormal3f(1, 1, 1);
	glVertex3f(.125,.125,.2165);
	glNormal3f(1, 0, 1);
	glVertex3f(.125,0,.2165);

	glNormal3f(-1, 0, -1);
	glVertex3f(-.125,0,-.2165);
	glNormal3f(-1, 0, 0);
	glVertex3f(-.25,0,0);
	glNormal3f(-1, 1, 0);
	glVertex3f(-.25,0.125,0);
	glNormal3f(-1, 1, -1);
	glVertex3f(-.125,.125,-.2165);

	glNormal3f(-1, 0, -1);
	glVertex3f(-.125,0,-.2165);
	glNormal3f(1, 0, -1);
	glVertex3f(.125,0,-.2165);
	glNormal3f(1, 1, -1);
	glVertex3f(.125,.125,-.2165);
	glNormal3f(1, 1, -1);
	glVertex3f(-.125,.125,-.2165);

	glNormal3f(1, 0, -1);
	glVertex3f(.125,0,-.2165);
	glNormal3f(1, 1, -1);
	glVertex3f(.125,.125,-.2165);
	glNormal3f(1, 1, 0);
	glVertex3f(.25,.125,0);
	glNormal3f(1, 0, 0);
	glVertex3f(.25,0,0);
	glEnd();
	glPopMatrix();
	glPopMatrix();
	glPopMatrix();
}