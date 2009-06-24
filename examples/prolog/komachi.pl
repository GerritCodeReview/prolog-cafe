%   File   : komachi.pl
%   Authors: Naoyuki Tamura (tamura@kobe-u.ac.jp)
%   Updated: 10 March 2008
%   Purpose: Solve komachi-zan

main :-
	write('Komachi-zan'), nl,
	write('N = '),
	flush_output,
	read(N),
	N > 0,
	read_yn('All solutions (y/n)? ', All),
	read_yn('Output (y/n)? ', Output),
	statistics(runtime, _),
	komachi_solve(N, all(All), output(Output)),
	statistics(runtime, [_,T]),
	write('CPU time = '), write(T), write(' msec'), nl.

read_yn(Message, YN) :-
	write(Message),
	flush_output,
	read(X),
	(X == 'y' -> YN = yes; YN = no).

komachi_solve(N, all(X), output(Y)) :-
	komachi(N, E),
	(Y == yes -> write(E=N), nl; true),
	X == no,
	!.
komachi_solve(_,_,_).

komachi(N, E) :-
	generate(9, E),
	eval(E, N).

generate(I, E) :-
	I > 0,
	gen_num(M, I, I1),
	gen_next(I1, M, E).

gen_num(I0, I0, I) :-
	I0 > 0,
	I is I0-1.
gen_num(M, I0, I) :-
	I0 > 0,
	I1 is I0-1,
	gen_num(M0, I1, I),
	M is 10*M0 + I0.

gen_next(0, M, M  ).
gen_next(0, M, E  ) :- E is -M.
gen_next(I, M, E+M) :- I > 0, generate(I, E).
gen_next(I, M, E-M) :- I > 0, generate(I, E).
gen_next(I, M, E*M) :- I > 0, generate(I, E).

% Evaluates arithmethic expression
eval(E, N) :- N is E.

% for(I, Start, End, Inc)
for(M, M, N, _) :- M =< N.
for(I, M, N, S) :- M =< N, M1 is M+S, for(I, M1, N, S).
