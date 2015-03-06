:- package 'com.googlecode.prolog_cafe.builtin'.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Stream selection and control
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%:- public open/4 (written in Java)
:- public open/3.
%:- public close/2 (written in Java)
:- public close/1.

open(Source_sink, Mode, Stream) :- open(Source_sink, Mode, Stream, []).

close(S_or_a) :- close(S_or_a, []).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Character input/output
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%:- public get_char/2.   (written in Java)
%:- public put_char/2, put_code/2.   (written in Java)
:- public nl/1.

nl(S) :- put_char(S, '\n').

%:- public get/2.  (written in Java)
%:- public tab/2.  (written in Java)
%:- public skip/2. (written in Java)
