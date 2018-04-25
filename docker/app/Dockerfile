FROM maven:3-jdk-8-alpine
ARG USER_NAME
ARG DOCKER_USER_ID

# from https://hub.docker.com/r/enil/alpine-supervisord/
ENV PYTHON_VERSION=2.7.14-r2
ENV PY_PIP_VERSION=9.0.1-r1
ENV SUPERVISOR_VERSION=3.3.0

RUN apk update && apk add -u python=$PYTHON_VERSION py-pip=$PY_PIP_VERSION
RUN pip install supervisor==$SUPERVISOR_VERSION
RUN mkdir /var/log/supervisord/
RUN adduser -D -u ${DOCKER_USER_ID} ${USER_NAME}
RUN apk add openssh
RUN apk update && apk add ca-certificates wget && update-ca-certificates

ENTRYPOINT ["supervisord", "--nodaemon", "--configuration", "/etc/supervisord.conf"]
