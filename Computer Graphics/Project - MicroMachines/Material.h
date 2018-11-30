#ifndef MATERIAL_H_
#define MATERIAL_H_

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

class Material {
private:
	float _ambient[4];
	float _diffuse[4];
	float _specular[4];
	float _shine;

public:
	Material();
	Material(float ambient[], float diffuse[], float specular[], float shine);
	float* getAmbient();
	float* getDiffuse();
	float* getSpecular();
	float getShine();
	void setAmbient(float ambient[]);
	void setDiffuse(float diffuse[]);
	void setSpecular(float specular[]);
	void setShine(float shine);
};


#endif