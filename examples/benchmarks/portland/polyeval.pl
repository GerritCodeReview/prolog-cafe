/*  VAX C-Prolog Benchmark Package  */
/*  Copyright 1985 by Tektronix, Inc., and Portland State University  */

gen_list(0,[]).
gen_list(N,[[N,N]|L])   :-  N1 is N-1, gen_list(N1,L).
expand_term(0,Xvalue, Tvalue)     :-     Tvalue is Xvalue.
expand_term(Power,Xvalue, Tvalue)   :-  TempValue  is 2*Xvalue,
	Power1 is Power - 1,
	expand_term(Power1 ,TempValue, Tvalue).

eval_term([Coef,Power],Value,Xvalue) :-  expand_term(Power,Xvalue, Tvalue),
	Value is Coef*Tvalue .  

eval_poly([],Answer, Svalue)          :-   Answer is Svalue .
eval_poly([Term|Rest],Answer, Svalue) :-  eval_term(Term,Value,1),
	Ans is Svalue + Value,
	eval_poly(Rest,Answer,Ans).
polybench(N,Answer)  :-  gen_list(N,Poly),!,
	eval_poly(Poly,Answer, 0 ).
