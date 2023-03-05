import java.sql.*;
import java.util.ArrayList;

/**
 * To use this, export the database using the "Export Divisions" button after the match list has been generated.
 * Then change the `url` to match the location where the file was exported.
 * You can also customize the `header` to what your Judge Match Observers want to evaluate.
 * Finally, compile and execute this class, redirecting the output to a file.
 * This will generate two sections, separated by some blank lines.
 * Copy each section into its own file.
 * For each section / file, open a new Excel workbook and use the 'Data' area of the Ribbon to import the file.
 * The first section can be used by your Judge Match Observers to note what the teams are doing on the field.
 * The second section can be used by judges doing pit interviews to help them determine the best time to interview teams.
 * */
public class Connect {
    // Alternate version for Windows:
    // static String url = "jdbc:sqlite:C:/ftc.db";
    static String url = "jdbc:sqlite:/Users/johnmcdonnell/FTC/Scorekeeper_Binary/FIRST-Tech-Challenge-Live-v4.2.6/db/usnjcmptpke_1.db";
    static String header = "Team|Match|Navigated|Cones Scored|Parked|Game Play|Reliability|Standout";

    public static void showMatches(Connection con, Boolean makeTable) throws SQLException {
        var selectTeamNumbers = "select distinct number from teams order by number";
        var teamList = new ArrayList<Integer>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(selectTeamNumbers);
        while (rs.next()) {
            teamList.add(rs.getInt("number"));
        }

        if (makeTable) {
            System.out.println(header);
        }
        for (Integer teamNumber: teamList) {
            var selectMatches = String.format(
                    "select match, (select TeamNameShort from Team where TeamNumber = %d) " +
                            "from quals where %d in (red1, red2, blue1, blue2);",
                    teamNumber, teamNumber);
            ResultSet rsGetMatches = stmt.executeQuery(selectMatches);

            if (makeTable) {
                while (rsGetMatches.next()) {
                    System.out.printf("%d | %d %n", teamNumber, rs.getInt("match"));
                }
            }
            else {
                System.out.printf("%d | %s | ", teamNumber, rs.getString(2));
                var sb = new StringBuilder();
                while (rsGetMatches.next()) {
                    sb.append(String.format("%d | ", rs.getInt("match")));
                }
                System.out.printf("%s %n", sb.toString().replaceAll("[|]\\s+$",""));
            }
        }
    }


    /** This just shows the match list in its usual form. */
    public static void showMatchListRegular(Connection con) throws SQLException {
        String query = "select match, red1, red2, blue1, blue2 from quals order by match";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int match = rs.getInt("match");
            int red1 = rs.getInt("red1");
            int red2 = rs.getInt("red2");
            int blue1 = rs.getInt("blue1");
            int blue2 = rs.getInt("blue2");
            System.out.println(match+ ": " + red1 + ", " + red2+
                    ", " + blue1 + ", " + blue2);
        }
    }

    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection(url);

            // showMatchListRegular(con);
            showMatches(con, true);
            System.out.printf("%n%n");
            showMatches(con, false);

            con.close();
        }
        catch (SQLException e) {
            System.err.printf("Error getting data from: %s%n", url);
            e.printStackTrace();
        }
    }
}