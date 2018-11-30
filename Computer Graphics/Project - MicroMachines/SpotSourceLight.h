
#ifndef ____SpotSourceLight__
#define ____SpotSourceLight__

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

#include "LightSource.h"

class SpotSourceLight : public LightSource {
protected:
public:
	SpotSourceLight(GLenum num);
	~SpotSourceLight();
	void brightness();
	void shine();
	void updatePos(Vector3* carPos, float carRotate);

};
#endif


