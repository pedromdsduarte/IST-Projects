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

#include "OrthogonalCamera.h"

OrthogonalCamera::OrthogonalCamera() {

}

OrthogonalCamera::~OrthogonalCamera() {

}

void OrthogonalCamera::update(double left, double right, double bottom, double top, double unear, double ufar, double ratio, double aspect) {
	_right = right;
	_left = left;
	_top = top;
	_bottom = bottom;
	_near = unear;
	_far = ufar;
	_ratio = ratio;
	_aspect = aspect;
}

void OrthogonalCamera::computeProjectionMatrix() {
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(_left, _right, _bottom, _top, _near, _far);
	if (_aspect > _ratio) {
		glScalef(_ratio / _aspect, 1, 1);
	}
	else
		glScalef(1, _aspect / _ratio, 1);
}

void OrthogonalCamera::computeVisualizationMatrix() {
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}


void OrthogonalCamera::setParam(Vector3* eye, Vector3* at, Vector3* up) {
	//
}

void OrthogonalCamera::updatePos(Vector3* carPos, double carRotate) {
	//
}