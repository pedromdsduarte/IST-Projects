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

#include "GameObject.h"
#define PI  3.14159265


GameObject::GameObject() : Entity() {
}

GameObject::~GameObject() {

}

//virtual void GameObject::draw() = 0;

void GameObject::update(double delta_t) {

}

void GameObject::setMaterial(Material* material) {
	_material = material;
}

Material* GameObject::getMaterial() {
	return _material;
}