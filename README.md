# FTCJudgeHelper
Generates files which can help judges at events. Not supported by or affiliated with FIRST.

# Setup Instructions
Clone the repository and open with your IDE (the author used IntelliJ). Configure your IDE to compile using Java 17.

Alternatively, copy the `src/main/java/Connect.java` file and add a sqlite driver to your classpath.

Well before the event, you should download the FIRST Tech Challenge scoring system locally, create an offline event 
with fake teams, and go through the steps below to make you can get it working.

# Day of Event Instructions
* In the FIRST Tech Challenge Scoring system, import the teams and follow the normal steps to generate the match list.
* Export the database using the "Export Divisions" button after the match list has been generated.
* Then change the `url` in `JudgeHelper.java` to match the location where the database file(s) export location.
* Optionally, customize the `header` to what your Judge Match Observers want to evaluate.
* Compile and execute `JudgeHelper.java` from within your IDE, redirecting the output to a file.
* Executing the JudgeHelper file will generate two sections, separated by some blank lines.
* Copy each section into its own file.
* For each section / file, open a new Excel workbook and use the 'Data' area of the Ribbon to import the file.
* The first section can be used by your Judge Match Observers to note what the teams are doing on the field.
* The second section can be used by judges doing pit interviews to help them determine the best time to interview teams.
