window.onload = (function() {
	var start = Date.now();
	
	var submitButton = document.getElementById("quiz-submit-button");
	submitButton.onclick = calculateTime;

	function calculateTime(e) {
		var end = Date.now();
		var elapsed = end - start;

		var dateParam = document.createElement("input");
		dateParam.type = "hidden";
		dateParam.name = "elapsedTime";
		dateParam.value = elapsed;

		var form = document.getElementById("quiz-submit-form");
		form.appendChild(dateParam);
	}
	
});