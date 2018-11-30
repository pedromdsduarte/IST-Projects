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

#include "PointSourceLight.h"

PointSourceLight::PointSourceLight(GLenum num) {
	_num = num;


	GLfloat amb[] = { 1.0f,1.0f,1.0f,1.0f };
	GLfloat diff[] = { 0.72f,0.7f,0.6f,1.0f };
	GLfloat spec[] = { 1.0f,1.0f,1.0f,1.0f };
	GLfloat shine = 75.0f;

	_material = new Material(amb, diff, spec, shine);

	_position = new Vector4(0, 0, 0, 1);

}
PointSourceLight :: ~PointSourceLight() {

}
void PointSourceLight::brightness() {

}

void PointSourceLight::shine() {
	LightSource::shine();

	GLfloat position[] = { static_cast<GLfloat>(_position->getX()), static_cast<GLfloat>(_position->getY()), static_cast<GLfloat>(_position->getZ()+.5), static_cast<GLfloat>(_position->getW()) };

	glLightfv(_num, GL_POSITION, position);
	glLightf(_num, GL_CONSTANT_ATTENUATION, _const_att);
	glLightf(_num, GL_LINEAR_ATTENUATION, _linear_att);
	glLightf(_num, GL_QUADRATIC_ATTENUATION, _quad_att);
}

void PointSourceLight::drawPost() {

	glPushMatrix();
		glTranslatef(_position->getX(), _position->getY(), _position->getZ()-1.5);
		glScalef(0.5, 0.5, 0.5);
	
		glPushMatrix();

			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, getMaterial()->getAmbient());
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, getMaterial()->getDiffuse());
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, getMaterial()->getSpecular());
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, getMaterial()->getShine());
			
			glColor3f(219/255.0, 232/255.0, 181/255.0);
			glTranslatef(0, 0, 1);
			glutSolidDodecahedron();
			/*glBegin(GL_POLYGON);
				GLUquadricObj *obj = gluNewQuadric();
				gluCylinder(obj, 1, 1, 3, 20, 20);
			glEnd();*/


		glPopMatrix();

		glPushMatrix();
			glTranslatef(0, 0, 2.5);
			glLineWidth(1);
			glColor3f(0, 0, 0);
				glBegin(GL_LINES);
				glVertex3f(0, 0, 0);
				glVertex3f(0, 0, 1);
			glEnd();
		glPopMatrix();

		glPushMatrix();

			GLfloat amb[] = { 1.0f,0.25f,0.0f,1.0f };
			GLfloat diff[] = { 0.62f,0.44f,0.2f,1.0f };
			GLfloat spec[] = { 1.0f,0.7f,0.7f,1.0f };
			GLfloat shine = 128.0f;
			GLfloat emission[] = { 1.0, 0.25f, 0.0, 1.0 };

			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb);
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff);
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec);
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine);
			if (getState())
				glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, emission);

			glTranslatef(0, 0, 4);
			glScalef(0.25, 0.25, 0.75);
			glColor3f(255, 64 / 255.0, 0);
			glutSolidSphere(1, 10, 10);
		glPopMatrix();

	glPopMatrix();

	GLfloat default[] = { 0, 0, 0, 1.0 };
	glMaterialfv(GL_FRONT, GL_EMISSION, default);

}

void PointSourceLight::setMaterial(Material* material) {
	_material = material;
}

Material* PointSourceLight::getMaterial() {
	return _material;
}