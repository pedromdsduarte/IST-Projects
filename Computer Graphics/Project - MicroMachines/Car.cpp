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
#include "CalculateNormal.h"
#include "Car.h"

//#define IMMUNE {0.5f,0.5f,0.5f,1.0f}
#define IMMUNE {1,1,0,0.7}
#define IMMUNE1 {0.7f,0.7f,0.7f,1.0f}
Car::Car() {
	_lost = false;
    _stop =false;
	
	
}


Car::Car(Vector3 * vec) {
	_position = vec;
	setHitBox(1.5);

	float amb[] = { 0.1745f,0.01175f,0.01175f,1.0f };
	float diff[] = { 0.61424f,0.04136f,0.04136f,1.0f };
	float spec[] = { 0.727811f,0.626959f,0.626959f,1.0f };
	float shine = 76.8f;
	_material = new Material(amb, diff, spec, shine);

}

Car::~Car(){
	
}

void Car::update(double delta_t) {

	if (getLost() && !getImmune()) {
		setPosition(ORIG_X_CAR, ORIG_Y_CAR, ORIG_Z_CAR);
		setLost(false);

		_rotation = 0;
		speed = 0;
		return;
	}
    if (getPosition()->getX()<-36.7|| getPosition()->getY()>36.7 || getPosition()->getX()>37.5 || getPosition()->getY()<-32){
		setLost(true);
		setPosition(ORIG_X_CAR, ORIG_Y_CAR, ORIG_Z_CAR);
		_rotation = 0;
		speed = 0;
		return;
    }
	setVelocity(0, 0, 0);
	
	double max_velocity = -0.25; //velocidade maxima
	double rotate = 0;	//rotacao que o carro vai ter
	double accel = -0.0004;//velocidade que vai ser adicionada
	double atrito = -0.002;
	Vector3* speedVec = new Vector3();
    
    if(GetColisionStop()){
        speed = 0;
		ColisionStop(false);
		return;
    }

	if (_upPressed && !GetColisionStop()) {
		if (speed + accel*delta_t >= max_velocity) {
			speed += accel*delta_t;
		}
		else if (speed + accel*delta_t<max_velocity) {
			speed = max_velocity;
		}
	}
	if (!_upPressed && !_downPressed) {
		if (speed - atrito*delta_t <= 0) {
			speed -= atrito*delta_t;
		}
		else if (speed - atrito*delta_t>0) {
			speed = 0;
		}
        ColisionStop(false);
	}
	if (_rightPressed) {
		rotate -= 3;
	}

	if (_leftPressed) {
		rotate += 3;
	}
	if (_downPressed) rotate = -rotate;
	/*
	if (_upPressed) {
		if (_rightPressed) {
			rotate -= 3;
		}

		if (_leftPressed) {
			rotate += 3;
		}
	}

	if (_downPressed) {
		if (_rightPressed) {
			rotate += 3;
		}

		if (_leftPressed) {
			rotate -= 3;
		}
	}*/



	if (_downPressed && !GetColisionStop()) {
		if (speed - accel*delta_t <= -max_velocity) {
			speed -= accel*delta_t;
		}
		else if (speed - accel*delta_t>-max_velocity) {
			speed = -max_velocity;
		}
	}


	if (speed != 0)			//Para nao rodar no mesmo sitio
		setRotate(rotate);

	speedVec->setX(speed * cos(getRotate() * PI / 180));
	speedVec->setY(speed * sin(getRotate() * PI / 180));
	speedVec->setZ(0);

	setVelocity(speedVec);

	setPosition(_position->add(getVelocity()));
}


void Car::draw(){
	
	glEnable(GL_NORMALIZE);


	glPushMatrix();
		glTranslatef(getPosition()->getX(), getPosition()->getY(), getPosition()->getZ());
		glRotatef(90.0f,1.0f,0.0f,0.0f);
		glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
		rotate();
		glScalef(.85f, 1.0f, 1.0f);
        glPushMatrix();
    
			//RODAS
			for (int i = 0; i < 4; i++) {
				
				double pos1, pos2;
				if (i % 2 == 0) 
					pos1 = 0.75f;
                else{
					pos1 = -0.94f;
                }
                if (i < 2){
					pos2 = 0.92f;
                glColor3f(0.0f, 0.0f, 0.0f);
                glPushMatrix();
                glTranslatef(pos1, 0.0f, pos2);
                glPushMatrix();
                glScaled(1.55f,1.55f,1.55f);
                glRotatef(-90.0f,0.0f,1.0f,0.0f);
                glRotatef(90.0f,0.0f,0.0f,1.0f);
                glPushMatrix();
                GLfloat amb[] = { 0.02f,0.02f,0.02f,0.02f };
                GLfloat diff[] = { 0.01f,0.01f,0.01f,1.0f };
                GLfloat spec[] = { 0.4f,0.4f,0.4f,1.0f };
                GLfloat shine = 0.078125f;
                
				if (!getImmune()) {
					glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb);
					glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff);
					glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec);
					glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine);
				}
				else {
					GLfloat immune_amb[] = IMMUNE;
					GLfloat immune_diff[] = IMMUNE;
					GLfloat immune_spec[] = IMMUNE1;
					GLfloat immune_shine = 0.078125f;
					glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb);
					glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff);
					glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec);
					glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine);
				}

				glBegin(GL_POLYGON);
                glBegin(GL_TRIANGLES);
                drawWheel();
                }
                else{
					pos2 = -0.94f;
                    glColor3f(0.0f, 0.0f, 0.0f);
                    glPushMatrix();
                    glTranslatef(pos1, 0.0f, pos2);
                    glPushMatrix();
                    glScaled(1.55f,1.55f,1.55f);
                    glRotatef(90.0f,0.0f,1.0f,0.0f);
                    glRotatef(90.0f,0.0f,0.0f,1.0f);
                    glPushMatrix();
                    GLfloat amb[] = { 0.02f,0.02f,0.02f,0.02f };
                    GLfloat diff[] = { 0.01f,0.01f,0.01f,1.0f };
                    GLfloat spec[] = { 0.4f,0.4f,0.4f,1.0f };
                    GLfloat shine = 0.078125f;
                    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb);
                    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff);
                    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec);
                    glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine);
                    glBegin(GL_POLYGON);
                    glBegin(GL_TRIANGLES);
                    drawWheel();
                }
               
			}

			//Chassis
    
			//glPushMatrix();
            GLfloat amb1[] = { 0.05f,0.0f,0.0f,1.0f };
            GLfloat diff1[] = { 0.61424f,0.04136f,0.04136f,1.0f };
            GLfloat spec1[] = { 0.727811f,0.04f,0.04f,1.0f };
            GLfloat shine1 = 0.078125f;
            
			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec1);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine1);
			}
			else {
				GLfloat immune_amb1[] = IMMUNE;
				GLfloat immune_diff1[] = IMMUNE;
				GLfloat immune_spec1[] = IMMUNE1;
				GLfloat immune_shine1 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff1);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec1);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine1);
			}

			/*glBegin(GL_POLYGON);
			glVertex3f(1.50f, -0.25f, 0.75f);
			glVertex3f(1.50f, -0.25f, -0.75f);
			glVertex3f(-1.50f, -0.25f, -0.75f);
			glVertex3f(-1.50f, -0.25f, 0.75f);
            normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(1.50f, -0.25f, -0.75f), new Vector3(-1.50f, -0.25f, -0.75f)));
            Normal(normal);
			glEnd();
			glPopMatrix();*/

			//Para-choques
			glColor3f(1.0f, 0.0f, 0.0f);
            /*glPushMatrix();
			glBegin(GL_POLYGON);
            normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(1.50f, -0.25f, -0.75f), new Vector3(2.00f, -0.25f, 0.0f)));
            Normal(normal);
			glVertex3f(1.50f, -0.25f, 0.75f);
			glVertex3f(1.50f, -0.25f, -0.75f);
			glVertex3f(2.00f, -0.25f, 0.0f);
			glEnd();
			glPopMatrix();*/
    
            glColor3f(1.0f, 0.0f, 0.0f);
			glPushMatrix();
			glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(2.00f, -0.25f, 0.00f), new Vector3(2.00f, 0.50f, 0.00f)));
            //Normal(normal);
            glNormal3f(0.5625, 0, 0.375);
			glVertex3f(1.50f, -0.25f, 0.75f);
            glNormal3f(0.5625, 0, 0.375);
			glVertex3f(2.00f, -0.25f, 0.00f);
            glNormal3f(0.5625, 0, 0.375);
			glVertex3f(2.00f, 0.50f, 0.00f);
			//glVertex3f(1.50f, 0.50f, 0.75f);
			glEnd();
            glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(1.50f, 0.50f, 0.75f), new Vector3(2.00f, 0.5f, 0.00f)));
            //Normal(normal);
            glNormal3f(0.525, 0, 0.375);
            glVertex3f(1.50f, -0.25f, 0.75f);
            glNormal3f(0.525, 0, 0.375);
            glVertex3f(1.50f, 0.50f, 0.75f);
            glNormal3f(0.525, 0, 0.375);
            glVertex3f(2.00f, 0.5f, 0.00f);
            glEnd();
            glPopMatrix();
    
            glColor3f(1.0f, 0.0f, 0.0f);
			glPushMatrix();
    
			glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(2.00f, -0.25f, 0.00f), new Vector3(2.00f, 0.50f, 0.00f)));
            //Normal(normal);
            glNormal3f(0.5625, 0.75, 0.375);
			glVertex3f(1.50f, -0.25f, -0.75f);
            glNormal3f(0.5625, 0, -0.375);
			glVertex3f(2.00f, -0.25f, 0.00f);
			glVertex3f(2.00f, 0.50f, 0.00f);
			//glVertex3f(1.50f, 0.50f, -0.75f);
			glEnd();
    
            glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(2.00f, -0.25f, 0.00f), new Vector3(2.00f, 0.50f, 0.00f)));
            //Normal(normal);
            glVertex3f(1.50f, -0.25f, -0.75f);
            glVertex3f(1.50f, 0.50f, -0.75f);
            //glVertex3f(2.00f, -0.25f, 0.00f);
            glVertex3f(2.00f, 0.50f, 0.00f);
            glEnd();
			glPopMatrix();

			//Para-brisas
			glColor3f(0.091f, 0.055f, 0.236f);
			glPushMatrix();
			GLfloat amb2[] = { 0.0f,0.0f,0.0f,1.0f };
			GLfloat diff2[] = { 0.56f,0.55f,0.55f,1.0f };
			GLfloat spec2[] = { 0.7f,0.7f,0.7f,1.0f };
			GLfloat shine2 = 32.0f;
			
			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb2);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff2);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec2);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine2);
			}
			else {
				GLfloat immune_amb2[] = IMMUNE;
				GLfloat immune_diff2[] = IMMUNE;
				GLfloat immune_spec2[] = IMMUNE1;
				GLfloat immune_shine2 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb2);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff2);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec2);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine2);
			}

			glBegin(GL_POLYGON);
			glVertex3f(2.00f, 0.50f, 0.00f);
			glVertex3f(1.50f, 0.50f, -0.75f);
			glVertex3f(1.25f, 1.00f, -0.50f);
			glVertex3f(1.25f, 1.00f, 0.50f);
			glVertex3f(1.50f, 0.50f, 0.75f);
            normal=(CalculateNormal(new Vector3(2.00f, 0.50f, 0.00f), new Vector3(1.50f, 0.50f, -0.75f), new Vector3(1.25f, 1.00f, -0.50f)));
            Normal(normal);
			glEnd();
			glPopMatrix();

			// Direito
			glColor3f(1.0f, 0.0f, 0.0f);
			glPushMatrix();
            GLfloat amb15[] = { 0.05f,0.0f,0.0f,1.0f };
            GLfloat diff15[] = { 0.61424f,0.04136f,0.04136f,1.0f };
            GLfloat spec15[] = { 0.727811f,0.04f,0.04f,1.0f };
            GLfloat shine15 = 0.078125f;

			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb15);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff15);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec15);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine15);
			}
			else {
				GLfloat immune_amb15[] = IMMUNE;
				GLfloat immune_diff15[] = IMMUNE;
				GLfloat immune_spec15[] = IMMUNE1;
				GLfloat immune_shine15 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb15);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff15);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec15);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine15);
			}
    
            glBegin(GL_TRIANGLES);
            /*normal= (CalculateNormal(new Vector3(1.50f,-0.25f, 0.75f), new Vector3(1.50f, 0.50f, 0.75f), new Vector3(1.50f, 0.50f, 0.75f)));
            Normal(normal->inverte());*/
            glNormal3f(0, 0, 2.25);
            glVertex3f(1.50f, -0.25f, 0.75f);
            glNormal3f(0, 0, 2.25);
            glVertex3f(-1.50f, 0.50f, 0.75f);
            glNormal3f(0, 0, 2.25);
            glVertex3f(1.50f, 0.50f, 0.75f);
            glEnd();
    
			glBegin(GL_TRIANGLES);
            //normal= (CalculateNormal(new Vector3(1.50f, -0.25f, 0.75f), new Vector3(-1.50f, -0.25f, 0.75f), new Vector3(-1.50f, 0.50f, 0.75f)));
            //Normal(normal);
            glNormal3f(0, 0, 2.25);
            glVertex3f(1.50f, 0.25f, 0.75f);
            glNormal3f(0, 0, 2.25);
			glVertex3f(-1.50f, -0.25f, 0.75f);
            glNormal3f(0, 0, 2.25);
			glVertex3f(-1.50f, 0.50f, 0.75f);
			glEnd();
			glPopMatrix();
    
			//Lado Esquerdo
            glColor3f(1.0f, 0.0f, 0.0f);
			glPushMatrix();
            GLfloat amb3[] = { 0.75f,0.0f,0.0f,1.0f };
            GLfloat diff3[] = { 0.61424f,0.04136f,0.04136f,1.0f };
            GLfloat spec3[] = { 0.727811f,0.04f,0.04f,1.0f };
            GLfloat shine3 = 0.078125f;
			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb3);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff3);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec3);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine3);
			}
			else {
				GLfloat immune_amb3[] = IMMUNE;
				GLfloat immune_diff3[] = IMMUNE;
				GLfloat immune_spec3[] = IMMUNE1;
				GLfloat immune_shine3 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb3);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff3);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec3);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine3);
			}
			glBegin(GL_POLYGON);
            normal=(CalculateNormal(new Vector3(-1.50f, -0.25f, -0.75f), new Vector3(1.50f, -0.25f, -0.75f), new Vector3(-1.50f, 0.50f, -0.75f)));
            Normal(normal);
			glVertex3f(-1.50f, -0.25f, -0.75f);
			glVertex3f(1.50f, -0.25f, -0.75f);
			glVertex3f(1.50f, 0.50f, -0.75f);
			glVertex3f(-1.50f, 0.50f, -0.75f);
			glEnd();
			glPopMatrix();

			//Para-choques traseiro
            glColor3f(1.0f, 0.0f, 0.0f);
            glPushMatrix();
			glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(-1.50f, -0.25f, -0.75f), new Vector3(-1.50f, -0.25f, 0.75f), new Vector3(-1.50f, 0.50f, 0.75f)));
            //Normal(normal->inverte());
            glNormal3f(-1.125, 0, 0);
			glVertex3f(-1.50f, -0.25f, 0.75f);
            //glVertex3f(-1.50f, -0.25f, -0.75f);
            glNormal3f(-1.125, 0, 0);
			glVertex3f(-1.50f, 0.50f, 0.75f);
            glNormal3f(-1.125, 0, 0);
			glVertex3f(-1.50f, 0.50f, -0.75f);
			glEnd();
            glBegin(GL_TRIANGLES);
            //normal=(CalculateNormal(new Vector3(-1.50f, -0.25f, -0.75f), new Vector3(-1.50f, -0.25f, 0.75f), new Vector3(-1.50f, 0.50f, 0.75f)));
            //Normal(normal->inverte());
            glNormal3f(-1.125, 0, 0);
            glVertex3f(-1.50f, -0.25f, -0.75f);
            glNormal3f(-1.125, 0, 0);
            glVertex3f(-1.50f, -0.25f, 0.75f);
            //glVertex3f(-1.50f, 0.50f, 0.75f);
            glNormal3f(-1.125, 0, 0);
            glVertex3f(-1.50f, 0.50f, -0.75f);
            glEnd();
			glPopMatrix();

			//Vidro traseiro
            glColor3f(0.0f, 0.0f, 0.0f);
			glPushMatrix();
            GLfloat amb7[] = { 1.0f,1.0f,1.0f,1.0f };
            GLfloat diff7[] = { 0.8,0.8f,0.8f,1.0f };
            GLfloat spec7[] = { 0.5f,0.0f,1.0f,1.0f };
            GLfloat shine7 = 32.0f;
			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb7);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff7);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec7);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine7);
			}
			else {
				GLfloat immune_amb7[] = IMMUNE;
				GLfloat immune_diff7[] = IMMUNE;
				GLfloat immune_spec7[] = IMMUNE1;
				GLfloat immune_shine7 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb7);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff7);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec7);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine7);
			}
			glBegin(GL_POLYGON);
			glVertex3f(-1.50f, 0.50f, -0.75f);
			glVertex3f(-1.50f, 0.50f, 0.75f);
			glVertex3f(-1.25f, 1.00f, 0.50f);
			glVertex3f(-1.25f, 1.00f, -0.50f);
            normal=(CalculateNormal(new Vector3(-1.50f, 0.50f, -0.75f), new Vector3(-1.50f, 0.50f, 0.75f), new Vector3(-1.25f, 1.00f, 0.50f)));
            Normal(normal);
			glEnd();
			glPopMatrix();

			//Teto
    
			glColor3f(1.0f, 1.0f, 1.0f);
			glPushMatrix();

			GLfloat amb5[] = { 1.0f,1.0f,1.0f,1.0f };
			GLfloat diff5[] = { 1.0f,1.00f,1.00f,1.0f };
			GLfloat spec5[] = { 1.0f,1.0f,1.0f,1.0f };
			GLfloat shine5 = 5.25f;

			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec5);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine5);
			}
			else {
				GLfloat immune_amb5[] =IMMUNE;
				GLfloat immune_diff5[] = IMMUNE;
				GLfloat immune_spec5[] = IMMUNE1;
				GLfloat immune_shine5 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, immune_amb5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, immune_diff5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, immune_spec5);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, immune_shine5);
			}
			glBegin(GL_POLYGON);
			glVertex3f(-1.25f, 1.00f, -0.50f);
			glVertex3f(-1.25f, 1.00f, 0.50f);
			glVertex3f(1.25f, 1.00f, 0.50f);
			glVertex3f(1.25f, 1.00f, -0.50f);
            normal=(CalculateNormal(new Vector3(-1.25f, 1.00f, -0.50f), new Vector3(-1.25f, 1.00f, 0.50f), new Vector3(1.25f, 1.00f, 0.50f)));
            Normal(normal);
			glEnd();
			glPopMatrix();

			//Janela Esquerda
			glColor3f(0.0f, 0.0f, 0.0f);
			glPushMatrix();
            GLfloat amb13[] = { 1.0f,1.0f,1.0f,1.0f };
            GLfloat diff13[] = { 1.0,1.0f,1.0f,1.0f };
            GLfloat spec13[] = { 1.0f,1.0f,1.0f,1.0f };
            GLfloat shine13 = 32.0f;
			if (!getImmune()) {
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff5);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec5);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine5);
			}else {
				GLfloat _immune_amb13[] = IMMUNE;
				GLfloat _immune_diff13[] = IMMUNE;
				GLfloat _immune_spec13[] = IMMUNE1;
				GLfloat _immune_shine13 = 0.078125f;
				glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, _immune_amb13);
				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, _immune_diff13);
				glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, _immune_spec13);
				glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, _immune_shine13);
			}
			glBegin(GL_POLYGON);
			glVertex3f(-1.25f, 1.00f, -0.50f);
			glVertex3f(1.25f, 1.00f, -0.50f);
			glVertex3f(1.50f, 0.50f, -0.75f);
			glVertex3f(-1.50f, 0.50f, -0.75f);
            normal=(CalculateNormal(new Vector3(-1.25f, 1.00f, -0.50f), new Vector3(1.25f, 1.00f, -0.50f), new Vector3(1.50f, 0.50f, -0.75f)));
            Normal(normal);
			glEnd();
			glPopMatrix();

			//Janela Direita
            glColor3f(0.0f, 0.0f, 0.0f);
			glPushMatrix();
			glBegin(GL_POLYGON);
			glVertex3f(-1.25f, 1.00f, 0.50f);
			glVertex3f(1.25f, 1.00f, 0.50f);
			glVertex3f(1.50f, 0.50f, 0.75f);
			glVertex3f(-1.50f, 0.50f, 0.75f);
            normal=(CalculateNormal(new Vector3(-1.25f, 1.00f, 0.50f), new Vector3(1.25f, 1.00f, 0.50f), new Vector3(1.50f, 0.50f, 0.75f)));
            Normal(normal);
			glEnd();
			glPopMatrix();


			//Farois
			GLfloat amb14[] = { 0.77f, 0.72f, 0.0745f, 1.0f };
			GLfloat diff14[] = { 0.91f, 0.60648f, 0.0f, 1.0f };
			GLfloat spec14[] = { 1.0f, 1.0f, 0.366065f, 1.0f };
			GLfloat shine14 = 128.0f;
			glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, amb14);
			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diff14);
			glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, spec14);
			glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shine14);
			GLfloat emission[] = { 1.0, 1.0f, 0.0, 1.0 };
			glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, emission);

			glPushMatrix();
				glColor3f(1, 1, 0);
			
				glPushMatrix();
					glTranslatef(1, 0, -0.2);
					
					glRotatef(-45, 0, 1, 0);
					glScalef(1, .2, .3);
					glBegin(GL_POLYGON);
						glVertex3f(1, -1, -1);
						glVertex3f(1, -1, 1);
						glVertex3f(1, 1, 1);
						glVertex3f(1, 1, -1);
					glEnd();
				glPopMatrix();
				glPushMatrix();
					glTranslatef(1, 0, 0.2);
					
					glRotatef(45, 0, 1, 0);
					glScalef(1, .2, .3);
					
					glBegin(GL_POLYGON);
						glVertex3f(1, -1, -1);
						glVertex3f(1, -1, 1);
						glVertex3f(1, 1, 1);
						glVertex3f(1, 1, -1);
					glEnd();
				glPopMatrix();
			glPopMatrix();


		glPopMatrix();
	glPopMatrix();

	glEnable(GL_NORMALIZE);

	GLfloat default[] = { 0, 0, 0, 1.0 };
	glMaterialfv(GL_FRONT, GL_EMISSION, default);

}

void Car::setKeyPress(int key, bool pressed) {
	if (key == GLUT_KEY_RIGHT)
		_rightPressed = pressed;
	else if (key == GLUT_KEY_LEFT)
		_leftPressed = pressed;
	else if (key == GLUT_KEY_UP)
		_upPressed = pressed;
	else if (key == GLUT_KEY_DOWN)
		_downPressed = pressed;
}	

void Car::setLost(bool value) {
	_lost = value;
}

bool Car::getLost() {
	return _lost;
}
void Car::ColisionStop(bool value) {
    _stop = value;
}
bool Car::GetColisionStop(){
    return _stop;
}
double Car::getSpeed(){
    return speed;
}
bool Car::getImmune() {
	return _immune;
}
void Car::setImmune(bool value) {
	_immune = value;
}