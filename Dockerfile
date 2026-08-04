FROM ubuntu:latest
LABEL authors="henrique"

ENTRYPOINT ["top", "-b"]