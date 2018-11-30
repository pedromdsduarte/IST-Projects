
#ifndef ____DirectSourceLight__
#define ____DirectSourceLight__

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

class DirectSourceLight : public LightSource {
    public:
    DirectSourceLight (GLenum num);
    ~ DirectSourceLight();
    void brightness();
	void shine();
};
#endif


