import { generateChartForPair } from "./chartPanel.js";
import { generateNewPositionsPanel } from "./newPositionsPanel.js";
import { generateOpenedPositionsPanel } from "./openedPositionsPanel.js";
import { generatePendingOrdersPanel } from "./pendingOrdersPanel.js";

function reload() {
	generateNewPositionsPanel();
	generateOpenedPositionsPanel();
	generatePendingOrdersPanel();

	setTimeout(reload, 60*1000);
}

window.onload = function() {
	reload();
	generateChartForPair('EURUSD');
}
