%   File   : thread2.pl
%   Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%            Naoyuki Tamura (tamura@kobe-u.ac.jp)
%   Updated: 26 May 2008
%   Purpose: Multi-thread execution
%   Usage  :
%	     % plcafe -cp queens.jar
%	     ?- [thread2].
%	     ?- main.
%         or
%	     % plcafe -cp thread2.jar:queens.jar
%	     ?- main.

main :-
	statistics(runtime, _),
	start,
	statistics(runtime, [_,T]),
	write('Time = '), write(T), 
	write(' msec.'), nl.

start :- 
	G1 = (queens(10, X), write(X), nl, fail),
	msg(start1 = G1),
	start(G1, E1),
	G2 = (queens(8, Y), write(Y), nl, fail),
	msg(start2 = G2),
	start(G2, E2),
	join(E1),
	join(E2),
	stop(E1),
	stop(E2),
	msg(end).

msg(Msg) :-
	write(Msg), nl, flush_output.

%%
%% Utilities
%%
start(G, Engine) :-
	java_constructor0('jp.ac.kobe_u.cs.prolog.lang.PrologControl', Engine),
	java_method0(Engine, setPredicate(G), _),
	java_method0(Engine, start, _).

in_success(Engine) :-
	java_get_field0('java.lang.Boolean', 'TRUE', T),
	java_method(Engine, in_success, T).

in_failure(Engine) :-
	java_get_field0('java.lang.Boolean', 'TRUE', T),
	java_method(Engine, in_failure, T).

ready(Engine) :-
	java_get_field0('java.lang.Boolean', 'TRUE', T),
	java_method(Engine, ready, T).

cont(Engine) :- java_method0(Engine, cont, _).

join(Engine) :- java_method0(Engine, join, _).

stop(Engine) :- java_method0(Engine, stop, _).

sleep(N) :- java_method('java.lang.Thread', sleep(N), _).

%result(Engine, Result) :- java_method0(Engine, next, Result).

