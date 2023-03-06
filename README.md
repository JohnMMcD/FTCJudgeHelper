# FTCJudgeHelper
This generates files which can help judges at events. It is not supported by or affiliated with FIRST.

# Prerequisites
These instructions assume:
* some familiarity with the FIRST Tech Challenge scoring system
* some familiarity with programming software in Java and operating an IDE (the author used IntelliJ)
* some familiarity with importing delimited data into spreadsheet programs

# Setup Instructions
Clone the repository and open with your IDE (the author used IntelliJ). Configure your IDE to compile using Java 8 or higher.

If you are proficient using the JDK, you can copy the `src/main/java/Connect.java` file, add a sqlite driver to the current directory, compile the class, then run it with:
    java -cp sqlite.jar:. JudgeHelper DATABASE_FILE_INCLUDING_EXTENSION

Well before the event, you should download the FIRST Tech Challenge scoring system locally, do a manual event setup 
with fake teams in offline mode, and go through the steps below to make sure you can do them correctly on the day of the event.

# Day of Event Instructions
* In the FIRST Tech Challenge Scoring system, import the teams and follow the normal steps to generate the match list.
* Optionally, customize the `header` to what your Judge Match Observers want to evaluate.
* After the match list has been generated, export the database using the "Export Divisions" button.
* Copy the database files to a known location, such as the same directory as the cloned repository.
* Add a command-line parameter with the name of the database file.
* Compile and execute `JudgeHelper.java` from within your IDE. 
* Executing the JudgeHelper file will generate several files. 
* For each file (see below for what file does what):
  * open a new spreadsheet
  * Excel users should use the 'Data' area of the Ribbon to import the file. Mac users, launch Numbers, then drag the file from Finder onto the Numbers icon in the Dock.
  * Save the file in the native spreadsheet format.
  * For '*paper* files, adjust the column and row sizes, set printing area, and print the files.
 
The jmo_* files can be used by your Judge Match Observers to note what the teams are doing on the field. The '*paper' files
are designed to be printed, while the '*spreadsheet*' files are designed for direct data entry.

For direct data entry, the Judge Match Observers should use the Filter function in the spreadsheet to show only 
the current match, and change the Filter after each match. Remind them to save the file every once in a while.

The 'pitfinder' file shows the matches which each team will be playing in; this can be used by judges doing pit 
interviews to help them determine the best time to interview a team.
