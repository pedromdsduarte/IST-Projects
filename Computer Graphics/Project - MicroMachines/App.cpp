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

#include "App.h"
#include <iostream>

GameManager* gm;


void timer(int value) {
	//glutPostRedisplay();
	if (value == 0) {
		gm->onTimer();
		glutTimerFunc(10, timer, 0);
	}else if (value == 1) {
		gm->levelUp();
		glutTimerFunc(10000, timer, 1); // Level up after 10 seconds
	}
	else if (value == 2) {
		gm->spawnOrange();
		glutTimerFunc(rand() % 6000, timer, 2); //Spawn orange at random time between 1 and 6 seconds
	}

}

void reshape(GLsizei w, GLsizei h) {
	gm->reshape(w, h);
}

void display() {
	gm->display();
}

void keyPressed(unsigned char key, int x, int y) {
	gm->keyPressed(key, x, y);
	if (gm->getRestart()) {
		gm = new GameManager();
		gm->init();
	}
}
void keySpecial(int key, int x, int y) {
    gm->keySpecial(key, x, y);
}

void keySpecialUp(int key, int x, int y) {
	gm->keySpecialUp(key, x, y);
}

int main(int argc, char** argv) {
	gm = new GameManager();
	
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
	glutInitWindowSize(900, 700);
	glutInitWindowPosition(400, 0);
	glutCreateWindow("Micromachines");

	glEnable(GL_DEPTH_TEST);
	gm->init();

	glutReshapeFunc(reshape);
	glutDisplayFunc(display);
	glutKeyboardFunc(keyPressed);
    glutSpecialFunc(keySpecial);
	glutSpecialUpFunc(keySpecialUp);

	glutTimerFunc(1, timer, 0);	//on timer
	glutTimerFunc(1000, timer, 1); //increase level
	glutTimerFunc(100, timer, 2); //spawn orange

	glutMainLoop();

	return 0;
}
