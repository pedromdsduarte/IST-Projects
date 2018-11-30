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

#include "Vector3.h"

Vector3::Vector3() {
	_x = 0;
	_y = 0;
	_z = 0;
}

Vector3::Vector3(double x, double y, double z) {
	_x = x;
	_y = y;
	_z = z;
}

Vector3::~Vector3() {
	
}

double Vector3::getX() {
	return _x;
}

double Vector3::getY() {
	return _y;
}

double Vector3::getZ() {
	return _z;
}

void Vector3::setX(double x) {
	_x = x;
}

void Vector3::setY(double y) {
	_y = y;
}

void Vector3::setZ(double z) {
	_z = z;
}

void Vector3::set(double x, double y, double z) {
	_x = x;
	_y = y;
	_z = z;
}

Vector3 Vector3::operator=(const Vector3 &vec) {
	if (this != &vec) {
		_x = vec._x;
		_y = vec._y;
		_z = vec._z;
	}
	return *this;

}

Vector3 Vector3::operator*(double num) {
	_x = _x * num;
	_y = _y * num;
	_z = _z * num;
	return *this;
}

Vector3 Vector3::operator+(const Vector3 &vec) {
	double x = _x + vec._x;
	double y = _y + vec._y;
	double z = _z + vec._z;

	//return Vector3(x,y,z);
	return Vector3(x,y,z);
}


Vector3 Vector3::operator-(const Vector3 &vec) {
	double x = _x - vec._x;
	double y = _y - vec._y;
	double z = _z - vec._z;

	return Vector3(x, y, z);
}

void Vector3::print() {
	printf("(%f, %f, %f)\n", _x, _y, _z);
}

Vector3* Vector3::add(Vector3* v2) {
	double x = _x + v2->getX();
	double y = _y + v2->getY();
	double z = _z + v2->getZ();

	return new Vector3(x, y, z);
}

double Vector3::getDistance(Vector3* b) {
	Vector3* a = this;
	double xa, ya, za, xb, yb, zb;
	xa = a->getX();
	ya = a->getY();
	za = a->getZ();

	xb = b->getX();
	yb = b->getY();
	zb = b->getZ();
	//return sqrt(pow(xa - xb, 2) + pow(ya - yb, 2) + pow(za - zb, 2));
	return sqrt(pow(xa - xb, 2) + pow(ya - yb, 2));
}

Vector3* Vector3:: inverte(){
    return new Vector3(- _x,- _y,-_z);
}

double Vector3::getMagnitude() {
	return sqrt(pow(_x, 2) + pow(_y, 2) + pow(_z, 2));
}