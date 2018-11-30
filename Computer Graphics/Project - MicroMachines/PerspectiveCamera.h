#ifndef PERSPECTIVECAMERA_H_
#define PERSPECTIVECAMERA_H_

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
#define PI  3.14159265

class PerspectiveCamera : public Camera{
	private:
		double _fovy = 0;
		double _aspect = 0;
		double _ratio = 0;

		Vector3* _eye;
		Vector3* _at;
		Vector3* _up;

	public:
		PerspectiveCamera();
		~PerspectiveCamera();
		void update(double fovy, double ratio, double aspect, double zNear, double zFar);
		void computeProjectionMatrix();
		void computeVisualizationMatrix();
		void setParam(Vector3* eye, Vector3* at, Vector3* up);
		void updatePos(Vector3* carPos, double carRotate);
};
#endif