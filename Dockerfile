FROM openjdk:11

ENV SPRING_HOME /usr/local/mywebapp
WORKDIR $SPRING_HOME

ADD target $SPRING_HOME
COPY src/main/scripts/docker_start.sh .

RUN chmod +x ./docker_start.sh
CMD [ "./docker_start.sh" ]
