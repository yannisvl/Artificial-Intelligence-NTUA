check_1(X,Y,Z):-
    next(X,Y),
    belongsTo(X,L),
    belongsTo(Y,L),
    traf(L,high),
    check_2(L,Z1),
    Z is Z1*1.5.

check_1(X,Y,Z):-
    next(X,Y),
    belongsTo(X,L),
    belongsTo(Y,L),
    traf(L,medium),
    check_2(L,Z1),
    Z is Z1*1.3.

check_1(X,Y,Z):-
    next(X,Y),
    belongsTo(X,L),
    belongsTo(Y,L),
    traf(L,low),
    check_2(L,Z),
    Z is Z1*1.1.


check_1(X,Y,Z):-
    next(X,Y),
    belongsTo(X,L),
    belongsTo(Y,L),
    check_2(L,Z).


check_2(L,Z):-
    highway(L,motorway),
    check_3(L,Z1),
    Z is Z1*0.6.


check_2(L,Z):-
    highway(L,trunk),
    check_3(L,Z1),
    Z is Z1*0.7.


check_2(L,Z):-
    highway(L,primary),
    check_3(L,Z1),
    Z is Z1*0.8.


check_2(L,Z):-
    highway(L,secondary),
    check_3(L,Z1),
    Z is Z1*0.9.


check_2(L,Z):-
    highway(L,tertiary),
    check_3(L,Z1),
    Z is Z1*1.1.


check_2(L,Z):-
    highway(L,residential),
    check_3(L,Z1),
    Z is Z1*1.2.


check_2(L,Z):-
    highway(L,unclassified),
    check_3(L,Z1),
    Z is Z1*1.3.


check_2(L,Z):-
    highway(L,service),
    check_3(L,Z1),
    Z is Z1*1.5.


check_2(L,Z):-
    highway(L,motorway_link),
    check_3(L,Z1),
    Z is Z1*0.6.


check_2(L,Z):-
    highway(L,primary_link),
    check_3(L,Z1),
    Z is Z1*0.7.


check_2(L,Z):-
    highway(L,secondary_link),
    check_3(L,Z1),
    Z is Z1*0.9.


check_2(L,Z):-
    highway(L,pedestrian),
    check_3(L,Z1),
    Z is Z1*100.


check_2(L,Z):-
    highway(L,footway),
    check_3(L,Z1),
    Z is Z1*100.


check_2(L,Z):-
    highway(L,path),
    check_3(L,Z1),
    Z is Z1*100.


check_2(L,Z):-
    highway(L,steps),
    check_3(L,Z1),
    Z is Z1*100.


check_2(L,Z):-
    check_3(L,Z).


check_3(L,Z):-
    toll(L,yes),
    check_4(L,Z1),
    Z is 2*Z1.

check_3(L,Z):-
    check_4(L,Z).


check_4(L,Z):-
    maxspeed(L,Z1),
    check_5(L,Z2),
    Z is 50/Z1 * Z2.


check_4(L,Z):-
    check_5(L,Z).


check_5(L,Z):-
    lanes(L,Z1),
    Z is 2/Z1.

check_5(L,1).