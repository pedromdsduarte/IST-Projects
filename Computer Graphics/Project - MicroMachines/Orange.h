#ifndef ORANGE_H_
#define ORANGE_H_

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
#include <stdlib.h>                                
#include <time.h>   
#include "Car.h"

class Orange : public Obstacle {
	private:
		double angle = 0;
		int rotateTime;
		int randDirection;
		int level;
	public:
		Orange();
		Orange(Vector3 *vec);
		~Orange();
		void update(double delta);
		void draw();
		void setAngle(double alpha);
		void setPosition(Vector3 *vec);
		double getAngle();
		void setLevel(int level);
		int getLevel();
		void handleCollision(double delta_t);
};

#endif  