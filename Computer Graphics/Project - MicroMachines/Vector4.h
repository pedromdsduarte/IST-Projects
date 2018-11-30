#ifndef VECTOR4_H_
#define VECTOR4_H_

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

#include "Vector3.h"

class Vector4 :public Vector3 {
private:
	double _w;
public:
	Vector4();
	Vector4(double x, double y, double z, double w);
	~Vector4();
	double getW();
	void set(double x, double y, double z, double w);
	Vector4 operator=(const Vector4 &vec);
	Vector4 operator*(double num);
	Vector4 operator+(const Vector4 &vec);
	Vector4 operator-(const Vector4 &vec);
	Vector4* add(Vector4* v2);
	void print();
};
#endif 
