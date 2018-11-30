#include "Entity.h"
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

Entity::Entity() {
	_position = new Vector3(0,0,0);
}

Entity::Entity(Vector3* vec) {
	_position = vec;
}

Entity::~Entity() {
	delete _position;

}

Vector3* Entity::getPosition() {
	return _position;
}

Vector3* Entity::setPosition(double x, double y, double z) {
	_position->setX(x);
	_position->setY(y);
	_position->setZ(z);

	return _position;

}

Vector3* Entity::setPosition(Vector3 * vector) {
	delete _position;
	_position  = new Vector3(vector->getX(), vector->getY(),vector->getZ());
	return _position;

}
