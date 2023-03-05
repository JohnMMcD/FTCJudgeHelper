import java.sql.*;
import java.util.ArrayList;

/** See README.md for instructions. */
public class JudgeHelper {
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