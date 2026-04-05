Welcome to Instinct V1.0!
    Instinct is designed to be a Strategy-Assistance
    -Tool for advanced mathematical predictions and 
    calculations, rather than a standalone scouting 
    system.

    -Courtesy of Team 4256


EPAConsts
    Houses interpolation table of unitless EPA 
    vs standard EPA. All data is pulled from
    Statbotics.

GoogleSheetWriter
    Handles writing values to a designated Google
    Sheet based on the Sheet URL. Requires a 
    Google service account and a credentials.json
    key.

StatboticsAPI
    Custom Statbotics interface that pulls data
    from JSON requests to the Statbotics website.
    Also handles calculating EPA.


Update Log:

Alpha 1 (4/4/2026)
    -Can type in team # via terminal (no GUI)
    -Will recieve unitless EPA & standard EPA
    (calculated with interpolation table) from
    Statbotics JSON files
    -Data is printed to a Google Sheet