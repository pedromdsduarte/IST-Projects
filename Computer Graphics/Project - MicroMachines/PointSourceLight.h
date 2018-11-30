
#ifndef ____PointSourceLight__
#define ____PointSourceLight__

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
#include "Material.h"

class PointSourceLight : public LightSource {
protected:
	Material* _material;
public:
	PointSourceLight(GLenum num);
	~PointSourceLight();
	void brightness();
	void shine();
	void drawPost();
	void setMaterial(Material* material);
	Material* getMaterial();
};
#endif


