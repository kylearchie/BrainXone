window.addEventListener("load", function() {
	var addButton = document.getElementById("add-another-answer");
	var numAnswers = 1;

	addButton.onclick = addAnotherAnswer;

	function addAnotherAnswer() {
		
		numAnswers++;

		var newAnswer = document.createElement("div");
		newAnswer.className = "multi-choice-choice";
		newAnswer.id = "answer" + numAnswers;

		var removeDiv = document.createElement("div");
		var remove = document.createElement("div");
		remove.className = "choice-remove-button";
		remove.onclick = removeThisBox;

		var optionDiv = document.createElement("div");
		var option = document.createElement("input");
		option.type = "text";
		option.name = "answer" + numAnswers + "option1";

		var variantDiv = document.createElement("div");
		var variant = document.createElement("div");
		variant.className="variant-add-button";

		var answer1 = document.getElementById("answer1");
		var variantAdd1 = answer1.getElementsByClassName("variant-add-button")[0];
		variant.onclick = variantAdd1.onclick;

		removeDiv.appendChild( remove );
		optionDiv.appendChild( option );
		variantDiv.appendChild( variant );

		newAnswer.appendChild( removeDiv );
		newAnswer.appendChild( optionDiv );
		newAnswer.appendChild( variantDiv );


		var form = document.getElementById("multi-choice-form-choices");
		form.appendChild( newAnswer );

	}

	function removeThisBox( e ) {

		numAnswers --;

		var row = this.parentElement.parentElement;
		console.log("row: ");
		console.log(row);
		var parent = row.parentElement;
		var toRemove = row.nextSibling;
		while( true ) {
			console.log(toRemove);
			if( toRemove === null ) break; 
			if( toRemove.className === "multi-choice-choice" ) break;
			var temp = toRemove.nextSibling
			toRemove.remove( toRemove );
			toRemove = temp;
		}
		console.log("parent:");
		console.log(parent);
		console.log(row);
		row.remove( row );

		var arr = document.getElementsByClassName("multi-choice-choice");
		var answers = 0;
		var options = 1;
		for( var i = 1; i < arr.length; i++ ) {
			if( arr[i].className === "multi-choice-choice" ) {
				options = 1;
				answers++;
				arr[i].id = "answer" + answers;
				arr[i].children[1].firstChild.name = "answer" + answers + "option" + options;
			} else {
				options++;
				arr[i].children[1].children[1].name = "answer" + answers + "option" + options;
			}
		}
	}

});