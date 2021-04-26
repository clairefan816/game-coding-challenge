const url = 'http://localhost:8080/v1/mindmaster';
const color_0 = '#19161D';
const color_1 = '#E05F0B';
const color_2 = '#FE3336';
const color_3 = '#FA5229';
const color_4 = '#FA9634';
const color_5 = '#FCDA47';
const color_6 = '#D8D03D';
const color_7 = '#0BB3FA';
const color_8 = '#3362CA';
const color_9 = '#6D56BE';
const slot_1 = document.querySelector('.one-guess');

const print = () => {
    slot_1.style.backgroundColor = color_0;
}
//Load sound and add click listener to keyboard
const audio_elements = ['#number-0', '#number-1', '#number-2', '#number-3', '#number-4', '#number-5', '#number-6', '#number-7', '#number-8', '#number-9'];
const audio_files = ['/wave/1.wav', '2.wav', '3.wav', '4.wav', '5.wav', '6.wav','7.wav', '8.wav', '9.wav', '10.wav'];

var audio = [];
var audio_play_funcs = [];
var audio_objects = [];

for (let i = 0; i < 10; i++) {
    audio_objects[i] = document.querySelector(audio_elements[i]);
    audio[i] = new Audio(audio_files[i]);
    audio_play_funcs[i] = () => {
        audio[i].play();
    }
    audio_objects[i].addEventListener('click', audio_play_funcs[i]);
    audio_objects[i].addEventListener('click', print);
}
const easyGame = document.querySelector('#easy');
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
            alert("Success" + data.gameId);
        }
    })
}
easyGame.addEventListener('click', create_game)





