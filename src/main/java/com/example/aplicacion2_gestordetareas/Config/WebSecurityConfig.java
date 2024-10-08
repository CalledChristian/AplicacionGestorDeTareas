package com.example.aplicacion2_gestordetareas.Config;


import com.example.aplicacion2_gestordetareas.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    final UsuarioRepository usuarioRepository;

    //Para indicarle a Spring security que utilice la base de datos
    // para autenticar a los usuarios
    final DataSource dataSource;
    /*Se debe hacer uso de DefaultSavedRequest, el cual nos indica
    si el usuario está viniendo de la página mediante el link de login
    o directamente de una URL protegida.*/
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public WebSecurityConfig(DataSource dataSource,UsuarioRepository usuarioRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository= usuarioRepository;
    }

    /*Debido a que los password están con hash bcrypt,
    debemos indicarle a Spring cómo comparará este campo con PasswordEncoder,
    anotado con @Bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager usuarios = new JdbcUserDetailsManager(dataSource);
        String sql1 = " SELECT correo, contrasenia, estado FROM usuarios WHERE correo = ? ";
        String sql2 = " SELECT u.correo, r.nombre FROM usuarios u " +
                "INNER JOIN rol r ON (u.idrol = r.idrol) " +
                "WHERE u.correo = ? and u.estado = 1";

        usuarios.setUsersByUsernameQuery(sql1);
        usuarios.setAuthoritiesByUsernameQuery(sql2);
        return usuarios;
    }




    /*Para proteger solo ciertas rutas en Spring Security se debe implementar
    el método SecurityFilterChain filterChain(HttpSecurity http) y anotado con @Bean,
    el cual crea un filtro que se aplica a todos los requests antes de llegar
    a los controladores.*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //formulario por defecto de spring security
        http.formLogin()
                //ruta donde será estará la ventana de login (getmapping)
                .loginPage("/login")
                //ruta donde se envía el formulario de login (postmapping)
                .loginProcessingUrl("/processLogin")
                //Si se desea usar otros nombres para el correo y contraseña del login
                .usernameParameter("correo")
                .passwordParameter("contrasenia")
                .successHandler((request, response, authentication) -> {

                    //Spring session
                    HttpSession session = request.getSession(); //autenthicacion.getName() devuelve el username que es correo en este caso
                    session.setAttribute("usuario",usuarioRepository.findByCorreo(authentication.getName()));

                    DefaultSavedRequest defaultSavedRequest =
                            (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                    if (defaultSavedRequest != null) {
                        String targetURL = defaultSavedRequest.getRedirectUrl();
                        redirectStrategy.sendRedirect(request, response, targetURL);
                    } else {
                        String rol = "";
                        for (GrantedAuthority role : authentication.getAuthorities()) {
                            rol = role.getAuthority();
                            break;
                        }
                        if (rol.equals("Super Usuario")) {
                            response.sendRedirect("/tareas");
                        } else if(rol.equals("Administrador")){
                                response.sendRedirect("/tareas");
                        } else {
                            response.sendRedirect("/tareas");
                        }
                    }
                });

        //cerrar sesión

       //Luego de cerrar sesión,
        //el sistema lo envía a la página de inicio de sesión (login).
        //http.logout();

        //También podemos redireccionarlo a cierta ruta que deseemos
        http.logout()
                .logoutSuccessUrl("/")
                /*Debido a que ahora se está usando Spring Session,
                se debe configurar que Spring Security invalide la sesión gestionada por Spring Session.*/
                .deleteCookies("JSESSIONID") //cookie asociada a Spring Session
                .invalidateHttpSession(true); //para que borre todos los objetos vinculados con la sesión.


        /*indica que los siguientes métodos serán sobre rutas a ser protegidas o excluidas.*/
        http.authorizeHttpRequests()
                //ruta(s) a ser protegidas según el rol
                //("/ruta") --> proteger solo esta ruta
                //("/ruta/*") --> proteger hasta un sub nivel de la ruta
                //("/ruta/**") --> proteger toda ruta con comienze con "/ruta"

                //En nuestro caso queremos que la ruta /tareas sea protegida , entonces
                .requestMatchers("/tareas","/tareas/**").hasAnyAuthority("Super Usuario", "Administrador","Usuario")
                //para cualquier otra solicitud permitir sin autenticar
                .anyRequest().permitAll();


        return http.build();
    }



}
