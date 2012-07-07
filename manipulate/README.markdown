Manipulate demo
===============

Little demo inspired Mathematica's [Manipulate function](http://reference.wolfram.com/mathematica/tutorial/IntroductionToManipulate.html).

It's pretty bare-bones at the moment (no real UI visual cues or error handling).
Input a Clojure maths expression in the textarea.
Graph of fn vs. t will be shown, and sliders created for all other free variables.
E.g., inputting

    (* a (Math/sin (* t x)))

would show a graph of that fn against *t* and create sliders for modifying *a* and *x*.

Build
=====

Run 

    ./make.sh

script, then

    lein run 8080

to start the server on localhost port 8080.
