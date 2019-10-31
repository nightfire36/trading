<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<link rel="stylesheet" href=
			"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
			integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" 
			crossorigin="anonymous">
	</head>
	<body>
		<div class="card text-center">
			<div class="card-header">
				<ul class="nav nav-tabs card-header-tabs">
					<li class="nav-item">
						<a class="nav-link" href="/user/description">Platform description</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/user/info">User info</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/user/opened_positions">Opened positions</a>
					</li>
					<li class="nav-item">
						<a class="nav-link active" href="/user/closed_positions">Closed positions</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/user/pending_orders">Pending orders</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="/user/trade">Trading panel</a>
					</li>
					<li class="nav-item ml-auto">
						<form action="/user/logout" method="POST">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							<input type="submit" value="Logout" class="btn btn-primary">
						</form>
					</li>
				</ul>
			</div>
			<div class="card-body mx-auto" style="width: 70rem;">
				<h5 class="card-title">Transactions history</h5>
				<p class="card-text">
					<table class="table table-striped table-sm">
						<thead>
							<tr>
								<th scope="col">Transaction ID</th>
								<th scope="col">Currency pair</th>
								<th scope="col">Amount</th>
								<th scope="col">Opening price</th>
								<th scope="col">Closing price</th>
								<th scope="col">Opening date</th>
								<th scope="col">Closing date</th>
								<th scope="col">Profit</th>
								<th scope="col">Position type</th>
							</tr>
						</thead>
						<tbody>
							<c:set var="total" value="0" />
							<c:forEach var="transaction" items="${closed}">
								<tr>
									<th scope="row">${transaction.tid}</th>
									<td>${transaction.currencyPair}</td>
									<td>${transaction.amount}</td>
									<td>${transaction.openingPrice}</td>
									<td>${transaction.closingPrice}</td>
									<td>${transaction.openingTimestamp}</td>
									<td>${transaction.closingTimestamp}</td>
									<c:choose>
										<c:when test="${transaction.profit > 0}">
											<td><font color="green">${transaction.profit}</font></td>
										</c:when>
										<c:when test="${transaction.profit < 0}">
											<td><font color="red">${transaction.profit}</font></td>
										</c:when>
										<c:otherwise>
											<td>${transaction.profit}</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${transaction.longPosition==true}">
											<td><font color="green">Long</font></td>
										</c:when>
										<c:when test="${transaction.longPosition==false}">
											<td><font color="red">Short</font></td>
										</c:when>
										<c:otherwise>
											<td>-</td>
										</c:otherwise>
									</c:choose>
								</tr>
								<c:set var="total" value="${total + transaction.profit}" />
							</c:forEach>
							<th scope="row"></th>
							<td></td><td></td><td></td><td></td><td></td>
							<td><b>Total</b></td><td><b>${total}</b></td><td></td>
						</tbody>
					</table>
				</p>
			</div>
		</div>
	</body>
</html>