FROM gcc:12 AS build-ucx
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
RUN git clone --depth 1 -b v1.13.0 --single-branch --depth 1 https://github.com/openucx/ucx.git && \
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
COPY . .
RUN git clone https://github.com/hhu-bsinfo/hadroNIO.git submodules/hadroNIO
RUN cd submodules/hadroNIO && \
    ./gradlew shadowJar && \
    ./gradlew installDist
COPY --from=build-ucx /usr/app/ucx-bin /opt/ucx
ENV LD_LIBRARY_PATH=/opt/ucx/lib
ENV PATH=$PATH:/opt/ucx/bin