#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#include <string>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#include <GL\GL.h>
#include <GL\GLU.h>
#endif

#include "Material.h"

Material::Material() {

}

Material::Material(float ambient[], float diffuse[], float specular[], float shine) {
	std::copy(ambient, ambient + sizeof(ambient), _ambient);
	std::copy(diffuse, diffuse + sizeof(diffuse), _diffuse);
	std::copy(specular, specular + sizeof(specular), _specular);
	_shine = shine;

}

float* Material::getAmbient() {
	return _ambient;
}

float* Material::getDiffuse() {
	return _diffuse;
}

float* Material::getSpecular() {
	return _specular;
}

float Material::getShine() {
	return _shine;
}

void Material::setAmbient(float ambient[]) {
	std::copy(ambient, ambient + sizeof(ambient), _ambient);
}

void Material::setDiffuse(float diffuse[]) {
	std::copy(diffuse, diffuse + sizeof(diffuse), _diffuse);
}

void Material::setSpecular(float specular[]) {
	std::copy(specular, specular + sizeof(specular), _specular);
}

void Material::setShine(float shine) {
	_shine = shine;
}