FROM openjdk:11-jre
ARG TARGETOS
RUN echo "I'm building for $TARGETOS "

COPY assembly/target/assembly /opt/karaf

RUN if [ "x$TARGETOS" = "xwindows" ] ; then setx path "%path%;C:\\opt\\karaf\\bin\\" ; else export PATH=$PATH:/opt/karaf/bin; fi

EXPOSE 8101 1099 44444 8181
CMD ["karaf", "karaf.bat"]