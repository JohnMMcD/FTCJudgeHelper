import java.sql.*;
import java.util.ArrayList;

/** See README.md for instructions. */
public class JudgeHelper {
    // Alternate version for Windows:
    // static String url = "jdbc:sqlite:C:/ftc.db";
    static String url = "jdbc:sqlite:/Users/johnmcdonnell/FTC/Scorekeeper_Binary/FIRST-Tech-Challenge-Live-v4.2.6/db/usnjcmptpke_1.db";
    static String header = "Team\tMatch\tNavigated\tAuto Cones\tGame Play\tReliability\tStandout";

    public static void showMatches(Connection con, Boolean makeTable) throws SQLException {
        var selectTeamNumbers = "SELECT DISTINCT number FROM teams ORDER BY number";
        var teamList = new ArrayList<Integer>();
        Statement stmt = con.createStatement();
        ResultSet rsTeamList = stmt.executeQuery(selectTeamNumbers);
        while (rsTeamList.next()) {
            teamList.add(rsTeamList.getInt("number"));
        }

        if (makeTable) {
            System.out.println(header);
        }
        for (Integer teamNumber: teamList) {
            var selectMatches = String.format(
                    "SELECT match, (SELECT TeamNameShort FROM Team WHERE TeamNumber = %d) " +
                            "FROM quals WHERE %d IN (red1, red2, blue1, blue2) ORDER BY match;",
                    teamNumber, teamNumber);
            ResultSet rsGetMatches = stmt.executeQuery(selectMatches);

            if (makeTable) {
                while (rsGetMatches.next()) {
                    System.out.printf("%d \t %d %n", teamNumber, rsTeamList.getInt("match"));
                }
            }
            else
            // Team-finder mode: print the matches the teams are in
            {
                System.out.printf("%d \t %s \t ", teamNumber, rsTeamList.getString(2));
                var sb = new StringBuilder();
                while (rsGetMatches.next()) {
                    sb.append(String.format("%d \t ", rsTeamList.getInt("match")));
                }
                System.out.printf("%s %n", sb.toString().replaceAll("\\s+$",""));
            }
        }
    }


    /** This shows the match list in its usual form. */
    public static void showMatchListRegular(Connection con) throws SQLException {
        String query = "SELECT match, red1, red2, blue1, blue2 FROM quals ORDER BY match";
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