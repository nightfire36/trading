var openedOPForm = null;

function orderClosure(tid, price, trigger)
{
	console.log("orderClosure", tid, price, trigger);

	var req = new XMLHttpRequest();

	req.open("POST", "/api/order_closure/" + tid + "/" + trigger + "/" + price + "/");
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("order closure");
			if(openedOPForm != null)
			{
				closeOPForm();
			}
			generatePendingOrdersPanel();
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("order closure failure");
		}
	}

	return 0;
}

function closePosition(tid)
{
	console.log("closePosition", tid);

	var req = new XMLHttpRequest();

	req.open("POST", "/api/close/" + tid + "/");
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function()
	{
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200)
		{
			console.log("position closed");
			if(openedOPForm != null)
			{
				closeOPForm();
			}
			generateOpenedPositionsPanel();
		}
		else
		{
			console.log("position closing failure");
		}
	}

	return 0;
}

function closeOPForm()
{
	document.getElementById(openedOPForm + "_op_form").innerHTML = "";

	document.getElementById(openedOPForm + "_op_button").setAttribute("onClick", "openOpenedPositionsForm('" + 
		openedOPForm + 
		"')");
	document.getElementById(openedOPForm + "_op_button").innerHTML = "Action";
	document.getElementById(openedOPForm + "_op_button").className = "btn btn-outline-primary btn-sm";

	openedOPForm = null;
	return 0;
}

function openOpenedPositionsForm(tid)
{
	if(openedOPForm != null)
	{
		closeOPForm();
	}

	openedOPForm = tid;

	var open = "<td colspan=\"7\">" + 

	"<div class=\"form-row justify-content-center\" style=\"width: 32rem;\">\n" +
	"<div class=\"form-group col-md-6\">\n" +
	"<div><label for=\"button\"><b>Close position</b></label></div>\n" +
	"<div><button type=\"button\" class=\"btn btn-primary btn-lg\" onclick=\"closePosition(" +
	tid + 
	")\">Close position</button></div>\n" + 
	"</div>" + 
	"<div class=\"form-group col-md-6\">\n" +
	"<label for=\"cp_price\"><b>Price</b></label>" +
	"<input type=\"text\" class=\"form-control\" placeholder=\"Enter price\" name=\"cp_price\" id=\"cp_price\">" +
	"<div class=\"form-check\">" +
	"<input class=\"form-check-input\" type=\"radio\" name=\"cp_trigger\" id=\"cp_trigger\" value=\"above\" checked>" +
	"<label class=\"form-check-label\" for=\"cp_trigger\">above</label>" +
	"</div>" +
	"<div class=\"form-check\">" +
	"<input class=\"form-check-input\" type=\"radio\" name=\"cp_trigger\" id=\"cp_trigger\" value=\"below\">" +
	"<label class=\"form-check-label\" for=\"cp_trigger\">below</label>" +
	"</div>" +
	"<button type=\"button\" class=\"btn btn-primary btn-sm\" onclick=\"orderClosure(" +
	tid + 
	", document.getElementById('cp_price').value, " + 
	"document.getElementsByName('cp_trigger')[0].checked" +
	")\">Order closure</button>\n" + 
	"</div></div>" + 
	"</td>";

	console.log(open);

	document.getElementById(tid + "_op_form").innerHTML = open;

	document.getElementById(tid + "_op_button").setAttribute("onClick", "closeOPForm()");
	document.getElementById(tid + "_op_button").innerHTML = "Close form";
	document.getElementById(tid + "_op_button").className = "btn btn-outline-secondary btn-sm";

	return 0;
}

function buildOpenedPositionsRow(position)
{
	var date = new Date();
	date.setTime(position.openingTimestamp);

	var longPositionStr, currentProfitStr;

	if(position.longPosition)
	{
		longPositionStr = "<font color=\"green\">Long</font>";
	}
	else
	{
		longPositionStr = "<font color=\"red\">Short</font>";
	}

	if(position.currentProfit > 0)
	{
		currentProfitStr = "<font color=\"green\">" + position.currentProfit + "</font>";
	}
	else if(position.currentProfit < 0)
	{
		currentProfitStr = "<font color=\"red\">" + position.currentProfit + "</font>";
	}
	else
	{
		currentProfitStr = position.currentProfit;
	}

	var row = "<tr><th scope=\"row\">" + 
		position.tid + "</th><td>" + 
		position.currencyPair + "</td><td>" + 
		position.amount + "</td><td>" + 
		position.openingPrice + "</td><td>" + 
		date.toLocaleString() + "</td><td>" + 
		longPositionStr + "</td><td>" + 
		currentProfitStr + "</td><td>" +
		"<button type=\"button\" id=\"" + 
		position.tid +
		"_op_button\" class=\"btn btn-outline-primary btn-sm\" onclick=\"openOpenedPositionsForm(" + 
		position.tid + 
		")\">Action</button>\n" + 
		"</td></tr>\n" + 
		"<tr class=\"form-popup\" id=\"" + 
		position.tid +
		"_op_form\"></tr>";

	return row;
}

function buildOpenedPositionsPanel(json)
{
	var tableCode = "<table class=\"table table-sm\"><thead><tr>" + 
		"<th scope=\"col\">ID</th>" + 
		"<th scope=\"col\">Currency pair</th>" + 
		"<th scope=\"col\">Amount</th>" + 
		"<th scope=\"col\">Opening price</th>" + 
		"<th scope=\"col\">Opening timestamp</th>" + 
		"<th scope=\"col\">Position type</th>" + 
		"<th scope=\"col\">Current profit</th>" + 
		"<th scope=\"col\">Close position</th>" + 
		"</tr></thead>\n" + 
		"<tbody>";
	
	for(i in json)
	{
		tableCode += buildOpenedPositionsRow(json[i]);
	}
	
	tableCode += "</tbody></table>";

	console.log(tableCode);

	return tableCode;
}

function openedPositionsListener()
{
	console.log(this.status);
	if(this.readyState == 4 && this.status == 200)
	{
		console.log("opened positions received successfully");
		if(this.responseText[0] == '[')
		{
			var jsonPositions = JSON.parse(this.responseText);
			document.getElementById("opened_positions").innerHTML = buildOpenedPositionsPanel(jsonPositions);

			if(openedOPForm != null)
			{
				openOpenedPositionsForm(openedOPForm);
			}
		}
		

	}
	else
	{
		console.log("opened positions receive failure");
	}
}

function generateOpenedPositionsPanel()
{
	var req = new XMLHttpRequest();
	
	req.addEventListener("load", openedPositionsListener);
	req.open("GET", "/api/opened_positions", true);
	req.send();
}