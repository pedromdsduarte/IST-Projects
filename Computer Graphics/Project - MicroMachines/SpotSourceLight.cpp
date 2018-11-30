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

#include "SpotSourceLight.h"
#include "math.h"
#define PI 3.14159265

SpotSourceLight::SpotSourceLight(GLenum num) {
	_num = num;
}
SpotSourceLight :: ~SpotSourceLight() {

}
void SpotSourceLight::brightness() {

}

void SpotSourceLight::shine() {

	LightSource::shine();

	GLfloat position[] = { static_cast<GLfloat>(_position->getX()), static_cast<GLfloat>(_position->getY()), static_cast<GLfloat>(_position->getZ() + .5), static_cast<GLfloat>(_position->getW()) };
	GLfloat direction[] = { static_cast<GLfloat>(_direction->getX()), static_cast<GLfloat>(_direction->getY()), static_cast<GLfloat>(_direction->getZ()) };
	GLfloat cutoff = _cutoff;
	GLfloat exponent = _exponent;

	glLightfv(_num, GL_POSITION, position);
	glLightfv(_num, GL_SPOT_DIRECTION, direction);
	glLightf(_num, GL_SPOT_CUTOFF, cutoff);
	glLightf(_num, GL_SPOT_EXPONENT, exponent);

	glLightf(_num, GL_CONSTANT_ATTENUATION, _const_att);
	glLightf(_num, GL_LINEAR_ATTENUATION, _linear_att);
	glLightf(_num, GL_QUADRATIC_ATTENUATION, _quad_att);

}

void SpotSourceLight::updatePos(Vector3* carPos, float carRotate) {
	double offset = -1;
	double angle_offset = -20;

	if (_num == GL_LIGHT6) {	//se for o farol direito
		offset = 1;								//desloca a luz
		angle_offset = -angle_offset;			//inverte o angulo
	}
	
	setPosition(new Vector4(carPos->getX(), carPos->getY() + offset, carPos->getZ(), 1));
	setDirection(new Vector4(-cos((carRotate+angle_offset)* PI/180), -sin((carRotate+angle_offset) * PI/180), -1/2, 1));
}