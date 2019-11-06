# TODOOSE

**A to-do application for [OOSE](https://www.jhu-oose.com)**

[Live Version](https://todoose.herokuapp.com)

[Code Base](https://github.com/jhu-oose/todoose)

[Video on Getting Started](https://archive.org/download/todoose/todoose--getting-started.mp4)

[Videos on Building TODOOSE from Scratch](https://www.jhu-oose.com/todoose)

[![Build Status](https://travis-ci.com/jhu-oose/todoose.svg?branch=master)](https://travis-ci.com/jhu-oose/todoose)

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

## Architecture

TODOOSE is a web application. It’s divided in two components, the [server](#server), and the [client](#client). It uses the tools in the [Toolbox](https://www.jhu-oose.com/toolbox/).

## Server

[`Server`](/src/main/java/com/jhuoose/todoose/Server.java): The entry point of the Server, including the `main` method that is called when the Server starts. The `Server` class uses [Javalin](https://www.jhu-oose.com/toolbox/#web-server-javalin) to start a web server, sets up routing for incoming HTTP requests, connects to the [SQLite database](https://www.jhu-oose.com/toolbox/#database-management-systemdbms-sqlite), and so forth.

[`Item`](/src/main/java/com/jhuoose/todoose/models/Item.java): The class containing the logic of the application—something called a **model**. TODOOSE is an artificially simple application for pedagogical purposes, so this model doesn’t include much logic (it only holds data), and it’s the only model in the application. A more realistic to-do application would probably include the notion of, for example, when items are due, who’s assigned to each user, and so forth, and there would be multiple models responsible for determining who has overdue items, how a project is progressing over time, and so forth. If the application were a game, for example, the models are where the game rules would be.

[`ItemsRepository`](/src/main/java/com/jhuoose/todoose/repositories/ItemsRepository.java): A **repository** is a bridge between models (see above) and [the database](https://www.jhu-oose.com/toolbox/#database-management-systemdbms-sqlite): the repository saves Items to the database, finds Items in the database, removes Items from the database, and so forth. Repositories are where you can find SQL in the application. This is the only repository in the application because TODOOSE only has one model, but typical applications include more repositories. Do not confuse the term **repository** used here with the term **repository** used by Git—they are completely different things.

[`ItemsController`](/src/main/java/com/jhuoose/todoose/controllers/ItemsController.java): A controller handles the HTTP requests as they come in, orchestrate the work of models and repositories (see above), and determine what the HTTP response should be.

## Client

The code for the Client runs directly on the user’s browser, and it is sent there by the [Server](#server). When the user visits <https://todoose.herokuapp.com>, for example, the Server knows that by convention the user wants the file `index.html`, so it sends that file to the user’s browser. This is different from what happens when the browser requests <https://todoose.herokuapp.com/items>, for example, which is one of the API endpoints (see below) with functionality for TODOOSE. In this case, the Server has to use the router, controllers, models, repositories, and so forth, to produce a [JSON response](https://www.jhu-oose.com/toolbox/#data-interchange-format-javascript-object-notationjson).

[`index.html`](/src/main/resources/public/index.html): The entry point of the Client, which sets up the stylesheets and the JavaScript (see below).

[`styles.css`](/src/main/resources/public/stylesheets/styles.css): The stylesheets for the application. The styles for the whole application live in one file because they’re very simple, but in a more sophisticated application it may be necessary to break them apart in multiple files, for example, one file per collection of components (see below).

[`client.js`](/src/main/resources/public/javascripts/client.js): The entry point for the JavaScript portion of the Client, which mounts the [React](https://www.jhu-oose.com/toolbox/#user-interface-builder-react) component.

[`ItemsComponents.js`](/src/main/resources/public/javascripts/components/ItemsComponents.js): A collection of React components for Items.

[`vendor/`](/src/main/resources/public/javascripts/vendor): A directory containing the libraries we’re using on the Client: React and [Babel](https://babeljs.io) (to add support for JSX).

## API Documentation

[**Postman Collection**](/docs/TODOOSE.postman_collection.json): The [Server](#server) and the [Client](#client) communicate through an API. The [Postman](https://www.jhu-oose.com/toolbox/#application-programming-interfaceapi-development-environmentade-postman) Collection documents what are the endpoints that the Server must provide and the Client may consume. It includes examples of the API in use as well as tests (see below).

## Tests

[`ItemTests`](/src/test/java/com/jhuoose/todoose/models/ItemTests.java): Automated tests for the `Item` model (see above). This is something called **Unit Tests**, because it tests an **unit** of code (in this case, a **model**). These tests are artificially simple because the models in TODOOSE are artificially simple. In a more realistic application these tests would exercise the logic in the models, for example, the rules of a game.

[**Postman Tests**](/docs/TODOOSE.postman_collection.json): Automated tests for the Server as a whole—they’re sometimes called **System Tests**. They simulate what a browser would do, communicating with the Server through HTTP. To run these tests, you must start the server and import the Postman Collection.

## Auxiliary Files

[`.gitignore`](/.gitignore): Configuration for [Git](https://www.jhu-oose.com/toolbox/#version-control-systemvcs-git), specifying which files must **not** be versioned (tracked), because they belong not the project, but the developer’s machine, for example, the `.DS_Store` files generated by Finder in macOS.

[`.travis.yml`](/.travis.yml): Configuration for [Travis CI](https://www.jhu-oose.com/toolbox/#continuous-integrationci-server-travisci), specifying how to run the tests for TODOOSE.

[`app.json`](/app.json): Configuration for the **Deploy to Heroku** button above.

[`build.gradle`](/build.gradle): Configuration for [Gradle](https://www.jhu-oose.com/toolbox/#build-system-gradle), which lists the libraries in which TODOOSE depends, specifies that `Server` (see above) is the class that must run when the Server starts, specifies how to run unit tests (see above), and so forth.

[`CODE_OF_CONDUCT.md`](/CODE_OF_CONDUCT.md): The [Code of Conduct](https://www.contributor-covenant.org/version/1/4/code-of-conduct) for TODOOSE contributors.

[`gradlew`](/gradlew) and [`gradlew.bat`](/gradlew.bat): Wrappers for Gradle for macOS/Linux and Windows, respectively. These wrappers install and run Gradle, simplifying the setup process.

[`LICENSE`](/LICENSE): The [License](https://choosealicense.com/licenses/mit/) for TODOOSE.

[`Procfile`](/Procfile): Configuration for [Heroku](https://www.jhu-oose.com/toolbox/#platform-heroku), specifying how to start the application.

[`README.md`](/README.md): This documentation.

[`settings.gradle`](/settings.gradle): Gradle configuration generated by [IntelliJ](https://www.jhu-oose.com/toolbox/#integrated-development-environmentide-intellijidea).

[`system.properties`](/system.properties): Configuration for Heroku, specifying which version of Java to run.
