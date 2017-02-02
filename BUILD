load("//:prolog.bzl", "pl2j")

VERSION = '1.4.2'
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

#zip_file(
#  name = 'all',
#  srcs = [
#    ':cafeteria',
#    ':compiler',
#    ':runtime',
#  ],
#  out = 'all.zip',
#)

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
    exclude = REPL),
)

genrule(
    name = "builtin_srcjar",
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cd java",
        "zip -q $$ROOT/$@ com/googlecode/prolog_cafe/builtin/*.java"]),
    local = 1,
    outs = ["builtin.srcjar"],
)

#java_library(
#  name = 'builtin',
#  srcs = glob([SRC + 'builtin/*.java'], exclude = REPL + IO) +
#  [
##    ':builtin_srcs',
##    ':system_srcs',
#  ],
#  deps = [':lang'],
#)

java_library(
  name = 'builtin',
  srcs = [
    ":builtin_srcjar",
    ":builtin_srcs",
    ":system_srcs",
  ],
  deps = [':lang'],
)

pl2j(
  name = 'builtin_srcs',
  src = 'src/builtin/builtins.pl',
  out = 'builtins.srcjar',
)

pl2j(
  name = 'system_srcs',
  src = 'src/builtin/system.pl',
  out = 'system.srcjar',
)

java_library(
  name = 'io',
  srcs = IO + [':io_srcs'],
  deps = [
    ':builtin',
    ':lang',
  ],
)

#pl2j(
#  name = 'io_srcs',
#  src = 'src/builtin/io.pl',
#  out = 'io.src.zip',
#)

#java_library(
#  name = 'compiler',
#  srcs = glob([SRC + 'compiler/**/*.java']) + [
#    ':pl2am_srcs',
#    ':am2j_srcs',
#  ],
#  deps = [
#    ':builtin',
#    ':io',
#    ':lang',
#  ],
#)

#pl2j(
#  name = 'pl2am_srcs',
#  src = 'src/compiler/pl2am.pl',
#  out = 'pl2am.src.zip',
#)

#pl2j(
#  name = 'am2j_srcs',
#  src = 'src/compiler/am2j.pl',
#  out = 'am2j.src.zip',
#)

#pl2j(
#  name = 'cafeteria_srcs',
#  src = 'src/builtin/cafeteria.pl',
#  out = 'cafeteria.src.zip',
#)
