# FTCJudgeHelper
Generates files which can help judges at events. Not supported by or affiliated with FIRST.

# Setup Instructions
Clone the repository and open with your IDE (the author used IntelliJ). Configure your IDE to compile using Java 17.

Alternatively, copy the `src/main/java/Connect.java` file and add a sqlite driver to your classpath.

Well before the event, you should download the FIRST Tech Challenge scoring system locally, create an offline event 
with fake teams, and go through the steps below to make sure you can do them correctly on the day of the event.

# Day of Event Instructions
* In the FIRST Tech Challenge Scoring system, import the teams and follow the normal steps to generate the match list.
* After the match list has been generated, export the database using the "Export Divisions" button. You can also copy the database files to a known location.
* Change the `url` in `JudgeHelper.java` to match the location of the database file or files.
* Optionally, customize the `header` to what your Judge Match Observers want to evaluate.
* Compile and execute `JudgeHelper.java` from within your IDE, redirecting the output to a file. Executing the JudgeHelper file will generate two sections, separated by some blank lines, which should look like the sample output at the bottom of this readme.
* Copy each section into its own file.
* For each section / file, open a new Excel workbook and use the 'Data' area of the Ribbon to import the file. Save each file in Excel format.
 
The first section can be used by your Judge Match Observers to note what the teams are doing on the field. The Judge Match Observers should use the Filter function in Excel to show only the current match, and change it after each match. Remind them to save the file every once in a while.

The second section shows the matches which each team will be playing in; this can be used by judges doing pit interviews to help them determine the best time to interview a team.

        Team|Match|Navigated|Cones Scored|Parked|Game Play|Reliability|Standout
        207 | 3 
        207 | 7 
        207 | 11 
        207 | 15 
        207 | 17 
        237 | 1 
        237 | 6 
        237 | 10 
        237 | 15 
        237 | 19 
        247 | 4 
        247 | 6 
        247 | 9 
        247 | 12 
        247 | 16 
        248 | 4 
        248 | 7 
        248 | 10 
        248 | 13 
        248 | 17 
        ...

        207 | Critical Mass | 3 | 7 | 11 | 15 | 17  
        237 | MFS Foxes | 1 | 6 | 10 | 15 | 19  
        247 | Reboot | 4 | 6 | 9 | 12 | 16  
        248 | Fatal Error | 4 | 7 | 10 | 13 | 17  
        ...
