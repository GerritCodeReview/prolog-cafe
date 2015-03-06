SRC = 'java/com/googlecode/prolog_cafe/'

genrule(
  name = 'all',
  cmd = ':>all',
  deps = [
    ':cafeteria',
    ':compiler',
    ':runtime',
  ],
  out = '__fake.all__',
)

java_binary(
  name = 'runtime',
  deps = [
    ':builtin',
    ':lang',
  ],
)

java_library(
  name = 'lang',
  srcs = glob(
    [SRC + 'lang/*.java'],
    excludes = [SRC + 'lang/PrologMain.java'],
  ),
)

java_library(
  name = 'builtin',
  srcs = glob([SRC + 'builtin/*.java']) + [
    ':builtin_srcs',
    ':system_srcs',
  ],
  deps = [':lang'],
)

pl2j(
  name = 'builtin_srcs',
  src = 'src/builtin/builtins.pl',
  out = 'builtins.src.zip',
)

pl2j(
  name = 'system_srcs',
  src = 'src/builtin/system.pl',
  out = 'system.src.zip',
)

java_library(
  name = 'compiler',
  srcs = glob([SRC + 'compiler/**/*.java']) + [
    ':pl2am_srcs',
    ':am2j_srcs',
  ],
  deps = [
    ':builtin',
    ':lang',
  ],
)

pl2j(
  name = 'pl2am_srcs',
  src = 'src/compiler/pl2am.pl',
  out = 'pl2am.src.zip',
)

pl2j(
  name = 'am2j_srcs',
  src = 'src/compiler/am2j.pl',
  out = 'am2j.src.zip',
)

java_binary(
  name = 'cafeteria',
  main_class = 'com.googlecode.prolog_cafe.lang.PrologMain',
  deps = [':cafeteria_lib'],
)

java_library(
  name = 'cafeteria_lib',
  srcs = [
    SRC + 'lang/PrologMain.java',
    ':cafeteria_srcs',
  ],
  deps = [
    ':builtin',
    ':lang',
  ],
)

pl2j(
  name = 'cafeteria_srcs',
  src = 'src/builtin/cafeteria.pl',
  out = 'cafeteria.src.zip',
)
