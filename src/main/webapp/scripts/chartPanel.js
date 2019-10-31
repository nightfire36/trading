var currentPairName = null;

function generateChart(pairId, timescale)
{
	var chartCode = "<div><iframe height=\"285\" width=\"630\" " +
		"src=\"https://sslcharts.forexprostools.com/index.php?force_lang=15&pair_ID=" +
		pairId +
		"&timescale=" +
		timescale + 
		"&candles=100&style=line\"></iframe></div>";

	console.log(chartCode);
	document.getElementById("chart").innerHTML = chartCode;
}

function generateChartForPair(pairName)
{
	var pairId = null;

	if(currentPairName == pairName)
	{
		return 0;
	}

	switch(pairName)
	{
		case 'AUDCAD':
			pairId = 47;
			break;
		case 'AUDCHF':
			pairId = 48;
			break;
		case 'AUDJPY':
			pairId = 49;
			break;
		case 'AUDUSD':
			pairId = 5;
			break;
		case 'CADJPY':
			pairId = 51;
			break;
		case 'CHFPLN':
			pairId = 86;
			break;
		case 'EURAUD':
			pairId = 15;
			break;
		case 'EURCAD':
			pairId = 16;
			break;
		case 'EURCHF':
			pairId = 10;
			break;
		case 'EURGBP':
			pairId = 6;
			break;
		case 'EURJPY':
			pairId = 9;
			break;
		case 'EURPLN':
			pairId = 46;
			break;
		case 'EURUSD':
			pairId = 1;
			break;
		case 'GBPAUD':
			pairId = 53;
			break;
		case 'GBPCAD':
			pairId = 54;
			break;
		case 'GBPCHF':
			pairId = 12;
			break;
		case 'GBPJPY':
			pairId = 11;
			break;
		case 'GBPPLN':
			pairId = 89;
			break;
		case 'GBPUSD':
			pairId = 2;
			break;
		case 'USDCAD':
			pairId = 7;
			break;
		case 'USDCHF':
			pairId = 4;
			break;
		case 'USDJPY':
			pairId = 3;
			break;
		case 'USDPLN':
			pairId = 40;
			break;
		default:
			break;
	}

	if(pairId != null)
	{
		generateChart(pairId, 3600);
		currentPairName = pairName;
	}
}
