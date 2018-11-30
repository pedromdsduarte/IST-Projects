#ifndef CAR_H_
#define CAR_H_

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
#include "Wheels.h"
#define ORIG_X_CAR 0.0f
#define ORIG_Y_CAR -13.0f
#define ORIG_Z_CAR 1.65f


class Car : public DynamicObject{
	private:
		bool _upPressed;
		bool _downPressed;
		bool _rightPressed;
		bool _leftPressed;
		bool _immune;
		int direction;
		double speed;
		Vector3 * normal;

		bool _lost = false;
    bool _stop =false;
	public:
		Car();
		Car(Vector3 *vec);
		~Car();
		void draw();
		void update(double delta);
		void setKeyPress(int key, bool pressed);
		void setLost(bool value);
		bool getLost();
        void ColisionStop(bool value);
        bool GetColisionStop();
        double getSpeed();
		bool getImmune();
		void setImmune(bool value);
};
#endif