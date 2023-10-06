# mc_website_backend

## Project Overview
Done in the summer to practice spring boot microservices, this project was a simulation of a website of a comic book store. 

## Table of Contents
- [Installation](#installation)
- [Set Up](#set-up)
- [Usage](#usage)
- [Acknowledgments](#acknowledgments)

<a name="installation"></a>
## Installation 
1. Clone the repository
2. use docker-compose up --build
3. Create an account
  POST http://localhost:8080/api/v1/users
  With the body :
```json
{
"firstName":"name",
"lastName":"lname",
"email":"email@email.com",
"phoneNumber":"222-222-2222",
"password":"pwd"
}
```
5. Login with the new account
  POST http://localhost:8080/api/v1/users/login
  With the body :
```json
{
"email":"email@email.com",
"password":"pwd"
}
```

<a name="set-up"></a>
### Set Up
No set up required other than having docker destop, git bash and docker-compose.

#### Platform used
This project uses Spring boot, which can be edited anywhere, but Intellij is probably one of the best IDE for this. I also use docker-compose with this yml file :
```yml
services:
  api-gateway:
    build: api-gateway
    hostname: api-gateway
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - users-service
      - orders-service

  users-service:
    # to find the Dockerfile
    build: users-service
    hostname: users-service
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      mysql1:
        condition: service_healthy
  orders-service:
    # to find the Dockerfile
    build: orders-service
    hostname: orders-service
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - mongodb
  mysql1:
    image: mysql:5.7
    container_name: MySql1
    ports:
      - "3307:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=rootpwd
      - MYSQL_DATABASE=users-db
      - MYSQL_USER=user
      - MYSQL_PASSWORD=pwd
    # if we want to save data
    volumes:
      - ./data/mysql1:/var/lib/mysql1
      - ./data/init.d:/docker-entrypoint-initdb.d
      - db_data:/var/lib/mysql

    # sending commands to mysql container to see if it works
    healthcheck:
      test:
        [
            "CMD",
            "mysqladmin",
            "ping",
            "-uuser",
            "-ppwd",
            "-h",
            "localhost",
        ]
      interval: 10s
      timeout: 5s
      retries: 10
  phpmyadmin:
    image: phpmyadmin:5.2.0
    container_name: PhpMyAdmin
    links:
      - mysql1
    restart: always
    ports:
      - 5013:80
    environment:
      - PMA_ARBITRARY=1

  mongodb:
    image: mongo
    container_name: mongo_db
    ports:
      - 27017:27017
    volumes:
      - mongo:/data
      - mongodata:/data/db
    environment:
      - MONGO_INITDB_ROOT_USERNAME=user
      - MONGO_INITDB_ROOT_PASSWORD=pwd
  mongo-express:
    image: mongo-express
    container_name: mongo-express
    restart: always
    ports:
      - 8081:8081
    environment:
      - ME_CONFIG_MONGODB_ADMINUSERNAME=user
      - ME_CONFIG_MONGODB_ADMINPASSWORD=pwd
      - ME_CONFIG_MONGODB_SERVER=mongodb
volumes:
  mongo: {}
  mongodata:
  db_data:
```


<a name="usage"></a>
## Usage
This is an API, and you can store and retrive data, so this is a complete list of the endpoints we have :
### Order related
/api/v1/users/orders
/api/v1/users/orders/{orderId}
/api/v1/users/orders/{orderId}
/api/v1/users/orders/{orderId}
/api/v1/users/{userId}/orders
/api/v1/users/{userId}/orders
### Users related
/api/v1/users/api/v1/users
/api/v1/users/email
/api/v1/users/forgot_password
/api/v1/users/forgot_password
/api/v1/users/login
/api/v1/users/reset_password
/api/v1/users/reset_password
/api/v1/users/{userId}
/api/v1/users/{userId}
/api/v1/users/{userId}



<a name="difficulties"></a>
## Difficulties
I would say that my biggest challenge was the configuration of Spring security.
## Authentication Manager Bean

```java
@Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(encoder());
        return new ProviderManager(authProvider);
    }

```

#Official "Each `AuthenticationProvider` performs a specific type of authentication. For example, `[DaoAuthenticationProvider](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html#servlet-authentication-daoauthenticationprovider)` supports username/password-based authentication, while `JwtAuthenticationProvider` supports authenticating a JWT token."

We, in this case, use `DaoAuthenticationProvider`. This bean is for login in a user, for example in my controller I would do or better yet, call my service to do something like this :

```java
//login is the request body of the post to /login
Authentication authenticate = authenticationManager
        .authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(), login.getPassword()
                )
        );

UserPrincipalImpl user = (UserPrincipalImpl) authenticate.getPrincipal();

```

This will confirm that the user :
1-exists
2-has the correct credentials

Then after you confirm, you can return the token different ways, here is one way with http cookie:

```java
ResponseCookie token = ResponseCookie.from("Bearer", jwtTokenUtil.generateToken(user))
        .httpOnly(true)
        .secure(true)
        .maxAge(Duration.ofHours(1))
        .sameSite("Lax").build();

response.setHeader(HttpHeaders.SET_COOKIE, token.toString());
return ResponseEntity.ok()
        .body(removePwdMapper.responseModelToResponseModelLessPassword(userService.getUserByEmail(user.getUsername())));
/*

//in another class called JwtTokenUtil

*/
public String generateToken(UserPrincipalImpl userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    claims.put(CLAIM_KEY_CREATED, new Date());

    return generateToken(claims);
}

String generateToken(Map<String, Object> claims) {
    Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

    return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, SecurityConst.SECRET )
            .compact();
}

```

This code creates a cookie, with `httpOnly` and `secure` set to true, this is IMPORTANT.
Then simply returns it in the response.
The JWT (JSON web token) is generated with the `JWTokenUtil` class, I won't go in details since there is documentation online and this step is mostly done in pet clinic.

## Security Filter Chain Bean

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers(HttpMethod.POST,"/api/v1/users/login").permitAll()
                    .requestMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
                    .requestMatchers("/api/v1/users/forgot_password").permitAll()
                    .requestMatchers("/api/v1/users/reset_password").permitAll()
                    .requestMatchers("/api/v1/users/**").authenticated()
                    .anyRequest().denyAll())
            .logout(logout -> logout
                    .logoutUrl("/api/v1/users/logout")
                    .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    })
                    .addLogoutHandler((request, response, auth) -> {
                        for (Cookie cookie : request.getCookies()) {
                                String cookieName = cookie.getName();
                                Cookie cookieToDelete = new Cookie(cookieName, null);
                                cookieToDelete.setMaxAge(0);
                                response.addCookie(cookieToDelete);
                        }
                    }))
            .httpBasic()
            .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
            .and()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource());
    http.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter.class
    );

    return http.build();
}

```

### Authorize Http Requests

```java
authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers(HttpMethod.POST,"/api/v1/users/login").permitAll()
                    .requestMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
                    .requestMatchers("/api/v1/users/forgot_password").permitAll()
                    .requestMatchers("/api/v1/users/reset_password").permitAll()
                    .requestMatchers("/api/v1/users/**").authenticated()
                    .anyRequest().denyAll())

```

#Official "Allows restricting access based upon the `HttpServletRequest` using `RequestMatcher` implementations (i.e. via URL patterns)."

In other words, this will look at the request and if the URI matched from the one specifies it follows the procedure you tell it to.

For example :

```java
 .requestMatchers(HttpMethod.POST,"/api/v1/users/login").permitAll()

```

This looks if the method is a post and the URI is `/api/v1/users/login`, if it is it allows everyone to access it. This means users without tokens can access it.

```java
.requestMatchers("/api/v1/users/forgot_password").permitAll()

```

This just looks for if the URI matches it.

A more complex example :

```java
.requestMatchers("/api/v1/users/**").authenticated()

```

This matches the URI like before BUT `/**` means that it also matches whatever comes after.

#StackOverflow
Example :

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
    // ...
    .antMatchers(HttpMethod.GET, "/**").permitAll
    .antMatchers(HttpMethod.POST, "/*").permitAll
    // ...
 }

```

"In this configuration any "**Get**" request will be permitted, for example:

- /book
- /book/20
- /book/20/author

So, all this urls match text with pattern "/**".

Permitted urls for "**Post**":

- /book
- /magazine

Urls above match with "/*""

### Logout

```java
.logout(logout -> logout
                    logout -> logout
                    .logoutUrl("/api/v1/users/logout")
                    .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    })
                    .addLogoutHandler((request, response, auth) -> {
                        for (Cookie cookie : request.getCookies()) {
                                String cookieName = cookie.getName();
                                Cookie cookieToDelete = new Cookie(cookieName, null);
                                cookieToDelete.setMaxAge(0);
                                response.addCookie(cookieToDelete);
                        }
                    }))

```

Lets break it down :

### Logout URL

```java
.logoutUrl("/api/v1/users/logout")

```

This sets what URL to call to logout.

### Logout Success Handler

```java
.logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {                        httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
    })

```

This handles what happens when the logout is accomplished successfully, in this case it simply returns an empty response body with a Http status of 204.

### Add logout Handler

```java
.addLogoutHandler((request, response, auth) -> {
                        for (Cookie cookie : request.getCookies()) {
                                String cookieName = cookie.getName();
                                Cookie cookieToDelete = new Cookie(cookieName, null);
                                cookieToDelete.setMaxAge(0);
                                response.addCookie(cookieToDelete);
                        }
                    }))

```

This is where you do the logic to logout. In this case, since I was using cookies, I am deleting the cookie (technically expiring). This deletes ALL cookies, so the token and also the JSESSIONID which is important.

#Note After writing this, I think I found a better way :

```java
CookieClearingLogoutHandler cookies = new CookieClearingLogoutHandler("our-custom-cookie");
http
    .logout((logout) -> logout.addLogoutHandler(cookies))

```

Or even better (I think ?) :

```java
HeaderWriterLogoutHandler clearSiteData =
		new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter());
http
    .logout((logout) -> logout.addLogoutHandler(clearSiteData))

```

#Note I think we should also create a `CustomLogoutHandler` implementing `LogoutHandler` to make the code cleaner and follow regulations.

### Http basic

```java
.httpBasic()

```

#Official "Configures HTTP Basic authentication."

### Authentication Entry Point

```java
.authenticationEntryPoint(customBasicAuthenticationEntryPoint)

```

#Official "Used by `ExceptionTranslationFilter` to commence an authentication scheme."
#Official "The `AuthenticationEntryPoint` to be populated on `BasicAuthenticationFilter` in the event that authentication fails. The default to use `BasicAuthenticationEntryPoint` with the realm "Realm""

In other words, when an exception is thrown, it goes there.

For example :

```java

@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("YOUR REALM");
        super.afterPropertiesSet();
    }

```

### And

#Official "Return the `SecurityBuilder` when done using the `SecurityConfigurer`. This is useful for method chaining."

### CSRF disabled

```java
.csrf().disable()

```

This disables CSRF

### CSRF

#Google"Definition. Cross-Site Request Forgery (CSRF) is **an attack that forces authenticated users to submit a request to a Web application against which they are currently authenticated**."

### CORS

```java
.cors().configurationSource(corsConfigurationSource());

```

Your declare where it should look for the CORS configuration.

### CORS Definition

#Google "Cross-origin resource sharing (CORS) is **a mechanism for integrating applications**. CORS defines a way for client web applications that are loaded in one domain to interact with resources in a different domain."

### Add Filter Before

```java
  http.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter.class
    );

```

It takes two parameters, the first one is the custom filter the token will pass through. The second one is the filter that the custom one will be placed before.

In this case we want two parameters, a username and a password.

### Http Build

And that's it ! We build our config and return it so it can be used.

## CORS Configuration

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("<http://localhost:3000>");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedHeader("*");
    source.registerCorsConfiguration("/**", config);
    return source;
}

```

This configures CORS, I highly recommend you read up on what CORS is and why it matters.
All you need to know is that this line :

```java
    config.addAllowedOrigin("<http://localhost:3000>");
```

This is the URL of the frontend, since we want to receive requests from it.
Otherwise your requests will work with postman but not through a browser with AXIOS for example.

## User Details Service

```java
@Bean
public UserDetailsService userDetailsServiceBean() throws Exception {
    UserDetails user = User.withUsername("user")
            .password(encoder().encode("userPass"))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
    }

```

This adds a basic user.

## Password Encoder

```java
   @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
```

This sets what password encoder is used.

<a name="acknowledgments"></a>
## Acknowledgments 
I would like to thank all my teammates who contributed to this project: Karina, Denisa, and Mila.
