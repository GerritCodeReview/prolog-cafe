################################################################
#	Makefile for Prolog Cafe
################################################################

################################################################
PLJAVAC     = pljavac
PLJAVACOPTS = -v

PLJAR     = pljar
PLJAROPTS = -v
################################################################
all: 
	-rm -f *.pl
	ln -s ../prolog/peg.pl .
	ln -s ../prolog/pentomino.pl .
	ln -s ../prolog/queens.pl .
	ln -s ../prolog/knight.pl .
	$(PLJAR) $(PLJAROPTS) peg.jar peg.pl
	$(PLJAR) $(PLJAROPTS) pentomino.jar pentomino.pl
	$(PLJAR) $(PLJAROPTS) queens.jar queens.pl
	$(PLJAR) $(PLJAROPTS) knight.jar knight.pl
	-rm -f -r peg
	-rm -f -r pentomino
	-rm -f -r queens
	-rm -f -r knight
	$(PLJAVAC) $(PLJAVACOPTS) -cp peg.jar Peg.java
	$(PLJAVAC) $(PLJAVACOPTS) -cp pentomino.jar Pentomino.java
	$(PLJAVAC) $(PLJAVACOPTS) -cp queens.jar Queens.java
	$(PLJAVAC) $(PLJAVACOPTS) -cp knight.jar Knight.java

clean:
	-rm -f core *~

realclean: clean
	-rm -f *.jar *.class *.pl
