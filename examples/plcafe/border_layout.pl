%   File   : border_layout.pl
%   Authors: Mutsunori Banbara (banbara@kobe-u.ac.jp)
%   Updated: 15 May 2008
%   Purpose: java.awt.BorderLayout
%   Usage  :
%	     % plcafe -cp border_layout.jar
%	     ?- main.

main :-
	java_constructor('java.awt.Frame', X),
	java_get_field('java.awt.Color', blue, Blue),
	java_get_field('java.awt.Color', white, White),
	java_method(X, setSize(300,200), _),
	java_method(X, setBackground(Blue), _),
	java_method(X, setForeground(White), _),
	java_method(X, setTitle('Prolog Cafe'), _),
	java_constructor('java.awt.BorderLayout', Border),
	java_method(X, setLayout(Border), _),
	java_get_field('java.awt.Font', 'BOLD', Bold),
	java_constructor('java.awt.Font'('Helvetica', Bold, 12), F1),
	java_method(X, setFont(F1), _),
	java_constructor('java.awt.Button'('A Prolog to Java Translator'), B1),
	java_constructor('java.awt.Button'('Prolog Cafe'), B2),
	java_constructor('java.awt.Button'('Prolog'), B3),
	java_constructor('java.awt.Button'('Java'), B4),
	java_constructor('java.awt.Button'('produced by M.Banbara and N.Tamura'), B5),
	java_method(X, add('North',  B1),_),
	java_method(X, add('Center', B2),_),
	java_method(X, add('West',   B3),_),
	java_method(X, add('East',   B4),_),
	java_method(X, add('South',  B5),_),
	java_get_field('java.lang.Boolean', 'TRUE', True),
	java_method(X, setVisible(True), _).



