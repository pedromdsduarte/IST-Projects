#ifndef DYNAMICOBJECT_H_
#define DYNAMICOBJECT_H_

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

#include "GameObject.h"
#define PI  3.14159265

class DynamicObject : public GameObject {
	protected:
		Vector3* _velocity;
		Vector3* _direction;
		float _rotation;
		double speed = 0;

		double _hitBox;
		bool _collision;
		float _rotate;
		DynamicObject* _collider;
		bool _alive;

	public:
		DynamicObject();
		~DynamicObject();
		void draw();
		void update(double delta_t);
		void rotate();
		void setRotate(float angle);
		float getRotate();
		void setVelocity(Vector3* vec);
		void setVelocity(double x, double y, double z);
		Vector3* getVelocity();

		virtual void setHitBox(double hitBox);
		virtual double getHitBox();
		virtual void setCollision(bool value, DynamicObject* obj);
		virtual bool hadCollision() const;
		virtual void handleCollision(double delta_t);
		virtual void setLevel(int level);
		virtual int  getLevel();
		virtual void setAlive(bool value);
		virtual bool getAlive();
		virtual double getSpeed();
};
#endif