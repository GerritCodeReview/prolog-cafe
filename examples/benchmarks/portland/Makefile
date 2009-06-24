################################################################
#  Makefile
################################################################
PLCAFE       = plcafe
PLCAFEOPTS   = -J '-Xmx100m'
PLJAR        = pljar
PLJAROPTS    = -v -J '-Xmx100m' -C '-J-Xmx100m'

SICSTUS      = /usr/local/bin/sicstus
SICSTUSOPTS  = 

SWI          = /opt/local/bin/swipl
SWIOPTS      = -L100m

################################################################
.SUFFIXES:
.SUFFIXES: .ql .qlf .jar .pl .sicstus .swi .plcafe .in $(SUFFIXES)

plcafe: comp_plcafe run_plcafe

sicstus: comp_sicstus run_sicstus

swi: comp_swi run_swi

all: comp run

################################################################
# run
################################################################
plcafe_out_objects  := $(patsubst %.in,%.plcafe, $(wildcard *.in))
sicstus_out_objects := $(patsubst %.in,%.sicstus,$(wildcard *.in))
swi_out_objects     := $(patsubst %.in,%.swi,    $(wildcard *.in))

.in.plcafe:
	-rm -f out/$@
	/bin/echo "['$<'], halt." \
	| $(PLCAFE) $(PLCAFEOPTS) -cp $*.jar:bench_util.jar > out/$@

.in.sicstus:
	-rm -f out/$@
	/bin/echo "ensure_loaded($*), ensure_loaded(bench_util), ['$<'], halt." \
	| $(SICSTUS) $(SICSTUSOPTS) > out/$@

.in.swi:
	-rm -f out/$@
	/bin/echo "ensure_loaded($*), ensure_loaded(bench_util), ['$<'], halt." \
	| $(SWI) $(SWIOPTS) > out/$@

run: run_plcafe run_sicstus run_swi

run_plcafe: $(plcafe_out_objects)

run_sicstus: $(sicstus_out_objects)

run_swi: $(swi_out_objects)

################################################################
# compile
################################################################
jar_objects := $(patsubst %.pl,%.jar,$(wildcard *.pl))
ql_objects  := $(patsubst %.pl,%.ql, $(wildcard *.pl))
qlf_objects := $(patsubst %.pl,%.qlf,$(wildcard *.pl))

.pl.jar:
	$(PLJAR) $(PLJAROPTS) $@ $<
	-rm -f -r $*

.pl.ql:
	/bin/echo "[$*], fcompile($*), halt." | $(SICSTUS) $(SICSTUSOPTS)

.pl.qlf:
	/bin/echo "qcompile($*), halt." | $(SWI) $(SWIOPTS)
#	/bin/echo "[$*], qcompile($*), halt." | $(SWI) $(SWIOPTS)

comp: comp_plcafe comp_sicstus comp_swi

comp_plcafe: $(jar_objects)

comp_sicstus: $(ql_objects)

comp_swi: $(qlf_objects)

################################################################
# clean up
################################################################
clean:
	-rm -f core *~
	-rm -f /out/core out/*~
	-rm -f *.ql
	-rm -f *.qlf

realclean: clean
	-rm -f *.jar *.class
	-rm -f out/*.plcafe out/*.sicstus out/*.swi

# END

