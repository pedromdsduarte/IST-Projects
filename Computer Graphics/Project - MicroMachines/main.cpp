#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>
#endif
#include <iostream>


using namespace std;

void changeViewPort(int w, int h) {
	float xmin = -2.0, xmax = 2.0, ymin = -2.0, ymax = 2.0;
	float ratio = (xmax - xmin) / (ymax - ymin);
	float aspect = (float)w / h;
	glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	if (ratio < aspect) {
		float delta = ((ymax - ymin)*aspect - (xmax - xmin)) / 2;
		gluOrtho2D(xmin - delta, xmax + delta, ymin, ymax);
	}
	else {
		float delta = ((xmax - xmin) / aspect - (ymax - ymin)) / 2;
		gluOrtho2D(xmin, xmax, ymin - delta, ymax + delta);
	}
}

void render() {
	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glColor3f(0.0f, 1.0f, 0.0f);
	glutWireCube(2);
	glEnd();
	glFlush();
}


/*
int main(int argc, char* argv[]) {

	// Initialize GLUT
	glutInit(&argc, argv);
	// Set up some memory buffers for our display
	glutInitDisplayMode(GLUT_RGBA | GLUT_SINGLE | GLUT_DEPTH);
	// Set the window size
	glutInitWindowSize(400, 400);
	// Set the window position 
	glutInitWindowPosition(-1, -1);
	// Create the window with the title "Hello,GL"
	glutCreateWindow("Teste");

	// Bind the two functions (above) to respond when necessary
	glutReshapeFunc(changeViewPort);
	glutDisplayFunc(render);

	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_COLOR_MATERIAL);
	

	glutMainLoop();
	return 0;
}*/