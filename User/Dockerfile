# Usamos OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado en la carpeta target
COPY target/*.jar app.jar

# Exponemos el puerto del servicio
EXPOSE 8081

# Comando para ejecutar el servicio
CMD ["java", "-jar", "app.jar"]
