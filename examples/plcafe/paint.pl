%   File   : paint.pl
%   Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%   Updated: 15 May 2008
%   Purpose: Graphics
%   Usage  :
%	     % plcafe -cp paint.jar
%	     ?- main.
%         or
%	     % plcafe -cp paint.jar -t main

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Main
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
main :-
	start, 
	repeat,
	  write('Simple Graphical Examples (bye. to quit)'), nl,
	  tab(4), write('Example: ex1.'), nl,
	  tab(4), write('Example: ex2.'), nl,
	  tab(4), write('Example: ex3.'), nl, nl,
	  write('|: '), flush_output,
	  read(Cmd),
	do(Cmd),
	!.

do(X) :- var(X), !, fail.
do(bye)  :- !, stop.
do(quit) :- !, stop.
do(end_of_file) :- !, stop.
do(Cmd) :-
	statistics(runtime, _),
	call(Cmd),
	statistics(runtime, [_,T]),
	tab(3), write('Time = '), write(T), write(' msec'), nl, nl,
	fail.

%%% Example 1
ex1 :- init, ex1(30).

ex1(N) :-
	integer(N),
	R is random*255, 
	G is random*255, 
	B is random*255, 
	setColor(rgb(R,G,B)),
	getCenter(Cx, Cy),
	for(I, -N, N),
	    X1 is Cx+I*5,
	    Y1 is Cy+N*5,
	    X2 is Cx+N*5,
	    Y2 is Cy-I*5,
	    drawLine(X1, Y1, X2, Y2),
	    A1 is Cx-I*5,
	    B1 is Cy-N*5,
	    A2 is Cx-N*5,
	    B2 is Cy+I*5,
	    drawLine(A1, B1, A2, B2),
        fail.
ex1(_).

%%% Example 2
ex2 :- init, ex2(6).

ex2(N) :-
	integer(N),
	for(I, 1, N),
	  M is I*5, 
	  ex1(M),
        fail.
ex2(_).

%%% Example 3
ex3 :- init, ex3(100).

ex3(N) :-
	integer(N),
	getCenter(Cx, Cy),
	R is random*255, 
	G is random*255, 
	B is random*255, 
	setColor(rgb(R,G,B)),
	for(I, 1, N),
	    X is round(Cx-I*3/2),
	    Y is round(Cy-I*3/2),
	    W is I*3,
	    H is I*3,
	    drawOval(X, Y, W, H),
        fail.	
ex3(X).

%%% Utilities
for(I, I, N) :- I =< N.
for(I, J, N) :- J < N, J1 is J+1, for(I, J1, N).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Start, Init, and Stop
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- dynamic current_image/1, current_canvas/1, current_frame/1.
:- dynamic current_color/2, current_font/3.

start :- start(350, 350), init.

start(Width, Height) :-
	integer(Width),
	integer(Height),
	retractall(current_frame(_)),
	retractall(current_canvas(_)),
	retractall(current_image(_)),
	retractall(current_color(_,_)),
	retractall(current_font(_,_,_)),
	java_constructor('java.awt.Frame', Frame),
	java_constructor('java.awt.BorderLayout', Border),
	java_method(Frame, setLayout(Border), _),
	java_method(Frame, setSize(Width,Height), _),
	java_method(Frame, setTitle('Simple Graphical Examples'), _),
	java_constructor('java.awt.Canvas', Canvas),
	java_method(Frame, add('Center', Canvas), _),
	java_get_field('java.lang.Boolean', 'TRUE', True),
	java_method(Frame, setVisible(True), _),
	java_method(Canvas, getWidth,  CW),
	java_method(Canvas, getHeight, CH),
	java_method(Canvas, createImage(CW,CH), Image),
	assertz(current_frame(Frame)),
	assertz(current_canvas(Canvas)),
	assertz(current_image(Image)).

init :-
	setColor(black),
	setBackground(white),
	setFont(dialog, italic, 12),
	getSize(W, H),
	clearRect(0, 0, W, H).

stop :-
	current_frame(Frame),
	java_get_field('java.lang.Boolean', 'FALSE', F),
	java_method(Frame, setVisible(F), _).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Size
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
getHeight(X) :-
	current_canvas(C),
	java_method(C, getHeight, X).

getWidth(X) :-
	current_canvas(C),
	java_method(C, getWidth, X).

getCenter(X, Y) :-
	getWidth(W),
	getHeight(H),
	X is (W+1)//2,
	Y is (H+1)//2.

getSize(X, Y) :- 
	getWidth(X), 
	getHeight(Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Graphics
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
get_graphics(Gra, Canvas) :-
	current_canvas(Canvas),
	java_method(Canvas, getGraphics, Gra).

get_image_graphics(ImgGra, Image) :-
	current_image(Image),
	java_method(Image,  getGraphics, ImgGra).

paint(Function) :-
	get_graphics(Gra, Canvas),
	get_image_graphics(ImgGra, Image),
	java_method(Gra,    Function, _),
	java_method(ImgGra, Function, _),
	java_method(Gra,    drawImage(Image,0,0,Canvas), _).

repaint :-
	get_graphics(Gra, Canvas),
	get_image_graphics(_, Image),
	java_method(Gra, drawImage(Image,0,0,Canvas), _).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Drawing Functions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clearRect(X, Y, Width, Height) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	paint(clearRect(X,Y,Width,Height)).

drawLine(X1, Y1, X2, Y2) :- 
	integer(X1), integer(Y1), integer(X2), integer(Y2),
	paint(drawLine(X1,Y1,X2,Y2)).

drawString(Str, X, Y) :- 
	atom(Str), integer(X), integer(Y),
	paint(drawString(Str,X,Y)).

drawOval(X, Y, Width, Height) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	paint(drawOval(X,Y,Width,Height)).

fill3DRect(X, Y, Width, Height, Raise) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	boolean_map(Raise, Bool),	
	paint(fill3DRect(X,Y,Width,Height,Bool)).

drawRect(X, Y, Width, Height) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	paint(drawRect(X,Y,Width,Height)).

fillRect(X, Y, Width, Height) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	paint(fillRect(X,Y,Width,Height)).

draw3DRect(X, Y, Width, Height, Raise) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	boolean_map(Raise, Bool),	
	paint(draw3DRect(X,Y,Width,Height,Bool)).

fillOval(X, Y, Width, Height) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	paint(fillOval(X,Y,Width,Height)).

drawArc(X, Y, Width, Height, StartAngle, ArcAngle) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	integer(StartAngle), integer(ArcAngle), 
	paint(drawArc(X,Y,Width,Height,StartAngle,ArcAngle)).

fillArc(X, Y, Width, Height, StartAngle, ArcAngle) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	integer(StartAngle), integer(ArcAngle), 
	paint(fillArc(X,Y,Width,Height,StartAngle,ArcAngle)).

drawRoundRect(X, Y, Width, Height, ArcWidth, ArcHeight) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	integer(ArcWidth), integer(ArcHeight),
	paint(drawRoundRect(X,Y,Width,Height,ArcWidth,ArcHeight)).

fillRoundRect(X, Y, Width, Height, ArcWidth, ArcHeight) :-
	integer(X), integer(Y), integer(Width), integer(Height),
	integer(ArcWidth), integer(ArcHeight),
	paint(fillRoundRect(X,Y,Width,Height,ArcWidth,ArcHeight)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Color
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
setColor(Co) :- color_map(Co, Obj), !,
	current_canvas(Canvas),
	java_method(Canvas, setForeground(Obj), _),
	retractall(current_color(_,_)),
	assertz(current_color(Co, Obj)).
setColor(Co) :- 
	findall(Co, color(Co), Domain),
	throw(domain_error(setColor(Co),1,['rgb(R,G,B)'|Domain],Co)).

setBackground(Co) :- color_map(Co, Obj), !,
	current_canvas(Canvas),
	java_method(Canvas, setBackground(Obj), _).
setBackground(Co) :- 
	findall(Co, color(Co), Domain),
	throw(domain_error(setBackground(Co),1,['rgb(R,G,B)'|Domain],Co)).

color_map(Co, Obj) :- color(Co),  !, 
	java_get_field('java.awt.Color', Co, Obj).
color_map(Co, Obj) :- 
	Co = rgb(X,Y,Z), 
	R is round(X), 0 =< R, R =< 255,
	G is round(Y), 0 =< G, G =< 255,
	B is round(Z), 0 =< B, B =< 255,
	!,
	java_constructor('java.awt.Color'(R,G,B), Obj).

color(black).
color(blue).
color(cyan).
color(darkGray).
color(gray).
color(green).
color(lightGray).
color(magenta).
color(orange).
color(pink).
color(red).
color(white).
color(yellow).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Font
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
setFont(Font, Style, Size) :- font_map(Font, Style, Size, Obj), !,
	current_canvas(Canvas),
	java_method(Canvas, setFont(Obj), _),
	retractall(current_font(_,_,_)),
	assertz(current_font(Font,Style,Size)).
setFont(Font, Style, Size) :-
	findall(F, font(F), Domain),
	throw(domain_error(setFont(Font, Style, Size),1,Domain,Font)).

font_map(Font, Style, Size, Obj) :- 
	integer(Size),
	font(Font, F),
	font_style(Style, St),
	java_constructor('java.awt.Font'(F, St, Size), Obj).

font(helvetica,    'Helvetica').
font(times_roman,  'TimesRoman').
font(courier,      'Courier').
font(dialog,       'Dialog').
font(dialog_input, 'DialogInput').

font_style(plain,  0).
font_style(bold,   1).
font_style(italic, 2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Boolean
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
boolean_map(Bool, Obj) :-
	boolean(Bool,  B),
	java_get_field('java.lang.Boolean', B, Obj).

boolean(true,  'TRUE').
boolean(false, 'FALSE').

%%% END
