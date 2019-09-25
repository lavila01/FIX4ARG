 ********** FIX **************
-java 8 requerido en el sistema
-El .sql de la BD esta en la carpeta de /config
-la configuration que tiene el proyecto es mysql la base de datos se llama fix (y la tabla como esta en el sql)
-usuario de la bd root, password : root

-los datos:
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/fix?zeroDateTimeBehavior=convertToNull"/>
<property name="javax.persistence.jdbc.user" value="root"/>
<property name="javax.persistence.jdbc.password" value="root"/>
-De querer cambiarlo hay que compilar.

-en la configuración archivo: /config/app.cfg se debe configurar el usuario y el pass si necesidad de compilar puesto que esta fuera de la app.
-Ejemplo: (que es como esta ahora) lo que esta comentado dejarlo como esta porque se rompe, 
excepto el proxy que si se puede configurar de necesitarlo.

#ROFEX
[SESSION]
SocketConnectHost=remarket.cloud.primary.com.ar
SocketConnectPort=9876
SenderCompID=marvelalvarez892294
Password=gxwyoM4%

-eso seria todo
********* NOTAS ***************
La vista no esta terminada, porque la intención es continuar con las ejecuciones.

******* FAQ ********
si no levanta con doble click probar desde la consola del sistema con el comando:
->java -jar nombre_de_la_aplicacion.jar 
ahi te va a mostrar si tiene errores o que pasa.