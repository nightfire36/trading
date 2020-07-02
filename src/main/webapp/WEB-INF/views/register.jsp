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
		<br />
		<div class="card mx-auto" style="width: 24rem;">
			<h5 class="card-title mx-auto">Create new account</h5>
			<div class="card-body">
				<c:choose>
					<c:when test="${message == 1}">
						<font color="red">Registration failed! Account with provided <br /> e-mail address already exists.</font>
					</c:when>
					<c:when test="${message == 2}">
						<font color="red">Registration failed!</font>
					</c:when>
				</c:choose>
				<form action = "register" method = "POST">
					<div class="form-group">
						<label for="first_name">First name</label>
						<input type="text" class="form-control" name="first_name" id="first_name" placeholder="First name">
						<c:choose>
							<c:when test="${first_name_help != null}">
								<small id="firstNameHelp" class="form-text text-muted"><font color="red">${first_name_help}</font></small>
							</c:when>
							<c:otherwise>
								<small id="firstNameHelp" class="form-text text-muted">Minimum 3 letters.</small>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="form-group">
						<label for="lastName">Last name</label>
						<input type="text" class="form-control" name="last_name" id="last_name" aria-describedby="lastNameHelp" placeholder="Last name">
						<c:choose>
							<c:when test="${last_name_help != null}">
								<small id="firstNameHelp" class="form-text text-muted"><font color="red">${last_name_help}</font></small>
							</c:when>
							<c:otherwise>
								<small id="lastNameHelp" class="form-text text-muted">Minimum 3 letters.</small>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="form-group">
						<label for="exampleInputEmail1">Email address</label>
						<input type="email" class="form-control" name="email" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
						<c:choose>
							<c:when test="${email_help != null}">
								<small id="firstNameHelp" class="form-text text-muted"><font color="red">${email_help}</font></small>
							</c:when>
							<c:otherwise>
								<small id="emailHelp" class="form-text text-muted">Must be valid e-mail address.</small>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="form-group">
						<label for="exampleInputPassword1">Password</label>
						<input type="password" class="form-control" name="password" id="exampleInputPassword1" aria-describedby="passwordHelp" placeholder="Password">
						<c:choose>
							<c:when test="${password_help != null}">
								<small id="firstNameHelp" class="form-text text-muted"><font color="red">${password_help}</font></small>
							</c:when>
							<c:otherwise>
								<small id="passwordHelp" class="form-text text-muted">Must consist of minimum 6 signs.</small>
							</c:otherwise>
						</c:choose>
					</div>
					<input type = "hidden" name = "${_csrf.parameterName}" value = "${_csrf.token}" />
					<div class="text-center">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
					<div class="text-center">
						Already have an account? <a href="login">Sign in</a>
					</div>
				</form>
			</div>
		</div>
    </body>
</html>