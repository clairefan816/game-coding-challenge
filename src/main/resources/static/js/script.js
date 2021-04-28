const url = 'http://localhost:8080/v1/mindmaster';

 //Load sound and add click listener to keyboard
const audio_files = ['/wav/0.wav', '/wav/1.wav', '/wav/2.wav', '/wav/3.wav', '/wav/4.wav', '/wav/5.wav', '/wav/6.wav','/wav/7.wav', '/wav/8.wav', '/wav/9.wav'];
let audio_objects = [];
for (let i = 0; i < 10; i++){
    audio_objects[i] = new Audio(audio_files[i]);
}


const colors = ["#2A1A1A", "#E05F0B", "#FE3336", "#FA5229", "#FA9634", "#FCDA47", "#D8D03D", "#0BB3FA", "#3362CA", "#6D56BE"]

let currentBoard = [];
let roundNum = currentBoard.length;
let secretNumber ="";
let gameId ="";
const create_game = () => {
    $.ajax({
        url:url+"/game",
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data:JSON.stringify({
            "PlayerPreference":"EASY"
            }),
        success: function(data){
            currentBoard = data.guesses;
            secretNumber = data.secretNumber;
            gameId = data.gameId;
            stepResults = data.stepResults;
            console.log(currentBoard);
            console.log(secretNumber);
            console.log(gameId);
            console.log(stepResults);
        }
    })
}

$(document).ready(function(){
    $('#easy').on('click', create_game);
})



let ten_rounds = [];

$(document).ready(function(){
    $('.result-container').on('click', '.one-result', function(){
        let index = $(".one-result").index(this);
        alert(index);
    })
})

// make a guess
// add click event listener to every key note
let round_guess = [];
$(document).ready(function(){
    $('.key-container').on('click', '.key-num', function(){
        let number = $(this).data("number");
        audio_objects[number].play();
        let guessSize = round_guess.length;
        if (guessSize < 4){
            // 1. push the guess number into round_guess
            round_guess.push(number);
            // 2. update the guess number to the guess board
            let rowNum = 9 - currentBoard.length;
            let boxNum = 4 * rowNum + guessSize;
            let selected_color = colors[number];
            $('.one-box:eq('+boxNum+')').text(number).css("background-color",selected_color);
        }
        console.log(round_guess);
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
            round_guess = [];
            currentBoard = data.guesses;
            roundNum = currentBoard.length;
            myObject = data.stepResults[roundNum -1];

                let a = myObject.matchDigitAndPosition;
                let b = myObject.matchDigit;
                let resultText = a + "A" + b + "B";
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






