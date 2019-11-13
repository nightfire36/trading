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
		<br />
		<br />
		<div class="card mx-auto" style="width: 36rem;">
			<h5 class="card-title mx-auto">Welcome on the webpage</h5>
			<div class="card-body">
			This trading platform was designed for Forex learning purposes. If you don't have an account 
			you need to <a href="register">create one for free</a>. It is necessary for maintaining continuity of your transactions 
			history and account balance.
			</div>
		</div>
		<br />
		<div class="card mx-auto" style="width: 24rem;">
			<h5 class="card-title mx-auto">Login</h5>
			<div class="card-body">
				${message}
				<form action = "login" method = "POST">
					<div class="form-group">
						<label for="exampleInputEmail1">Email address</label>
						<input type="email" class="form-control" name="username" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
						<small id="emailHelp" class="form-text text-muted">Enter your e-mail address.</small>
					</div>
					<div class="form-group">
						<label for="exampleInputPassword1">Password</label>
						<input type="password" class="form-control" name="password" id="exampleInputPassword1" placeholder="Password">
					</div>
					<input type = "hidden" name = "${_csrf.parameterName}" value = "${_csrf.token}" />
					<div class="text-center">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
					<div class="text-center">
						Don't have an account? <a href="register">Create for free.</a>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>