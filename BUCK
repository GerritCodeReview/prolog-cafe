VERSION = '1.4.1'
SRC = 'java/com/googlecode/prolog_cafe/'

REPL = [
  SRC + 'builtin/PRED_$write_toString_2.java',
]

IO = [
  SRC + 'builtin/PRED_close_2.java',
  SRC + 'builtin/PRED_flush_output_1.java',
  SRC + 'builtin/PRED_open_4.java',
  SRC + 'builtin/PRED_read_line_2.java',
  SRC + 'builtin/PRED_tab_2.java',
]

zip_file(
  name = 'all',
  srcs = [
    ':cafeteria',
    ':compiler',
    ':runtime',
  ],
  out = 'all.zip',
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
  srcs = glob([
      SRC + 'exceptions/*.java',
      SRC + 'lang/*.java',
    ],
    excludes = REPL),
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
  name = 'plc',
  main_class = 'com.googlecode.prolog_cafe.compiler.Compiler',
  deps = [':compiler'],
)

java_binary(
  name = 'cafeteria',
  main_class = 'com.googlecode.prolog_cafe.repl.PrologMain',
  deps = [':cafeteria_lib'],
)

java_library(
  name = 'cafeteria_lib',
  srcs = glob([SRC + 'repl/*.java']) + REPL + [':cafeteria_srcs'],
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

java_sources(
  name = 'runtime_src',
  srcs = glob([
      SRC + 'builtin/*.java',
      SRC + 'lang/*.java',
    ],
    excludes = REPL + IO
  ) + [
    'src/builtin/builtins.pl',
    'src/builtin/system.pl',
  ],
)

java_sources(
  name = 'io_src',
  srcs = IO + ['src/builtin/io.pl'],
)

java_sources(
  name = 'compiler_src',
  srcs = glob([SRC + 'compiler/**/*.java']) + [
    'src/compiler/pl2am.pl',
    'src/compiler/am2j.pl',
  ],
)

java_sources(
  name = 'cafeteria_src',
  srcs = glob([SRC + 'repl/*.java']) + REPL + [
    'src/builtin/cafeteria.pl',
  ],
)

maven_package(
  group = 'com.googlecode.prolog-cafe',
  version = VERSION,
  repository = 'gerrit-maven-repository',
  url = 'gs://gerrit-maven/',
  jar = {
    'prolog-cafeteria': ':cafeteria_lib',
    'prolog-compiler': ':compiler',
    'prolog-io': ':io',
    'prolog-runtime': ':runtime',
  },
  src = {
    'prolog-cafeteria': ':cafeteria_src',
    'prolog-compiler': ':compiler_src',
    'prolog-io': ':io_src',
    'prolog-runtime': ':runtime_src',
  },
)
