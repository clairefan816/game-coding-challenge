const url = 'http://localhost:8080/v1/mindmaster';

const HARD_MAX_INDEX = 10;
const EASY_MAX_INDEX = 7;
const colors = ["#2A1A1A", "#E05F0B", "#FE3336", "#FA5229", "#FA9634", "#FCDA47", "#D8D03D", "#0BB3FA", "#3362CA", "#6D56BE"]
 //Load sound and add click listener to keyboard
const audio_files = ['/wav/0.wav', '/wav/1.wav', '/wav/2.wav', '/wav/3.wav', '/wav/4.wav', '/wav/5.wav', '/wav/6.wav','/wav/7.wav', '/wav/8.wav', '/wav/9.wav'];
let audio_objects = [];
for (let i = 0; i < 10; i++){
    audio_objects[i] = new Audio(audio_files[i]);
}
let round_guess = [];

let game = {
    gameId : "",
    secretNumber: "",
    currentBoard : [],
    stepResults : [],
    status: "",
    playerPreference : {
        preference : "",
        withColor : true,
        withNumber : true
    }
}

let webWithColor = true;
let webWithNumber = true;

$(document).ready(function(){
    $('#with-color').on('click', function(){
        if(webWithColor === false){
            webWithColor = true;
            $('#with-color').text("With Color");
        } else {
            webWithColor = false;
            $('#with-color').text("No Color")
        }
        console.log("color: " + game.playerPreference.withColor);
    })
})

$(document).ready(function() {
    $('#with-number').on('click', function () {
        if (webWithNumber === false) {
            webWithNumber = true;
            $('#with-number').text("With Number");
        } else {
            webWithNumber = false;
            $('#with-number').text("No Number");
        }
    })
})


// Create a new game
const create_game = (playerPreference) => {
    $.ajax({
        url:url+"/game",
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data:JSON.stringify(playerPreference),
        success: function(data){
            game = {
                gameId : data.gameId,
                secretNumber : data.secretNumber,
                currentBoard : data.guesses,
                stepResults : data.stepResults,
                status : data.status,
                playerPreference: {
                    preference: data.playerPreference.preference,
                    withColor: data.playerPreference.withColor,
                    withNumber: data.playerPreference.withNumber
                }
            }
            loadPlayerPreferenceInformation(game);
        },
        error: function (error){
            console.log(error);
        }
    })
}

const loadPlayerPreferenceInformation = (game) => {
    $('#game-id').text(game.gameId);
    $('#game-result').text(game.status);
    $('#peek-digit-answer').text(game.secretNumber);
    let resultArray = [...game.secretNumber];
    $('#result1').css('background-color',colors[resultArray[0]]);
    $('#result2').css('background-color',colors[resultArray[1]]);
    $('#result3').css('background-color',colors[resultArray[2]]);
    $('#result4').css('background-color',colors[resultArray[3]]);
}

// if (sessionStorage.getItem("gameId") != null){
//     $('.box-content').load("http://localhost:8080/v1/mindmaster/" + sessionStorage.getItem("gameId"))
// }

const resetGame = () => {
    // reset result board
    $(".result-container").children(".one-result").text("");
    // reset guess board
    $(".guess-container").find(".one-box").text("").css("background-color","white")
    // reset information board

    $('#game-id').text("");
    $('#game-result').text("");
    $('#peek-digit-answer').text("");
}

// Create a easy level game
$(document).ready(function (){
    $('#easy').on('click', function (){
        round_guess = [];
        resetGame();

        game.playerPreference.preference = "EASY";
        game.playerPreference.withColor = webWithColor;
        game.playerPreference.withNumber = webWithNumber;
        create_game(game.playerPreference);

        $('#hide8').hide();
        $('#hide9').hide();
        $('#key-hide-8').css('cursor', 'not-allowed');
        $('#key-hide-9').css('cursor', 'not-allowed');
        console.log(game.gameId);
    })
})



// Create a hard level game
$(document).ready(function (){
    $('#hard').on('click', function (){
        round_guess = [];
        resetGame();
        game.playerPreference.preference = "HARD";
        game.playerPreference.withColor = webWithColor;
        game.playerPreference.withNumber = webWithNumber;
        create_game(game.playerPreference);
        $('#hide8').show();
        $('#hide9').show();
        $('#key-hide-8').css('cursor', 'grab');
        $('#key-hide-9').css('cursor', 'grab');
        console.log(game.gameId);
    })
})




// make one guess
// add click event listener to every key note

$(document).ready(function(){
    $('.key-container').on('click', '.key-num', function(){
        console.log("hehe");
        let selectedNumber = $(this).data("number");
        playSound(selectedNumber)

        if (game.status === "IN_PROGRESS"){
            // control the tries of one round guess within 4
            let guessSize = round_guess.length;
            if (guessSize < 4){
                // 1. push the guess number into round_guess
                if (game.playerPreference.preference === "EASY" && selectedNumber < 8 || game.playerPreference.preference === "HARD" && selectedNumber < 10){
                    round_guess.push(selectedNumber);
                    // 2. find the box location
                    let rowNum = 9 - game.currentBoard.length;
                    let boxNum = 4 * rowNum + guessSize;
                    // 3. update the guess number to the guess board
                    let selected_color = colors[selectedNumber];
                    if(game.playerPreference.withColor){
                        fillBoxColor(boxNum, selected_color);
                    }
                    if (game.playerPreference.withNumber){
                        fillBoxNumber(boxNum, selectedNumber);
                    }
                    if (!game.playerPreference.withColor && !game.playerPreference.withNumber){
                        fillBoxShadow(boxNum);
                    }
                }
            }
            console.log(round_guess);
        }

    })
})

const playSound = (selectedNumber) => {
    if (game.playerPreference.preference === "EASY" && selectedNumber <= EASY_MAX_INDEX){
        audio_objects[selectedNumber].play();
    }
    if (game.playerPreference.preference === "HARD" && selectedNumber <= HARD_MAX_INDEX){
        audio_objects[selectedNumber].play();
    }
    console.log(game.playerPreference.preference);
    if (game.playerPreference.preference.length === 0){
        audio_objects[selectedNumber].play();
    }
}


// revert a number
$(document).ready(function(){
    $('.button-cancel').on('click', function(){
        if (game.status === "IN_PROGRESS"){
            if (round_guess.length > 0){
                let rowNum = 9 - game.currentBoard.length;
                let boxNum = 4 * rowNum + round_guess.length - 1;
                $('.one-box:eq('+boxNum+')').text("").css("background-color","white");
                round_guess.pop();

            }
        }
        console.log(round_guess);
    })
})



const one_round_guess = (round_guess, gameId) =>{

    $.ajax({
        url:url+"/game/guess",
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data:JSON.stringify({
            "guess": round_guess,
            "gameId": gameId
        }),
        success: function(data){
            console.log(data);
            game = {
                gameId : data.gameId,
                secretNumber : data.secretNumber,
                currentBoard : data.guesses,
                stepResults : data.stepResults,
                status : data.status,
                playerPreference: {
                    preference: data.playerPreference.preference,
                    withColor: data.playerPreference.withColor,
                    withNumber: data.playerPreference.withNumber
                }
            }

            loadGuessBoard(game.currentBoard);
            loadResults(game.stepResults);
            loadPlayerPreferenceInformation(game);

            if (game.status === "PLAYER_VICTORY"){
                alert("You win!");
            }
            if (game.status === "PLAYER_LOST"){
                alert("You lost!");
            }
        },
        error: function (error){
            console.log(error);
        }
    })
}

// send a post request for one-round guess
$(document).ready(function(){
    $('.button-confirm').on('click', function(){
        if (game.status !== "IN_PROGRESS"){
            alert("Please first start a new game!")
        } else if (round_guess.length !== 4){
            alert("Your guess is yet to complete!")
        } else {
            one_round_guess(round_guess, game.gameId);
            round_guess = [];
        }
    })
})


const fillBoxNumber= (boxNum, selectedNumber) => {
    if (!game.playerPreference.withColor){
        $('.one-box:eq('+boxNum+')').text(selectedNumber).css('color','black');
    }
    $('.one-box:eq('+boxNum+')').text(selectedNumber);
}

const fillBoxColor = (boxNum, selected_color) => {
    $('.one-box:eq('+boxNum+')').css("background-color",selected_color);
}

const fillBoxShadow = (boxNum) => {
    $('.one-box:eq('+boxNum+')').css("background-color","#DFBDB1");
}

const loadResults = (stepResults) => {
    let roundsAlready = stepResults.length;
    for (let resultBoxIndex = 9; resultBoxIndex > 9 - roundsAlready; resultBoxIndex--){
        let oneRoundResult = stepResults[9 - resultBoxIndex];
        let a = oneRoundResult.a;
        let b = oneRoundResult.b;
        let resultText = a + "  🐂  " + b + "  🐄  ";
        $('.one-result:eq('+resultBoxIndex+')').text(resultText);
    }
}

// currentBoard is list<int[]>
const loadGuessBoard = (currentBoard) => {
    let boardLen = currentBoard.length;
    for (let row = 0; row < boardLen; row++){
        let rowNum = 9 - row;
        for (let roundSize = 0; roundSize < 4; roundSize++){
            let boxNum = 4 * rowNum + roundSize;
            let selectedNum = currentBoard[row][roundSize];
            if(game.playerPreference.withColor){
                fillBoxColor(boxNum, colors[selectedNum]);
            }
            if (game.playerPreference.withNumber){
                fillBoxNumber(boxNum, selectedNum);
            }
            if (!game.playerPreference.withColor && !game.playerPreference.withNumber){
                fillBoxShadow(boxNum);
            }
        }
    }
}
