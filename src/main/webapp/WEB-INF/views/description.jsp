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
						<a class="nav-link active" href="/user/description">Platform description</a>
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
			<div class="card-body">
				<h5 class="card-title">Introduction to trading platform</h5>
				<p class="card-text">
					<div>
						<p class="text-left">
							This trading platform was developed for learning purposes. It enables you to learn how to 
							invest in currency pairs, test your strategies and analyse achieved results. <br />
							Main menu of the platform consists of six tabs: <br />
						</p>
						<p class="text-center"><img src="..\images\panel.png" height="51" width="808"></p><br />
						<p class="text-left">
							<b>User info</b> tab contains information about currently logged in user.<br />
							In <b>Opened positions</b> tab information about opened positions are displayed.<br />
							<b>Closed positions</b> tab contains list of positions that were already closed. It is kind of historical 
							review of transactions carried out. There is also included information about achieved profit or loss.<br />
							In <b>Pending orders</b> tab list of orders pending for execution is displayed. These orders are being 
							executed when certain condition is met. The condition is to achieve a specified market 
							currency pair rate.<br />
							<b>Trading panel</b> is a heart of the platform. In this tab all the operations are carried out. 
							On the left side of the <b>trading panel</b> there is <b>Open new position</b> card:<br />
						</p>
						<p class="text-center"><img src="..\images\newPosition.png" height="420" width="608"></p><br />
						<p class="text-left">
							In the card, list of all available currency pairs to open position is displayed, 
							together with their present exchange rate. After clicking on the <b>New position</b>
							button, selected row expands and menu which allows to open new position appears.
							Opening new position can be done in two ways: 
							<ul>
								<li><p class="text-left">Immediately - by typing desired amount and clicking Buy/Sell button. In this 
								case new position for provided amount of money and for specified currency pair is taken 
								immediately at present exchange rate. Difference between 'Buy' and 'Sell' depends on 
								type of position taken. If we <b>buy</b> currency pair or in other words we take 
								<b>long position</b> we play for rise of particular currency pair rate. If we <b>sell</b> 
								currency pair or in other words take <b>short position</b> we expect decline of the rate 
								and make profit when it falls. </p></li>
								<li><p class="text-left">Conditionally - by typing desired amount and desired price at which position 
								should be opened. In this case new pending order is created and it is being conditionally executed 
								when market currency pair reaches indicated level. By choosing 'above' or 'below' option we point 
								whether order should be executed above or below indicated price.</p></li>
							</ul>
							<p class="text-left">In the top right corner of the panel is exchange rate chart for the currency pair 
							currently expanded in <b>New positions</b> card.
							Below it there is <b>Opened positions</b> card:</p>
						</p>
						<p class="text-center"><img src="..\images\openedPositions.png" height="452" width="634"></p><br />
						<p class="text-left">
							The card contains list of all currently opened positions and their details such as amount, 
							opening date, price and information whether it is long or short position (see explanation above).
							After clicking on 'Action' button, row expands and menu with available options appears.
							First option is to close position immediately by clicking <b>Close position</b> button. In 
							this case position will be closed at current market rate.
							Second option is to indicate at which rate position should be closed. In this case position will 
							appear amongst pending orders as a position to be closed when the market rate achieves specified level.
							There are also options 'above' and 'below' that determine if position is to be closed above or below 
							specified rate. <br />
							On the bottom of the right side of the panel there is <b>Pending orders</b> card:<br />
						</p>
						<p class="text-center"><img src="..\images\pendingOrders.png" height="354" width="634"></p><br />
						<p class="text-left">
							In this card all orders pending for execution are listed. These orders are being executed when market 
							currency pair rate achieves specified level. Mechanism of defining pending orders was explained above,  
							during description of 'Open new position' and 'Opened positions' cards. Each pending order can be 
							cancelled by clicking <b>Cancel order</b> button. <br />
							Note that some pending orders have empty 'ID' column while others have empty 'Amount' column: those 
							with empty 'ID' column are orders pending for being open and those with empty 'Amount' column are 
							opened positions which are pending for being closed. <br />
							<br />
							Have fun while using this trading platform and <b>good luck!</b> <br />
							<br/>
							<br />
						</p>
					</div>
				</p>
			</div>
		</div>
	</body>
</html>