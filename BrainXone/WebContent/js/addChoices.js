window.onload = function() {
	var addButton = document.getElementById("add-another-answer");
	var numAnswers = 1;

	addButton.onclick = addAnotherAnswer;

	function addAnotherAnswer() {
		
		numAnswers++;

		var newAnswer = document.createElement("div");
		newAnswer.className = "multi-choice-choice";

		var removeDiv = document.createElement("div");
		var remove = document.createElement("div");
		remove.className = "choice-remove-button";
		remove.onclick = removeThisBox;

		var optionDiv = document.createElement("div");
		var option = document.createElement("input");
		option.type = "text";
		option.name = "option" + numAnswers;

		var validDiv = document.createElement("div");
		var valid = document.createElement("input");
		valid.type = "checkbox";
		valid.name = "valid" + numAnswers;
		valid.value = "1";

		removeDiv.appendChild( remove );
		optionDiv.appendChild( option );
		validDiv.appendChild( valid );

		newAnswer.appendChild( removeDiv );
		newAnswer.appendChild( optionDiv );
		newAnswer.appendChild( validDiv );

		var form = document.getElementById("multi-choice-form-choices");
		form.appendChild( newAnswer );

	}

	function removeThisBox( e ) {

		numAnswers --;

		var row = this.parentElement;
		row.parentElement.remove( row );

		var arr = document.getElementsByClassName("multi-choice-choice");
		for( var i = 0; i < arr.length; i++ ) {
			arr[i].children[1].firstChild.name = "option" + i;
			arr[i].children[2].firstChild.name = "valid" + i;
		}
	}

	var submitButton = document.getElementById("submit-multi-choice");

	submitButton.onclick = function( e ) {

		// e.preventDefault();

		var numAnswerField = document.createElement("input");
		numAnswerField.type = "hidden";
		numAnswerField.name = "numAnswers";
		numAnswerField.value = numAnswers;

		var choices = document.getElementById("multi-choice-form-choices");
		choices.appendChild( numAnswerField );

		// var form = document.getElementById("multi-choice-form");
		// form.submit();

	};

};