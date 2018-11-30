#ifndef ORTHOGONALCAMERA_H_
#define ORTHOGONALCAMERA_H_

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

#include "Camera.h"

class OrthogonalCamera : public Camera {
private:
	double _left;
	double _right;
	double _bottom;
	double _top;
	double _ratio;
	double _aspect;
public:
	OrthogonalCamera();
	~OrthogonalCamera();
	void update(double right, double left, double bottom, double top, double dnear, double dfar, double ratio, double aspect);
	void computeProjectionMatrix();
	void computeVisualizationMatrix();
	void setParam(Vector3* eye, Vector3* at, Vector3* up);
	void updatePos(Vector3* carPos, double carRotate);
};
#endif