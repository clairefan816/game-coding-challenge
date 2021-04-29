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

let withColor = true;
let withNumber = true;
let status = "";
let currentBoard = [];
let roundNum = currentBoard.length;
let secretNumber ="";
let gameId ="";
let gameLevel;


// set up the color
$(document).ready(function(){
    $('#with-color').on('click', function(){
        if (status === "IN_PROGRESS"){
            alert("This setting cannot be adjust during the game!")
        }
        if (!withColor){
            withColor = true;
            $('#with-color').text("With Color");
        } else {
            withColor = false;
            $('#with-color').text("No Color")
        }
        console.log("color: " + withColor);
    })
})

// set up the number
$(document).ready(function(){
    $('#with-number').on('click', function(){
        if (status === "IN_PROGRESS"){
            alert("This setting cannot be adjust during the game!")
        }
        if (!withNumber){
            withNumber = true;
            $('#with-number').text("With Number");
        } else {
            withNumber = false;
            $('#with-number').text("No Number");
        }
        console.log("number: " + withNumber);
    })
})




// Create a easy level game
$(document).ready(function (){
    $('#easy').on('click', function (){
        gameLevel = "EASY";
        create_game(gameLevel);
        $('#hide8').hide();
        $('#hide9').hide();
        $('#key-hide-8').css('cursor', 'not-allowed')
        $('#key-hide-9').css('cursor', 'not-allowed')
        console.log(gameId);
    })
})


// Create a hard level game
$(document).ready(function (){
    $('#hard').on('click', function (){
        gameLevel = "HARD";
        create_game(gameLevel);
        $('#hide8').show();
        $('#hide9').show();
        $('#key-hide-8').css('cursor', 'grab')
        $('#key-hide-9').css('cursor', 'grab')
    })
})


const create_game = (gameLevel) => {
    $.ajax({
        url:url+"/game",
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data:JSON.stringify({
            "preference":gameLevel
            }),
        success: function(data){
            currentBoard = data.guesses;
            secretNumber = data.secretNumber;
            gameId = data.gameId;
            stepResults = data.stepResults;
            status = data.status;

            $('#game-id').text(gameId);
            $('#game-result').text(status);
            $('#peek-answer').text(secretNumber);
            console.log(currentBoard);
            console.log(stepResults);
        },
        error: function (error){
            console.log(error);
        }
    })
}



// make a guess
// add click event listener to every key note
let round_guess = [];
$(document).ready(function(){
    $('.key-container').on('click', '.key-num', function(){
        let selectedNumber = $(this).data("number");
        playSound(selectedNumber)

        let guessSize = round_guess.length;
        if (guessSize < 4){
            // 1. push the guess number into round_guess
            round_guess.push(selectedNumber);
            // 2. update the guess number to the guess board
            let rowNum = 9 - currentBoard.length;
            let boxNum = 4 * rowNum + guessSize;
            let selected_color = colors[selectedNumber];
            if(withColor){
                fillBoxColor(boxNum, selected_color);
            }
            if (withNumber){
                fillBoxNumber(boxNum, selectedNumber);
            }
            if (!withColor && !withNumber){
                fillBoxShadow(boxNum);
            }
        }
        console.log(round_guess);
    })
})

const playSound = (selectedNumber) => {
    if (gameLevel === "EASY" && selectedNumber <= EASY_MAX_INDEX){
        audio_objects[selectedNumber].play();
    }
    if (gameLevel === "HARD" && selectedNumber <= HARD_MAX_INDEX){
        audio_objects[selectedNumber].play();
    }
    if (gameLevel == null){
        audio_objects[selectedNumber].play();
    }
}

const fillBoxNumber= (boxNum, selectedNumber) => {
    if (!withColor){
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









let ten_rounds = [];

$(document).ready(function(){
    $('.result-container').on('click', '.one-result', function(){
        let index = $(".one-result").index(this);
        alert(index);
    })
})



// revert a number
$(document).ready(function(){
    $('.button-cancel').on('click', function(){
        if (round_guess.length > 0){
            let rowNum = 9 - currentBoard.length;
            let boxNum = 4 * rowNum + round_guess.length - 1;
            $('.one-box:eq('+boxNum+')').text("").css("background-color","white");
            round_guess.pop();

        }
        console.log(round_guess);
    })
})





let stepResults = [];
let matchDigitAndPosition = 0;
let matchDigit = 0;
var myObject;
const one_round_guess = () =>{
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
            // reset the round-guess array
            console.log(data);
            round_guess = [];
            currentBoard = data.guesses;
            roundNum = currentBoard.length;
            myObject = data.stepResults[roundNum -1];
            let a = myObject.a;
            let b = myObject.b;
            let resultText = a + "  🐂  " + b + "  🐄  ";
            roundNum = currentBoard.length;
            let resultIndex = 10-roundNum;
            $(document).ready(function(){
                $('.one-result:eq('+resultIndex+')').text(resultText);
                console.log(resultText);
            })

        }
    })
}

// send a post request for one-round guess
$(document).ready(function(){
    $('.button-confirm').on('click', one_round_guess);
    // update result
    // if (myObject != null){
    //     $('.button-confirm').on('click', function(){
    //         let a = myObject.matchDigitAndPosition;
    //         let b = myObject.matchDigit;
    //         let resultText = a + "A" + b + "B";
    //         roundNum = currentBoard.length;
    //         let resultIndex = 9-roundNum;
    //         $('.one-box:eq('+resultIndex+')').text(resultText);
    //         console.log(resultText);
    //     })
    // }

})






