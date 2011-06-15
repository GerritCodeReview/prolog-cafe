################################################################
#	Makefile for Prolog Cafe
################################################################

################################################################
# The following two definitions will be overridden.
#
# PROLOG : the command of Prolog system 
#          (ex. sicstus, swipl, or prolog)
# PSYSTEM: the type of Prolog system 
#          (ex. 'SICStus', 'SWI-Prolog' or others)
#
PROLOG  = sicstus
PSYSTEM = 'SICStus'
#PROLOG  = swipl
#PSYSTEM = 'SWI-Prolog'

# JAVAC : the command of Java compiler system (ex. javac)
# JAR   : the command of Jar archive system (ex. jar)
JAVAC     = javac
JAVACOPTS = -d . -Xlint -classpath $$PLCAFEDIR/lang.jar:$$PLCAFEDIR/builtin.jar:$$CLASSPATH
JAR       = jar
JAROPTS   = cvf
# JAVA  : the command of Java Virtual Machine (ex. java)
JAVA      = java
T         = ../../target/generated-sources/prologcafe
P         = com/googlecode/prolog_cafe/compiler
################################################################
.SUFFIXES:
.SUFFIXES: .plj .txt .am .plc .pl $(SUFFIXES)

.pl.plc:
	cp ../builtin/system.pl  .
	./comp_pl -v `pwd`/$*.pl `pwd`/$*.plc $(PROLOG) $(PSYSTEM)

.pl.am:
	../../bin/pl2am.plc -v -O $< $@
	-mkdir $T-$*
	../../bin/am2j.plc -v -d $T-$* $@

.txt.plj:
	./comp_pl -v `pwd`/$*.pl `pwd`/$*.plj $(JAVA) 'PrologCafe'

plc: pl2am.plc am2j.plc

compiler: gen-compiler
	$(JAVAC) $(JAVACOPTS) *.java $T-pl2am/$P/pl2am/*.java $T-am2j/$P/am2j/*.java
	$(JAR) $(JAROPTS) compiler.jar $P
gen-compiler: pl2am.am am2j.am

plj: pl2am.plj am2j.plj

clean:
	-rm -f -r com
	-rm -f core *~ *.ql *.sav *.plc *.qlf *.qsav *.am *.plj *.jar system.pl

realclean: clean


