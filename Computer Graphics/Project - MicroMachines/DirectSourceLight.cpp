#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#include <GL\GL.h>
#include <GL\GLU.h>
#endif

#include "DirectSourceLight.h"

DirectSourceLight::DirectSourceLight(GLenum num){
    _num = num;
}
DirectSourceLight :: ~DirectSourceLight(){
    
}
void DirectSourceLight:: brightness(){
    
}

void DirectSourceLight::shine() {

	LightSource::shine();

	GLfloat direction[] = { static_cast<GLfloat>(_direction->getX()), static_cast<GLfloat>(_direction->getY()), static_cast<GLfloat>(_direction->getZ()), static_cast<GLfloat>(_direction->getW()) };
	
	glLightfv(_num, GL_POSITION, direction);	

}
