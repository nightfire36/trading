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
						<a class="nav-link" href="/user/closed_positions">Closed positions</a>
					</li>
					<li class="nav-item">
						<a class="nav-link active" href="/user/pending_orders">Pending orders</a>
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
				<h5 class="card-title">Pending orders</h5>
				<p class="card-text">
					<table class="table table-striped table-sm">
						<thead>
							<tr>
								<th scope="col">Transaction ID</th>
								<th scope="col">Currency pair</th>
								<th scope="col">Amount</th>
								<th scope="col">Order price</th>
								<th scope="col">Order date</th>
								<th scope="col">Position type</th>
								<th scope="col">Triggered</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="order" items="${pending}">
								<tr>
									<c:choose>
										<c:when test="${order.tid != null}">
											<th scope="row">${order.tid}</th>
										</c:when>
										<c:otherwise>
											<th scope="row">-</th>
										</c:otherwise>
									</c:choose>
									<td>${order.currencyPair}</td>
									<c:choose>
										<c:when test="${order.amount != null}">
											<td>${order.amount}</td>
										</c:when>
										<c:otherwise>
											<td>-</td>
										</c:otherwise>
									</c:choose>
									<td>${order.orderPrice}</td>
									<td>${order.orderTimestamp}</td>
									<c:choose>
										<c:when test="${order.longPosition==true}">
											<td><font color="green">Long</font></td>
										</c:when>
										<c:when test="${order.longPosition==false}">
											<td><font color="red">Short</font></td>
										</c:when>
										<c:otherwise>
											<td>-</td>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${order.triggeredAbove==true}">
											<td><font color="green">Above</font></td>
										</c:when>
										<c:when test="${order.triggeredAbove==false}">
											<td><font color="red">Below</font></td>
										</c:when>
										<c:otherwise>
											<td>-</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</p>
			</div>
		</div>
	<br />
	<br />
	</body>
</html>