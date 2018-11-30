#ifndef ENTITY_H_
#define ENTITY_H_
#include "Vector3.h"

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
class Entity {
	protected:
		Vector3* _position;
	public:
		Entity();
		Entity(Vector3 *vec);
		~Entity();
		Vector3* getPosition();
		Vector3* setPosition(double x, double y, double z);
		Vector3* setPosition(Vector3* vector);
};
#endif