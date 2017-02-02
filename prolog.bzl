# Translating Prolog into WAM-based Intermediate Code
def pl2am(name, src, out):
  native.genrule(
    name = name,
    outs = [out],
    cmd = 'TMP=$$(mktemp -d || mktemp -d -t bazel-tmp) && '
      + 'echo "go :- '
      + 'load_files(['
        + "'src/compiler/package_fx.pl',"
        + "'src/builtin/system.pl',"
        + "'src/compiler/pl2am.pl'"
      + ']),'
      + 'pl2am(['
        + "'%s'," % src
        + "'$@',"
        + '[ed,ac,ie,rc,idx]'
      + ']).'
      + '">$$TMP/go.pl && '
      + 'swipl --traditional --quiet -g go,halt -t "halt(1)" $$TMP/go.pl',
    srcs = [
      'src/compiler/package_fx.pl',
      'src/compiler/pl2am.pl',
      'src/builtin/system.pl',
    ],
    local = 1,
  )

# Translating WAM-based Intermediate Code into Java
def am2j(name, am, out):
  native.genrule(
    name = name,
    outs = [out],
    cmd = 'ROOT=$$PWD && TMP=$$(mktemp -d || mktemp -d -t bazel-tmp) && echo "go :- '
      + 'load_files(['
        + "'src/compiler/am2j.pl'"
      + ']),'
      + 'am2j(['
        + "'$(location %s)'," % am
        + "'$$TMP/java'"
      + ']).'
      + '">$$TMP/go.pl;'
      + 'mkdir $$TMP/java;'
      + 'swipl --traditional --quiet -g go,halt -t "halt(1)" $$TMP/go.pl;'
      + 'cd $$TMP/java; zip -qr $$ROOT/$@ .',
    srcs = [
      'src/compiler/am2j.pl',
      am,
    ],
    local = 1,
  )

def pl2j(name, src, out):
  pl2am(
    name = name + '_rule.am',
    src = src,
    out = name + '.am',
  )
  am2j(
    name = name,
    am = ':' + name + '_rule.am',
    out = out,
  )
