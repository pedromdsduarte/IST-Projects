

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

#include "LightSource.h"
#include "Vector3.h"

Vector3* CalculateNormal(Vector3* vertice0, Vector3* vertice1, Vector3* vertice2);
void Normal(Vector3* normal);
