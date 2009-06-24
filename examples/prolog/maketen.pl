% File   : maketen.pl
% Authors: Naoyuki Tamura (tamura@kobe-u.ac.jp)
% Updated: 14 February 2008
% Purpose: Make 10 from given 4 numbers
% Notes  : Slightly changed by M.Banbara


%%
%% Make 10 from given 4 numbers
%%

%% Examples
%%	[1,1,5,8]  10=8/(1-1/5)
%%	[1,1,9,9]  10=9*(1/9+1)
%%	[3,4,7,8]  10=8*(3-7/4)
%%	[5,5,5,7]  10=7*5-5*5
%%	[9,9,9,9]  10=(9*9+9)/9
ex :-
	make_number([1,1,5,8], 10),
	make_number([1,1,9,9], 10),
	make_number([3,4,7,8], 10),
	make_number([5,5,5,7], 10),
	make_number([9,9,9,9], 10).

%% すべての4数字について調べる
make_ten_all :-
	make_number_all(10).

make_number_all(N) :-
	for(I, 0, 9),
	for(J, I, 9),
	for(K, J, 9),
	for(L, K, 9),
%	make_number([I,J,K,L], N),
	make_number_x([I,J,K,L], N),
	fail.
make_number_all(_).

make_number(Ds, N) :-
	write(Ds), write('  '),
	make_num(Ds, N, E),
	write(N=E), nl,
	!.
make_number(_, _) :-
	write(fail), nl.

make_number_x(Ds, N) :-
	write('Make '), write(N), write(' from '),
	write(Ds), write(': '),
	findall(E, make_num(Ds, N, E), Ss),
	length(Ss, L),
	write(L), write(' sol(s): '),
	write_heads(3, Ss),
	nl.

write_heads(_, []) :- !.
write_heads(0, _) :- !,
	write('...').
write_heads(N, [X]) :- N > 0, !,
	write(X).
write_heads(N, [X|Xs]) :- N > 0, !,
	N1 is N-1,
	write(X), write(', '),
	write_heads(N1, Xs).

make_num(Ds, N, E) :-
	all_exp(Ds, E),
	q(E, [N,1]).

%%
%% all_exp(:List, ?Exp)
%%   Listの要素から，加減乗除演算子で作られる式Expを返す
%%   + と * は可換とする
%%
all_exp(Ds0, E) :-
	sort_list(Ds0, Ds),
	all_exp0(Ds, E).

all_exp0([D], D).
all_exp0(Ds0, E) :-
	Ds1 = [_|_], 
	Ds2 = [_|_], 
	div_list(Ds0, Ds1, Ds2),
	all_exp0(Ds1, E1),
	all_exp0(Ds2, E2),
	add_op(E1, E2, E).
	
add_op(E1, E2, E1+E2) :- E1 @=< E2.
add_op(E1, E2, E1-E2).
add_op(E1, E2, E1*E2) :- E1 @=< E2.
add_op(E1, E2, E1/E2).

div_list(Ds0, Ds1, Ds2) :-
	de_merge(Ds1, Ds2, Ds0).
%	Ds1 @>= Ds2.

de_merge([], [], []).
de_merge(Zs, [], Zs) :- Zs = [_|_].
de_merge([], Zs, Zs) :- Zs = [_|_].
de_merge([X|Xs], [Y|Ys], [X|Zs]) :-
	de_merge(Xs, [Y|Ys], Zs),
	X @< Y.
de_merge([X|Xs], [Y|Ys], [Y|Zs]) :-
	de_merge([X|Xs], Ys, Zs),
	Y @=< X.

%% ソート (遅い!)
sort_list([], []).
sort_list([D|Ds0], Ds) :-
	sort_list(Ds0, Ds1),
	insert(D, Ds1, Ds).

insert(D0, [], [D0]).
insert(D0, [D|Ds0], [D0,D|Ds0]) :-
	D0 @=< D, !.
insert(D0, [D|Ds0], [D|Ds]) :-
	D0 @> D,
	insert(D0, Ds0, Ds).

for(I, M, N) :- M =< N, I=M.
for(I, M, N) :- M =< N, M1 is M+1, for(I, M1, N).

%%
%% q(X, Q)
%%	式 X の計算結果として有理数 Q を返す
%%	Q = [N,D]	N>0, D>0, 正の有理数を表す (NとDは互いに素)
%%	Q = [N,D]	N<0, D>0, 負の有理数を表す (NとDは互いに素)
%%	Q = [0,1]	0を表す
%%	Q = [1,0]	無限大を表す
%%	Q = [-1,0]	-無限大を表す
%%	Q = [0,0]	不定を表す
%%
q(X, [X,1]) :-
	integer(X).
q(-X, Q) :-
	q(X, Q1),
	q_neg(Q1, Q).
q(X+Y, Q) :-
	q(X, Q1),
	q(Y, Q2),
	q_add(Q1, Q2, Q).
q(X-Y, Q) :-
	q(X, Q1),
	q(Y, Q2),
	q_neg(Q2, Q3),
	q_add(Q1, Q3, Q).
q(X*Y, Q) :-
	q(X, Q1),
	q(Y, Q2),
	q_mul(Q1, Q2, Q).
q(X/Y, Q) :-
	q(X, Q1),
	q(Y, Q2),
	q_inv(Q2, Q3),
	q_mul(Q1, Q3, Q).

%%
%% 有理数の加算
%%
q_add([0,0], _, [0,0]) :- !.
q_add(_, [0,0], [0,0]) :- !.
q_add([1,0], [1,0], [1,0]) :- !.
q_add([1,0], [-1,0], [0,0]) :- !.
q_add([-1,0], [1,0], [0,0]) :- !.
q_add([-1,0], [-1,0], [-1,0]) :- !.
q_add([N1,D1], [N2,D2], Q) :- D1 =\= 0, D2 =\= 0,
	N is N1*D2+N2*D1,
	D is D1*D2,
	q_norm([N,D], Q).

%%
%% 有理数の負数
%%
q_neg([N1,D], Q) :-
	N is -N1,
	q_norm([N,D], Q).

%%
%% 有理数の乗算
%%
q_mul([N1,D1], [N2,D2], Q) :-
	N is N1*N2,
	D is D1*D2,
	q_norm([N,D], Q).

%%
%% 有理数の逆数
%%
q_inv([N1,D1], Q) :-
	q_norm([D1,N1], Q).

%%
%% 有理数の正規化
%%
q_norm([0,0], [0,0]) :- !.
q_norm([N,0], [1,0]) :- N > 0, !.
q_norm([N,0], [-1,0]) :- N < 0, !.
q_norm([0,D], [0,1]) :- D =\= 0, !.
q_norm([N1,D1], Q) :- N1 =\= 0, D1 < 0, !,
	N2 is -N1,
	D2 is -D1,
	q_norm([N2,D2], Q).
q_norm([N1,D1], [N,D]) :- N1 =\= 0, D1 > 0, !,
	gcd(N1, D1, G),
	N is N1//G,
	D is D1//G.
	
gcd(0, B, G) :-
	G is abs(B).
gcd(A, B, G) :-
	A =\= 0,
	R is B mod A,
	gcd(R, A, G).

