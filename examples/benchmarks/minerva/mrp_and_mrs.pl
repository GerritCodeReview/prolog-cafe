/* CHANGELOG by M.Banbara
  - for/3 is added.
  - comment out write/1, nl/0.
*/

% File   : MrP_and_MrS.pl
% Authors: IF Computer
% Purpose:  Martin Nilsson's Mr. P and Mr. S problem
% Notes  : http://www.ifcomputer.com/MINERVA/ExamplePrograms/Benchmarks/MrPandMrS/home_jp.html

%ps  :- w(o1, _, X), write(X), nl.
ps  :- w(o1, _, X).

w(s1, (I,J), (M,N)) :-
	for(2, M, 99),
	for(2, N, M),
	M+N =:= I+J.
w(p1, (I,J), (M,N)) :-
	for(2, M, 99),
	for(2, N, M),
	M*N =:= I*J.
w(s2, S, X) :- w(s1, S, X), p3(X).
w(p2, S, X) :- w(p1, S, X), p4(X).
w(s3, S, X) :- w(s2, S, X), p5(X).
w(o1, _, (M,N)) :-
	for(2, M, 99), for(2, N, M), %write((M,N)), nl,
	p3((M,N)), %write(p3), nl,
	p4((M,N)), %write(p4), nl,
	p5((M,N)), %write(p5), nl,
	p6((M,N)). %write(p6), nl.

p3(X) :- has_two_or_more_solutions(w(p1,X,_)).
p4(X) :- has_two_or_more_solutions(w(s2,X,_)),
	all_in_are(Z, (w(s1,X,W),value(p3(W),Z)),true).
p5(X) :- has_exactly_one_solution(w(p2,X,_)).
p6(X) :- has_exactly_one_solution(w(s3,X,_)).

value(X,V) :- (X, V=true; V=false), !.

all_in_are(X,G,Z) :- \+ ((G,X \= Z)), !.

has_exactly_one_solution(X) :- 
	copy_term(X, X2),
	X,
	all_in_are(X2, X2, X), 
	!.

has_two_or_more_solutions(X) :- 
	copy_term(X, X2),
	X,
	!,
	X2,
	X \= X2,
	!.

for(M, I, N) :- M =< N, I=M.
for(M, I, N) :- M =< N, M1 is M+1, for(M1, I, N).


