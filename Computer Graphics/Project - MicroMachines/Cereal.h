#ifndef CEREAL_H_
#define CEREAL_H_

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
#include "Car.h"

class Cereal : public DynamicObject{
private:
    double atrito;
    int direction;
public:
	Cereal(Vector3* _vec);
	Cereal();
	~Cereal();
	void draw();
	void update(double delta_t);
	void handleCollision(double delta_t);
    void handleCollisionBackwards(double delta_t);
    void handleCollisionForward(double delta_t);
};
#endif