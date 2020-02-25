function cancelOrder(oid) {
	console.log("cancelOrder", oid);

	var req = new XMLHttpRequest();

	req.open("POST", "/api/cancel/" + oid);
	req.setRequestHeader("x-csrf-token", document.getElementById('csrfToken').value);
	req.send(null);
	
	req.onreadystatechange = function() {
		console.log(this.status);
		if(this.readyState == 4 && this.status == 200) {
			console.log("order cancelled");
			generatePendingOrdersPanel();
		}
		else {
			console.log("order cancellation failure");
		}
	}
}

function buildPendingOrdersRow(order) {
	var date = new Date();
	date.setTime(order.orderTimestamp);
	
	var longPositionStr, triggeredAboveStr, transactionId, amount;

	if(order.longPosition) {
		longPositionStr = `<font color="green">Long</font>`;
	}
	else {
		longPositionStr = `<font color="red">Short</font>`;
	}

	if(order.triggeredAbove) {
		triggeredAboveStr = `<font color="green">Above</font>`;
	}
	else {
		triggeredAboveStr = `<font color="red">Below</font>`;
	}

	if(order.tid) {
		transactionId = order.tid;
	}
	else {
		transactionId = "-";
	}

	if(order.amount) {
		amount = order.amount;
	}
	else {
		amount = "-";
	}

	var row = `
		<tr>
			<th scope="row">${transactionId}</th>
			<td>${order.currencyPair}</td>
			<td>${amount}</td>
			<td>${order.orderPrice}</td>
			<td>${date.toLocaleString()}</td>
			<td>${longPositionStr}</td>
			<td>${triggeredAboveStr}</td>
			<td>
				<button type="button" class="btn btn-outline-primary btn-sm" id="cancel_order_button">
					Cancel order
				</button>
			</td>
		</tr>`;
	
	return row;
}

function buildPendingOrdersPanel(json) {

	var tableCode = '';

	for(var i in json) {
		tableCode += buildPendingOrdersRow(json[i]);
	}

	var htmlCode = `
		<table class="table table-sm">
			<thead>
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Currency pair</th>
					<th scope="col">Amount</th>
					<th scope="col">Order Price</th>
					<th scope="col">Order timestamp</th>
					<th scope="col">Position type</th>
					<th scope="col">Triggered</th>
					<th scope="col">Cancel order</th>
				</tr>
			</thead>
			<tbody>
				${tableCode}
			</tbody>
		</table>`;

	return htmlCode;
}

function pendingOrdersListener() {
	console.log(this.status);
	if(this.readyState == 4 && this.status == 200) {
		console.log("pending orders received successfully");
		if(this.responseText[0] == '[') {
			var jsonOrders = JSON.parse(this.responseText);
			document.getElementById("pending_orders").innerHTML = buildPendingOrdersPanel(jsonOrders);

			for(var i in jsonOrders) {
				document.getElementById("cancel_order_button").onclick = function() {
					cancelOrder(jsonOrders[i].oid);
				};
			}
		}
	}
	else {
		console.log("pending orders receive failure");
	}
}

export function generatePendingOrdersPanel() {
	var req = new XMLHttpRequest();
	
	req.addEventListener("load", pendingOrdersListener);
	req.open("GET", "/api/pending_orders", true);
	req.send();
}
