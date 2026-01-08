# Farm Simulator

## Introduction

A farm management simulation system that enables users to experience the complete farm operation lifecycle. This application provides intuitive tools for managing farm resources, creating custom crops, planning planting schedules, and tracking growth progress through quarterly time progression. With real-time status monitoring and detailed crop information display, users can make informed decisions to optimize their farming operations. The system features automated harvest calculations and seasonal reset functionality, offering a complete and realistic farming simulation experience.

## User Stories

- As a user, I want to be able to set the total area of my farm.
- As a user, I want to be able to create new crop varieties and set their attributes.
- As a user, I want to be able to plant crops with specified areas on my farm.
- As a user, I want to be able to advance time to observe crop growth.
- As a user, I want to be able to check my current farm status including planted crops, time progression, and total revenue.
- As a user, I want to be able to view all available crops with their complete specifications.
- As a user, I want to be able to reset the farm to clear all planted crops and reset time while preserving the farm area.
- As a user, I want to be given the option to save my farm to file.
- As a user, I want to be given the option to load my farm from file when I start the application.

## Instructions for End User

- The farm panel displays all the planted crops in the farm.
- Select a crop from *Planting Select Crop*, type in a number to *Planted Area*, and click *Plant* to plant a selected type of crop with input area.
- Click *Create New Crop*, type in the required information and click *Create Crop* to create a new crop type wuth input status.
- Every bar that represents a crop in the farm panel has a corresbonding image on its left.
- Click *Save Farm* to save current farm to file.
- Click *Load Farm* to load farm from file.

## Phase 4: Task 2

=== FARM SIMULATOR EVENT LOG ===  
Application closed at: 2025-11-27T21:58:23.764593900

Thu Nov 27 21:57:16 PST 2025
Created new farm with area: 0 sq km

Thu Nov 27 21:57:22 PST 2025
Farm area set to: 10 sq km

Thu Nov 27 21:57:34 PST 2025
Created new crop: wheat - Revenue: $75000 - Seeds: 5 sq km - Time: 1 quarters

Thu Nov 27 21:57:46 PST 2025
Created new crop: rice - Revenue: $180000 - Seeds: 0 sq km - Time: 2 quarters

Thu Nov 27 21:57:52 PST 2025
Added 5 sq km seeds to rice - Total seeds now: 5 sq km

Thu Nov 27 21:57:56 PST 2025
Planted 2 sq km of wheat

Thu Nov 27 21:57:59 PST 2025
Planted 1 sq km of rice

Thu Nov 27 21:58:03 PST 2025
Time advanced to quarter 1

Thu Nov 27 21:58:03 PST 2025
Harvested 1 crops, revenue: $150000

Thu Nov 27 21:58:06 PST 2025
Planted 2 sq km of rice

Thu Nov 27 21:58:07 PST 2025
Time advanced to quarter 2

Thu Nov 27 21:58:07 PST 2025
Harvested 1 crops, revenue: $180000

Thu Nov 27 21:58:09 PST 2025
Time advanced to quarter 3

Thu Nov 27 21:58:09 PST 2025
Harvested 1 crops, revenue: $360000

Thu Nov 27 21:58:18 PST 2025
Planted 1 sq km of wheat

Thu Nov 27 21:58:21 PST 2025
Farm reset to initial state

=== END OF EVENT LOG ===

## Phase 4: Task 3

If there is sufficient time, I would like to perform two refactorings. First, I intend to apply the Observer Pattern to the three Panel classes in the GUI package and the FarmAppGUI class, as these three panels are affected by changes in FarmAppGUI. This refactoring can effectively reduce code coupling and improve readability and maintainability. Second, I wish to apply the Singleton Pattern to the Farm class in the model package, since I actually want only one Farm instance to exist throughout the entire program for now. However, this modification presents certain issues. For example, in the current design, the JsonReader class implements the functionality of reading JSON files by first generating a Farm instance and then modifying its parameters to match those saved in the JSON file. Therefore, if the Farm class constructor is changed to a private method, the implementation of JsonReader would also require significant alterations. Moreover, if there is a future need to support multiple Farm instances, the Singleton Pattern would increase the implementation difficulty.
