workspace(name = "prolog_cafe")

load("//:bazlets.bzl", "load_bazlets")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

load_bazlets(
    commit = "f30a992da9fc855dce819875afb59f9dd6f860cd",
    #    local_path = "/home/<user>/projects/bazlets"
)

http_archive(
    name = "bazel_skylib",
    sha256 = "2ea8a5ed2b448baf4a6855d3ce049c4c452a6470b1efd1504fdb7c1c134d220a",
    strip_prefix = "bazel-skylib-0.8.0",
    urls = ["https://github.com/bazelbuild/bazel-skylib/archive/0.8.0.tar.gz"],
)

# Check Bazel version when invoked by Bazel directly
load("//tools/bzl:bazelisk_version.bzl", "bazelisk_version")

bazelisk_version(name = "bazelisk_version")

load("@bazelisk_version//:check.bzl", "check_bazel_version")

check_bazel_version()
