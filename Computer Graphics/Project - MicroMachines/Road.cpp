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
#include "Road.h" 
#include "Cereal.h"
#include <vector>
#include "Vector3.h"
#include <math.h>


Road::Road(std::vector<Cereal*> cerealArray){
	respawnRoad(cerealArray);
}


Road::~Road() {

}


//Quadratic BezierFunc Aux
int Road::getPt(int n1, int n2, float perc) {
	int diff = n2 - n1;
	return n1 + (diff*perc);
}


// Cubic BezierFunc
Vector3* Road::bezierFunc(Vector3* A, Vector3* B, Vector3* C, Vector3* D, double t) {
	Vector3* P = new Vector3();
	P->setX(pow((1 - t), 3) * A->getX() + 3 * t * pow((1 - t), 2) * B->getX() + 3 * (1 - t)* pow(t, 2)* C->getX() + pow(t, 3)*D->getX());
	P->setY(pow((1 - t), 3) * A->getY() + 3 * t * pow((1 - t), 2) * B->getY() + 3 * (1 - t)* pow(t, 2)* C->getY() + pow(t, 3)*D->getY());
	P->setZ(double(1.5f));
	return P;
}

void Road::draw() {
	
	for (Cereal* cereal : _cerealArray) {
		cereal->draw();
	}

}


void Road::respawnRoad(std::vector<Cereal*> cerealArray) {
	Vector3* vec;
	float xa, ya, xb, yb, x, y;
	float i;
	int num_cereals = 0;
	_cerealArray = cerealArray;

	//Curve 1
	for (i = 0; i < 1; i += 0.10) {
		xa = getPt(-35.0f, -35.0f, i);
		ya = getPt(10.0f, 35.0f, i);
		xb = getPt(-35.0f, -10.0f, i);
		yb = getPt(35.0f, 35.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//Curve 2
	for (i = 0; i < 1; i += 0.20) {
		xa = getPt(-25.0f, -25.0f, i);
		ya = getPt(10.0f, 25.0f, i);
		xb = getPt(-25.0f, -10.0f, i);
		yb = getPt(25.0f, 25.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//Curve 3
	for (i = 0; i < 1; i += 0.1) {
		xa = getPt(-8.0f, 15.0f, i);
		ya = getPt(34.0f, 27.0f, i);
		xb = getPt(15.0f, 20.0f, i);
		yb = getPt(26.0f, 15.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}
	// curve 3 interior
	for (i = 0; i < 1; i += 0.1) {
		xa = getPt(-18.0f, 5.0f, i);
		ya = getPt(24.0f, 17.0f, i);
		xb = getPt(5.0f, 10.0f, i);
		yb = getPt(16.0f, 5.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}



	//Curve 4
	for (double t = 0.0; t <= 1.0; t += 0.05) {
		vec = bezierFunc(new Vector3(17.0f, 15.0f, 0.0f), new Vector3(37.0f, 15.0f, 0.0f), new Vector3(37.0f, -30.0f, 0.0f), new Vector3(15.0f, -30.0f, 0.0f), t);

		_cerealArray.at(num_cereals)->setPosition(vec);
		num_cereals++;
	}
	//Curva 4 interior
	for (double t = 0.0; t <= 1.0; t += 0.08) {
		vec = bezierFunc(new Vector3(7.0f, 5.0f, 0.0f), new Vector3(27.0f, 4.0f, 0.0f), new Vector3(27.0f, -20.0f, 0.0f), new Vector3(5.0f, -4.0f, 0.0f), t);

		_cerealArray.at(num_cereals)->setPosition(vec);
		num_cereals++;
	}


	//curve 5
	for (i = 0; i < 1; i += 0.10) {
		xa = getPt(15.0f, 5.0f, i);
		ya = getPt(-31.0f, 0.0f, i);
		xb = getPt(5.0f, -15.0f, i);
		yb = getPt(-28.0f, -22.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//curve 5 interior
	for (i = 0; i < 1; i += 0.2) {
		xa = getPt(5.0f, 3.0f, i);
		ya = getPt(-4.0f, -15.0f, i);
		xb = getPt(3.0f, -17.0f, i);
		yb = getPt(-15.0f, -4.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//curve 6
	for (i = 0; i < 1; i += 0.10) {
		xa = getPt(-15.0f, -25.0f, i);
		ya = getPt(-22.0f, -31.0f, i);
		xb = getPt(-30.0f, -25.0f, i);
		yb = getPt(-34.0f, -9.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//curve 6 interior
	for (i = 0; i < 1; i += 0.18) {
		xa = getPt(-12.0f, -13.0f, i);
		ya = getPt(-6.0f, -0.0f, i);
		xb = getPt(-13.0f, -26.0f, i);
		yb = getPt(0.0f, 9.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}

	//curve 7
	for (i = 0; i < 1; i += 0.15) {
		xa = getPt(-25.0f, -33.0f, i);
		ya = getPt(-9.0f, -0.0f, i);
		xb = getPt(-33.0f, -35.0f, i);
		yb = getPt(0.0f, 10.0f, i);

		x = getPt(xa, xb, i);
		y = getPt(ya, yb, i);

		_cerealArray.at(num_cereals)->setPosition(new Vector3(x, y, 1.5f));
		num_cereals++;
	}
}