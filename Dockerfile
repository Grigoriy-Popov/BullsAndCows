#Для jar
FROM amazoncorretto:11
COPY target/*.jar bullsAndCows.jar
ENTRYPOINT ["java","-jar","/bullsAndCows.jar"]

#Для war
#FROM tomcat:8.5.32
#COPY target/*.war /usr/local/tomcat/webapps
#Для Tomcat9+
#COPY target/*.war /usr/local/tomcat/webapps-javaee
