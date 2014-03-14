window.addEventListener("load", function() {
	var addButton = document.getElementsByClassName("variant-add-button")[0];
	var optionsArr = [];

	addButton.onclick = addAnotherAnswerOption;

	function addAnotherAnswerOption() {
		
		var containingChoice = this.parentElement.parentElement;
		var answerNumber = containingChoice.id.substr(6);

		var numOptions = optionsArr[answerNumber];
		if( numOptions === undefined ) numOptions = 1;
		numOptions++;
		optionsArr[answerNumber] = numOptions;

		var newAnswer = document.createElement("div");
		newAnswer.className = "multi-choice-choice answer-variant";

		var filler = document.createElement("div");

		var containingDiv = document.createElement("div");

		var label = document.createElement("label");
		label.className = "variant-or";
		label.for = "answer" + answerNumber + "option" + numOptions;
		label.innerHTML = "- OR -";

		var variant = document.createElement("input");
		variant.type = "text";
		variant.className="variant-input";
		variant.name = "answer" + answerNumber +"option" + numOptions;

		containingDiv.appendChild( label );
		containingDiv.appendChild( variant );

		newAnswer.appendChild( filler );
		newAnswer.appendChild( containingDiv );

		var toAppendTo = containingChoice.nextSibling;
		while( true ) {
			if( toAppendTo == null ) break;
			if( toAppendTo.className === "multi-choice-choice" ) break;
			toAppendTo = toAppendTo.nextSibling;

		}

		containingChoice.parentElement.insertBefore( newAnswer, toAppendTo );

	}


});