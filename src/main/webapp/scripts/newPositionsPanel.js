import { generateOpenedPositionsPanel } from "./openedPositionsPanel.js";
import { generatePendingOrdersPanel } from "./pendingOrdersPanel.js";
import { generateChartForPair } from "./chartPanel.js";

var openedNPForm = null;

function orderLong(currencyPair, amount, price, trigger) {
	console.log("orderLong", currencyPair, amount, price, trigger);

	var req = new XMLHttpRequest();

	req.open("POST", "/api/order_long/" + currencyPair + "/" + amount + "/" + 
		trigger + "/" + price + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function() {
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200) {
			console.log("long order set");
			generatePendingOrdersPanel();
			generateOpenedPositionsPanel();
		}
		else {
			console.log("long order setting failure");
		}
	}
}

function orderShort(currencyPair, amount, price, trigger) {
	console.log("orderShort", currencyPair, amount, price, trigger);
	
	var req = new XMLHttpRequest();

	req.open("POST", "/api/order_short/" + currencyPair + "/" + amount + "/" + 
		trigger + "/" + price + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function() {
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200) {
			console.log("short order set");
			generatePendingOrdersPanel();
			generateOpenedPositionsPanel();
		}
		else {
			console.log("short order setting failure");
		}
	}
}

function longPosition(currencyPair, amount) {
	console.log("longPosition", currencyPair, amount);

	var req = new XMLHttpRequest();
	
	req.open("POST", "/api/long/" + currencyPair + "/" + amount + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function() {
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200) {
			console.log("long position opened");
			generateOpenedPositionsPanel();
		}
		else {
			console.log("long position opening failure");
		}
	}
}

function shortPosition(currencyPair, amount) {
	console.log("shortPosition", currencyPair, amount);

	var req = new XMLHttpRequest();
	
	req.open("POST", "/api/short/" + currencyPair + "/" + amount + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function() {
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200) {
			console.log("short position opened");
			generateOpenedPositionsPanel();
		}
		else {
			console.log("short position opening failure");
		}
	}
}

function closeNPForm() {
	document.getElementById(openedNPForm + "_opn_form").innerHTML = "";

	var openNPFormHandler = function(currencyPair) {
		return function() {
			openNPForm(currencyPair);
		}
	};

	document.getElementById(openedNPForm + "_opn_button").onclick = openNPFormHandler(openedNPForm);

	document.getElementById(openedNPForm + "_opn_button").innerHTML = "New position";
	document.getElementById(openedNPForm + "_opn_button").className = "btn btn-outline-primary btn-sm";

	openedNPForm = null;
}

function openNPForm(currencyPair) {
	if(openedNPForm != null) {
		closeNPForm();
	}

	openedNPForm = currencyPair;

	var htmlCode = `
		<td colspan="5">
			<div class="form-row justify-content-center" style="width: 32rem;">
				<div class="form-group col-md-6">
					<label for="amount">
						<b>Amount</b>
					</label>
					<input type="text" class="form-control" placeholder="Enter amount" name="amount" id="amount">
					<button type="button" class="btn btn-success btn-sm" id="long_position_button">
						Buy
					</button>
					<button type="button" class="btn btn-danger btn-sm" id="short_position_button">
						Sell
					</button>
				</div>
				<div class="form-group col-md-6">
					<label for="price">
						<b>Price</b>
					</label>
					<input type="text" class="form-control" placeholder="Enter price" name="price" id="price">
					<div class="form-check">
						<input class="form-check-input" type="radio" name="trigger" id="trigger" value="above" checked>
						<label class="form-check-label" for="trigger">above</label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="trigger" id="trigger" value="below">
						<label class="form-check-label" for="trigger">below</label>
					</div>
					<button type="button" class="btn btn-success btn-sm" id="order_long_button">
						Order Buy
					</button>
					<button type="button" class="btn btn-danger btn-sm" id="order_short_button">
						Order Sell
					</button>
				</div>
			</div>
		</td>`;
	
	document.getElementById(currencyPair + "_opn_form").innerHTML = htmlCode;

	document.getElementById("long_position_button").onclick = function() {
		longPosition(currencyPair, document.getElementById('amount').value);
	};
	document.getElementById("short_position_button").onclick = function() {
		shortPosition(currencyPair, document.getElementById('amount').value);
	};

	document.getElementById("order_long_button").onclick = function() {
		orderLong(currencyPair, document.getElementById('amount').value, document.getElementById('price').value, 
			document.getElementsByName('trigger')[0].checked);
	};
	document.getElementById("order_short_button").onclick = function() {
		orderShort(currencyPair, document.getElementById('amount').value, document.getElementById('price').value, 
			document.getElementsByName('trigger')[0].checked);
	};

	document.getElementById(currencyPair + "_opn_button").onclick = function() {
		closeNPForm();
	};

	document.getElementById(currencyPair + "_opn_button").innerHTML = "Close form";
	document.getElementById(currencyPair + "_opn_button").className = "btn btn-outline-secondary btn-sm";

	generateChartForPair(currencyPair);
}

function buildNewPositionsRow(pair) {
	var date = new Date();
	date.setTime(pair.timestamp);

	var row = `
	<tr>
		<th scope="row">
			${pair.currencyPair}
		</th>
		<td>${pair.ask}</td>
		<td>${pair.bid}</td>
		<td>${date.toLocaleString()}</td>
		<td>
			<button type="button" id="${pair.currencyPair}_opn_button" class="btn btn-outline-primary btn-sm">
				New position
			</button>
		</td>
	</tr>
	<tr class="form-popup" id="${pair.currencyPair}_opn_form\">
	</tr>`;

	return row;
}

function buildNewPositionsTable(json) {

	var tableCode = '';
	for(var i in json) {
		tableCode += buildNewPositionsRow(json[i]);
	}
	
	var htmlCode = `
		<table class="table table-sm">
			<thead>
				<tr>
					<th scope="col">Currency Pair</th>
					<th scope="col">Ask price</th>
					<th scope="col">Bid price</th>
					<th scope="col">Timestamp</th>
					<th scope="col">New position</th>
				</tr>
			</thead>
			<tbody>
				${tableCode}
			</tbody>
		</table>`;

	return htmlCode;
}



function newPositionsListener() {
	console.log(this.status);
	if(this.readyState == 4 && this.status == 200) {
		console.log("rates received successfully");
		if(this.responseText[0] == '[') {
			var jsonRates = JSON.parse(this.responseText);
			document.getElementById("new_positions").innerHTML = buildNewPositionsTable(jsonRates);

			var openNPFormHandler = function(currencyPair) {
				return function() {
					openNPForm(currencyPair);
				}
			}

			for(var i in jsonRates) {
				document.getElementById(jsonRates[i].currencyPair + "_opn_button").onclick = 
					openNPFormHandler(jsonRates[i].currencyPair);
			}

			if(openedNPForm != null) {
				openNPForm(openedNPForm);
			}
		}
	}
	else {
		console.log("rates receive failure");
	}
}

export function generateNewPositionsPanel() {
	var req = new XMLHttpRequest();
	
	req.addEventListener("load", newPositionsListener);
	req.open("GET", "/api/rates", true);
	req.send();
}
