################################################################
# Makefile
################################################################
# PROLOG - the command of Prolog system 
#        ::= sicstus | swipl | pl | prolog | ...
# PSYSTEM- the type of Prolog system 
#        ::= 'SICStus' | 'SWI-Prolog' | 'Others'

PROLOG  = sicstus
PSYSTEM = 'SICStus'
# PROLOG  = swipl
# PSYSTEM = 'SWI-Prolog'
# PROLOG  = prolog
# PSYSTEM = 'Others'

JAVA      = java
JAVAC     = javac
JAVACOPTS = -d . -J-Xmx100m
JAR       = jar
JAROPTS   = cf

PWD := $(shell pwd)
################################################################
all: plc lang builtin compiler plcafe plj

plc:
	(cd src/compiler; $(MAKE) plc \
	PROLOG='$(PROLOG)' PSYSTEM='$(PSYSTEM)')
	cp src/compiler/pl2am.plc bin/
	cp src/compiler/am2j.plc bin/

lang:
	(cd src/lang; $(MAKE) lang \
	JAVAC='$(JAVAC)' JAVACOPTS='$(JAVACOPTS)' \
	JAR='$(JAR)' JAROPTS='$(JAROPTS)')
	cp src/lang/lang.jar .

builtin:
	(cd src/builtin; $(MAKE) builtin \
	JAVAC='$(JAVAC)' JAVACOPTS='$(JAVACOPTS) -classpath $(PWD)/lang.jar' \
	JAR='$(JAR)' JAROPTS='$(JAROPTS)')
	cp src/builtin/builtin.jar .

compiler:
	(cd src/compiler; $(MAKE) compiler \
	JAVAC='$(JAVAC)' \
	JAVACOPTS='$(JAVACOPTS) -classpath $(PWD)/lang.jar:$(PWD)/builtin.jar' \
	JAR='$(JAR)' JAROPTS='$(JAROPTS)')
	cp src/compiler/compiler.jar .

plcafe:
	$(JAVAC) $(JAVACOPTS) src/lang/*.java src/builtin/*/*.java \
	src/compiler/pl2am/*.java src/compiler/am2j/*.java src/compiler/Compiler.java
	$(JAR) $(JAROPTS) plcafe.jar jp/ac/kobe_u/cs/prolog

plj:
	(cd src/compiler; $(MAKE) plj PROLOG='$(JAVA)')
	cp src/compiler/pl2am.plj bin/
	cp src/compiler/am2j.plj bin/
################################################################
clean:
	(cd src/builtin; $(MAKE) clean)
	(cd src/lang; $(MAKE) clean)
	(cd src/compiler; $(MAKE) clean)
	-rm -f bin/pl2am.plc
	-rm -f bin/am2j.plc
	-rm -f -r jp
	-rm -f core *~

realclean: clean
	(cd src/builtin; $(MAKE) realclean)
	(cd src/lang; $(MAKE) realclean)
	(cd src/compiler; $(MAKE) realclean)
	-rm -f -r doc/javadoc
	-rm -f bin/pl2am.plj
	-rm -f bin/am2j.plj
	-rm -f plcafe.jar
	-rm -f compiler.jar
	-rm -f builtin.jar
	-rm -f lang.jar
################################################################
ex:
	(cd examples;  $(MAKE) all)
################################################################
JAVADOC      = javadoc
JAVADOCOPTS  = -J-Xmx100m \
	       -locale en_US -d doc/javadoc -breakiterator \
               -windowtitle $(WINDOWTITLE) -doctitle $(DOCTITLE) \
               -header $(HEADER) -bottom $(BOTTOM)

WINDOWTITLE  = 'Prolog Cafe v1.2 API Specification'
DOCTITLE     = 'Prolog Cafe v1.2 API Specification'
HEADER       = '<b><font color="red">Prolog Cafe v1.2</font></b><br>'
BOTTOM       = '<font size="-1"> \
                 Copyright (C) 1997-2009 \
                 <a href="http://kaminari.istc.kobe-u.ac.jp/banbara.html">M.BANBARA</a> and \
                 <a href="http://bach.istc.kobe-u.ac.jp/tamura.html">N.TAMURA</a> \
               </font>'

javadoc: 
	$(JAVADOC) $(JAVADOCOPTS) src/lang/*.java \
	src/compiler/Compiler.java
################################################################
VER = 1.2.5
DIR = PrologCafe$(VER)
TGZ = PrologCafe$(VER).tgz
ZIP = PrologCafe$(VER).zip

tar: clean
	(cd ..; tar cfz $(DIR)/$(TGZ) $(DIR)/*)

zip: clean
	(cd ..; zip -r $(DIR)/$(ZIP) $(DIR)/*)
################################################################
# END
