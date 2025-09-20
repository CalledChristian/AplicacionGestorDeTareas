# Stage 1: Build the application
FROM eclipse-temurin:23-jdk AS builder

# Set the working directory
WORKDIR /app

# Copy the application code
COPY . .

# Given permissions to mvnw
RUN chmod +x mvnw

# Build the application (requires Maven or Gradle)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:23-jre

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar gestorTareas.jar

# Expose the port the app will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "gestorTareas.jar"]

#1) comience indicando la imagen base (de docker hub), utilizando la sentencia FROM.
#En este caso la imagen es "openjdk:17.0.2-jdk" por nuestra version openjdk 17 --- java 17
#FROM openjdk:17.0.2-jdk
#2) Luego, cada vez que apague y vuelva a prender un contenedor,
#los archivos creados son borrados y no tienen persistencia.
#Se puede definir un directorio que no se borre aún cuando apague y prenda el contenedor.
#Esto Se realiza con la sentencia VOLUME.
#VOLUME /tmp

#3) A continuación, se debe indicar qué puerto estará expuesto en el contenedor con la sentencia EXPOSE.
#En este caso, la aplicación funciona en el 8080
#EXPOSE 8080

#4) Luego, debemos agregar nuestro archivo .jar del proyecto al directorio local del contenedor.
#Esto se logra con la sentencia ADD <src> <dst>.
#ADD ./target/Aplicacion2_GestorDeTareas-0.0.1-SNAPSHOT.jar gestorTareas.jar
#COPY --from=builder /app/target/*.jar gestorTareas.jar
#Aquí estamos cambiando el nombre del archivo a gestortareas.jar


#5) Finalmente, debemos indicar a manera de arreglo los comandos usados para correr la aplicación,
#usando la sentencia ENTRYPOINT.
#Si para correr la aplicación se usaba: java -jar app.jar
#→ Entonces en forma de arreglo ["java","-jar","app.jar"]
#ENTRYPOINT ["java","-jar","gestorTareas.jar"]

