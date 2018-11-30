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

#include "LightSource.h"

LightSource::LightSource() {
    _state = true;
}

LightSource::LightSource(GLenum number) {
	_num = number;
	_state = true;
}

LightSource::~LightSource() {

}

bool LightSource::getState() {
    return _state;
}

void LightSource::setState(bool state) {
	_state = state;
}

GLenum LightSource::getNum() {
	return _num;
}

void LightSource::setPosition(Vector4 *pos) {
    _position = pos;
}

void LightSource::setDirection(Vector4 *direction) {
    _direction = direction;
}

void LightSource::setCutOff(float cut_off) {
    _cutoff = cut_off;
}

void LightSource::setExponent(float exponent) {
    _exponent = exponent;

}

void LightSource::setAmbient(Vector4 *ambient) {
    _ambient = ambient;
}

void LightSource::setDiffuse(Vector4 *diffuse) {
    _diffuse = diffuse;
}

void LightSource::setSpecular(Vector4 *specular) {
    _specular = specular;
}

void LightSource::draw() {

    
}

void LightSource::setAttenuation(float cons, float linear, float quad) {
	_const_att = cons;
	_linear_att = linear;
	_quad_att = quad;
}

void LightSource::shine() {
	GLfloat ambient[] = { static_cast<GLfloat>(_ambient->getX()), static_cast<GLfloat>(_ambient->getY()), static_cast<GLfloat>(_ambient->getZ()), static_cast<GLfloat>(_ambient->getW()) };
	GLfloat diffuse[] = { static_cast<GLfloat>(_diffuse->getX()), static_cast<GLfloat>(_diffuse->getY()), static_cast<GLfloat>(_diffuse->getZ()), static_cast<GLfloat>(_diffuse->getW()) };
	GLfloat specular[] = { static_cast<GLfloat>(_specular->getX()), static_cast<GLfloat>(_specular->getY()), static_cast<GLfloat>(_specular->getZ()), static_cast<GLfloat>(_specular->getW()) };

	glLightfv(_num, GL_AMBIENT, ambient);
	glLightfv(_num, GL_DIFFUSE, diffuse);
	glLightfv(_num, GL_SPECULAR, specular);

}