
// On a computer (desktop or laptop, not an iOS / Android device), connect to the
// scoring system and open the Match Schedule. The URL should look like:
// http://SCORING_SYSTEM_IP_ADDRESS_HERE/event/YOUR_EVENT_CODE_HERE/schedule/
// Open the browser's DevTools
// Copy the script below, and then paste the script into the console. The first time you do this, you will
// receive a scary warning about pasting in code from unknown sources. You will have to type something in
// the console window to indicate you know what you are doing.


function getElementByXpath(path) {
  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
}

// This map's key is the team number, and the value is an array of matches the team is playing in.
var mapMatches = new Map();

var table = getElementByXpath("(//table)[2]/tbody");
for (var i = 0, row; row = table.rows[i]; i++) {
   for (var j = 3, col; col = row.cells[j]; j++) {
      // Each cell holds the team number for a match, with a * at the end if the match is a surrogate.
      // Get the team number without the surrogate designator
      var team = col.textContent.replaceAll("*", "");
      // Get the array of the team's matches (if any)
      var matches = (mapMatches.get(team) === undefined) ? [] : mapMatches.get(team);

      // Prepare the surrogate designator
      var isSurrogate = (col.textContent.indexOf("*") > 0) ? "*": "";
      // Add the current match number to the array
      matches.push(row.cells[1].textContent.replaceAll("Qualification ", "") + isSurrogate);
      // Update the map with the new array
      mapMatches.set(team, matches);
   }
}

mapSorted = new Map([...mapMatches.entries()].sort( (a, b) => parseInt(a) > parseInt(b) ? 1 : -1 ));

var output = '\n';
function logMapElements(value, key, map) {
  output = output + `Team ${key} matches: ${value}` + '\n';
}
mapSorted.forEach(logMapElements);

console.log(output);

