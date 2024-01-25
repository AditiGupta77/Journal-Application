# JournalApp

## Overview

Welcome to JournalApp, your personal space for keeping and showcasing diary entries with details such as date, start time, and end time. 

## Features

- **Enhanced Navigation:** Implemented navigation actions in the navigation graph, leveraging safe args for efficient data transfer among fragments. A shared view model is used to send data from a dialog to the calling fragment.

- **Database Modifications:** The Room database has been updated to include new columns for date, start time, and end time. DELETE functionality has been added, and unit tests for the database ensure robustness.

- **Delete Button Addition:** A DELETE button is now present on the menu bar in the EntryDetailsFragment, with a confirmation dialog to prevent accidental deletions.

- **Share Functionality:** Integrated a SHARE button on the EntryDetailsFragment menu bar. The shared content includes a formatted text message indicating the title, date, start time, and end time of the journal entry.

- **Info Button and Navigation Graph Update:** An INFO button has been added to the menu bar in the EntryList fragment, connecting to a new fragment with a short description of the app. The navigation graph has been updated to reflect these additions.

- **Accessibility Features:** Conducted accessibility checks using Talk Back, generated a report with the Accessibility Scanner, and created Espresso test cases to ensure accessibility.

## Development Credits

- **Android Studio Documentation:** [Official Android Studio documentation](https://developer.android.com/docs)

- **Course Assistance:** Course slides were referenced for guidance.

- **AI Tool:** BING AI was used to generate relevant test cases.

- **Debugging Reference:** Stack Overflow was referred to for debugging assistance.

## Number of Hours and Difficulty Level

- **Number of Hours:** Approximately 70-75 hours

- **Difficulty Level:** 10 out of 10 (Significant challenges were faced, especially with navigation, making the development process intricate.)

Feel free to explore your JournalApp and start documenting your journey! 📖📅⏰
