%   File   : knight.pl
%   Authors: Naoyuki Tamura (tamura@kobe-u.ac.jp)
%            Mutsunori Banbara (banbara@kobe-u.ac.jp)
%   Updated: 26 May 2008
%   Purpose: Knight's Tour (posed by Brook Taylor, 18C)
%   Note   : Each position (I,J) on the board N*N 
%            is indexed by (N+2)*(I-1)+J-1

/*
| ?- main.
Finds a Knight's Tour (N>=8 takes a long time...)
N = 8.

  1 12  9  6  3 14 17 20
 10  7  2 13 18 21  4 15
 31 28 11  8  5 16 19 22
 64 25 32 29 36 23 48 45
 33 30 27 24 49 46 37 58
 26 63 52 35 40 57 44 47
 53 34 61 50 55 42 59 38
 62 51 54 41 60 39 56 43

yes
*/

:- dynamic n/1, corner/1, next_K/1.

main :-
	write('Finds a Knight''s Tour (N>=8 takes a long time...)'), nl,
	write('N = '),
	flush_output,
	read(N),
	statistics(runtime, _),
	knight_tour(N),
	statistics(runtime, [_,T]),
	nl,
	write('CPU time = '), write(T), write(' msec'), nl,
	!.

knight_tour(N) :-
	knight_init(N),
	gen_res(L),
	solve(1, 1, L),
	write_board(L).

knight_init(N) :-
	retractall(n(_)),
	retractall(corner(_)),
	retractall(next_K(_)),
	assertz(n(N)).

%  gen_res/1 creates resources k/2 as a list, and asserts corner/1 and next_K/1.
%    - k(K, _)     for  K = (N+2)*(I-1)+J-1, I = 1..N, J = 1..N
%    - corner(K)   for  K = (N+2)*(I-1)+J-1, I = 1,N, J = 1,N
%    - next_K(DK)  for DK = (N+2)*DI+DJ, (DI,DJ) = (+-2,+-1),(+-1,+-2)

gen_res(L) :- gen_res(1, 1, L).

gen_res(I, _, []) :- clause(n(N), _), I > N, !,
	calc_K(1, 1, C1),
	calc_K(1, N, C2),
	calc_K(N, 1, C3),
	calc_K(N, N, C4),
	calc_DK(-2, -1, DK1),
	calc_DK(-2,  1, DK2),
	calc_DK(-1, -2, DK3),
	calc_DK(-1,  2, DK4),
	calc_DK( 1, -2, DK5),
	calc_DK( 1,  2, DK6),
	calc_DK( 2, -1, DK7),
	calc_DK( 2,  1, DK8),
	assertz(corner(C1)),
	assertz(corner(C2)),
	assertz(corner(C3)),
	assertz(corner(C4)),
	assertz(next_K(DK1)),
	assertz(next_K(DK2)),
	assertz(next_K(DK3)),
	assertz(next_K(DK4)),
	assertz(next_K(DK5)),
	assertz(next_K(DK6)),
	assertz(next_K(DK7)),
	assertz(next_K(DK8)).
gen_res(I, J, L) :- clause(n(N), _), J > N, !,
	I1 is I + 1,
	gen_res(I1, 1, L).
gen_res(I, J, [k(K, _)|L]) :-
	clause(n(N), _),
	I =< N, J =< N,
	calc_K(I, J, K),
	J1 is J + 1,
	gen_res(I, J1, L).

% solve(+I, +J, +R) finds knight tour starting from the position (I,J).
% R is a list of k(Index, Step).
solve(I, J, R) :-
	clause(n(N), _),
	Step = 1,
	calc_K(I, J, K),
	del_res(k(K,Step), R, R0),
	solve(Step, N, K, R0).

solve(Step, N, _, []) :-
	Step >= N*N,
	!.
solve(Step, N, K, R) :-
	Step < N*N,
	check(Step, N, R),
	Step1 is Step + 1,
	next(K, K1, R),
	del_res(k(K1,Step1), R, R0),
	solve(Step1, N, K1, R0).

del_res(X, [X|Xs], Xs).
del_res(X, [Y|Ys], [Y|Zs]) :- del_res(X, Ys, Zs).

check(Step, N, R) :-
	(Step mod  4 =:= 0, Step < N*N-1
	 -> \+ isolate_one(_, R)
	 ;  true),
	!.

isolate_one(K, R) :-
	del_res(k(K, _), R, _),
	\+ (next0(K, K1), del_res(k(K1, _), R, _)).

% if current is a corner, it's ok
next(K, K1, _) :-
	clause(corner(K), _),
	!,
	next0(K, K1).
% if the next can be a not-visitied corner, it should be selected
next(K, K1, R) :-
	next0(K, K1),
	clause(corner(K1), _),
	not_visited(K1, R),
	!.
% otherwise
next(K, K1, _) :- next0(K, K1).

next0(K, K1) :- clause(next_K(DK), _), K1 is K + DK.

not_visited(K, R) :- \+ \+ del_res(k(K, _), R, _).

calc_K(I, J, K) :- clause(n(N), _), K is (N+2)*(I-1)+(J-1).

calc_DK(DI, DJ, DK) :- clause(n(N), _), DK is (N+2)*DI+DJ.


%  write_board(L) writes step numbers S for each I = 1..N, J = 1..N
write_board(L) :-
	clause(n(N), _),
	for(I, 1, N),
	  nl,
	  for(J, 1, N),
	    calc_K(I, J, K),
	    member(k(K, Step), L),
	    write_number(Step),
	fail.
write_board(_) :- nl.

for(M, M, N) :- M =< N.
for(I, M, N) :- M =< N, M1 is M + 1, for(I, M1, N).

write_number(C) :- C < 10, !, write('  '), write(C).
write_number(C) :- write(' '), write(C).

member(X, [X|_]).
member(X, [_|Xs]) :- member(X, Xs).


%%% 
knight_tour_applet(N, Ans) :-
	knight_init(N), 
	gen_res(L), 
	solve(1, 1, L), 
	get_step(L, Ans).

get_step(L, A) :-
	clause(n(N), _),
        get_step(1, 1, N, L, A).
        
get_step(I, _, N, _, []) :- 
	I > N, 
	!.
get_step(I, J, N, L, A) :- J > N, !,
        I1 is I+1,
        get_step(I1, 1, N, L, A).
get_step(I, J, N, L, [Step|A]) :-
        I =< N, J =< N,
        calc_K(I, J, K),
        member(k(K,Step), L),
	!,
        J1 is J+1,
        get_step(I, J1, N, L, A).




