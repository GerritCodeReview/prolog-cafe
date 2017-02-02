# Prolog Cafe

A fork of Mutsunori BANBARA's [PrologCafe][1] to support
Gerrit Code Review's customizable project rules.

[1]: http://kaminari.istc.kobe-u.ac.jp/PrologCafe/

## Build

To bootstrap [Bazel] and [SWI-Prolog] >= 6.6.4 must be installed and
then build the runtime and compiler with:

    bazel build :all

To package for Maven into the local `~/.m2/repository` directory:

    ./mvn.sh install

To publish to the gerrit-maven storage bucket:

    ./mvn.sh deploy

[Bazel]: https://www.bazel.build/versions/master/docs/install.html
[SWI-Prolog]: http://www.swi-prolog.org/
