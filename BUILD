load("@com_googlesource_gerrit_bazlets//tools/maven:package.bzl", "maven_package")
load("//:prolog.bzl", "pl2j")

VERSION = "1.4.3"

SRC = "java/com/googlecode/prolog_cafe/"

REPL = [
    SRC + "builtin/PRED_$write_toString_2.java",
]

IO = [
    SRC + "builtin/PRED_close_2.java",
    SRC + "builtin/PRED_flush_output_1.java",
    SRC + "builtin/PRED_open_4.java",
    SRC + "builtin/PRED_read_line_2.java",
    SRC + "builtin/PRED_tab_2.java",
]

genrule(
    name = "all",
    srcs = [
        ":cafeteria_deploy.jar",
        ":compiler",
        ":runtime_deploy.jar",
    ],
    outs = ["all.zip"],
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cp $(SRCS) $$TMP",
        "cd $$TMP",
        "zip -qr $$ROOT/$@ .",
    ]),
)

java_binary(
    name = "runtime",
    main_class = "Dummy",
    runtime_deps = [
        ":builtin",
        ":lang",
    ],
)

java_library(
    name = "lang",
    srcs = glob(
        [
            SRC + "exceptions/*.java",
            SRC + "lang/*.java",
        ],
        exclude = REPL,
    ),
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

# TODO(davido): Fix that mess when this major Bazel bug is fixed:
# https://github.com/bazelbuild/bazel/issues/374
# That why I left the original :builtin rule from Buck, so that
# you can feel my pain, to emulate the glob with zip, to hide
# the files that contain '$' from Bazel.
genrule(
    name = "builtin_srcjar",
    outs = ["builtin.srcjar"],
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cd java",
        "zip -q $$ROOT/$@ com/googlecode/prolog_cafe/builtin/*.java",
        "zip -qd $$ROOT/$@ com/googlecode/prolog_cafe/builtin/PRED_\$$write_toString_2.java %s" % " ".join([s[5:] for s in IO]),
    ]),
    local = 1,
)

java_library(
    name = "builtin",
    srcs = [
        ":builtin_srcjar",
        ":builtin_srcs",
        ":system_srcs",
    ],
    deps = [":lang"],
)

pl2j(
    name = "builtin_srcs",
    src = "src/builtin/builtins.pl",
    out = "builtins.srcjar",
)

pl2j(
    name = "system_srcs",
    src = "src/builtin/system.pl",
    out = "system.srcjar",
)

java_library(
    name = "io",
    srcs = IO + [":io_srcs"],
    deps = [
        ":builtin",
        ":lang",
    ],
)

pl2j(
    name = "io_srcs",
    src = "src/builtin/io.pl",
    out = "io.srcjar",
)

java_library(
    name = "compiler",
    srcs = glob([SRC + "compiler/**/*.java"]) + [
        ":pl2am_srcs",
        ":am2j_srcs",
    ],
    deps = [
        ":builtin",
        ":io",
        ":lang",
    ],
)

pl2j(
    name = "pl2am_srcs",
    src = "src/compiler/pl2am.pl",
    out = "pl2am.srcjar",
)

pl2j(
    name = "am2j_srcs",
    src = "src/compiler/am2j.pl",
    out = "am2j.srcjar",
)

java_binary(
    name = "plc",
    main_class = "com.googlecode.prolog_cafe.compiler.Compiler",
    runtime_deps = [":compiler"],
)

java_binary(
    name = "cafeteria",
    main_class = "com.googlecode.prolog_cafe.repl.PrologMain",
    runtime_deps = [":cafeteria_lib"],
)

# TODO(davido): Same as above
genrule(
    name = "cafeteria_lib_srcjar",
    outs = ["cafeteria_lib.srcjar"],
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cd java",
        "zip -q $$ROOT/$@ com/googlecode/prolog_cafe/repl/*.java com/googlecode/prolog_cafe/builtin/PRED_\$$write_toString_2.java",
    ]),
    local = 1,
)

java_library(
    name = "cafeteria_lib",
    srcs = [
        ":cafeteria_lib.srcjar",
        ":cafeteria_srcs",
    ],
    deps = [
        ":builtin",
        ":io",
        ":lang",
    ],
)

pl2j(
    name = "cafeteria_srcs",
    src = "src/builtin/cafeteria.pl",
    out = "cafeteria.srcjar",
)

# TODO(davido): Same as above
genrule(
    name = "runtime_src",
    srcs = [
        "src/builtin/builtins.pl",
        "src/builtin/system.pl",
    ],
    outs = ["runtime_sources.zip"],
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cp $(SRCS) $$TMP/",
        "cd $$TMP",
        "unzip -q $$ROOT/$(location :builtin_srcjar)",
        "zip -qr $$ROOT/$@ .",
    ]),
    tools = [":builtin_srcjar"],
)

java_library(
    name = "io_src",
    resources = IO + ["src/builtin/io.pl"],
)

java_library(
    name = "compiler_src",
    resources = glob([SRC + "compiler/**/*.java"]) + [
        "src/compiler/pl2am.pl",
        "src/compiler/am2j.pl",
    ],
)

# TODO(davido): Same as above
genrule(
    name = "cafeteria_src",
    srcs = [
        "src/builtin/cafeteria.pl",
    ],
    outs = ["cafeteria_sources.zip"],
    cmd = " && ".join([
        "TMP=$$(mktemp -d || mktemp -d -t bazel-tmp)",
        "ROOT=$$PWD",
        "cp $(SRCS) $$TMP/",
        "cd $$TMP",
        "unzip -q $$ROOT/$(location :cafeteria_lib_srcjar)",
        "zip -qr $$ROOT/$@ .",
    ]),
    tools = [":cafeteria_lib_srcjar"],
)

maven_package(
    src = {
        "prolog-cafeteria": ":cafeteria_src",
        "prolog-compiler": ":compiler_src",
        "prolog-io": ":io_src",
        "prolog-runtime": ":runtime_src",
    },
    group = "com.googlecode.prolog-cafe",
    jar = {
        "prolog-cafeteria": ":cafeteria_lib",
        "prolog-compiler": ":compiler",
        "prolog-io": ":io",
        "prolog-runtime": ":runtime_deploy.jar",
    },
    repository = "gerrit-maven-repository",
    url = "gs://gerrit-maven/",
    version = VERSION,
)
