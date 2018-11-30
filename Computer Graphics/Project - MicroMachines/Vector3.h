#ifndef VECTOR3_H_
#define VECTOR3_H_

#include <stdio.h>


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
#include <iostream>
#endif

class Vector3 {
	protected:
		double _x;
		double _y;
		double _z;

	public:
		Vector3();
		Vector3(double x, double y, double z);
		~Vector3();
		double getX();
		double getY();
		double getZ();
		void setX(double x);
		void setY(double y);
		void setZ(double z);
		void set(double x, double y, double z);
		Vector3 operator=(const Vector3 &vec);
		Vector3 operator*(double num);
		Vector3 operator+(const Vector3 &vec);
		Vector3 operator-(const Vector3 &vec);
		void print();
		Vector3* add(Vector3* v2);
    Vector3* inverte();
		double getMagnitude();
		double getDistance(Vector3* b);
};

#endif