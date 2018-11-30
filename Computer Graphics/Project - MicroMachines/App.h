#ifndef APP_H_
#define APP_H_

#ifdef __APPLE__
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#else
#ifdef _WIN32
#include <windows.h>
#endif
#include <GL\glut.h>
#endif


#include "GameManager.h"
#include "StaticObject.h"
#include "Table.h"

#endif