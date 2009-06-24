% File   : pentomino.pl
% Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%          Naoyuki Tamura (tamura@kobe-u.ac.jp)
% Updated: 14 February 2008
% Purpose: Pentomino Puzzle
%          (posed by Henry E. Dudeny, 1907 and Solomon W. Golomb, 1953)

/****************************************************************
  Count All Solutions
****************************************************************/
count_all(N) :-
        findall(B, solve_pentomino(N, B), L),
        length(L, C),
        write(C), write(' solutions'), nl.

/****************************************************************
  Main
****************************************************************/
main :-
	write('Pentomino Puzzle '), nl,
	write('    Please select a board size.'), nl,
	write('    3 : 3 * 20'), nl,
	write('    4 : 4 * 15'), nl,
	write('    5 : 5 * 12'), nl,
	write('    6 : 6 * 10'), nl,
	write('    8 : 8 * 8 (with 4 blanks in the center)'), nl,
	write('Please select a board size in (3..8).'), nl,
	write('Number = '), 
	current_output(Out),
	flush_output(Out),
	read(X),
	read_yn('All solutions (y/n)? ', All),
	read_yn('Output (y/n)? ', Output),
	statistics(runtime, _),
	solve_pent(X, all(All), output(Output)),
	statistics(runtime, [_,T]),
	write('CPU time = '), write(T), write(' msec'), nl.

read_yn(Message, YN) :-
	write(Message),
	flush_output,
	read(X),
	(X == 'y' -> YN = yes; YN = no).

solve_pent(X, all(All), output(Output)) :-
	solve_pentomino(X, Board),
	(Output == yes -> show_result(Board),nl; true),
	All == no,
	!.
solve_pent(_, _, _).
/****************************************************************
  複数のペントミノを配置する
  solve_pentomino(X, B)
    X : ボードの縦幅(行の数)
    B : ボードの構造体
****************************************************************/
solve_pentomino(X, Board) :-
	pent_board(X, Y, Board),
	Col is Y+2, 
	set_x_pentomino(X, Y, Col, Board, Z0, Z),
	solve_pent(X, Y, Col, Board),
	remove_symmetry(Z0, Z).

remove_symmetry(Z0, Z) :-
	nonvar(Z0), nonvar(Z), 
	!,
	Z0 @=< Z.
remove_symmetry(_, _).

solve_pent(X, Y, Col, Board) :-
	get_search_list(List, 1, 1, X, Y, Col, Board),
	Pts = ['F','I','L','N','P','T','U','V','W','Y','Z'],
	solve_pent0(Pts, List, Col, Board).

solve_pent0([], _, _, _) :- !.
solve_pent0(Pts, [(N, E)|Ls], Col, Board) :-
	var(E),
	!,
	not_one_space(N, Col, Board),
	pent_select(P, Pts, Pts1),
	place_pent(P, N, Col, Board),
	solve_pent0(Pts1, Ls, Col, Board).
solve_pent0(Pts, [_|Ls], Col, Board) :-
	solve_pent0(Pts, Ls, Col, Board).

not_one_space(N, Col, B) :-
	(
	    N1 is N+1, arg(N1, B, X1), var(X1)
	;
	    N2 is N+Col, arg(N2, B, X2), var(X2)
	),
	!.

get_search_list([], I, J, X, Y, _, _) :-
	J =:= Y+1, I =:= X-1, 
	!.
get_search_list(L, I, J, X, Y, Col, B) :-
	I =:= 0, J =< X,
	!,
	get_search_list(L, J, 1, X, Y, Col, B).
get_search_list(L, I, J, X, Y, Col, B) :-
	I =:= 0, J > X,
	!,
	J1 is J-X+1,
	get_search_list(L, X, J1, X, Y, Col, B).
get_search_list(L, I, J, X, Y, Col, B) :-
	J =:= Y+1,
	!,
	J1 is I+2+Y-X,
	get_search_list(L, X, J1, X, Y, Col, B).
get_search_list([(N,Z)|Ls], I, J, X, Y, Col, B) :-
	N is Col*I+J+1,
	arg(N, B, Z),
        var(Z),
        !,
        I1 is I-1,
        J1 is J+1,
	get_search_list(Ls, I1, J1, X, Y, Col, B).
get_search_list(L, I, J, X, Y, Col, B) :-
        I1 is I-1,
        J1 is J+1,
	get_search_list(L, I1, J1, X, Y, Col, B).
/****************************************************************
  Remove Symmetry by using X-pentomino
****************************************************************/
set_x_pentomino(X, _, _, Board, Z0, Z) :-
	X =:= 3, !,
	(
	    place_pent('X', 2, 1, 22, Board),
	    look_up_board(1, 3, 22, Board, Z0),
	    look_up_board(3, 3, 22, Board, Z)
	;
    	    place_pent('X', 2, 2, 22, Board),
	    look_up_board(1, 4, 22, Board, Z0),
	    look_up_board(3, 4, 22, Board, Z)
	;
    	    place_pent('X', 2, 3, 22, Board),
	    look_up_board(1, 5, 22, Board, Z0),
	    look_up_board(3, 5, 22, Board, Z)
	;
    	    place_pent('X', 2, 4, 22, Board),
	    look_up_board(1, 6, 22, Board, Z0),
	    look_up_board(3, 6, 22, Board, Z)
	;
    	    place_pent('X', 2, 5, 22, Board),
	    look_up_board(1, 7, 22, Board, Z0),
	    look_up_board(3, 7, 22, Board, Z)
	;
    	    place_pent('X', 2, 6, 22, Board),
	    look_up_board(1, 8, 22, Board, Z0),
	    look_up_board(3, 8, 22, Board, Z)
	;
    	    place_pent('X', 2, 7, 22, Board),
	    look_up_board(1, 9, 22, Board, Z0),
	    look_up_board(3, 9, 22, Board, Z)
	;
    	    place_pent('X', 2, 8, 22, Board),
	    look_up_board(1, 10, 22, Board, Z0),
	    look_up_board(3, 10, 22, Board, Z)
	;
    	    place_pent('X', 2, 9, 22, Board),
	    look_up_board(1, 11, 22, Board, Z0),
	    look_up_board(3, 11, 22, Board, Z)
	).
	
set_x_pentomino(X, _, _, Board, Z0, Z) :-
	X =:= 8, !,
	(
	    place_pent('X', 3, 1, 10, Board)
	;
    	    place_pent('X', 4, 1, 10, Board)
	;
    	    place_pent('X', 3, 2, 10, Board),
	    look_up_board(2, 4, 10, Board, Z0),
	    look_up_board(4, 2, 10, Board, Z)
	).
set_x_pentomino(X, Y, Col, Board, Z0, Z) :-
	4 =< X, X =< 6, !,
	M is (X-1)//2,
	N is (Y-1)//2,
	for(J0, 1, N),
	   for(I0, 1, M),
	    I is I0+1,
	    J is J0,
	    place_pent('X', I, J, Col, Board),
	    (
		X =:= 5, I =:= 3 ->
		look_up_board(1, J, Col, Board, Z0),
		look_up_board(5, J, Col, Board, Z)
	    ;
		true
	    ),
	    (
		X =:= 4, I =:= 2, J =:= 7 ->
		look_up_board(1, 7, Col, Board, Z0),
		look_up_board(1, 9, Col, Board, Z)
	    ;
		true
	    ).
/****************************************************************
  ボードの(I、J)成分を返す
  look_up_board(I, J, Col, B, X)
    I  : 行番号
    J  : 列番号
    Col: 列の数+2(+2は壁のため必要)
    B  : ボードの構造体
    X  : マス
****************************************************************/ 
look_up_board(I, J, Col, B, X) :-
	P is Col*I+J+1,
	arg(P, B, X).
/****************************************************************
  ボードの作成
  pent_board(I, J, B)
    I : ボードの縦幅(行の数)
    J : ボードの横幅(列の数)
    B : ボードの構造体
****************************************************************/
pent_board(I, J, Board) :- 3 =< I, I =< 6, !,
	J is 60//I,
	make_board(I, J, Board).

pent_board(I, I, Board) :- I == 8, !,
	make_board(8, 8, Board),
	look_up_board(4, 4, 10, Board, ' '),
	look_up_board(4, 5, 10, Board, ' '),
	look_up_board(5, 4, 10, Board, ' '),
	look_up_board(5, 5, 10, Board, ' ').

make_board(I, J, Board) :-
	M is I+2, N is J+2,
	Total is M*N,
	functor(Board, b, Total), 
	frame(1, 1, M, N, Board).

frame(_, J, _, N, _) :- J > N, !.
frame(I, J, M, N, Board) :- I > M, !,
	J1 is J+1,
	frame(1, J1, M, N, Board).
frame(I, J, M, N, Board) :- 
	(I =:= 1 ; I =:= M ; J =:= 1 ; J =:= N),
	 !,
	 L is (I-1)*N+J,
	 arg(L, Board, '*'),
	 I1 is I+1,
	 J1 is J,
	 frame(I1, J1, M, N, Board).
frame(I, J, M, N, Board) :- 
	I1 is I+1,
	J1 is J,
	frame(I1, J1, M, N, Board).
/****************************************************************
  ボードの表示
  show_result(B)
    B : ボードの構造体
****************************************************************/
show_result(B) :-
	board_size(B, H, W),
	Col is W+2, 
	for(I, 1, H),
	nl,
	   for(J, 1, W),
	   look_up_board(I, J, Col, B, P),
	   write_pent(P),
        fail.
show_result(_) :- nl.

write_pent(P) :- var(P), !, write('_ ').
write_pent(P) :- write(P), write(' ').
/****************************************************************
  ボードのサイズ
  board_size(B, H, W)
    B : ボードの構造体
    H : ボードの縦幅
    W : ボードの横幅
****************************************************************/
board_size(B, H, W) :- 
	board_width(B, W),
	board_height(W, H).

board_width(B, W) :- 
	count_flame(B, 1, W).

count_flame(B, N, W) :- 
	arg(N, B, P), P == '*',
	!,
	N1 is N+1,
	count_flame(B, N1, W).
count_flame(_, N, W) :-
	W is N-4.

board_height(W, H) :-
	10 =< W, W =< 20,
	!,
	H is 60//W.
board_height(8, 8).
/****************************************************************
  ペントミノを１つ配置する
  place_pent(P, I, J, Col, B)
    P  : ペントミノの種類名
    I  : 行番号
    J  : 列番号
    Col: 列の数+2(+2は壁のため必要)
    B  : ボードの構造体
****************************************************************/	
place_pent(P, I, J, Col, B) :-
	C is Col*I+J+1,
	!,
	place_pent(P, C, Col, B).

% Pentmino = 'X'
% Number = 1
%   D
% C E G
%   F
place_pent('X', C, Col, Board) :-
	arg(C, Board, 'X'),
	D is C-Col+1,
	arg(D, Board, 'X'),	
	E is C+1,
	arg(E, Board, 'X'),
	F is E+Col,
	arg(F, Board, 'X'),
	G is E+1,
	arg(G, Board, 'X').
% Pentmino = 'F'
% Number = 1
%   D F
% C E 
%   G
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C-Col+1,
	arg(D, Board, 'F'),	
	E is C+1,
	arg(E, Board, 'F'),
	F is D+1,
	arg(F, Board, 'F'),
	G is E+Col,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 2
%   D
% C E F
%     G
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C-Col+1,
	arg(D, Board, 'F'),	
	E is C+1,
	arg(E, Board, 'F'),
	F is E+1,
	arg(F, Board, 'F'),
	G is F+Col,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 3
%   C
%   E G
% D F 
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C+(2*Col)-1,
	arg(D, Board, 'F'),	
	E is C+Col,
	arg(E, Board, 'F'),
	F is D+1,
	arg(F, Board, 'F'),
	G is E+1,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 4
% C
% D E G
%   F
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C+Col,
	arg(D, Board, 'F'),	
	E is D+1,
	arg(E, Board, 'F'),
	F is E+Col,
	arg(F, Board, 'F'),
	G is E+1,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 5
% C D
%   E G
%   F
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C+1,
	arg(D, Board, 'F'),	
	E is D+Col,
	arg(E, Board, 'F'),
	F is E+Col,
	arg(F, Board, 'F'),
	G is E+1,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 6
%     E
% C D G
%   F
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C+1,
	arg(D, Board, 'F'),	
	E is C-Col+2,
	arg(E, Board, 'F'),
	F is D+Col,
	arg(F, Board, 'F'),
	G is D+1,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 7
%   D
% C E
%   F G
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C-Col+1,
	arg(D, Board, 'F'),	
	E is C+1,
	arg(E, Board, 'F'),
	F is E+Col,
	arg(F, Board, 'F'),
	G is F+1,
	arg(G, Board, 'F').
% Pentmino = 'F'
% Number = 8
%   D
% C F G
% E
place_pent('F', C, Col, Board) :-
	arg(C, Board, 'F'),
	D is C-Col+1,
	arg(D, Board, 'F'),	
	E is C+Col,
	arg(E, Board, 'F'),
	F is C+1,
	arg(F, Board, 'F'),
	G is F+1,
	arg(G, Board, 'F').
% Pentmino = 'I'
% Number = 1
% C
% D
% E
% F
% G
place_pent('I', C, Col, Board) :-
	arg(C, Board, 'I'),
	D is C+Col,
	arg(D, Board, 'I'),	
	E is D+Col,
	arg(E, Board, 'I'),
	F is E+Col,
	arg(F, Board, 'I'),
	G is F+Col,
	arg(G, Board, 'I').
% Pentmino = 'I'
% Number = 2
% C D E F G
place_pent('I', C, _, Board) :-
	arg(C, Board, 'I'),
	D is C+1,
	arg(D, Board, 'I'),	
	E is D+1,
	arg(E, Board, 'I'),
	F is E+1,
	arg(F, Board, 'I'),
	G is F+1,
	arg(G, Board, 'I').
% Pentmino = 'L'
% Number = 1
% C 
% D
% E 
% F G 
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+Col,
	arg(D, Board, 'L'),	
	E is D+Col,
	arg(E, Board, 'L'),
	F is E+Col,
	arg(F, Board, 'L'),
	G is F+1,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 2
% C E F G
% D
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+Col,
	arg(D, Board, 'L'),	
	E is C+1,
	arg(E, Board, 'L'),
	F is E+1,
	arg(F, Board, 'L'),
	G is F+1,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 3
% C D
%   E
%   F
%   G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+1,
	arg(D, Board, 'L'),	
	E is D+Col,
	arg(E, Board, 'L'),
	F is E+Col,
	arg(F, Board, 'L'),
	G is F+Col,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 4
%       F
% C D E G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+1,
	arg(D, Board, 'L'),	
	E is D+1,
	arg(E, Board, 'L'),
	F is E-Col+1,
	arg(F, Board, 'L'),
	G is E+1,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 5
%   C
%   D
%   F  
% E G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+Col,
	arg(D, Board, 'L'),	
	E is D+(2*Col)-1,
	arg(E, Board, 'L'),
	F is D+Col,
	arg(F, Board, 'L'),
	G is E+1,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 6
% C
% D E F G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+Col,
	arg(D, Board, 'L'),	
	E is D+1,
	arg(E, Board, 'L'),
	F is E+1,
	arg(F, Board, 'L'),
	G is F+1,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 7
% C E
% D
% F 
% G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+Col,
	arg(D, Board, 'L'),	
	E is C+1,
	arg(E, Board, 'L'),
	F is D+Col,
	arg(F, Board, 'L'),
	G is F+Col,
	arg(G, Board, 'L').
% Pentmino = 'L'
% Number = 8
% C D E F
%       G
place_pent('L', C, Col, Board) :-
	arg(C, Board, 'L'),
	D is C+1,
	arg(D, Board, 'L'),	
	E is D+1,
	arg(E, Board, 'L'),
	F is E+1,
	arg(F, Board, 'L'),
	G is F+Col,
	arg(G, Board, 'L').
% Pentmino = 'N'
% Number = 1
% C
% D
% E F
%   G
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+Col,
	arg(D, Board, 'N'),	
	E is D+Col,
	arg(E, Board, 'N'),
	F is E+1,
	arg(F, Board, 'N'),
	G is F+Col,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 2
%   D F G
% C E
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C-Col+1,
	arg(D, Board, 'N'),	
	E is C+1,
	arg(E, Board, 'N'),
	F is D+1,
	arg(F, Board, 'N'),
	G is F+1,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 3
% C
% D E
%   F
%   G
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+Col,
	arg(D, Board, 'N'),	
	E is D+1,
	arg(E, Board, 'N'),
	F is E+Col,
	arg(F, Board, 'N'),
	G is F+Col,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 4
%      E G
%  C D F
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+1,
	arg(D, Board, 'N'),	
	E is C-Col+2,
	arg(E, Board, 'N'),
	F is D+1,
	arg(F, Board, 'N'),
	G is E+1,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 5
%   C
%   E
% D G
% F
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+(2*Col)-1,
	arg(D, Board, 'N'),	
	E is C+Col,
	arg(E, Board, 'N'),
	F is D+Col,
	arg(F, Board, 'N'),
	G is D+1,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 6
% C D
%   E F G
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+1,
	arg(D, Board, 'N'),	
	E is D+Col,
	arg(E, Board, 'N'),
	F is E+1,
	arg(F, Board, 'N'),
	G is F+1,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 7
%   D
% C F
% E
% G
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C-Col+1,
	arg(D, Board, 'N'),	
	E is C+Col,
	arg(E, Board, 'N'),
	F is C+1,
	arg(F, Board, 'N'),
	G is E+Col,
	arg(G, Board, 'N').
% Pentmino = 'N'
% Number = 8
% C D E 
%     F G
place_pent('N', C, Col, Board) :-
	arg(C, Board, 'N'),
	D is C+1,
	arg(D, Board, 'N'),	
	E is D+1,
	arg(E, Board, 'N'),
	F is E+Col,
	arg(F, Board, 'N'),
	G is F+1,
	arg(G, Board, 'N').
% Pentmino = 'P'
% Number = 1
% C E
% D G
% F
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+Col,
	arg(D, Board, 'P'),	
	E is C+1,
	arg(E, Board, 'P'),
	F is D+Col,
	arg(F, Board, 'P'),
	G is D+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 2
% C D F
%   E G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+1,
	arg(D, Board, 'P'),	
	E is D+Col,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is E+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 3
%   D
% C F
% E G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C-(Col-1),
	arg(D, Board, 'P'),	
	E is C+Col,
	arg(E, Board, 'P'),
	F is C+1,
	arg(F, Board, 'P'),
	G is E+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 4
% C E
% D F G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+Col,
	arg(D, Board, 'P'),	
	E is C+1,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is F+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 5
% C E
% D F
%   G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+Col,
	arg(D, Board, 'P'),	
	E is C+1,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is F+Col,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 6
%   D F
% C E G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C-(Col-1),
	arg(D, Board, 'P'),	
	E is C+1,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is E+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 7
% C 
% D F
% E G
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+Col,
	arg(D, Board, 'P'),	
	E is D+Col,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is E+1,
	arg(G, Board, 'P').
% Pentmino = 'P'
% Number = 8
% C E G
% D F
place_pent('P', C, Col, Board) :-
	arg(C, Board, 'P'),
	D is C+Col,
	arg(D, Board, 'P'),	
	E is C+1,
	arg(E, Board, 'P'),
	F is D+1,
	arg(F, Board, 'P'),
	G is E+1,
	arg(G, Board, 'P').
% Pentmino = 'T'
% Number = 1
% C D F
%   E
%   G
place_pent('T', C, Col, Board) :-
	arg(C, Board, 'T'),
	D is C+1,
	arg(D, Board, 'T'),	
	E is D+Col,
	arg(E, Board, 'T'),
	F is D+1,
	arg(F, Board, 'T'),
	G is E+Col,
	arg(G, Board, 'T').
% Pentmino = 'T'
% Number = 2
%     E
% C D F
%     G
place_pent('T', C, Col, Board) :-
	arg(C, Board, 'T'),
	D is C+1,
	arg(D, Board, 'T'),	
	E is C-(Col-2),
	arg(E, Board, 'T'),
	F is D+1,
	arg(F, Board, 'T'),
	G is F+Col,
	arg(G, Board, 'T').
% Pentmino = 'T'
% Number = 3
%   C
%   E
% D F G
place_pent('T', C, Col, Board) :-
	arg(C, Board, 'T'),
	D is C+(Col*2)-1,
	arg(D, Board, 'T'),	
	E is C+Col,
	arg(E, Board, 'T'),
	F is D+1,
	arg(F, Board, 'T'),
	G is F+1,
	arg(G, Board, 'T').
% Pentmino = 'T'
% Number = 4
% C
% D F G
% E
place_pent('T', C, Col, Board) :-
	arg(C, Board, 'T'),
	D is C+Col,
	arg(D, Board, 'T'),	
	E is D+Col,
	arg(E, Board, 'T'),
	F is D+1,
	arg(F, Board, 'T'),
	G is F+1,
	arg(G, Board, 'T').
% Pentmino = 'U'
% Number = 1
% C   F
% D E G
place_pent('U', C, Col, Board) :-
	arg(C, Board, 'U'),
	D is C+Col,
	arg(D, Board, 'U'),	
	E is D+1,
	arg(E, Board, 'U'),
	F is C+2,
	arg(F, Board, 'U'),
	G is E+1,
	arg(G, Board, 'U').
% Pentmino = 'U'
% Number = 2
% C E
% D
% F G
place_pent('U', C, Col, Board) :-
	arg(C, Board, 'U'),
	D is C+Col,
	arg(D, Board, 'U'),	
	E is C+1,
	arg(E, Board, 'U'),
	F is D+Col,
	arg(F, Board, 'U'),
	G is F+1,
	arg(G, Board, 'U').
% Pentmino = 'U'
% Number = 3
% C E F
% D   G
place_pent('U', C, Col, Board) :-
	arg(C, Board, 'U'),
	D is C+Col,
	arg(D, Board, 'U'),	
	E is C+1,
	arg(E, Board, 'U'),
	F is E+1,
	arg(F, Board, 'U'),
	G is D+2,
	arg(G, Board, 'U').
% Pentmino = 'U'
% Number = 4
% C D
%   F
% E G
place_pent('U', C, Col, Board) :-
	arg(C, Board, 'U'),
	D is C+1,
	arg(D, Board, 'U'),	
	E is C+(2*Col),
	arg(E, Board, 'U'),
	F is D+Col,
	arg(F, Board, 'U'),
	G is E+1,
	arg(G, Board, 'U').
% Pentmino = 'V'
% Number = 1
% C 
% D
% E F G
place_pent('V', C, Col, Board) :-
	arg(C, Board, 'V'),
	D is C+Col,
	arg(D, Board, 'V'),	
	E is D+Col,
	arg(E, Board, 'V'),
	F is E+1,
	arg(F, Board, 'V'),
	G is F+1,
	arg(G, Board, 'V').
% Pentmino = 'V'
% Number = 2
% C E G
% D
% F
place_pent('V', C, Col, Board) :-
	arg(C, Board, 'V'),
	D is C+Col,
	arg(D, Board, 'V'),	
	E is C+1,
	arg(E, Board, 'V'),
	F is D+Col,
	arg(F, Board, 'V'),
	G is E+1,
	arg(G, Board, 'V').
% Pentmino = 'V'
% Number = 3
% C D E
%     F
%     G
place_pent('V', C, Col, Board) :-
	arg(C, Board, 'V'),
	D is C+1,
	arg(D, Board, 'V'),	
	E is D+1,
	arg(E, Board, 'V'),
	F is E+Col,
	arg(F, Board, 'V'),
	G is F+Col,
	arg(G, Board, 'V').
% Pentmino = 'V'
% Number = 4
%     D
%     F
% C E G
place_pent('V', C, Col, Board) :-
	arg(C, Board, 'V'),
	D is C-(2*Col)+2,
	D > 0,   %%%  arg/3 の第一引数が負の数にならないため必要
	arg(D, Board, 'V'),	
	E is C+1,
	arg(E, Board, 'V'),
	F is D+Col,
	arg(F, Board, 'V'),
	G is E+1,
	arg(G, Board, 'V').
% Pentmino = 'W'
% Number = 1
% C
% D E
%   F G
place_pent('W', C, Col, Board) :-
	arg(C, Board, 'W'),
	D is C+Col,
	arg(D, Board, 'W'),	
	E is D+1,
	arg(E, Board, 'W'),
	F is E+Col,
	arg(F, Board, 'W'),
	G is F+1,
	arg(G, Board, 'W').
% Pentmino = 'W'
% Number = 2
%   D G
% C F
% E
place_pent('W', C, Col, Board) :-
	arg(C, Board, 'W'),
	D is C-(Col-1),
	arg(D, Board, 'W'),	
	E is C+Col,
	arg(E, Board, 'W'),
	F is C+1,
	arg(F, Board, 'W'),
	G is D+1,
	arg(G, Board, 'W').
% Pentmino = 'W'
% Number = 3
% C D
%   E F
%     G
place_pent('W', C, Col, Board) :-
	arg(C, Board, 'W'),
	D is C+1,
	arg(D, Board, 'W'),	
	E is D+Col,
	arg(E, Board, 'W'),
	F is E+1,
	arg(F, Board, 'W'),
	G is F+Col,
	arg(G, Board, 'W').
% Pentmino = 'W'
% Number = 4
%     E
%   D G
% C F
place_pent('W', C, Col, Board) :-
	arg(C, Board, 'W'),
	D is C-(Col-1),
	arg(D, Board, 'W'),	
	E is D-(Col-1),
	arg(E, Board, 'W'),
	F is C+1,
	arg(F, Board, 'W'),
	G is D+1,
	arg(G, Board, 'W').
% Pentmino = 'Y'
% Number = 1
%     E
% C D F G
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+1,
	arg(D, Board, 'Y'),	
	E is C-(Col-2),
	arg(E, Board, 'Y'),
	F is D+1,
	arg(F, Board, 'Y'),
	G is F+1,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 2
% C
% D
% E G
% F
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+Col,
	arg(D, Board, 'Y'),	
	E is D+Col,
	arg(E, Board, 'Y'),
	F is E+Col,
	arg(F, Board, 'Y'),
	G is E+1,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 3
% C D F G 
%   E 
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+1,
	arg(D, Board, 'Y'),	
	E is D+Col,
	arg(E, Board, 'Y'),
	F is D+1,
	arg(F, Board, 'Y'),
	G is F+1,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 4
%   D
% C E
%   F
%   G
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C-(Col-1),
	arg(D, Board, 'Y'),	
	E is C+1,
	arg(E, Board, 'Y'),
	F is E+Col,
	arg(F, Board, 'Y'),
	G is F+Col,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 5
% C
% D F
% E
% G
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+Col,
	arg(D, Board, 'Y'),	
	E is D+Col,
	arg(E, Board, 'Y'),
	F is D+1,
	arg(F, Board, 'Y'),
	G is E+Col,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 6
% C D E G
%     F
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+1,
	arg(D, Board, 'Y'),	
	E is D+1,
	arg(E, Board, 'Y'),
	F is E+Col,
	arg(F, Board, 'Y'),
	G is E+1,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 7
%   C
%   E
% D F
%   G
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C+(2*Col)-1,
	arg(D, Board, 'Y'),	
	E is C+Col,
	arg(E, Board, 'Y'),
	F is D+1,
	arg(F, Board, 'Y'),
	G is F+Col,
	arg(G, Board, 'Y').
% Pentmino = 'Y'
% Number = 8
%   D
% C E F G
place_pent('Y', C, Col, Board) :-
	arg(C, Board, 'Y'),
	D is C-(Col-1),
	arg(D, Board, 'Y'),	
	E is C+1,
	arg(E, Board, 'Y'),
	F is E+1,
	arg(F, Board, 'Y'),
	G is F+1,
	arg(G, Board, 'Y').
% Pentmino = 'Z'
% Number = 1
% C D
%   E
%   F G
place_pent('Z', C, Col, Board) :-
	arg(C, Board, 'Z'),
	D is C+1,
	arg(D, Board, 'Z'),	
	E is D+Col,
	arg(E, Board, 'Z'),
	F is E+Col,
	arg(F, Board, 'Z'),
	G is F+1,
	arg(G, Board, 'Z').
% Pentmino = 'Z'
% Number = 2
%     F
% C E G
% D
place_pent('Z', C, Col, Board) :-
	arg(C, Board, 'Z'),
	D is C+Col,
	arg(D, Board, 'Z'),	
	E is C+1,
	arg(E, Board, 'Z'),
	F is C-(Col-2),
	arg(F, Board, 'Z'),
	G is E+1,
	arg(G, Board, 'Z').
% Pentmino = 'Z'
% Number = 3
%   C F
%   E
% D G
place_pent('Z', C, Col, Board) :-
	arg(C, Board, 'Z'),
	D is C+(2*Col)-1,
	arg(D, Board, 'Z'),	
	E is C+Col,
	arg(E, Board, 'Z'),
	F is C+1,
	arg(F, Board, 'Z'),
	G is D+1,
	arg(G, Board, 'Z').
% Pentmino = 'Z'
% Number = 4
% C
% D E F
%     G
place_pent('Z', C, Col, Board) :- 
	arg(C, Board, 'Z'),
	D is C+Col,
	arg(D, Board, 'Z'),	
	E is D+1,
	arg(E, Board, 'Z'),
	F is E+1,
	arg(F, Board, 'Z'),
	G is F+Col,
	arg(G, Board, 'Z').

/****************************************************************
	Utilities
****************************************************************/
for(M, M, N) :- M =< N.
for(I, M, N) :- M =< N, M1 is M + 1, for(I, M1, N).

pent_select(X, [X|Xs], Xs).
pent_select(X, [Y|Ys], [Y|Zs]) :- pent_select(X, Ys, Zs).

/****************************************************************
	For applet
****************************************************************/

pentomino_applet(X, B) :-
	solve_pentomino(X, B0),
	remove_asterisk(B0, B).

remove_asterisk(B0, B) :-
	B0 =.. [_|As],
	rm_aster(As, B).

rm_aster([], []) :- !.
rm_aster([A|As], Ps) :- A == '*', !,
	rm_aster(As,Ps).
rm_aster([A|As], [A|Ps]) :- 
	rm_aster(As,Ps).
	
	
