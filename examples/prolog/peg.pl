% File   : peg.pl
% Authors: Naoyuki Tamura (tamura@kobe-u.ac.jp)
% Updated: 14 February 2008
% Purpose: Peg solitaire game
% Notes  : Slightly changed by M.Banbara

/*
This solitaire game consists of a board of the following shape
and initial configuration

        x
       x x
      x o x
     x x x x
    x x x x x

where x denotes a peg and o an empty hole.  A peg may jump over
an adjacent peg if the hole behind it is empty.  The goal is to
be left with one peg.  I fooled around with this for about 2
minutes and decided it was too hard by hand---a Lolli program
was called for.
*/

main :-
	write('Peg puzzle (N>=6 takes a long time...)'), nl,
	write('N = '),
	current_output(Out),
	flush_output(Out),
	read(N), integer(N),
	write('Position of the first empty hole'), nl,
	write('Row (1..'), write(N), write(') = '),
	flush_output(Out),
	read(I0), integer(I0),
	write('Col (1..'), write(I0), write(') = '),
	flush_output(Out),
	read(J0), integer(J0),
	statistics(runtime, _),
	peg_game(N, I0, J0, Solution),
	statistics(runtime, [_,T]),
	write(Solution), nl,
	write('CPU time = '), write(T), write(' msec'), nl.
main :-
	statistics(runtime, [_,T]),
	write('No solutions'), nl,
	write('CPU time = '), write(T), write(' msec'), nl.

peg_game(N, I0, J0, S) :-
	N >= 3,
	0 < I0, I0 =< N,
	0 < J0, J0 =< I0,
	Step is N*(N+1)//2 - 2,
	place_pegs(N, N, I0, J0, Board),
	jump(Step, Board, S).

place_pegs(N, N, I0, J0, Board) :-
	place_pegs(N, N, I0, J0, [], Board).

place_pegs(0, 0, I0, J0, B0, B) :-
	peg_delete(B0, peg(I0, J0), B1),!,
	B = [empty(I0, J0)|B1].
place_pegs(M, 0, I0, J0, B0, B) :-
	M > 0,
	M1 is M-1,
	place_pegs(M1, M1, I0, J0, B0, B).
place_pegs(M, N, I0, J0, B0, B) :-
	M > 0, N > 0,
	N1 is N-1,
	place_pegs(M, N1, I0, J0, [peg(M,N)|B0], B).

peg_delete([P|Ys], P, Ys) :- !.
peg_delete([X|Ys], P, [X|Zs]) :-
	peg_delete(Ys, P, Zs).

peg_select(X, [X|Xs], Xs).
peg_select(X, [Y|Ys], [Y|Zs]) :- peg_select(X, Ys, Zs).

jump(Step, Board0, [move(X,Y,DX,DY)|Ms]) :-
	Step > 0,
	peg_select(peg(X,Y), Board0, Board1),
	direction(DX, DY),
	X1 is X+DX, Y1 is Y+DY,
	peg_select(peg(X1,Y1), Board1, Board2),
	X2 is X1+DX, Y2 is Y1+DY,
	peg_select(empty(X2,Y2), Board2, Board3),
	Step1 is Step-1,
	Board = [empty(X,Y), empty(X1, Y1), peg(X2, Y2)|Board3],
	jump(Step1, Board, Ms).
jump(0, _, []).

direction( 1, 0).
direction( 1, 1).
direction( 0, 1).
direction(-1, 0).
direction(-1,-1).
direction( 0,-1).


