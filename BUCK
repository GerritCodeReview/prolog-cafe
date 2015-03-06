SRC = 'java/com/googlecode/prolog_cafe/'

REPL = [
  SRC + 'builtin/PRED_$write_toString_2.java',
  SRC + 'lang/PrologMain.java',
]

IO = [
  SRC + 'builtin/PRED_close_2.java',
  SRC + 'builtin/PRED_flush_output_1.java',
  SRC + 'builtin/PRED_open_4.java',
  SRC + 'builtin/PRED_read_line_2.java',
  SRC + 'builtin/PRED_tab_2.java',
]

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
  srcs = glob([SRC + 'lang/*.java'], excludes = REPL),
)

java_library(
  name = 'builtin',
  srcs = glob([SRC + 'builtin/*.java'], excludes = REPL + IO) + [
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
  name = 'io',
  srcs = IO + [':io_srcs'],
  deps = [
    ':builtin',
    ':lang',
  ],
)

pl2j(
  name = 'io_srcs',
  src = 'src/builtin/io.pl',
  out = 'io.src.zip',
)

java_library(
  name = 'compiler',
  srcs = glob([SRC + 'compiler/**/*.java']) + [
    ':pl2am_srcs',
    ':am2j_srcs',
  ],
  deps = [
    ':builtin',
    ':io',
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
  srcs = REPL + [':cafeteria_srcs'],
  deps = [
    ':builtin',
    ':io',
    ':lang',
  ],
)

pl2j(
  name = 'cafeteria_srcs',
  src = 'src/builtin/cafeteria.pl',
  out = 'cafeteria.src.zip',
)
