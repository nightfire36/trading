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
		<input type = "hidden" name = "${_csrf.parameterName}" id="csrfToken" value = "${_csrf.token}" />
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
						<a class="nav-link" href="/user/pending_orders">Pending orders</a>
					</li>
					<li class="nav-item">
						<a class="nav-link active" href="/user/trade">Trading panel</a>
					</li>
					<li class="nav-item ml-auto">
						<form action="/user/logout" method="POST">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							<input type="submit" value="Logout" class="btn btn-primary">
						</form>
					</li>
				</ul>
			</div>
			<div class="card-body">
				<h5 class="card-title">Trading panel</h5>
				<p class="card-text">
					<div class="container-fluid">
						<div class="row">
							<div class="col">
								<div class="card border-primary">
									<div class="card-header">Open new position</div>
									<p class="card-text">
										<p id = "new_positions">Loading content...</p>
									</p>
								</div>
							</div>
							<div class="col">
								<div class="row">
									<div class="card border-secondary">
										<div class="card-header">Chart</div>
										<p class="card-text">
											<p id = "chart">Loading content...</p>
										</p>
									</div>
								</div>
								<br />
								<div class="row">
									<div class="card border-secondary">
										<div class="card-header">Opened positions</div>
										<p class="card-text">
											<p id = "opened_positions">Loading content...</p>
										</p>
									</div>
								</div>
								<br />
								<div class="row">
									<div class="card border-secondary">
										<div class="card-header">Pending orders</div>
										<p class="card-text">
											<p id = "pending_orders">Loading content...</p>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</p>
			</div>
		</div>
	</body>
	<script src = "..\scripts\chartPanel.js"></script>
	<script src = "..\scripts\newPositionsPanel.js"></script>
	<script src = "..\scripts\openedPositionsPanel.js"></script>
	<script src = "..\scripts\pendingOrdersPanel.js"></script>
	<script src = "..\scripts\tradeOnload.js"></script>
</html>