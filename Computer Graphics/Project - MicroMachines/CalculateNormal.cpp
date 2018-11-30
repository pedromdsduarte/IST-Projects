#include "CalculateNormal.h"
#include "Vector3.h"
#include <iostream>

Vector3* CalculateNormal(Vector3* vertice0, Vector3* vertice1, Vector3* vertice2){
    Vector3* normal = new Vector3();
    Vector3* v1 = new Vector3();
    Vector3* v2 = new Vector3();
    v1->setX(vertice2->getX()-vertice0->getX());
    v1->setY(vertice2->getY()-vertice0->getY());
    v1->setZ(vertice2->getZ()-vertice0->getZ());
    
    v2->setX(vertice1->getX()-vertice0->getX());
    v2->setY(vertice1->getY()-vertice0->getY());
    v2->setZ(vertice1->getZ()-vertice0->getZ());
    
    normal->setX(v1->getY()*v2->getZ() - v1->getZ() * v2->getY());
    if(normal->getX()!=0){
        normal->setX(normal->getX()/abs(normal->getX()));
    }
    normal->setY(v1->getZ()*v2->getX() - v1->getX() * v2->getZ());
    if(normal->getY()!=0){
        normal->setY(normal->getY()/abs(normal->getY()));
    }
    normal->setZ(v1->getX()*v2->getY() - v1->getY() * v2->getX());
    if(normal->getZ()!=0){
        normal->setZ(normal->getZ()/abs(normal->getZ()));
    }
    return normal;
}

void Normal(Vector3* normal){
    glNormal3f(normal->getX(), normal->getY(), normal->getZ());
}