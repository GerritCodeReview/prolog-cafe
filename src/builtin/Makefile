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
JAVACOPTS = -d . -Xlint -classpath $$PLCAFEDIR/lang.jar:$$CLASSPATH
JAR       = jar
JAROPTS   = cf
T         = ../../target/generated-sources/prologcafe-builtin/jp/ac/kobe_u/cs/prolog/builtin
################################################################
.SUFFIXES:
.SUFFIXES: .am .pl $(SUFFIXES)

am_objects = builtins.am \
	system.am \

.pl.am:
	../../bin/pl2am.plc -v -O $< $@
	-mkdir -p $(T)
	../../bin/am2j.plc -v -d $(T) $@

builtin:$(am_objects)
	$(JAVAC) $(JAVACOPTS) *.java $(T)/*.java
	$(JAR) $(JAROPTS) builtin.jar jp/ac/kobe_u/cs/prolog/builtin/

clean:
	-rm -f -r $(T)
	-rm -f -r jp
	-rm -f Base/core Base/*~ Base/*.class
	-rm -f core *~ *.am *.jar *.class

realclean: clean
