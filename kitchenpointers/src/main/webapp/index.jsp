

<html>
<head>
<title>Kitchen Pointers</title>
 
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

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

#recipeTable {
	visibility: hidden;
}

</style>

</head>

<body>

<script type="text/javascript">

$(document).ready(function(){

    var ingredientCounter = 2;
    var excludedIngredientCounter = 2;

    $("#addButton").click(function () {
			
		var newIngredientDiv = $(document.createElement('div'))
		     .attr("id", 'IngredientTextBoxDiv' + ingredientCounter);
	                
		newIngredientDiv.after().html('<label>Ingredient #'+ ingredientCounter + ' : </label>' +
		      '<input type="text" name="ingredientTextbox' + ingredientCounter + 
		      '" id="ingredientTextbox' + ingredientCounter + '" value="" >');
	            
		newIngredientDiv.appendTo("#IngredientsGroup");
	
					
		ingredientCounter++;
     });

     $("#removeButton").click(function () {
		if(ingredientCounter==1){
	          alert("No more textboxes to remove");
	          return false;
	       }   
	        
		ingredientCounter--;
				
	    $("#IngredientTextBoxDiv" + ingredientCounter).remove();
				
     });
		
     $("#addButton2").click(function () {
			
 		var newIngredientDiv = $(document.createElement('div'))
 		     .attr("id", 'ExcludedIngredientTextBoxDiv' + excludedIngredientCounter);
 	                
 		newIngredientDiv.after().html('<label>Excluded Ingredient #'+ excludedIngredientCounter + ' : </label>' +
 		      '<input type="text" name="excludedIngredientTextbox' + excludedIngredientCounter + 
 		      '" id="excludedIngredientTextbox' + excludedIngredientCounter + '" value="" >');
 	            
 		newIngredientDiv.appendTo("#ExcludedIngredientsGroup");
 	
 					
 		excludedIngredientCounter++;
      });

      $("#removeButton2").click(function () {
	 	if(excludedIngredientCounter==1){
	           alert("No more textboxes to remove");
	           return false;
	        }   
	         
	 	excludedIngredientCounter--;
 			
        $("#ExcludedIngredientTextBoxDiv" + excludedIngredientCounter).remove();
 			
      });
      
     
     $("#getButtonValue").click(function () {
		
		var enteredIngredients = [];
		var excludedIngredients = [];
		
		for(i=1; i<ingredientCounter; i++){
		 	  		enteredIngredients.push($('#ingredientTextbox' + i).val());
		}
		   
			     
		for(i=1; i<excludedIngredientCounter; i++){
					excludedIngredients.push($('#excludedIngredientTextbox' + i).val());
		}
		
		var ingredientList = {};
		
		if(enteredIngredients.length > 0) {
			ingredientList.includedIngredients = enteredIngredients;
		}
		if(excludedIngredients.length > 0) {
			ingredientList[excludedIngredients] = excludedIngredients;
		}
		if($('#cuisineTextbox').val()) {
			ingredientList[cuisine] = $('#cuisineTextbox').val();
		}
		if($('#calorieTexbox').val()) {
			ingredientList[calories] = $('#calorieTexbox').val();
		}
			
		  
	/*	var ingredientList = {
				"includedIngredients" : enteredIngredients,
				"excludedIngredients" : excludedIngredients,
				"cuisine" : $('#cuisineTextbox').val(),
				"calories" : parseInt($('#colorieTexbox').val()),
				"sortBy" : $('#sortBy option:selected').text()
			}
  	     */
  	     $.ajax({
  	       url: "/kitchenpointers/getRecipes",
  	       type: 'POST',
  	       contentType:'application/json',
  	       data: JSON.stringify(ingredientList),
  	       dataType:'json',
  	       success: function(response){
  	    	    createRecipeTable(response.recipes);
  	    	    console.log(ingredientList);
  	    	    console.log(response);
  	          },
  	       error: function(xhr, ajaxOptions, thrownError) {
  	          	//On error do this
  	            $.mobile.loading('hide')
  	            if (xhr.status == 200) {

  	                alert(ajaxOptions);
  	            }
  	            else {
  	                alert(xhr.status);
  	                alert(thrownError);
  	            }
  	        }
  	     });
     	
		$("#recipeTable").css('visibility', 'visible');
     });
     
     function createRecipeTable(recipeList) {
    	 for(var i = 0; i < recipeList.length; i++) {    		 	
    		 var link = document.createElement('a');
    		 link.setAttribute('href', recipeList[i].url);
    		 link.innerHTML = "[link to recipe]";
    			 
    		 var row = $("<tr>");

    		  row.append($("<td>" + recipeList[i].name + "</td>"))
    		     .append($("<td>" + recipeList[i].calories + "</td>"))
    		     .append($("<td>")
    		     	.append(link)
    		     	.append($("</td>")));
    		 
    		  $("#recipeTable tbody").append(row);
    	 }
    	 
     }
  });
</script>
</head>

<body>
<div id="container">

<h1>Online Recipe Matcher</h1>
	<label>Cuisine : </label><input id='cuisineTextbox' >
	   
	<label>Calorie Limit : </label><input id='calorieTexbox' >
	
	<br><br>
    
    <p>Please add your ingredients:</p>
    
	<div id='IngredientsGroup'>
		<div id="IngredientTextBoxDiv1">
			<label>Ingredient #1 : </label><input id='ingredientTextbox1' >
		</div>
	</div>
	<input type='button' value='Add Ingredient' id='addButton'>
	<input type='button' value='Remove Last Ingredient' id='removeButton'>
	
	<br><br>
	
	<p> Do you want to exclude any ingredients? </p>
	
	<div id='ExcludedIngredientsGroup'>
		<div id="ExcludedIngredientTextBoxDiv1">
			<label>Excluded Ingredient #1 : </label><input id='excludedIngredientTextbox1' >
		</div>
	</div>
	<input type='button' value='Add Excluded Ingredient' id='addButton2'>
	<input type='button' value='Remove Last Excluded Ingredient' id='removeButton2'>
	
	<br><br>
	
	<p>Sort by: 
	<select id=sortBy>
		<option value="sortByRating">Rating</option>
		<option value="sortByCalories">Calories</option>
	</select>
	</p>
	
	<br><br>
	
	<input type='button' value='Get Recipe' id='getButtonValue'>
	
	<br><br>
	
	<table id="recipeTable" class="table table-striped table-bordered">
		<thead>
			<tr>
				<th>Name</th>
				<th>Calories</th>
				<th>Link</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
	</table>
</div>
</body>
</html>
