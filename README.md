# drop-token-game

Libraries used
- Spring Boot
- Spring Rest
-Jersey/Jackson Serialization
- Javax Validation
- JUnit
- MongoDB

### Build Project
Used Spring Initializer

### REST APIs for different tasks
1. To create a new game
POST request to 'localhost:8080/drop_token' with JSON body  
  {  
    "Players": ["Player1", "Player2"],  
    "NoOfRows": 4,  
    "NoOfColumns": 4  
   }  
   
2. To retrieve all games in progress state
 GET request to 'localhost:8080/drop_token'
 returns a list of string with gameIds which are in progress state
 
3. To retrieve a game of given gameId
 GET request to 'localhost:8080/drop_token/{GENERATED ID}
 returns a json body
    {  
      "Players": ["Player1", "Player2"], <br/>
      "State": IN_PROGRESS,  
      "Winner": null  
    }  
    
4. To make a move in the game
POST request to 'localhost:8080/drop_token/{GAMEID}/{PlayerID}'
with a JSON body  
    {  
      "Column": 2  
     }  
returns a URI created 'localhost:8080/drop_token/{GAMEID}/move/{MOVE_NUMBER}'

5. To retrieve a move made at move count
GET request to 'localhost:8080/drop_token/{GAMEID}/move/{MOVE_NUMBER}'
returns a JSON body  
    {  
      "type": MOVE,  
      "Player": "PlayerId",  
      "Column": 2  
     }  
    
6. When a player wants to quit a game before finishing it
DELETE request to 'localhost:8080/drop_token/{GAMEID}/{PlayerID}'
returns a JSON body  
    {  
      "type": QUIT,  
      "Player": "PlayerId"  
    }  
 
