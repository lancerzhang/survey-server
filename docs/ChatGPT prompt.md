# prompts

1. I want to develop a survey tool using spring boot 2, the project name is "survey-server", it use java 1.8, lombok, and H2 as testing database, give me pom.xml, use the latest version for pom dependency.
2. Please give me database schema.
3. Please update surveys table, add more fields, 1. if this survey allow anonymous response. 2. if this survey allow re-submit after user responding. 3. if it's deleted. 4. last modify time. 5. when user can respond to this survey. 6. when user cannot respond to this survey. 7. when collect given number of response, the survey will be close.
4. I want to add lottery function for the survey, so that participant can draw price after respond to the survey, please add related table schema.
5. please change all table schema to plantuml entity diagram.
6. Please give me java data entity, ignore annotations except lombok getter & setter, jpa entity, id and GeneratedValue.
7. Please give me all repository file use by spring data jpa. 
8. Please give me all service java files without any implementation method.
9. Please give me all controller java files without any method.
10. Please provide request and response bean for controllers, using lombok getter & setter.
11. Implement SurveyService and SurveyController with create,read and update one survey methods, and query surveys by user order by createdAt with pagination.
12. Give me postman collection json file, so that I can test above controller endpoints
13. Implement UserService and UserController with create, read and update methods.
14. Give me postman collection json file content for UserController.


# Notes
1. Please note it may generate different answer each time, such as spring boot version may be different. The field name maybe different as well, e.g. "max_response" and "response_limit", you may try to generate the result few times and use better one.
2. You may try to limit the response words, otherwise the result may be break. You can achieve this by reducing the requirement in one question.
3. The result may be not correct, please verify the result.

# Tips
* You can copy the code from ChatGPT, and the select a package folder, then directly paste it, IDEA will create a file for it.
