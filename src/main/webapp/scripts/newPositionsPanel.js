var openedNPForm = null;

function orderLong(currencyPair, amount, price, trigger)
{
	console.log("orderLong", currencyPair, amount, price, trigger);

	var req = new XMLHttpRequest();

	req.open("POST", "/api/order_long/" + currencyPair + "/" + amount + "/" + 
		trigger + "/" + price + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("long order set");
			generatePendingOrdersPanel();
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("long order setting failure");
		}
	}

	return 0;
}

function orderShort(currencyPair, amount, price, trigger)
{
	console.log("orderShort", currencyPair, amount, price, trigger);
	
	var req = new XMLHttpRequest();

	req.open("POST", "/api/order_short/" + currencyPair + "/" + amount + "/" + 
		trigger + "/" + price + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("short order set");
			generatePendingOrdersPanel();
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("short order setting failure");
		}
	}

	return 0;
}

function longPosition(currencyPair, amount)
{
	console.log("longPosition", currencyPair, amount);

	var req = new XMLHttpRequest();
	
	req.open("POST", "/api/long/" + currencyPair + "/" + amount + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("long position opened");
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("long position opening failure");
		}
	}
}

function shortPosition(currencyPair, amount)
{
	console.log("shortPosition", currencyPair, amount);

	var req = new XMLHttpRequest();
	
	req.open("POST", "/api/short/" + currencyPair + "/" + amount + "/", true);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("short position opened");
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("short position opening failure");
		}
	}
}

function closeNPForm()
{
	document.getElementById(openedNPForm + "_opn_form").innerHTML = "";

	document.getElementById(openedNPForm + "_opn_button").setAttribute("onClick", "openNPForm('" + 
		openedNPForm + 
		"')");
	document.getElementById(openedNPForm + "_opn_button").innerHTML = "New position";
	document.getElementById(openedNPForm + "_opn_button").className = "btn btn-outline-primary btn-sm";

	openedNPForm = null;
	return 0;
}

function openNPForm(currencyPair)
{
	if(openedNPForm != null)
	{
		closeNPForm();
	}

	openedNPForm = currencyPair;

	var open = "<td colspan=\"5\">" + 
	"<div class=\"form-row justify-content-center\" style=\"width: 32rem;\">\n" +
	"<div class=\"form-group col-md-6\">\n" +
	"<label for=\"amount\"><b>Amount</b></label>\n" +
	"<input type=\"text\" class=\"form-control\" placeholder=\"Enter amount\" name=\"amount\" id=\"amount\">\n" +
	"<button type=\"button\" class=\"btn btn-success btn-sm\" onclick=\"longPosition('" +
	currencyPair + 
	"', document.getElementById('amount').value" +
	")\">Buy</button>\n" + 
	"<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"shortPosition('" +
	currencyPair + 
	"', document.getElementById('amount').value" +
	")\">Sell</button>\n" + 
	"</div>" + 
	"<div class=\"form-group col-md-6\">\n" +
	"<label for=\"price\"><b>Price</b></label>" +
	"<input type=\"text\" class=\"form-control\" placeholder=\"Enter price\" name=\"price\" id=\"price\">" +
	"<div class=\"form-check\">" +
	"<input class=\"form-check-input\" type=\"radio\" name=\"trigger\" id=\"trigger\" value=\"above\" checked>" +
	"<label class=\"form-check-label\" for=\"trigger\">above</label>" +
	"</div>" +
	"<div class=\"form-check\">" +
	"<input class=\"form-check-input\" type=\"radio\" name=\"trigger\" id=\"trigger\" value=\"below\">" +
	"<label class=\"form-check-label\" for=\"trigger\">below</label>" +
	"</div>" +
	"<button type=\"button\" class=\"btn btn-success btn-sm\" onclick=\"orderLong('" +
	currencyPair + 
	"', document.getElementById('amount').value, document.getElementById('price').value, " + 
	"document.getElementsByName('trigger')[0].checked" +
	")\">Order Buy</button>\n" + 
	"<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"orderShort('" +
	currencyPair + 
	"', document.getElementById('amount').value, document.getElementById('price').value, " + 
	"document.getElementsByName('trigger')[0].checked" +
	")\">Order Sell</button>" + 
	"</div></div>";

	console.log(open);

	document.getElementById(currencyPair + "_opn_form").innerHTML = open;

	document.getElementById(currencyPair + "_opn_button").setAttribute("onClick", "closeNPForm()");
	document.getElementById(currencyPair + "_opn_button").innerHTML = "Close form";
	document.getElementById(currencyPair + "_opn_button").className = "btn btn-outline-secondary btn-sm";

	generateChartForPair(currencyPair);

	return 0;
}

function buildNewPositionsRow(pair)
{
	var date = new Date();
	date.setTime(pair.timestamp);

	var row = "<tr><th scope=\"row\">" + 
		pair.currencyPair + "</th><td>" + 
		pair.ask + "</td><td>" + 
		pair.bid + "</td><td>" + 
		date.toLocaleString() + "</td><td>" + 
		"<button type=\"button\" id=\"" +
		pair.currencyPair + 
		"_opn_button\" class=\"btn btn-outline-primary btn-sm\" onclick=\"openNPForm('" + 
		pair.currencyPair + 
		"')\">New position</button>\n" + 
		"</td></tr>\n" + 
		"<tr class=\"form-popup\" id=\"" + 
		pair.currencyPair +
		"_opn_form\"></tr>";

	return row;
}

function buildNewPositionsTable(json)
{
	var tableCode = "<table class=\"table table-sm\"><thead><tr>" + 
		"<th scope=\"col\">Currency Pair</th>" + 
		"<th scope=\"col\">Ask price</th>" + 
		"<th scope=\"col\">Bid price</th>" + 
		"<th scope=\"col\">Timestamp</th>" + 
		"<th scope=\"col\">New position</th>" + 
		"</tr></thead>\n" + 
		"<tbody>";
	
	for(i in json)
	{
		tableCode += buildNewPositionsRow(json[i]);
	}
	
	tableCode += "</tbody></table>";

	console.log(tableCode);

	return tableCode;
}

function generateNewPositionsPanel()
{
	var req = new XMLHttpRequest();
	
	req.open("GET", "/api/rates", true);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("rates received successfully");
			var jsonRates = JSON.parse(req.responseText);
			document.getElementById("new_positions").innerHTML = buildNewPositionsTable(jsonRates);

			if(openedNPForm != null)
			{
				openNPForm(openedNPForm);
			}

		}
		else
		{
			console.log("rates receive failure");
		}
	}
}