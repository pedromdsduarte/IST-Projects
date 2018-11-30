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
#include <iostream>
#include "Table.h"
using namespace std;


Table::Table() {
	GLfloat amb[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	GLfloat diff[] = { 0.55f, 0.55f, 0.55f, 1.0f };
	GLfloat spec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	GLfloat shine = 64.0f;

	_material = new Material(amb, diff, spec, shine);

	int width, height, channels;
	GLubyte* texture = SOIL_load_image("tablecloth.jpg", &width, &height, &channels, SOIL_LOAD_RGBA);
	//GLubyte* texture = SOIL_load_image("..\\..\\..\\..\\Projecto\\micromachines_home\\computer-graphics\\proj\\tablecloth.jpg", &width, &height, &channels, SOIL_LOAD_RGBA);
	//GLubyte* texture = SOIL_load_image("C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\tablecloth.jpg", &width, &height, &channels, SOIL_LOAD_RGBA);
	//GLubyte* texture = SOIL_load_image("..\\..\\..\\..\\Projecto\\micromachines_portable\\proj\\tablecloth.jpg", &width, &height, &channels, SOIL_LOAD_RGBA);


	glGenTextures(1, &texName);
	glBindTexture(GL_TEXTURE_2D, texName);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
	SOIL_free_image_data(texture);

}

Table::~Table() {

}


void Table::draw() {

	double x_at = -37.5f, y_at = 37.5f;
	double width = 75;

	
	int div_number = 256;	//divide a mesa em 4, 16, 64, 256 ...
	int factor = sqrt(div_number);
	glEnable(GL_TEXTURE_2D);
	glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
	glBindTexture(GL_TEXTURE_2D, texName);

	
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, getMaterial()->getAmbient());
	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, getMaterial()->getDiffuse());
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, getMaterial()->getSpecular());
	glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, getMaterial()->getShine());

	glColor3f(0, 0.5, 0.5);

	float x_off = 0;
	float  y_off = 0;
	for (int i = 1; i <= div_number; i++) {
		glPushMatrix();
		
		glTranslatef((factor-1) * x_at/factor + x_off, (factor-1) * y_at/factor + y_off, 1.25f);
		glScalef(width/factor, width/factor, 1);

		glBegin(GL_POLYGON);
			glNormal3f(0, 0, 1);

			glTexCoord2f(0, 0);
			glVertex3f(-0.5, 0.5, 0);

			glTexCoord2f(0, 1);
			glVertex3f(-0.5, -0.5, 0);

			glTexCoord2f(1, 1);
			glVertex3f(0.5, -0.5, 0);

			glTexCoord2f(1, 0);
			glVertex3f(0.5, 0.5, 0);
		glEnd();

		glPopMatrix();
		
		x_off -= -width/factor;
		if ( (i%factor) == 0) {
			x_off = 0;
			y_off -= width/factor;
		}

	}

	glDisable(GL_TEXTURE_2D);
}