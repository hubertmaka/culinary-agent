FROM ubuntu:latest
LABEL authors="hubertmaka"

ENTRYPOINT ["top", "-b"]