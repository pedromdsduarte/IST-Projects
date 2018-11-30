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
#include "DynamicObject.h"

DynamicObject::DynamicObject(){
	_velocity = new Vector3();
	_rotation = 0.0f;
	_direction = new Vector3();
}

DynamicObject::~DynamicObject(){
	
}

void DynamicObject::draw(){
}

void DynamicObject::update(double delta){
	
}

void DynamicObject::rotate() {
	glRotatef(_rotation, 0.0f, 1.0f, 0.0f);
}

void DynamicObject::setRotate(float angle) {
	_rotation = fmod((_rotation + angle),360);
}

float DynamicObject::getRotate() {
	return _rotation;
}


void DynamicObject::setVelocity(Vector3* vec){
	_velocity = vec;
}

void DynamicObject::setVelocity(double x, double y, double z) {
	_velocity->set(x, y, z);
}

Vector3* DynamicObject::getVelocity() {
	return _velocity;
}

void DynamicObject::setHitBox(double hitBox) {
	_hitBox = hitBox;
}

double DynamicObject::getHitBox() {
	return _hitBox;
}

void DynamicObject::setCollision(bool value, DynamicObject* obj) {
	_collision = value;
	_collider = obj;
}

bool DynamicObject::hadCollision() const {
	return _collision;
}


void DynamicObject::handleCollision(double delta_t) {
	//printf("Collided with %s\n", typeid(*this).name());
}

void DynamicObject::setLevel(int value) {

}

int DynamicObject::getLevel() {
	return 0;
}

bool DynamicObject::getAlive() {
	return _alive;
}

void DynamicObject::setAlive(bool value) {
	_alive = value;
}

double DynamicObject::getSpeed() {
	return speed;
}
