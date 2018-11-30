#ifndef GAMEMANAGER_H_
#define GAMEMANAGER_H_

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

#include <cstdlib>
#include <vector>


#include "GameObject.h"
#include "Camera.h"
#include "OrthogonalCamera.h"
#include "PerspectiveCamera.h"
#include "LightSource.h"
#include "Cereal.h"
#include "Vector3.h"
#include "Vector4.h"
#include "Table.h"
#include "Car.h"
#include "Road.h"
#include "Orange.h"
#include "Butter.h"
#include "DirectSourceLight.h"
#include "PointSourceLight.h"
#include "SpotSourceLight.h"


#define NUM_ORANGES 3
#define IMMUNE_TIME 3
#define LIVES_NUMBER 5

//Pedro

#define PAUSED "paused.png"
#define GAME_OVER "game_over.png"
#define LIVES "lives.png"

//JoaoPedro
//#define PAUSED "C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\paused.png"
//#define GAME_OVER "C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\game_over.png"
//#define LIVES "C:\\Users\\Joao Pedro\\Desktop\\glut\\Simple OpenGL Image Library\\lives.png"

//GoncaloPortatil
//#define PAUSED "..\\..\\..\\..\\Projecto\\micromachines_portable\\proj\\paused.png"
//#define GAME_OVER "..\\..\\..\\..\\Projecto\\micromachines_portable\\proj\\game_over.png"
//#define LIVES "..\\..\\..\\..\\Projecto\\micromachines_portable\\proj\\lives.png"

class GameManager {
	private:
		std::vector<GameObject*> _gameobjects;
		std::vector<DynamicObject*> _dynamicobjects;
		std::vector<Camera*> _cameras;
		std::vector<PointSourceLight*> _pointlights;
		std::vector<SpotSourceLight*> _spotlights;
		std::vector<Cereal*> _cerealArray;

		int _cameraLook = 1;
		OrthogonalCamera* _orthogonalCamera;
		OrthogonalCamera* _livesCamera;
		PerspectiveCamera* _perspectiveCamera2, *_perspectiveCamera3;
		Camera* _camera;
        DirectSourceLight* _light;
		Cereal* aux_cereal;
		double _oldTimeSinceStart;

		Vector3* _eye = new Vector3();
		Vector3* _at = new Vector3();
		Vector3* _up = new Vector3();

		int frameCount;
		double delta_t = 0;
		bool _wireMode = false;
		int _lives = 5;
		Car* _liveCar;
		Car* _car;
		Table* _table;
		Road* _road;
		int _immuneCount = 0;


		bool _lost = false;
        bool _on = true;
		bool _stop= false;
		bool _shademode = true;


		bool _paused;
		bool _game_over;
		bool _restart;

		GLuint pauseTex;


	public:
		GameManager();
		~GameManager();
		void display();
		void reshape(GLsizei w, GLsizei h);
        void keySpecial(int key, int x, int y);
		void keySpecialUp(int key, int x, int y);
        void keyPressed(unsigned char key, int x, int y);
		void onTimer();
		void idle();
		void update();
		void init();
		void levelUp();
		bool Collided(DynamicObject* a, DynamicObject* b);

		void addEntity(DynamicObject* gameObject);
		void addEntity(Camera* camera);
		void addEntity(PointSourceLight* lightSource);
		void addEntity(SpotSourceLight* lightSource);
    
        void ColisionStop(bool value);
		bool GetColisionStop();
		void setLost(bool value);
		bool getLost();
		void setLightOn(bool value);
		bool getLightOn();
		void spawnOrange();
		void decreaseLives();
		void resetLives();
		int  getLives();
		void gameOver();
		void drawLives();
		void checkImmune();

		void setPaused(bool value);
		bool getPaused();
		void setRestart(bool value);
		bool getRestart();
		void drawMessage(char* message);
		void loadTexture(char* texture);

		void setGameOver(bool value);
		bool getGameOver();
		void restartGame();
};
#endif