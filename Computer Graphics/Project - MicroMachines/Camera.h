#ifndef CAMERA_H_
#define CAMERA_H_

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

#include "Entity.h"

class Camera : public Entity {
protected:
	double _near;
	double _far;
	Vector3 _up;
	Vector3 _center;
	Vector3 _at;
public:
	Camera();
	Camera(double var_near, double var_far);
	~Camera();
	void update();
	virtual void computeProjectionMatrix();
	virtual void computeVisualizationMatrix();
	virtual void setParam(Vector3* eye, Vector3* at, Vector3* up);
	//virtual void updatePos(Vector3* carPos, double carRotate);

};
#endif