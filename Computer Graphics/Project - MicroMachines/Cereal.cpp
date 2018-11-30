#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#include <math.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#include <GL\GL.h>
#include <GL\GLU.h>
#endif

#include "Cereal.h"

Cereal::Cereal(Vector3* vec) {
	_position = vec;
	setHitBox(0.6f);
	speed = 0;

	GLfloat amb[] = { 0.89f,0.72f,0.32f,1.0f };
	GLfloat diff[] = { 0.76f,0.57f,0.3f,1.0f };
	GLfloat spec[] = { 0.03f,0.02f,0.0f,1.0f };
	GLfloat shine = 0.0f;

	_material = new Material(amb, diff, spec, shine);

}


Cereal::Cereal() {
	setHitBox(0.6f);
	speed = 0;

	GLfloat amb[] = { 0.83f,0.63f,0.12f,1.0f };
	GLfloat diff[] = { 0.76f,0.57f,0.3f,1.0f };
	GLfloat spec[] = { 0.03f,0.02f,0.0f,1.0f };
	GLfloat shine = 0.0f;

	_material = new Material(amb, diff, spec, shine);

}

Cereal::~Cereal() {

}

void Cereal::draw() {
	
	glPushMatrix();
		glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, getMaterial()->getAmbient());
		glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, getMaterial()->getDiffuse());
		glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, getMaterial()->getSpecular());
		glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, getMaterial()->getShine());

		glColor3f(0.89f, 0.72f, 0.32f);
		glTranslatef(getPosition()->getX(), getPosition()->getY(), getPosition()->getZ());
		glScalef(1, 1, 1.1);
		glutSolidTorus(0.2f, 0.6f, 10, 10);
	glPopMatrix();
}

void Cereal::update(double delta_t) {

	Vector3* speedVec = new Vector3(0, 0, 0);


	if (hadCollision()) {
		handleCollision(delta_t);
	}

	speed -= atrito * delta_t;
    if(direction==0){
        if (speed > 0) {
            speed = 0;
        }
    }
    if(direction==1){
        if (speed < 0) {
            speed = 0;
        }
    }


    speedVec->setX(speed * cos(((Car*)_collider)->getRotate() * PI / 180));
    speedVec->setY(speed * sin(((Car*)_collider)->getRotate() * PI / 180));
	speedVec->setZ(0);
	
	setVelocity(speedVec);
	setPosition(_position->add(getVelocity()));
}

void Cereal::handleCollision(double delta_t) {
    if(((Car*)_collider)->getSpeed()<0){
        atrito = -0.0009;
		speed = ((Car*)_collider)->getSpeed()-0.03;
		//speed = -0.3;
        direction=0;
		//((Car*)_collider)->setKeyPress(GLUT_KEY_UP, false);
    }
    else if(((Car*)_collider)->getSpeed() >0){
        atrito = 0.0009;
        speed = ((Car*)_collider)->getSpeed()+0.03;
		//speed = 0.3;
        direction=1;
		//((Car*)_collider)->setKeyPress(GLUT_KEY_DOWN, false);
    }



    //atrito = -0.0009;
    //speed = -0.3;

    ((Car*)_collider)->ColisionStop(true);
	


	//setRotate(_collider->getRotate());
	//setPosition(getPosition()->add(speedVec));
}
