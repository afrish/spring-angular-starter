### Spring Angular Starter Application

The aim of this project is to provide an easy to use starter project for building web applications with Spring Boot
and Angular.

The project is based on the following technologies:
- **Spring Boot** - for building the backend part of the application and for serving the frontend resources
- **Angular** - for building the frontend part of the application
- **Docker** - for building and running the application in containers
- **Docker Compose** - for building and running the application in dev mode
- **Cypress** - for running E2E, component and unit tests
- **Maven** - for building the application
- **Yarn** - for frontend dependency management

The goals of the project are:
- Allow starting a new project quickly
- Provide an opinionated way of building web applications
- Build, deploy, run and test everything together in one easy to use bundle
- Make the project fully containerized and ready to be deployed to the cloud
- Make it easy to build and run the application in dev mode
- Use simple and predictable folder structure
- Be "batteries included" to start writing business code right away, without spending time on configuring the project
  and finding the right tools and libraries
- Be as lean as possible, without any unnecessary code and dependencies
- Provide a fully working sample on how to integrate different technologies together
- Use the most recent versions of the technologies
- Allow testing the application locally using the same Docker image that will be used on production
- Allow writing tests for all the layers of the application from all possible perspectives

### Project structure

The folder stucture of the project follows Maven and Spring Boot conventions. The frontend part is built with Angular
and is specifically adapted to match the Maven folder structure too:
- **src**
  - **main**
    - **java** (Spring Boot backend sources)
    - **resources** (backend resources, properties, templates, etc)
    - **web** (Angular frontend sources) 
  - **test**
    - **java** (JUnit 5 / Spring Boot backend tests)
    - **resources** (backend test resources and properties)
    - **web** (Cypress frontend E2E, component and unit tests)

### Configuring the application

The application is configured using environment variables. It is supposed that the application will be run in Docker
containers. Application settings are loaded by Spring Boot from the environment variables. The environment variables are
passed from the outside to the Docker container depending on the mode the application is run in. See more details on how
to run the application in the next section.
- **Dev mode with Docker Compose:**  
  In the dev mode environment variables are loaded from the .env file located in the root folder. The .env file is not
  committed to the repository. You can use the .env.sample file as a template to create your own .env file. The .env
  file is loaded by the Docker Compose configuration.
- **Local testing mode:**  
  You can test production Docker image locally using `sh/start`. The script will load the .env file and pass environment
  variables to the Docker container.
- **Prod mode:**  
  In the prod mode environment variables are passed to the Docker container from the ecosystem where the container
  is run. E.g. in AWS ECS the environment variables are passed to the container from the task definition.

### Buidling and running the application

In general, the only hard requirement to build and run the application is Docker. The application is fully containerized
and can be built and run using Docker only, and to build and run it in dev mode you will need Docker Compose. However,
to make the development process more convenient, some additional tools may be used, see below.

The application is fully built using Maven. The frontend part is built using the frontend-maven-plugin. The frontend
sources are located in the `src/main/web` folder. The plugin will automatically install Node.js and Yarn, and run Yarn
to install all the dependencies. It will also run the Angular CLI to build the frontend. All the resulting files will be
located in the `target/classes/static` folder. The Spring Boot application will then serve the frontend from this
folder. The index.html file is located in the `src/main/web` folder and goes through 2 stages of processing:
- During the Angular build the index.html file is processed by the Angular CLI and the resulting file is located in the
  `target/classes/static` folder
- In the runtime the index.html file is processed by the Thymeleaf template engine and the resulting file is served by
  the Spring Boot application

There are multiple ways to build and run the application:
- **Using Docker Compose to quickly build and run the application in dev mode:**
  ```
  sh/serve
  ```
  In this case the application will be built and run using the Docker Compose configuration. The application will be
  available at `http://localhost:{DEV_HTTP_PORT}`. The application will be run in dev mode, which means that the
  frontend and the backend will be served using the Spring Boot devtools. It will create a Docker image for the
  application and run it using Docker Compose.\
  Prerequisites:
  - Bash
  - Docker
  - Docker Compose
  - Application settings in the .env file (see the .env.sample file for the example)
  - (Optional) Node.js, Yarn and Angular CLI installed on the host system to run the frontend in dev mode
    Run the following command to run the frontend in dev mode:
    ```
    yarn run watch
    ```
  After completing the steps above, every time you do a change in the frontend code, you will be able to refresh the
  application in the browser and see the changes. The application will be automatically restarted when the backend
  sources are changed. The container created by the Docker Compose runs under the current user. This means that the
  files created by the container will be owned by the current user. If you don't run the frontend in dev mode, you
  will still be able to see the backend changes.
- **Using the scripts in the `sh` folder to build and run the application in prod mode:**
  ```
  sh/start <version> # e.g. sh/start 1.0.0
  ```
  This will create a new Docker image with version equal to the version specified in the command and run it. The
  application will be available at http://localhost:8080. The application will be run in prod mode, which means that
  the frontend and the backend will be served using the Spring Boot embedded Tomcat. The application will not be
  automatically restarted when the sources are changed. This can be used to check if the application works correctly
  on production, because it should be 100% indentical to the application running on production, except the environment
  variables. You can use the image created by the `sh/build` and `sh/start` scripts to run the application on
  production. The container created by the `sh/start` script runs under the root user. This means that the files
  created by the container will be owned by the root user, but it should not be a problem when running locally, because
  while running in this mode there will be no folders mapped to the host system.\
  Prerequisites:
  - Bash
  - Docker
  - Application settings in the .env file (see the .env.sample file for the example)
- **Using the scripts in the `sh` folder to build the application in prod mode:**
  ```
  sh/build <version> # e.g. sh/build 1.0.0
  ```
  This will only create a new Docker image with version equal to the version specified in the command. The image will
  be tagged as `vk-share:<version>`.\
  Prerequisites:
  - Bash
  - Docker
- **Using Maven wrapper on the host system to build and run the application in dev mode:**
  ```
  ./mvnw spring-boot:run
  ```
  and then run the frontend in dev mode:
  ```
  yarn run watch
  ```
  Prerequisites:
  - Bash
  - JDK 17 
  - (Optional) Node.js, Yarn and Angular CLI installed on the host system to run the frontend in dev mode
- **Using Maven wrapper on the host system to build and run the application in prod mode:**
  ```
  ./mvnw clean package
  ```
  and then run the application:
  ```
  java -jar target/vk-share.jar
  ```
  Prerequisites:
  - Bash
  - JDK 17

### Debugging the application

By default in both dev and prod modes, in addition to the port 8080, the application listens also on the port 5005 for
the debugger. This means that you can attach the debugger to the application on this port. In IntelliJ IDEA you can
create a new "Remote JVM Debug" configuration and attach it to the application. Make sure not to expose this port to the
public on production via front proxy servers like nginx, Apache or Amazon ELB. However, you should still be able to
connect to this port from machines that have direct access to the server.

### Running the tests

The application has both backend and frontend tests.
- **To run all existing tests at once**, use the following command:
  ```
  sh/e2e
  ```
  It will run all the existing tests: backend, frontend unit, frontend component and frontend end-to-end. For the
  frontend end-to-end tests it will check the application running at the URL specified in the CYPRESS_BASE_URL. If
  the app is not available at this URL, it will try to run the application in dev mode using Docker Compose.\
  Prerequisites:
  - Bash
  - Node.js
  - Cypress
  - Yarn
  - CYPRESS_BASE_URL environment variable (see the .env.sample file for the example)
  - (Optional) Docker
  - (Optional) Docker Compose
  - (Optional) Application settings in the .env file (see the .env.sample file for the example)
- **Backend tests** are located in the `src/test/java` folder. The tests are based on JUnit 5, Mockito and Spring Boot
  Test libraries. You can run them using the following command:
  ```
  sh/test
  ```
  Prerequisites:
  - Bash
  - Docker
- **Frontend tests** are located in the `src/test/web folde`r. In general, there are three distinct types of frontend
  tests in this project: unit, component and end-to-end.
  - **Unit tests** - located in the `src/test/web/component` folder. These tests are based on Mocha and Chai libraries
    that are bundled with Cypress. From the point of view of Cypress, the unit tests are also component tests, because
    they depend on Angular and Webpack, use the same settings as the component tests and do not require the application
    to be running.
  - **Component tests** - located in the `src/test/web/component` folder. These tests are based on Cypress. To run
    both component and unit tests, use the following command:
    ```
    yarn run test
    ```
  - **End-to-end tests** - located in the `src/test/web/e2e folder`. These tests are based on Cypress. They are located
    in a separate folder, because they are run in a different way and do not require compilation of the frontend
    sources. Before running the end-to-end tests, make sure that the application is running on the address specified
    in the CYPRESS_BASE_URL env variable (see .env.sample for the example), e.g. you can run the application using
    the `sh/serve` script and then run the tests using the following command:
    ```
    yarn run e2e
    ```
    This will run the tests in the headless mode.
  - **Interactive mode** - choosing, running and debugging any of the tests in the Cypress GUI. To run the tests in the
    interactive mode, use the following command:
    ```
    yarn run cypress
    ```
    This will open the Cypress window, where you can select the tests to run and see the results. This is useful not
    only for debugging the tests, but it is also much faster than running the tests in the headless mode. However, the
    tests should be run in the headless mode on the CI server anyway.

### Analyzing the frontend bundle size

You can analyze the frontend bundle size using the following command:
```
yarn run analyze
```
This will run the production build of the frontend with source maps enabled and will open the bundle analyzer in the
browser. You can use it to analyze the bundle size and find out what is causing the bundle to be so big.
