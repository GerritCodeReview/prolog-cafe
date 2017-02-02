# Translating Prolog into WAM-based Intermediate Code
def pl2am(name, src, out):
  srcs = [
    'src/compiler/package_fx.pl',
    'src/compiler/pl2am.pl',
    'src/builtin/system.pl',
  ]
  if not src in srcs:
    srcs.append(src)
  native.genrule(
    name = name,
    srcs = srcs,
    cmd = " && ".join([
      'ROOT=$$PWD',
      'TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)',
      'echo "go :- '
        + 'load_files(['
          + "'$$ROOT/src/compiler/package_fx.pl',"
          + "'$$ROOT/src/builtin/system.pl',"
          + "'$$ROOT/src/compiler/pl2am.pl'"
        + ']),'
        + 'pl2am(['
          + "'$$(basename %s)'," % src
          + "'$$ROOT/$@',"
          + '[ed,ac,ie,rc,idx]'
        + ']).'
        + '">$$TMP/go.pl',
      'cd $$(dirname %s)' % src,
      'swipl --traditional --quiet -g go,halt -t "halt(1)" $$TMP/go.pl',
    ]),
    outs = [out],
  )

# Translating WAM-based Intermediate Code into Java
def am2j(name, am, out):
  native.genrule(
    name = name,
    srcs = [
      am,
      'src/compiler/am2j.pl',
    ],
    cmd = " && ".join([
      'ROOT=$$PWD',
      'TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)',
      'echo "go :- '
        + 'load_files(['
          + "'src/compiler/am2j.pl'"
        + ']),'
        + 'am2j(['
          + "'$(location %s)'," % am
          + "'$$TMP/java'"
        + ']).'
        + '">$$TMP/go.pl',
      'mkdir $$TMP/java',
      'swipl --traditional --quiet -g go,halt -t "halt(1)" $$TMP/go.pl',
      'cd $$TMP/java',
      'zip -qr $$ROOT/$@ .',
    ]),
    outs = [out],
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
