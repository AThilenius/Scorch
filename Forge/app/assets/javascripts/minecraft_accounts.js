
// var unField = document.getElementById('unField');
// var pwField = document.getElementById('pwField');
// var verifyDiv = document.getElementById('verifyDiv');
// var createForm = document.getElementById('createForm');

// createForm.onsubmit = function() {
//     verifyDiv.setAttribute('class', 'visible');
// };



// createForm.onsubmit = function (e) {
//   // stop the regular form submission
//   e.preventDefault();

//   // collect the form data while iterating over the inputs
//   var data = {};
//   for (var i = 0, ii = createForm.length; i < ii; ++i) {
//     var input = createForm[i];
//     if (input.name) {
//       data[input.name] = input.value;
//     }
//   }

//   // construct an HTTP request
//   var xhr = new XMLHttpRequest();
//   xhr.open(createForm.method, createForm.action, true);
//   xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

//   // send the collected data as JSON
//   xhr.send(JSON.stringify(data));

//   xhr.onloadend = function () {
//     // done
//   };
// };



// $("addButton").click(function(){
//   console.log("Button Clicked!");
//   $("newAccountDiv").animate({
//     height:'toggle'
//   });
// });

// $(document).ready(function(){
//   $("addButton").click(function(){
//     console.log("Button Clicked!");
//     $("newAccountDiv").animate({
//       height:'toggle'
//     });
//   });

//   $("testButton").click(function(){
//     console.log("Button Clicked!");
//   });

//   // $("#addButton").prev("a.ui-btn").unbind().click(function(){
//   //   console.log("Button Clicked!");
//   // });

// });