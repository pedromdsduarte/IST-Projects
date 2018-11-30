# es16al_27-project

  * Create database *

  $ mysql -p -u root

  Enter password: rootroot

  mysql> GRANT ALL PRIVILEGES ON *.* TO 'myDrive'@'localhost' IDENTIFIED BY 'myDriv3' WITH GRANT OPTION;

  mysql> CREATE DATABASE drivedb;

  mysql> \q


  * Get project from GitHub *

  $ git clone https://github.com/tecnico-softeng/es16al_27-project.git


  * Install dml2yuml maven plugin *

  $ git clone https://github.com/tecnico-softeng/dml2yuml.git

  $ cd dml2yuml/dml2yuml

  $ mvn clean install

  $ cd ../dml2yuml-maven-plugin

  $ mvn clean install

  Go to ~/.m2 and edit your settings.xml file or, just get it from (trusted source) https://my.pcloud.com/publink/show?code=XZcH64ZCbCEAOpdVo7BsPKhOobmk0d6lNo7

  * Install dbclean maven plugin *
  
  $ git clone https://github.com/tecnico-softeng/dbclean-maven-plugin.git

  $ cd dbclean-maven-plugin

  $ mvn clean compile install

  $ mvn pt.tecnico.plugin:dbclean-maven-plugin:1.0-SNAPSHOT:dbclean

  $ cd es16al_27-project 

  $ mvn dbclean:dbclean

  * Edit/Run the project commands *

  $ mvn clean compile		--> compile project

  $ mvn clean package           --> create package

  $ mvn dbclean:dblean		--> Reset drivedb database

  $ mvn exec:java -Dexec.args="src/main/resources/other.xml" --> to import xml
  
  $ mvn clean compile package dbclean:dbclean exec:java -Dmaven.test.skip=true --> ignore tests
  
  $ mvn test --> only run tests

  
