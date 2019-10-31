function reload()
{
	generateNewPositionsPanel();
	generateOpenedPositionsPanel();
	generatePendingOrdersPanel();

	setTimeout(reload, 60*1000);
}

window.onload = function()
{
	reload();
	generateChartForPair('EURUSD');
}