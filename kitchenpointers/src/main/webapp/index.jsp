

<html>
<head>
<title>Kitchen Pointers</title>
 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<style>

body {

background-color: lightblue;

text-align: center;

}



#container {

background-color: lightgrey;

width:80%;

padding: 25px;

border-color: grey;

border-style: solid;

margin: 0 auto;

}

</style>

</head>

<body>

<script type="text/javascript">

$(document).ready(function(){

    var counter = 2;
		
    $("#addButton").click(function () {
				
	if(counter>10){
            alert("Only 10 textboxes allow");
            return false;
	}   
		
	var newIngredientDiv = $(document.createElement('div'))
	     .attr("id", 'TextBoxDiv' + counter);
                
	newIngredientDiv.after().html('<label>Ingredient #'+ counter + ' : </label>' +
	      '<input type="text" name="textbox' + counter + 
	      '" id="textbox' + counter + '" value="" >');
            
	newIngredientDiv.appendTo("#TextBoxesGroup");

				
	counter++;
     });

     $("#removeButton").click(function () {
	if(counter==1){
          alert("No more textbox to remove");
          return false;
       }   
        
	counter--;
			
        $("#TextBoxDiv" + counter).remove();
			
     });
		
     $("#getButtonValue").click(function () {
		
	var msg = '';
	for(i=1; i<counter; i++){
   	  msg += "\n Ingredient #" + i + " : " + $('#textbox' + i).val();
	}
	
	 msg += "\n Cuisine: " + $('#cuisineTextbox').val();
	 msg += "\n Calories: " + $('#colorieTexbox').val();
    	  alert(msg);
     });
  });
</script>
</head>

<body>
<div id="container">

<h1>Online Recipe Matcher</h1>
	<label>Cuisine : </label><input type='textbox' id='cuisineTextbox' >
	   
	<label>Calorie Limit : </label><input type='textbox' id='colorieTexbox' >
	
	<br>
    
    <p>Please add your ingredients:</p>
    
	<div id='TextBoxesGroup'>
		<div id="TextBoxDiv1">
			<label>Ingredient #1 : </label><input type='textbox' id='textbox1' >
		</div>
	</div>
	<input type='button' value='Add Ingredient' id='addButton'>
	<input type='button' value='Remove Last Ingredient' id='removeButton'>
	
	<br><br>
	<input type='button' value='Get Recipe' id='getButtonValue'>
</div>
</body>
</html>
