// On a computer (desktop or laptop, not an iOS / Android device), connect to the event's
// scoring system and open the Event Info page for the current event. You do not need to authenticate.
// Open the Match Results Page. The URL should look like:
// http://SCORING_SYSTEM_IP_ADDRESS_HERE/event/YOUR_EVENT_CODE_HERE/results/
// Open the browser's Developer Tools using Control-Shift-I on Windows or Option-Command-I on Mac.
// Select the Console tab, if it's not already selected.
// Copy the entire script contents (Control-A, Control-C), and then paste the script into the Console.
// The first time you paste in the script, you will
// receive a scary warning about pasting in code from unknown sources. You will have to
// type something in the Console tab (usually "allow pasting")
// to indicate you know what you are doing.
// Press Enter, and the page should update to show the team numbers and the matches they are playing.

const showField = true; // set to false if you don't want to see the field numver

function getElementByXpath(path) {
  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
}

function clearTable() {
  const elem = getElementByXpath("//table");
  elem.remove();
}

function populateHeader(obj) {
  const matches = obj.matches;
  var mapMatches = new Map();
  for (const match of matches) {
    const currentMatchNumber = match.matchNumber;
    const fieldNum = match.field;
     const teams = new Array(match.red.team1, match.red.team2, match.blue.team1, match.blue.team2);
     for (const team of teams) {
         var matchesByTeam = (mapMatches.get(team) === undefined) ? [] : mapMatches.get(team);
         const data = currentMatchNumber + (showField ? "F" + fieldNum : "");
         // Add the current match number to the array
         matchesByTeam.push(data);
         // Update the map with the new array
         mapMatches.set(team, matchesByTeam);
     } // end loop over teams in a match
  } // end loop over matches
  // Sort the matches, then show them
  const mapSorted = new Map([...mapMatches.entries()].sort( (a, b) => parseInt(a) > parseInt(b) ? 1 : -1 ));
  writeMatches(mapSorted);
} // end populateHeader

function writeMatches(mapTeams) {
  const header = document.querySelector("small.col-12.text-center");
  var pageHeader = document.querySelector("h2.text-center.col-5");
  pageHeader.textContent = "Team Finder";
  var tb = document.createElement("table");
  var tbHeaderRow = document.createElement("tr");
  tbHeaderRow.style.border = '2px solid blue';
  var tbHeaderTeamNum = document.createElement("th");
  tbHeaderTeamNum.textContent = "Team Number";
  tbHeaderTeamNum.style.border = '1px solid red';
  var tbHeaderMatches = document.createElement("th");
  tbHeaderMatches.style.border = '1px solid red';
  tbHeaderMatches.textContent = "Matches";
  tbHeaderMatches.colSpan = "5";
  tbHeaderMatches.style.textAlign = "center";

  tbHeaderRow.appendChild(tbHeaderTeamNum);
  tbHeaderRow.appendChild(tbHeaderMatches);
  tb.appendChild(tbHeaderRow);

  for (const team of mapTeams) {
      var row = document.createElement("tr");
      row.style.borderBottom = '2px solid orange';
      const matchesPlayed = team[1];
      var teamNum = document.createElement("td");
      teamNum.style.border = '1px dotted orange';
      teamNum.textContent = team[0];
      row.appendChild(teamNum);
      for (const m of matchesPlayed) {
        var col = document.createElement("td");
        col.style.border = '1px dotted green';
        col.style.paddingLeft = '10px';
        col.style.paddingRight = '10px';
        col.textContent = m;
        row.appendChild(col);
      }
      tb.appendChild(row);
  }
  header.appendChild(tb);
}

async function populate() {
  const path = location.pathname;
  const eventCode = path.replace("/event/", "").replace("/results/", "");
  const requestURL = `http://${window.location.hostname}/api/v1/events/${eventCode}/matches/`;
  const request = new Request(requestURL);
  const response = await fetch(request);
  const matchDetails = await response.json();
  clearTable();
  populateHeader(matchDetails);
}

await populate();
