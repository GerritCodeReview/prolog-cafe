%   File   : frame.pl
%   Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%   Updated: 14 February 2008
%   Purpose: java.awt.Frame
%   Usage  :
%	     % plcafe -cp frame.jar
%	     ?- main.

main :-
	java_constructor('java.awt.Frame', X),
	java_method(X, setSize(200,200), _),
	java_get_field('java.lang.Boolean', 'TRUE', True),
	java_method(X, setVisible(True), _).



