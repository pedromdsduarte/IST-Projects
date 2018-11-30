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

#include "Butter.h" 

Butter::Butter() {

}

Butter::Butter(Vector3* vec) {
	_position = vec;
	setHitBox(3.5);
    speed = 0;

	GLfloat amb[] = { 0.83f,0.81f,0.13f,1.0f };
	GLfloat diff[] = { 0.94f,0.83f,0.1f,1.0f };
	GLfloat spec[] = { 0.99f,0.75f,0.13f,1.0f };
	GLfloat shine = 2.0f;

	_material = new Material(amb, diff, spec, shine);

}

Butter::~Butter() {

}

void Butter::draw() {
	glPushMatrix();

	glTranslatef(_position->getX(), _position->getY(), _position->getZ());
		glPushMatrix();

			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, getMaterial()->getAmbient());
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, getMaterial()->getDiffuse());
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, getMaterial()->getSpecular());
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, getMaterial()->getShine());

			glScalef(0.3, 0.3, 0.3);
			glTranslatef(0, 0, 10);
			glRotated(90, 1, 0, 0);
			glPushMatrix();
				glScalef(3.0f, 3.0f, 3.0f);
				glPushMatrix();
					glColor3f(0.9f, 0.8f, 0.1f);
					glTranslatef(1, -1.5, 1.0);
					glScalef(1.0f, 0.1f, 1.0f);
					glutSolidSphere(4, 10, 10);
				glPopMatrix();
				glPushMatrix();
					glColor3f(0.95f, 0.8f, 0.1f);
					glTranslatef(4, -1.5, 1.0);
					glScalef(1.0f, 0.1f, 1.0f);
					glutSolidSphere(2, 10, 10);
				glPopMatrix();
				glPushMatrix();
					glColor3f(0.95f, 0.8f, 0.1f);
					glTranslatef(1, -1.5, 3.7);
					glScalef(1.0f, 0.1f, 1.0f);
					glutSolidSphere(1.7, 10, 10);
				glPopMatrix();
				glPushMatrix();
					glColor3f(0.95f, 0.8f, 0.1f);
					glTranslatef(3.2, -1.5, 3.2);
					glScalef(1.0f, 0.1f, 1.0f);
					glutSolidSphere(1.7, 10, 10);
				glPopMatrix();
				glPushMatrix();
					glColor3f(1.0f, 0.8f, 0.0f);
					glScalef(1.0f, 0.5f, 1.5f);
					glutSolidCube(6);
				glPopMatrix();
			glPopMatrix();
		glPopMatrix();
	
	glPopMatrix();
}
void Butter::update(double delta_t) {
    
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
    speedVec->setY(speed * sin(((Car*)_collider)->getRotate()  * PI / 180));
    speedVec->setZ(0);

	//Check limits
	/*
	if (getPosition()->getX()<-36.7 || getPosition()->getX()>37.5) {
		speedVec->setX(0);
	}

	if (getPosition()->getY()<-36.7 || getPosition()->getY()>37.5) {
		speedVec->setY(0);
	}
    */

    setVelocity(speedVec);
    setPosition(_position->add(getVelocity()));
}

void Butter::handleCollision(double delta_t) {
    if(((Car*)_collider)->getSpeed()<0){
        atrito = -0.0009;
        //speed = -0.2;
		speed = ((Car*)_collider)->getSpeed() - 0.02;
        direction=0;
		//((Car*)_collider)->setKeyPress(GLUT_KEY_UP, false);
    }
    else if(((Car*)_collider)->getSpeed() >0){
        atrito = 0.0009;
       // speed= 0.2;
		speed = ((Car*)_collider)->getSpeed() + 0.02;
        direction=1;
		//((Car*)_collider)->setKeyPress(GLUT_KEY_DOWN, false);
    }

    ((Car*)_collider)->ColisionStop(true);
	

    //setRotate(_collider->getRotate());
    //setPosition(getPosition()->add(speedVec));
}

