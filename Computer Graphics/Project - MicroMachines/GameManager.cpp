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

#include "GameManager.h"
#include <iostream>
#include <math.h>

#define PI  3.14159265
int frame = 0, time_fps, timebase = 0;

GameManager::GameManager() {
	
}

GameManager::~GameManager() {
	for (GameObject* go : _gameobjects)
		delete go;
	for (DynamicObject* dn : _dynamicobjects)
		delete dn;
	for (Camera* cam : _cameras)
		delete cam;
	for (PointSourceLight* ls : _pointlights)
		delete ls;
	for (Cereal* cereal : _cerealArray)
		delete cereal;
}

void GameManager::display() {
	
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
	//glEnable(GL_COLOR_MATERIAL);
	glEnable(GL_NORMALIZE);

	_table->draw();
	_road->draw();
	for (DynamicObject* obj : _dynamicobjects) {
		if (_wireMode)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		else
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		obj->draw();
	}
	
	for (PointSourceLight* pl : _pointlights) {
		pl->drawPost();
		pl->shine();
	}

	for (SpotSourceLight* sl : _spotlights) {
		sl->shine();
	}
	_light->shine();

	if (getPaused() && !(getGameOver())) {
		drawMessage(PAUSED);
	}

	if (getGameOver()) {
		drawMessage(GAME_OVER);
	}


	/////////////////////////////////////////////
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	_camera->computeProjectionMatrix();
	_camera->computeVisualizationMatrix();
	/////////////////////////////////////////////

	glMatrixMode(GL_PROJECTION);
		glPushMatrix();
			glLoadIdentity();
			_livesCamera->computeProjectionMatrix();
			glDisable(GL_LIGHTING);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			
			drawLives();
			glEnable(GL_LIGHTING);
			glMatrixMode(GL_PROJECTION);
		glPopMatrix();
	glMatrixMode(GL_MODELVIEW);



	glutSwapBuffers();

}

void GameManager::drawLives() {
	//loadTexture("C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\lives.png");
	loadTexture(LIVES);


	glPushMatrix();
	glScalef(0.94f, 0.5f, 1.0f);
	glTranslatef(57, 80, 0);
		glBegin(GL_POLYGON);

		glTexCoord2f(0, 0);
		glVertex3f(-10, 10, 0);
		//esquerdo
		glTexCoord2f(1, 0);
		glVertex3f(10, 10, 0);
		//baixo
		glTexCoord2f(1, 1);
		glVertex3f(10, -10, 0);
		//direita
		glTexCoord2f(0, 1);
		glVertex3f(-10, -10, 0);
		glEnd();
		
	glPopMatrix();


	glDisable(GL_TEXTURE_2D);

	glPushMatrix();
	glTranslatef(107, 80, 0);
	for (int i = 0; i < getLives(); i++) {
		float x = -60 + (3 * i);
		_liveCar->setPosition(x, -45.0f, 0.0f);
		_liveCar->draw();
	}
	glPopMatrix();
}

void GameManager::reshape(GLsizei w, GLsizei h) {
	float xmin = -50.0f, xmax = 50.0f, ymin = -50.0f, ymax = 50.0f;
	float ratio = (xmax - xmin) / (ymax - ymin);
	float aspect = (float)w / h;
	float fovy = 45;
	float znear = 0.01, zfar = 150;
	glViewport(0, 0, w, h);
	
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	_perspectiveCamera2->update(fovy, ratio, aspect, znear, zfar);
	_perspectiveCamera3->update(fovy, ratio, aspect, znear, zfar);
	_orthogonalCamera->update(xmin, xmax, ymin, ymax, -20.0f, 15.0f, ratio, aspect);
	_livesCamera->update(xmin, xmax, ymin, ymax, -5, 10, ratio, aspect);
}

void GameManager::keySpecial(int key, int x, int y) {
	if (key == GLUT_KEY_RIGHT){
		_car->setKeyPress(GLUT_KEY_RIGHT, true);
    }

    else if (key == GLUT_KEY_LEFT){
		_car->setKeyPress(GLUT_KEY_LEFT, true);
    }

    else if (key == GLUT_KEY_UP){
		_car->setKeyPress(GLUT_KEY_UP, true);
    }

    else if (key == GLUT_KEY_DOWN) {
		_car->setKeyPress(GLUT_KEY_DOWN, true);
    }

   // glutPostRedisplay();
}

void GameManager::keySpecialUp(int key, int x, int y) {
	if (key == GLUT_KEY_RIGHT) {
		_car->setKeyPress(GLUT_KEY_RIGHT, false);
	}

	else if (key == GLUT_KEY_LEFT) {
		_car->setKeyPress(GLUT_KEY_LEFT, false);
	}
	else if (key == GLUT_KEY_UP) {
		_car->setKeyPress(GLUT_KEY_UP, false);
	}

	else if (key == GLUT_KEY_DOWN) {
		_car->setKeyPress(GLUT_KEY_DOWN, false);
	}
	//glutPostRedisplay();
}

void GameManager::keyPressed(unsigned char key, int x, int y) {

	unsigned char keyPressed = toupper(key);

	switch (keyPressed) {
	case 27:	//ESCAPE_KEY
		exit(0);

	case 'A':
		_wireMode = !_wireMode;
		break;

	case '1':
		if (_cameraLook != 1 && !getPaused()) {
			printf("Changed to camera 1\n");
			_cameraLook = 1;
			_camera = _orthogonalCamera;
			_camera->computeProjectionMatrix();
			_camera->computeProjectionMatrix();
		}
		break;
	case '2':
		if (_cameraLook != 2 && !getPaused()) {
			printf("Changed to camera 2\n");
			_cameraLook = 2;
			_camera = _perspectiveCamera2;

			_eye->set(50, 50, 50);
			_at->set(0, 0, 0);
			_up->set(0, 0, 1);

			_camera->setParam(_eye, _at, _up);
			_camera->computeProjectionMatrix();
			_camera->computeVisualizationMatrix();
		}
		break;
	case '3':
		if (_cameraLook != 3 && !getPaused()) {
			printf("Changed to camera 3\n");
			_cameraLook = 3;
			_camera = _perspectiveCamera3;

			_camera->computeProjectionMatrix();
			_camera->computeVisualizationMatrix();
		}
		break;
	case 'N':
		if (glIsEnabled(GL_LIGHTING) && !getPaused()) {
			printf("GL_LIGHT0: %d -> %d\n", glIsEnabled(_light->getNum()), !glIsEnabled(_light->getNum()));
			if (_light->getState()) {
				_light->setState(false);
				glDisable(_light->getNum());
			}
			else {
				_light->setState(true);
				glEnable(_light->getNum());
			}

		}
		break;
	case 'L':
		if (!getPaused()) {
			printf("GL_LIGHTING: %d -> %d\n", glIsEnabled(GL_LIGHTING), !glIsEnabled(GL_LIGHTING));
			if (glIsEnabled(GL_LIGHTING))
				glDisable(GL_LIGHTING);
			else
				glEnable(GL_LIGHTING);
		}
		break;
	case 'G':
		if (!getPaused()) {
			printf("ShadeModel: %d -> %d\n", _shademode, !_shademode);
			_shademode = !_shademode;
			if (_shademode)
				glShadeModel(GL_SMOOTH);
			else
				glShadeModel(GL_FLAT);
		}
		break;
	case 'C':
		if (!getPaused()) {
			if (glIsEnabled(GL_LIGHTING)) {
				printf("CANDLES: %d -> %d\n", _pointlights[0]->getState(), !_pointlights[0]->getState());

				if (_pointlights[0]->getState()) {
					for (PointSourceLight* pl : _pointlights) {
						pl->setState(false);
						glDisable(pl->getNum());
					}
				}
				else {
					for (PointSourceLight* pl : _pointlights) {
						pl->setState(true);
						glEnable(pl->getNum());
					}
				}
			}
		}
		break;
	case 'H':
		if (!getPaused()) {
			if (glIsEnabled(GL_LIGHTING)) {
				printf("SPOTLIGHTS: %d -> %d\n", _spotlights[0]->getState(), !_spotlights[0]->getState());

				if (_spotlights[0]->getState()) {
					for (SpotSourceLight* sl : _spotlights) {
						sl->setState(false);
						glDisable(sl->getNum());
					}
				}
				else {
					for (SpotSourceLight* sl : _spotlights) {
						sl->setState(true);
						glEnable(sl->getNum());
					}
				}
			}
		}
		break;

	case 'S':
		if(!getGameOver())
			setPaused(!getPaused());
		if (getPaused()&&!getGameOver())
			drawMessage(PAUSED);
		break;
	case 'R':
		if (getLives() == 0) {
			setPaused(false);
			setGameOver(false);
			restartGame();
		}
		break;
	}
}

void GameManager::idle() {

}

void GameManager::onTimer() {
	if (!getPaused())
		update();
}

void GameManager::levelUp() {
	if (!getPaused()) {
		int level = 0;
		for (DynamicObject* go : _dynamicobjects)
			if (typeid(Orange).name() == typeid(*go).name()) {
				if (go->getLevel() <= 50) {
					level = go->getLevel() + 1;
					go->setLevel(level);
				}
			}
		printf("levelUP %d\n", level);
	}
}

void GameManager::spawnOrange() {
	if (!getPaused()) {
		for (DynamicObject* go : _dynamicobjects)
			if (typeid(Orange).name() == typeid(*go).name()) {
				if (!(go->getAlive())) {
					go->setAlive(true);
					printf("spawned an orange!\n");
					break;
				}
			}
	}

}

bool GameManager::Collided(DynamicObject* a, DynamicObject* b) {
	if ((typeid(*a).name() != typeid(*b).name())) {			//se nao for carro VS carro
		return (a->getHitBox() + b->getHitBox() > a->getPosition()->getDistance(b->getPosition()));
	}
	return false;
}


void GameManager::update() {
	int timeSinceStart = glutGet(GLUT_ELAPSED_TIME);
	delta_t = timeSinceStart - _oldTimeSinceStart;
	_oldTimeSinceStart = timeSinceStart;

	int fps = 0;
	frame++;
	time_fps = timeSinceStart;
	if (time_fps - timebase > 1000) {
		fps = frame * 1000.0 / (time_fps - timebase);
		printf("FPS: %d\n", fps);
		timebase = time_fps;
		frame = 0;

		//Incrementa immune time
		_immuneCount++;
	}
	checkImmune();

	if (_car->getLost() && !_car->getImmune()) {
		decreaseLives();
		printf("PERDEU\t VIDAS:%d\n", getLives());
		_immuneCount = 0;
		if (getLives() == 0) {
			//setLost(true);
			setGameOver(true);
			setPaused(true);
			gameOver();
			printf("GAME OVER\t VIDAS:%d\n", getLives());
		}
	}else if (_car->getLost()) {
		_car->setLost(false);
	}

	for (DynamicObject* go : _dynamicobjects) {
		if (Collided(_car, go)) {
			go->setCollision(true, _car);  //TODO
		}
		else
			go->setCollision(false, _car);
		go->update(delta_t);
	}

	for (SpotSourceLight* sl : _spotlights) {
		sl->updatePos(_car->getPosition(), _car->getRotate());
	}

	_perspectiveCamera3->updatePos(_car->getPosition(), _car->getRotate());
	glutPostRedisplay();
}

void GameManager::init() {

	 _oldTimeSinceStart = 0;

	///////////////////////////////
	//	CAMERA INITIALIZATION	//
	///////////////////////////////////////////////////////////////////

	_orthogonalCamera = new OrthogonalCamera();
	_livesCamera = new OrthogonalCamera();
	_perspectiveCamera2 = new PerspectiveCamera();
	_perspectiveCamera3 = new PerspectiveCamera();
	_camera = _orthogonalCamera;

	 
	///////////////////////////////
	//	OBJECT INITIALIZATION	//
	///////////////////////////////////////////////////////////////////

	srand(time(0));
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //Set background color to black and opaque
	glClearDepth(1.0f); //Set background depth to farthest
	glEnable(GL_DEPTH_TEST); //Enable depth testing for z-culling
	glDepthFunc(GL_LEQUAL); //Set the type of depth-test
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); //Nice prespective corrections
	for (int i = 0; i < 106; i++) {
		aux_cereal = new Cereal();
		_cerealArray.push_back(aux_cereal);
		addEntity(aux_cereal);
	}
	
	_liveCar = new Car(new Vector3(0.0f, 0.0f, 0.0f));
	_car = new Car(new Vector3(ORIG_X_CAR, ORIG_Y_CAR, ORIG_Z_CAR));
	_table = new Table();
	_road = new Road(_cerealArray);

	addEntity(_car);

	for (int i = 0; i < NUM_ORANGES; i++) {
		addEntity(new Orange());
	}


	addEntity(new Butter(new Vector3(-13.0f,-20.0f, -0.25f)));
	addEntity(new Butter(new Vector3(-15.0f, 10.0f, -0.25f)));
	addEntity(new Butter(new Vector3(-30.0f, 27.5f, -0.25f)));
	addEntity(new Butter(new Vector3(10.0f, 3.0f, -0.25f)));
	addEntity(new Butter(new Vector3(25.0f, 20.0f, -0.25f)));
    _oldTimeSinceStart = glutGet(GLUT_ELAPSED_TIME);

	///////////////////////////////
	//	LIGHT INITIALIZATION	//
	///////////////////////////////////////////////////////////////////
	glEnable(GL_LIGHTING);
	glShadeModel(GL_SMOOTH);


	//Default lightning (night mode)
	GLfloat night[] = { 0.1, 0.1, 0.2, 1.0 };
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, night);

	_light = new DirectSourceLight(GL_LIGHT0);
	_light->setAmbient(new Vector4(0.8, 0.5, 0.5, 1.0));	//sets light that comes from all directions to "dusk mode"
	_light->setDiffuse(new Vector4(0.8, 0.5, 0.5, 1.0));	//diffuse reflection reflects light in all directions
	_light->setSpecular(new Vector4(1.0, 1.0, 1.0, 1.0)); 	//sets the specular color (white)
	_light->setDirection(new Vector4(1, 1, 0.5, 0));			//direction from which the light is shining

	Vector4* amb = new Vector4(0.5, 0.2, 0.2, 1);
	Vector4* diff = new Vector4(0.7, 0.5, 0.5, 1);
	Vector4* spec = new Vector4(1, 1, 1, 1);

	//Candles 
	for (int i = 1; i <= 5; i++) {
		addEntity(new PointSourceLight(GL_LIGHT0 + i));
	}

	_pointlights[0]->setPosition(new Vector4(ORIG_X_CAR + 30, ORIG_Y_CAR + 4, ORIG_Z_CAR + 1, 1));
	_pointlights[1]->setPosition(new Vector4(-25, -13.0, ORIG_Z_CAR + 1, 1));
	_pointlights[2]->setPosition(new Vector4(-25, 10, ORIG_Z_CAR + 1, 1));
	_pointlights[3]->setPosition(new Vector4(-17, 25, ORIG_Z_CAR + 1, 1));
	_pointlights[4]->setPosition(new Vector4(10, 27, ORIG_Z_CAR + 1, 1));

	for (PointSourceLight* pl : _pointlights) {
		pl->setAmbient(amb);
		pl->setDiffuse(diff);
		pl->setSpecular(spec);
		pl->setAttenuation(0.5, 0.5, 0.03);	// constant, linear, quadratic
		pl->setState(false);
	}

	//Spotlights
	for (int i = 6; i <= 7; i++) {
		addEntity(new SpotSourceLight(GL_LIGHT0 + i));
	}
	

	for (SpotSourceLight* sl : _spotlights) {
		sl->setAmbient(new Vector4(0.6, 0.3, 0.3, 1));
		sl->setDiffuse(new Vector4(0.2, 0.2, 0.2, 1));
		sl->setSpecular(new Vector4(1, 1, 1, 1));
		sl->setCutOff(45.0f);
		sl->setExponent(64);
		sl->setAttenuation(1,0.01,0.001);
		sl->setState(false);
	}

	//Nota: direcao das luzes é definida no update
	_spotlights[0]->setPosition(new Vector4(_car->getPosition()->getX(), _car->getPosition()->getY()+1, _car->getPosition()->getZ(), 1));
	_spotlights[1]->setPosition(new Vector4(_car->getPosition()->getX(), _car->getPosition()->getY()-1, _car->getPosition()->getZ(), 1));



	glEnable(GL_LIGHT0);

	///////////////////////////////////////////////////////////////////



}

void GameManager::addEntity(DynamicObject* gameObject) {
	_dynamicobjects.push_back(gameObject);
}
void GameManager::addEntity(Camera* camera) {
	_cameras.push_back(camera);
}
void GameManager::addEntity(PointSourceLight* lightSource) {
	_pointlights.push_back(lightSource);
}
void GameManager::addEntity(SpotSourceLight* lightSource) {
	_spotlights.push_back(lightSource);
}
void GameManager::setLost(bool value) {
	_lost = value;
}
void GameManager::ColisionStop(bool value) {
    _stop = value;
}
bool GameManager::GetColisionStop(){
    return _stop;
}
void GameManager::setLightOn(bool value){
    _on= value;
}
bool GameManager::getLightOn(){
    return _on;
}
bool GameManager::getLost() {
	return _lost;
}

void GameManager::setPaused(bool value) {
	_paused = value;
}

bool GameManager::getPaused() {
	return _paused;
}

void GameManager::decreaseLives() {
	_lives--;
}

void GameManager::resetLives() {
	_lives = LIVES_NUMBER;
}

int GameManager::getLives() {
	return _lives;
}

void GameManager::checkImmune() {
	if (_immuneCount >= IMMUNE_TIME) {
		_car->setImmune(false);
	}else{
		_car->setImmune(true);
	}
}


void GameManager::drawMessage(char* message) {

	loadTexture(message);
	//loadTexture("C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\paused.png");

	glPushMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-2, 2, -2, 2, 0,1);
	
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glPushMatrix();

			/*glBegin(GL_POLYGON);
				
				glTexCoord2f(-1, -1);
				glVertex3f(-1, 1, 0);	
										//esquerdo
				glTexCoord2f(-1, 1);
				glVertex3f(-1, -1, 0);
										//baixo
				glTexCoord2f(1, 1);
				glVertex3f(1, -1, 0);
										//direita
				glTexCoord2f(1, -1);
				glVertex3f(1, 1, 0);
			glEnd();*/

		glBegin(GL_POLYGON);

			glTexCoord2f(0, 0);
			glVertex3f(-1, 1, 0);
									//esquerdo
			glTexCoord2f(0, 1);
			glVertex3f(-1, -1, 0);
									//baixo
			glTexCoord2f(1, 1);
			glVertex3f(1, -1, 0);
									//direita
			glTexCoord2f(1, 0);
			glVertex3f(1, 1, 0);
		glEnd();

		glPopMatrix();

		glPopMatrix();
	glutPostRedisplay();

	glDisable(GL_TEXTURE_2D);

}

void GameManager::loadTexture(char* name) {

	//Para por a textura transparente
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	glEnable(GL_TEXTURE_2D);
	glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
	glBindTexture(GL_TEXTURE_2D, pauseTex);

	int width, height, channels;
	GLubyte* texture = SOIL_load_image(name, &width, &height, &channels, SOIL_LOAD_RGBA);

	glGenTextures(2, &pauseTex);
	glBindTexture(GL_TEXTURE_2D, pauseTex);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);

	SOIL_free_image_data(texture);
}


void GameManager::gameOver() {
	drawMessage(GAME_OVER);
}

void GameManager::restartGame() {
	resetLives();
	setGameOver(false);
	_cerealArray.clear();
	_dynamicobjects.clear();
	for (int i = 0; i < 106; i++) {
		aux_cereal = new Cereal();
		_cerealArray.push_back(aux_cereal);
		addEntity(aux_cereal);
	}
	_road = new Road(_cerealArray);

	addEntity(new Butter(new Vector3(-13.0f, -20.0f, -0.25f)));
	addEntity(new Butter(new Vector3(-15.0f, 10.0f, -0.25f)));
	addEntity(new Butter(new Vector3(-30.0f, 27.5f, -0.25f)));
	addEntity(new Butter(new Vector3(10.0f, 3.0f, -0.25f)));
	addEntity(new Butter(new Vector3(25.0f, 20.0f, -0.25f)));

	_car = new Car(new Vector3(ORIG_X_CAR, ORIG_Y_CAR, ORIG_Z_CAR));
	addEntity(_car);

	for (int i = 0; i < NUM_ORANGES; i++) {
		addEntity(new Orange());
	}

	for (DynamicObject* go : _dynamicobjects) {
		if (typeid(Orange).name() == typeid(*go).name()) {
			go->setLevel(0);
			//go->setAlive(true);
		}
	}


	//setRestart(true);
}

void GameManager::setGameOver(bool value) {
	_game_over = value;
}

bool GameManager::getGameOver() {
	return _game_over;
}
void GameManager::setRestart(bool value) {
	_restart = value;
}

bool GameManager::getRestart() {
	return _restart;
}