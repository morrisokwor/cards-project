# cards-project
## Run the Application locally  

Prerequisites
- Java
- Maven
- MySQL Database

#### Step 1: Download the code from github

   ``` git clone https://github.com/morrisokwor/cards-project.git ```

#### Step 2: Build the project
  Open the project in any editor like Intellij or Eclipse and the build from terminal run the following command.  
   
   ```mvn install ```

Step 3 Edit ```application.properties``` as per your MySQL credentials and Launch the Application
  using the command below or Running it from the IDE
  
  ``` mvn spring-boot:run ```

Note : Running the app``` Users ```table will be created in Database and few Users will be added.Stopping the application will delete the database.
  
Step 4 - Use Swaager [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)
 or Postman to test the REST apis created.
  By default it runs on port 8080.


### Sample APIs,

### 1.Login
The default users creted credentials are:

``` email: user@gmail.com, password: abc123 ```

``` email: admin@gmail.com, password: abc123 ```

#### http://localhost:8080/auth/signIn
   
   A POST METHOD
   
     {
        "email": "admin@gmail.com",
         "password": "abc123"
    }

Signin Response

    {
    "user": {
     "id": 2,
     "firstName": "Admin",
     "lastName": "Admin",
     "email": "admin@gmail.com",
     "role": "ADMIN"
    },
    "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTMwODQ1NDYsImV4cCI6MTY5MzA4NjM0Niwic3ViIjoiYWRtaW5AZ21haWwuY29tIn0.zIzLQaeaQQL9EdLYm8g2MrlU5RchmOapsZBOpzRd6JL-oX7SWApRGk9a7qqZJp9i0dDNRQu6yTXLEYasmlG6zA",
    "message": "Success",
    "success": true
    }    

### Adding Card
#### http://localhost:8080/card
   
   A POST METHOD
   
     {
     "name": "Test card",
     "color": "#FFFF00",
     "description": "Testing Card"
    }   

  Adding Card API Response

    {
    "success": true,
    "message": "Added Successfully",
     "data": {
        "id": 1,
        "name": "Test Card",
        "color": "#FF5733",
        "status": "To Do",
        "description": null,
        "createdBy": 1,
        "updatedBy": 1,
        "createdAt": "2023-08-26T18:22:24.000+00:00",
        "updatedAt": "2023-08-26T18:22:23.605+00:00"
        }
    }

   
   
