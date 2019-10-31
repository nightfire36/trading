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
						<a class="nav-link active" href="/user/info">User info</a>
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
			<div class="card-body mx-auto" style="width: 38rem;">
				<h5 class="card-title">Informations about current user</h5>
				<p class="card-text">
					<table class="table table-striped">
						<tbody>
							<tr>
								<th scope="row">First name</th>
								<td>${userinfo.firstName}</td>
							</tr>
							<tr>
								<th scope="row">Last name</th>
								<td>${userinfo.lastName}</td>
							</tr>
							<tr>
								<th scope="row">E-mail</th>
								<td>${userinfo.email}</td>
							</tr>
							<tr>
								<th scope="row">Date of creation</th>
								<td>${userinfo.createdAt}</td>
							</tr>
							<tr>
								<th scope="row">Account balance</th>
								<td>${userinfo.accountBalance} PLN</td>
							</tr>
						</tbody>
					</table>
				</p>
			</div>
		</div>
		<br />
		<br />
	</body>
</html>