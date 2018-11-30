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
#include <math.h>
#include "Orange.h"


Orange::Orange() {
	rotateTime = clock();
	randDirection = (rand() % 3 - 1);
	//printf("randDirection: %i\n", randDirection);
	setHitBox(2.3);
	_position = new Vector3(55.0f, 55.0f, 3.50f);

	GLfloat amb[] = { 1.0f,0.33f,0.0f,1.0f };
	GLfloat diff[] = { 0.23f,0.04f,0.0f,1.0f };
	GLfloat spec[] = { 0.82f,0.57f,0.02f,1.0f };
	GLfloat shine = 21.0f;

	_material = new Material(amb, diff, spec, shine);

}

/*
Orange::Orange(Vector3* vec) {
	_position = vec;
	rotateTime = clock();
	randDirection = (rand() % 3 - 1);
	printf("randDirection: %i\n", randDirection);
	setHitBox(1.15);
}*/

Orange::~Orange() {

}

void Orange::setAngle(double alpha) {
	angle = alpha;
}
void Orange::setPosition(Vector3* vec) {
	_position = vec;
}

double Orange::getAngle() {
	return angle;
}

void Orange::setLevel(int x) {
	level = x;
}

int Orange::getLevel() {
	return level;
}


void Orange::update(double delta_t) {
	int currentTime = clock();
	double max_velocity = -0.145; //velocidade maxima da laranja
	double accel = -0.0004;// velocidade da laranja
	double atrito = -0.002;
	Vector3* speedVec = new Vector3(0,0,0);
	int spawn;

	if ( _position->getX() < -40  || _position->getX() > 40 || _position->getY() < -35 || _position->getY() > 40) {
		setAlive(false);
		spawn = rand() % 4;
		randDirection = (rand() % 3 - 1);

		if (spawn == 0) {
			_position->setX(-39);
			_position->setY(rand() % 100 - 50);
			setAngle(135 + (rand() % 90));  // Angulo de 135 a 225
			//printf("Spawn: %i\tX: %f\t Y: %f\tAngle: %f\n", spawn, _position->getX(), _position->getY(), getAngle());
		}else if (spawn == 1) {
			_position->setX(39);
			_position->setY(rand() % 100 - 50);
			setAngle(315 + (rand() % 90));  // Angulo de 315 a 45
			//printf("Spawn: %i\tX: %f\t Y: %f\tAngle: %f\n", spawn, _position->getX(), _position->getY(), getAngle());
		}else if (spawn == 2) {
			_position->setY(39);
			_position->setX(rand() % 100 - 50);
			setAngle(45 + (rand() % 90)); // Angulo de 90 a 135
			//printf("Spawn: %i\tX: %f\t Y: %f\tAngle: %f\n",spawn, _position->getX(), _position->getY(), getAngle());
		}else if (spawn == 3) {
			_position->setY(-34);
			_position->setX(rand() % 100 - 50);
			setAngle(225 + (rand() % 90));
			//printf("Spawn: %i\tX: %f\t Y: %f\tAngle: %f\n", spawn, _position->getX(), _position->getY(), getAngle());
		}
	}else if(getAlive()){
		if (currentTime - rotateTime > 500) { // Vai alterando o valor do angulo ao longo do tempo 

			setAngle(fmod(getAngle() + randDirection * (0.057 * delta_t), 360));	//direcao da laranja
			rotateTime = currentTime;
		}
		if(level <= 50)
			speed = -0.05 - (level * 0.01);

		setRotate(1.3 + (level * 0.1)); // Rotacao da laranja
		//printf("\nLevel: %i\tSpeed: %f\tRotation: %f\n", level, speed, getRotate());

		speedVec->setX(speed * cos(getAngle() * PI / 180));
		speedVec->setY(speed * sin(getAngle() * PI / 180));
		speedVec->setZ(0);

		setVelocity(speedVec);

		setPosition(_position->add(getVelocity()));
	}

	if (hadCollision()) {
		handleCollision(delta_t);
	}

}

void Orange::draw() {
	if(getAlive()){

		glPushMatrix();
			glTranslatef(_position->getX(), _position->getY(), _position->getZ());
			glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			glScalef(2.0f, 2.0f, 2.0f);
			glPushMatrix();
				glPushMatrix();
				glRotatef(getRotate(), sin(getAngle() * PI / 180),0.0f, cos(getAngle() * PI / 180));

					glPushMatrix();
						//Orange leaf
						GLfloat amb[] = { 0.16f,1.0f,0.25f,1.0f };
						GLfloat diff[] = { 0.21f,0.11f,0.0f,1.0f };
						GLfloat spec[] = { 0.45f,0.17f,0.0f,1.0f };
						GLfloat shine = 35.0f;
						glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb);
						glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff);
						glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec);
						glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine);


						glLineWidth(1);
						glColor3f(0.3f, 0.2f, 0.2f);
						glBegin(GL_LINES);
						glVertex3f(0.0, 1, 0.0);
						glVertex3f(0.4, 1.35, 0);
						glEnd();
					glPopMatrix();

					glPushMatrix();
						glColor3f(0.0f, 0.2f, 0.1f);
						glTranslatef(0.8, 1.35, 0.0);
						glScalef(0.5f, 0.1f, 0.3f);
						glutSolidSphere(1, 10, 10);
					glPopMatrix();
			
					glPushMatrix();
						glColor3f(1.0f, 0.4f, 0.0f);
						glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, getMaterial()->getAmbient());
						glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, getMaterial()->getDiffuse());
						glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, getMaterial()->getSpecular());
						glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, getMaterial()->getShine());
						glutSolidSphere(1.15, 30, 30);
					glPopMatrix();
			
				glPopMatrix();
			glPopMatrix();
		glPopMatrix();
	}
}

void Orange::handleCollision(double delta_t) {
	//printf("ORANGE: collided with %s\n", typeid(*_collider).name());
	((Car*)_collider)->setLost(true);
}