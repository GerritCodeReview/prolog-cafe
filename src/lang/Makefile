################################################################
#	Makefile for Prolog Cafe
################################################################

################################################################
# The following two definitions will be overridden.
#
# JAVAC : the command of Java compiler system
#          (ex. javac)
# JAR   : the command of Jar archive system
#          (ex. jar)
#
JAVAC     = javac
JAVACOPTS = -d . -Xlint
JAR       = jar
JAROPTS   = cvf
################################################################
lang: 
	$(JAVAC)  $(JAVACOPTS) *.java
	$(JAR) $(JAROPTS) lang.jar jp/ac/kobe_u/cs/prolog/lang/

clean:
	-rm -f -r jp
	-rm -f core *~ *.class *.jar

realclean: clean


