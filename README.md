<img src="src/main/resources/static/images/logo.png" align= "right" width="160px" height="130px"/>

# Musicmind [![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
> A web game adapted from classical Mastermind game, with pitch recognition as an added challenge.
## 🚩 Table of Contents

- [Background](#Background)
- [Examples](#Examples)
- [Install](#Install)
- [Project Structure](#Project Structure)
- [Todo](#Todo)
- [FAQ](#FAQ)
- [Maintainer](#Maintainer)
- [Sponsors](#sponsors-)
- [License](#License)

## 📕 Background
### Value of this App
Why I'd like to develop this app.
The MUSICMIND app is a single page / single user experience design. 

Whom can benefit from this app.


### Game History
MusicMind app is a board / decode game adapted from an old British game called `🐂s and 🐄s`.
The idea of the game is for one player (the code-breaker) to guess the secret code chosen by the 
other player(the code-maker).  

The code is a sequence of 4 digits chosen from 0 ~ 9. Then, the code-breaker makes a serie of 
pattern guesses - after each guess the code-maker gives feedback in the form of 2 numbers. If 
the matching digits are in the right position, you earn a 🐂, if in different positions, you earn 
a 🐄.  The code-breaker wins the game by getting the secret number ( earn 4 🐂s in one round ) 
within 10 rounds.

For example:  
* Secret number: 1357  
* Opponent's try: 1538  
* Answer: 1 🐂 and 2 🐄s (The bull is "1", the cows are "2" and "5".)  

### New Features  
Following the playing rule of `🐂s and 🐄s`, MusicMind develop two more features into 
this game, 
decode the color pattern, and the sound pattern.

Users can either play with one mode or three modes:
* **Guess number pattern** :
* **Guess color pattern** :
* **Guess color pattern** :

For making this game more friendly to everyone, this app opened two difficulty level for users
to pick from :
* **Easy Level** : The secret number / color / sound pattern is picked from 8 candidates.
  It means there are `4096` possible combinations.
* **Hard Level** : The secret number / color / sound pattern is picked from 10 candidates.
  It means there are `10000` possible combinations.


## 🎨 Example

  
Colorblind-friendly version or someone who would like to try different version

## 🐾 User Feedback



## 🔧 Get started

### Pre-Installation Requirements

- [X] Setup Java Development Kit (JDK)
- [X] Setup an IntelliJ IDE




## 🧬 Project Architecture
This project follows the Spring MVC framework because the model-view-controller architecture 
and the ready components offered by Spring can be used to develop a flexible and loosely coupled 
web application.

The main REST APIS developed in the application serves for main functionalities of this 
game, including creating new game, retrieving a game, and checking game results.

Here are the three main endpoint URLs:
```URL
HTTP method: POST  CRUD: Create  ACTION: Create a new game
/v1/mindmaster/game

HTTP method: GET  CRUD: Read  Action: Returns a request game
/v1/mindmaster/game/{gameId}

HTTP method: POST  CRUD: Create  ACTION: Returns a game with answer checked result
/v1/mindmaster/game/guess
```


This below is the detailed structure of this application:

![musicmind (1)](https://user-images.githubusercontent.com/54572005/116755535-67c8e980-a9bf-11eb-82ec-69db58687ac3.png)


* The **Model** encapsulates the application data in the form of POJO. Besides, with **Lambok 
  library** (java library ) plugin for reducing "infrastructural code".
  
  
* The **Service**


* The **Controller** is responsible for taking **User Request** and calls the appropriate 
  service methods. 
  

  ```Java
  @PostMapping("/game")
    public ResponseEntity<Game> startNewGame(@RequestBody PlayerPreference playerPreference) throws IOException, InterruptedException, NoResponseException {
        if (!Constants.ALLOWED_LEVELS.contains(playerPreference.getPreference())){
            throw new BadRequestException(String.format("Game level must be one of: %s",
                    String.join(", ", Constants.ALLOWED_LEVELS)));
        }
        return ResponseEntity.ok(gameService.createGame(playerPreference));
    }
  ```
  ```Java
  @PostMapping("/game/guess")
    public ResponseEntity<Game> gamePlay(@RequestBody GameGuess gameGuess) throws InvalidGameException, NotFoundException, InvalidGuessException {
        Game game = gameService.playGame(gameGuess);
        return ResponseEntity.ok(game);
    }
  ```
  ```Java
  @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> retrieveOneGame(@PathVariable String gameId) throws InvalidParamException {
        return ResponseEntity.ok(gameService.retrieveGame(gameId));
    }
  ```
* The **Storage**


* The **View**


* The **Test**


## 🤸 Todo
* **Add more hints** :
* **Add score** :

## 👩‍💻 Maintainer

## 💬 FAQ
### It seems something wrong on your app?


If you find a bug (the website couldn't handle the query and gaven undesired results), kindly 
open an issue [here](https://github.com/clairefan816/game-coding-challenge/issues/new). Please 
include sample queries and their corresponding results.



## 📜 License