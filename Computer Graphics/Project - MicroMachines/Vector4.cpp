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

#include "Vector4.h"

Vector4::Vector4() {
    _w = 0.0;
}

Vector4::Vector4(double x, double y, double z, double w) : Vector3(x,y,z){
    _w = w;
}

Vector4::~Vector4() {

}

void Vector4::set(double x, double y, double z, double w) {
    Vector3::set(x, y, z);
    _w = w;

}

double Vector4::getW() {
	return _w;
}

Vector4 Vector4::operator=(const Vector4 &vec) {
	if (this != &vec) {
		_x = vec._x;
		_y = vec._y;
		_z = vec._z;
		_w = vec._w;
	}
	return *this;

}

void Vector4::print() {
	printf("(%f, %f, %f,%f)\n", _x, _y, _z,_w);
}

Vector4* Vector4::add(Vector4* v2) {
	double x = _x + v2->getX();
	double y = _y + v2->getY();
	double z = _z + v2->getZ();
	double w = _w + v2->getW();

	return new Vector4(x, y, z, w);
}