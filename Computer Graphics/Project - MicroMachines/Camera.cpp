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

#include "Camera.h"

Camera::Camera(double var_near, double var_far) {
	_near = var_near;
	_far = var_far;
}
Camera::Camera(){
}

Camera::~Camera() {

}

void Camera::update() {

}

void Camera::computeProjectionMatrix() {

}

void Camera::computeVisualizationMatrix() {

}

void Camera::setParam(Vector3* eye, Vector3* at, Vector3* up) {
	//
}