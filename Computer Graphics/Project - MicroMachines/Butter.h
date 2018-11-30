#ifndef BUTTER_H_
#define BUTTER_H_

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

#include "Obstacle.h"
#include "Car.h"

class Butter : public Obstacle {
	private:
    int direction;
    double atrito;
	public:
		Butter();
		Butter(Vector3 *vec);
		~Butter();
		void draw();
		void update(double delta_t);
        void handleCollision(double delta_t);
};

#endif 