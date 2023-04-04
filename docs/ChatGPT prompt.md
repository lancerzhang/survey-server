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
10. Implement SurveyService and SurveyController with create,read and update one survey methods, and query surveys by user order by createdAt with pagination.
11. Give me postman collection json file, so that I can test above controller endpoints
12. Implement UserService and UserController with create, read and update methods.
13. Give me postman collection json file content for UserController.
14. Write a GitHub Readme file for this project, give me source of this Readme.
15. I want to add initial user and survey data to in memory db when start the application.
16. There are two types of user, one is anonymous user, another is not, please update data schema, data entity, request and response bean.
17. Add last_modified, public key field to user table, give me updated code.
18. User can set delegates, so that others can read his/her survey, create/update survey for him. Please update codes.
19. Implement code for adding question and option when creating or updating survey.
20. Implement survey response with create and update method.
21. Verify this db schema, see if there is any problem, ``paste your db schema here``
22. Convert above db schema to plantUml entity diagram
23. Provide jpa entity java base on above db schema, use lombok setter and getter.
24. Give me unit test code of above controller and service java.
25. Give me unit test of below service java, ``paste your java code here``
26. I want to use oauth login, such as GitHub login, to protect my API, please change the code, I'm using maven and application.yaml.
27. how can I validate the field type and length in this spring boot web request?
28. I want to generate API rest doc at runtime, so that I can browse the doc in browser.
29. Give me postman collection json file content for SurveyReplyController.
30. Update code, implement update one Template, Get All Templates sort by createdAt with pagination.
31. Give me postman collection of template controller, current controller is, ``paste your java code here``
32. Implement delete survey function. just update the is_deleted to true.
33. Implement get survey replies by user id, and sort by createdAt with pagination. Pls also update postman content for this.
34. User can download all replies of a survey as csv file, each row is a survey reply, each question reply is a column, for text question type, just fill the reply text,  for radio need to get option text of the selected option, for checkbox, combine all selected option text as a string,  update the code.
35. User can view summary of the replies for a survey, e.g. how many replies. For radio and checkbox question, how many people select each option, percentage of each option for a question.
36. User can download all replies of a survey as csv file, each row is a survey reply, so each question or reply is a column. For header row, need to get question text from survey table. Other rows, fill reply to the column, for text question type, just fill the reply text,  for radio need to get option text of the selected option, for checkbox, combine all selected option text as a string,  update the code. Below are data models:
public class Survey {
@Id
@GeneratedValue
private Integer id;
private User user;
private String title;
private String description;
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "survey_id")
private List<Question> questions;
}
public enum QuestionType {
TEXT,
RADIO,
CHECKBOX
}
public class Question {
@Id
@GeneratedValue
private Integer id;
private Survey survey;
private Integer seq;
private String questionType;
private String questionText;
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "question_id")
private List<Option> options;
}
public class Option {
@Id
@GeneratedValue
private Integer id;
private Integer seq;
private String optionText;
@ManyToOne
@JoinColumn(name = "question_id")
@JsonIgnore
private Question question;
}

public class SurveyReply {
@Id
@GeneratedValue
private Integer id;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "survey_id", nullable = false)
private Survey survey;
@OneToMany(mappedBy = "surveyReply", cascade = CascadeType.ALL, orphanRemoval = true)
private List<QuestionReply> questionReply;
}

public class QuestionReply {
@Id
@GeneratedValue
private Integer id;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "survey_reply_id", nullable = false)
private SurveyReply surveyReply;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "question_id", nullable = false)
private Question question;
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "question_reply_id")
private List<OptionReply> optionReplies;
@Size(max = 4000)
private String replyText;
}

public class OptionReply {
@Id
@GeneratedValue
private Integer id;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "question_reply_id", nullable = false)
private QuestionReply questionReply;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "option_id", nullable = false)
private Option option;
private boolean selected;
}

# Notes
1. Please note it may generate different answer each time, such as spring boot version may be different. The field name maybe different as well, e.g. "max_response" and "response_limit", you may try to generate the result few times and use better one.
2. You may try to limit the response words, otherwise the result may be break. You can achieve this by reducing the requirement in one question. If it exceeds words limit, type "give me rest of the code".
3. The result may be not correct, please verify the result. It can help you to debug, just paste the error to it.
4. GPT will use different implementation even in the same context, e.g. it will call another service B in service A, but if you ask it again, it will use repository directly... sometimes it will use Integer type, but sometimes it use int type...
5. After modify it partially few times, you can ask it to verify the full db schema again.
6. The end date of GPT training data is September 2021. You should use an old version of dependency, e.g. spring boot 2.5.8

# Tips
* You can copy the code from ChatGPT, and the select a package folder, then directly paste it, IDEA will create a file for it.

# To do
When have testing database, ask below question:
1. I want to test if there is performance issue, how can I quickly insert millions of record to survey questions, options, and their replies, and winners tables?
