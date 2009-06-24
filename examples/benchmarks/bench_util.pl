%   File   : bench_util.pl
%   Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%   Updated: 24 February 2008
%   Purpose: Benchmark utilities
%   Note   : based on driver.pl in Pereira's benchmark

'$get_cpu_time'(T) :- statistics(runtime, [T,_]).

'$report'(Name, N, T0, T1, T2) :-
        TestTime is T1-T0,
        OverHead is T2-T1,
	Time is TestTime-OverHead,
        Average is Time/N,
	nl,
	write('# Name: '), write(Name), nl,
        write('# Iterations: '), write(N), nl,
	write('# TestTime: '), write(TestTime), write(' msec.\n'),
	write('# OverHead: '), write(OverHead), write(' msec.\n'),
	write('# TestTime-OverHead: '), write(Time), write(' msec.\n'),
	write('# (TestTime-OverHead)/Iterations: '), write(Average), write(' msec.\n'),
	'$report_csv'(['###CSV###',Name,N,TestTime,OverHead,Time,Average], ','),
	nl.

'$report_csv'([], _) :- !.
'$report_csv'([X], _) :- !, write(X), nl.
'$report_csv'([X|Xs], Delim) :- write(X), write(Delim), '$report_csv'(Xs, Delim).

'$benchmark'(Name, Iterations, Action, Control) :- 
        '$get_cpu_time'(T0),
        (   '$repeat'(Iterations), once(Action), fail
        ;   '$get_cpu_time'(T1)
        ),
        (   '$repeat'(Iterations), once(Control), fail
        ;   '$get_cpu_time'(T2)
        ),
        '$report'(Name, Iterations, T0, T1, T2).

'$repeat'(N) :- N > 0, '$from'(1, N).

'$from'(I, I) :- !.
'$from'(L, U) :- M is (L+U)>>1,   '$from'(L, M).
'$from'(L, U) :- M is (L+U)>>1+1, '$from'(M, U).

'$dummy'.
'$dummy'(_).
'$dummy'(_, _).
'$dummy'(_, _, _).
