#ifndef ROAD_H_
#define ROAD_H_

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

#include <vector>
#include "StaticObject.h"
#include "Cereal.h"

class Road : public DynamicObject {
private:
	std::vector<Cereal*> _cerealArray;
public: 
	Road(std::vector<Cereal*> cerealArray);
	~Road();
	void draw();
	int getPt(int n1, int n2, float perc);
	Vector3* bezierFunc(Vector3* A, Vector3* B, Vector3* C, Vector3* D, double t);
	void respawnRoad(std::vector<Cereal*> cerealArray);
};


#endif