JC = javac
.SUFFIXES: .java .class
.java.class:
			$(JC)	$*.java

CLASSES = \
		tes/TES.java\
        user/user.java \
        ecp/ECP.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class