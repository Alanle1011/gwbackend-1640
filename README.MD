# Project Title
Greenwich's Hall of Contributions Backend Service

# Description
Greenwich's Hall of Contributions Backend Service is a robust backend service developed using Java, PostgreSQL, Spring Boot, and Maven. It offers a comprehensive suite of APIs for managing various aspects of a platform, including contributions, comments, and users. The project is designed with a focus on efficiency, scalability, and ease of use.

# Installation
## Prerequisites
- Java 8 or higher
- Maven
- PostgreSQL

## Steps
1. Clone the repository to your local machine using `git clone https://github.com/Alanle1011/gwbackend-1640`.
2. Navigate to the project directory with `cd gwbackend-1640`.
3. Update the `application.properties` file with your database credentials.
4. Build the project using `mvn clean install`.
5. Start the application with `java -jar target/gwbackend-1640-0.0.1-SNAPSHOT.jar`.

# Usage
Once the application is up and running, you can interact with the APIs using Postman. Here's an example of how to get all contributions:

1. Open Postman.
2. Click on the + button to create a new request tab.
3. From the dropdown menu next to the URL bar, select GET.
4. In the URL bar, enter the URL of the API endpoint to get all contributions. It might look something like this: http://localhost:8080/contributions.
5. Click on the Send button to send the request.
6. You should see a list of contributions in the response body.

# Features
- Contribution Management: Create, update, delete, and retrieve contributions.
- Comment Management: Create and retrieve comments.
- User Management: Validate user existence.
- Document Type Detection: Automatically detect and set the document type (PDF or DOCX).

# License
This project is licensed under the MIT License - see the LICENSE file for details.
