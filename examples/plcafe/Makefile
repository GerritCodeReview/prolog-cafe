################################################################
#	Makefile for Prolog Cafe
################################################################

################################################################
PLJAVAC     = pljavac
PLJAVACOPTS = -v

PLJAR     = pljar
PLJAROPTS = -v
################################################################
.SUFFIXES:
.SUFFIXES: .jar .pl $(SUFFIXES)

jar_objects := $(patsubst %.pl,%.jar,$(wildcard *.pl))

.pl.jar:
	$(PLJAR) $(PLJAROPTS) $@ $<
	-rm -f -r $*

all: $(jar_objects)
	$(PLJAVAC) $(PLJAVACOPTS) -cp queens.jar T.java

clean:
	-rm -f core *~

realclean: clean
	-rm -f *.jar *.class

