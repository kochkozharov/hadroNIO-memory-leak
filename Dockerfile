FROM gcc:12 AS build-ucx
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
RUN git clone --depth 1 -b v1.17.0 --single-branch --depth 1 https://github.com/openucx/ucx.git && \
    cd ucx && \
    ./autogen.sh && \
    ./contrib/configure-release --prefix=/usr/app/ucx-bin && \
    make -j8 && \
    make install

FROM eclipse-temurin:11-jdk AS build-hadronio
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
RUN apt-get update && \
    apt-get install -y git libnuma-dev
ARG HADRONIO_URL
RUN git clone "${HADRONIO_URL:-https://github.com/hhu-bsinfo/hadroNIO.git}"
RUN cd hadroNIO && \
    git checkout bugfix/socket-channel-issue && \
    ./gradlew shadowJar
COPY --from=build-ucx /usr/app/ucx-bin /opt/ucx
COPY ./build/libs/hadroNIO-memory-leak-1.0-SNAPSHOT-all.jar $HOME
ENV LD_LIBRARY_PATH=/opt/ucx/lib
ENV PATH=$PATH:/opt/ucx/bin
ENV UCX_TLS=tcp
