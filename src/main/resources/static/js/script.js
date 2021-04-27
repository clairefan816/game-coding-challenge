const url = 'http://localhost:8080/v1/mindmaster';
// const color_0 = '#19161D';
// const color_1 = '#E05F0B';
// const color_2 = '#FE3336';
// const color_3 = '#FA5229';
// const color_4 = '#FA9634';
// const color_5 = '#FCDA47';
// const color_6 = '#D8D03D';
// const color_7 = '#0BB3FA';
// const color_8 = '#3362CA';
// const color_9 = '#6D56BE';
// const slot_1 = document.querySelector('.one-guess');
//
// const print = () => {
//     slot_1.style.backgroundColor = color_0;
// }
// //Load sound and add click listener to keyboard
// const audio_elements = ['#number-0', '#number-1', '#number-2', '#number-3', '#number-4', '#number-5', '#number-6', '#number-7', '#number-8', '#number-9'];
// const audio_files = ['/wave/1.wav', '2.wav', '3.wav', '4.wav', '5.wav', '6.wav','7.wav', '8.wav', '9.wav', '10.wav'];
//
// var audio = [];
// var audio_play_funcs = [];
// var audio_objects = [];
//
// for (let i = 0; i < 10; i++) {
//     audio_objects[i] = document.querySelector(audio_elements[i]);
//     audio[i] = new Audio(audio_files[i]);
//     audio_play_funcs[i] = () => {
//         audio[i].play();
//     }
//     audio_objects[i].addEventListener('click', audio_play_funcs[i]);
//     audio_objects[i].addEventListener('click', print);
// }

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
            "name":"claire"
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






