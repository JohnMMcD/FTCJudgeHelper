import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/** See README.md for instructions. */
public class JudgeHelper {
    // Alternate version for Windows:
    // static String url = "jdbc:sqlite:C:/ftc.db";

    static String header_jmo = "Team\tMatch\tNavigated\tAuto Cones\tGame Play\tReliability\tStandout" + System.lineSeparator();
    static String header_pit = "Team\tName\tM1\tM2\tM3\tM4\tM5\tM6\tM7\tM8" + System.lineSeparator();
    static String red = "red";
    static String blue = "blue";
    static String both = "both";

    public static void main(String[] args) {
        if (args == null || args.length < 1 || args[0].trim().equals("")) {
            System.err.println("Usage: java -cp sqlite.jar:. JudgeHelper DATABASE_FILE_INCLUDING_EXTENSION");
            System.exit(1);
        }
        String us = null;
        try {
            File f = new File(args[0]);
            us = f.getAbsolutePath();
            System.out.println("Getting data from database file: " + us);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        // String url = "jdbc:sqlite:/Users/johnmcdonnell/FTC/Scorekeeper_Binary/FIRST-Tech-Challenge-Live-v4.2.6/db/usnjcmptpke_1.db";
        String url = "jdbc:sqlite:" + us;

        try {
            Connection con = DriverManager.getConnection(url);
            String pitFinder = showMatchesForSpreadsheet(con, false, both);
            writeStringToFile(pitFinder, "pitfinder_spreadsheet.tsv");

            // showMatchListRegular(con);

            ArrayList<String> colors = new ArrayList<>(Arrays.asList(both, red, blue));

            for (String color: colors) {
                String matchesForPaper = showMatchesForPaper(con, color);
                writeStringToFile(matchesForPaper, "jmo_paper_" + color + ".csv");

                String sMatches = showMatchesForSpreadsheet(con, true, color);
                writeStringToFile(sMatches, "jmo_spreadsheet_" + color + ".csv");
            }

            con.close();
        }
        catch (SQLException e) {
            System.err.printf("Error getting data from: %s%n", url);
            e.printStackTrace();
        }
    }

    private static void writeStringToFile(String sMatchList, String sFile){
        try {
            BufferedWriter bfwMatchList = new BufferedWriter(new FileWriter(sFile));
            bfwMatchList.write(sMatchList);
            bfwMatchList.close();
        }
        catch (IOException ioException) {
            System.err.printf("Could not write file %s !%n", sFile);
            ioException.printStackTrace();
        }
    }

    public static String showMatchesForSpreadsheet(Connection con, Boolean makeTable, String color) throws SQLException {
        StringBuilder sbResult = new StringBuilder();
        String selectTeamNumbers = "SELECT DISTINCT number FROM teams ORDER BY number";
        ArrayList<Integer> teamList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rsTeamList = stmt.executeQuery(selectTeamNumbers);
        while (rsTeamList.next()) {
            teamList.add(rsTeamList.getInt("number"));
        }
        sbResult.append(makeTable ? header_jmo : header_pit);

        for (Integer teamNumber: teamList) {
            String selectAllMatches = String.format(
                    "SELECT match, (SELECT TeamNameShort FROM Team WHERE TeamNumber = %d) " +
                            "FROM quals WHERE %d IN (red1, red2, blue1, blue2) ORDER BY match;",
                    teamNumber, teamNumber);
            String selectRedMatches = String.format(
                    "SELECT match, (SELECT TeamNameShort FROM Team WHERE TeamNumber = %d) " +
                            "FROM quals WHERE %d IN (red1, red2) ORDER BY match;",
                    teamNumber, teamNumber);
            String selectBlueMatches = String.format(
                    "SELECT match, (SELECT TeamNameShort FROM Team WHERE TeamNumber = %d) " +
                            "FROM quals WHERE %d IN (blue1, blue2) ORDER BY match;",
                    teamNumber, teamNumber);

            ResultSet rsGetMatches;
            if (color.equalsIgnoreCase(both)) {
                rsGetMatches = stmt.executeQuery(selectAllMatches);

            } else if (color.equalsIgnoreCase(red)) {
                rsGetMatches = stmt.executeQuery(selectRedMatches);

            } else if (color.equalsIgnoreCase(blue)) {
                rsGetMatches = stmt.executeQuery(selectBlueMatches);
            }
            else {
                throw new IllegalArgumentException("Bad color");
            }

            if (makeTable) {
                while (rsGetMatches.next()) {
                    sbResult.append(String.format("%d \t %d %n", teamNumber, rsTeamList.getInt("match")));
                }
            }
            else
            // Team-finder mode: print the matches the teams are in
            {
                sbResult.append(String.format("%d \t %s \t ", teamNumber, rsTeamList.getString(2)));
                StringBuilder sb = new StringBuilder();
                while (rsGetMatches.next()) {
                    sb.append(String.format("%d \t ", rsTeamList.getInt("match")));
                }
                sbResult.append(String.format("%s %n", sb.toString().replaceAll("\\s+$","")));
            }
        }
        return  sbResult.toString();
    }

    /** This shows the match list for the JMOs to use on paper. */
    public static String showMatchesForPaper(Connection con, String color) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append(header_jmo);
        int currentMatch = 0;
        String query = "SELECT match, red1, red2, blue1, blue2 FROM quals ORDER BY match";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            currentMatch++;
            int match = rs.getInt("match");
            int red1 = rs.getInt("red1");
            int red2 = rs.getInt("red2");
            int blue1 = rs.getInt("blue1");
            int blue2 = rs.getInt("blue2");
            if (color.equalsIgnoreCase(both)) {
                sb.append(match).append("\t").append(red1).append(System.lineSeparator());
                sb.append(match).append("\t").append(red2).append(System.lineSeparator());
                sb.append(match).append("\t").append(blue1).append(System.lineSeparator());
                sb.append(match).append("\t").append(blue2).append(System.lineSeparator());
            } else if (color.equalsIgnoreCase(red)) {
                sb.append(match).append("\t").append(red1).append(System.lineSeparator());
                sb.append(match).append("\t").append(red2).append(System.lineSeparator());
            } else if (color.equalsIgnoreCase(blue)) {
                sb.append(match).append("\t").append(blue1).append(System.lineSeparator());
                sb.append(match).append("\t").append(blue2).append(System.lineSeparator());
            }
        }
        return sb.toString();
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

}