#ifndef GAMEOBJECT_H_
#define GAMEOBJECT_H_

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

#include "Entity.h"
#include "Material.h"

class GameObject: public Entity {
	protected:
		Material* _material;
	public:
		GameObject();
		~GameObject();
		virtual void draw() = 0;
		virtual void update(double delta_t);
		void setMaterial(Material* material);
		Material* getMaterial();

};
#endif