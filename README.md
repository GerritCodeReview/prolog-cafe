# Prolog Cafe

A fork of Mutsunori BANBARA's [PrologCafe][1] to support
Gerrit Code Review's customizable project rules.

[1]: http://kaminari.istc.kobe-u.ac.jp/PrologCafe/

## Build

To bootstrap [Buck] and [SWI-Prolog] >= 6.6.4 must be installed and
then build the runtime and compiler with:

    buck build all

To package for Maven into the local `~/.m2/repository` directory:

    buck build install

To publish to the gerrit-maven storage bucket:

    buck build deploy

[Buck]: https://github.com/facebook/buck/
[SWI-Prolog]: http://www.swi-prolog.org/
