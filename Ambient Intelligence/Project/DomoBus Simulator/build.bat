REM Executar na raiz do projecto

javac -d bin/ -cp src src\domospec\AbstractSpecEntity.java
javac -d bin/ -cp src src\domospec\Image.java
javac -d bin/ -cp src src\domospec\Property.java

javac -d bin/ -cp src src\domospec\home\Division.java
javac -d bin/ -cp src src\domospec\home\Floor.java
javac -d bin/ -cp src src\domospec\home\House.java

javac -d bin/ -cp src src\domospec\valuetypes\Array.java
javac -d bin/ -cp src src\domospec\valuetypes\Enumerated.java
javac -d bin/ -cp src src\domospec\valuetypes\Scalar.java
javac -d bin/ -cp src src\domospec\valuetypes\ValueType.java

javac -d bin/ -cp src src\domospec\conversion\Conversion.java
javac -d bin/ -cp src src\domospec\conversion\ConversionFormula.java
javac -d bin/ -cp src src\domospec\conversion\ConversionObject.java

javac -d bin/ -cp src src\domospec\devices\Device.java
javac -d bin/ -cp src src\domospec\devices\DeviceClass.java
javac -d bin/ -cp src src\domospec\devices\DeviceType.java

javac -d bin/ -cp src;external-libraries\* src\App\*.java

javac -d bin/ -cp src;external-libraries\* src\DComm\*.java

