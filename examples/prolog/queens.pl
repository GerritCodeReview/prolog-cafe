% File   : queens.pl
% Updated: 14 February 2008
% Purpose: N-Queen Puzzle (posed by Franz Nauch, 1850)

main :-
	write('N-Queen Puzzle (posed by Franz Nauch, 1850) '), nl,
	write('N = '),
	flush_output,
	read(N),
	N >= 4,
	read_yn('All solutions (y/n)? ', All),
	read_yn('Output (y/n)? ', Output),
	statistics(runtime, _),
	queen_solve(N, all(All), output(Output)),
	statistics(runtime, [_,T]),
	write('CPU time = '), write(T), write(' msec'), nl.

read_yn(Message, YN) :-
	write(Message),
	flush_output,
	read(X),
	(X == 'y' -> YN = yes; YN = no).

queen_solve(N, all(X), output(Y)) :-
	queens(N, Q),
	(Y == yes -> write(Q), nl; true),
	X == no,
	!.
queen_solve(_,_,_).

queens(N,Qs) :-
	range(1,N,Ns),
	queens(Ns,[],Qs).

queens([],Qs,Qs).
queens(UnplacedQs,SafeQs,Qs) :-
	select(UnplacedQs,UnplacedQs1,Q),
	not_attack(SafeQs,Q),
	queens(UnplacedQs1,[Q|SafeQs],Qs).

not_attack(Xs,X) :-
	not_attack(Xs,X,1).

not_attack([],_,_) :- !.
not_attack([Y|Ys],X,N) :-
	X =\= Y+N, X =\= Y-N,
	N1 is N+1,
	not_attack(Ys,X,N1).

select([X|Xs],Xs,X).
select([Y|Ys],[Y|Zs],X) :- select(Ys,Zs,X).

range(N,N,[N]) :- !.
range(M,N,[M|Ns]) :-
	M < N,
	M1 is M+1,
	range(M1,N,Ns).
