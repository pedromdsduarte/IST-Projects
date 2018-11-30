#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#include <GL\GL.h>
#include <GL\GLU.h>
#endif
#include <math.h>
#include "PerspectiveCamera.h"

PerspectiveCamera::PerspectiveCamera() {
	_eye = new Vector3();
	_at = new Vector3();
	_up = new Vector3(0,0,1);
}

PerspectiveCamera::~PerspectiveCamera() {

}

void PerspectiveCamera::update(double fovy, double ratio, double aspect, double zNear, double zFar) {
	_fovy = fovy;
	_ratio = ratio;
	_aspect = aspect;
	_near = zNear;
	_far = zFar;

}

void PerspectiveCamera::computeProjectionMatrix() {
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(_fovy, _aspect, _near, _far);
	gluLookAt(_eye->getX(), _eye->getY(), _eye->getZ(),
		_at->getX(), _at->getY(), _at->getZ(),
		_up->getX(), _up->getY(), _up->getZ());
}

void PerspectiveCamera::computeVisualizationMatrix() {
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	
	gluLookAt(_eye->getX(), _eye->getY(), _eye->getZ(),
			_at->getX(), _at->getY(), _at->getZ(),
			_up->getX(), _up->getY(), _up->getZ());
}

void PerspectiveCamera::setParam(Vector3* eye, Vector3* at, Vector3* up) {
	_eye = eye;
	_at = at;
	_up = up;
}

void PerspectiveCamera::updatePos(Vector3* carPos, double carRotate) {
	double distance = 25;
	double height = 9;

	_eye->setX(carPos->getX() + distance*cos(carRotate * PI / 180));
	_eye->setY(carPos->getY() + distance*sin(carRotate * PI / 180));
	_eye->setZ(height);

	_at = carPos;
	_up->set(0,0,1);
}