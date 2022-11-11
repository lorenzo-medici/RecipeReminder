# RecipeReminder


<img src="https://user-images.githubusercontent.com/67294212/201318196-9a290714-7549-46fe-8ccb-e5bfb114a516.jpg" width="250"> <img src="https://user-images.githubusercontent.com/67294212/201318224-33917bd1-6f6c-405d-8321-f3a9249e3c85.jpg" width="250" hspace="10">


This Android application is a small project I took on to learn how to create Android apps using Kotlin.
RecipeReminder is mainly a repository of Recipes the user likes to cook and eat. Its main purpose is to order each recipe based on the last time they were cooked, from least to most recently cooked.

In addition, a simple shopping list section allows the user to add items they need and tick them off when they are bought.

The application opens on the recipe View, swipe left with your finger to move to the shopping list, and left to move to the recipe view again.

## Recipes

Each recipe has a name, a description and a type.
The description is optional, the other two fields have to be filled in.

Types are unfortunately hardcoded as this is a small project.

The floating buttons in the bottom right corner are used to add a new recipe and to filter by type which recipes will be shown in the main view. Please note that this does not change their order! The type selected will be shown in the top right of the screen, alongside the app name. If no type appears all saved recipes are shown.

<img src="https://user-images.githubusercontent.com/67294212/201318619-72fbd9c2-10f8-42b3-abc3-08cccb9f36ef.jpg" width="250">

When tapping on a new recipe in the list the view for editing it will open. Here you can freely change its type, name and description and save the changes using the first button. The second button sets the recipe's ```lastCooked``` property at the instant it is pressed. This ways it will be shown last in the list when returning to the main view. The third button deletes the recipe from the database.

<img src="https://user-images.githubusercontent.com/67294212/201318789-348865e1-dea8-47ab-8f31-095f33c8d92c.jpg" width="250">

## Shopping list

The list starts off empty. To add an item to the list tap the floating button in the bottom right corner. A dialog will open where the item's name/description can be inserted. The item will now be shown in the list view.

When items are added to the list, all their names are showed in alphabetical order with a check sign on the right of each one. Tapping this sign will delete the corresponding item from the list, as if it was bought.
To edit an item, tap on its name. A dialog will open where you will be able to change the item's name. Saved changes will be reflected immediately in the list view.

## Installation

The APK file for Android 10 (API 29) is available in the release section of this repository.

This repository tracks my AndroidStudio project folder, you can download it and import into the IDE to work on it, or you can build it for your specific android version. This is needed for android versions older than 10.

## Future Development

### Layout issues

The layouts found in activity_existing_ricetta.xml and activity_new_ricetta.xml have sizes and positions of component hardcoded.
For example, the description field has a maximum number of lines of 13 in activity_existing_ricetta. This is because I set it to fill the screen of my smartphone if needed.
This would of course not work at all on other phones: on smaller screens the bottom buttons would wander off the screen, on bigger screens space would be left empty at the bottom, even if a really long description was typed in.
The solution is using relative layouts and limiting the expansion of the description field to the margin of the buttons.

Another fix would be to import new_ricetta in existing_ricetta as the top part is actually the same to make future layout changes simpler.
For the same reason, some refactoring of the NewRicettaActivity and ExistingRicettaActivity would be a good thing to avoid a lot of code repetition.

### Other changes

As of now, the app (and parts of the code) are written in Italian, my first language. Some code clean-up and translation would be needed to translate the strings shown to the user in English and make the code readable to most people
Furthermore, recipe type translations would be really tricky to do because types are coded as an enum and their strings hardcoded as properties of said enums. Context managing to show different type names depending on the language is almost impossible as of now.

Another issue regarding types arises: the app was coded for my needs and I didn't bother creating functionalities for making it more flexible culturally. In Italy we are used to labeling dish types as 'Primi piatti' (consisting mainly of pasta, rice or soup dishes) and 'Secondi Piatti' (consisting of meat, fish and cheese dishes). 'Piatti unici' are also a type of dish but I usually consider them as protein dishes anyways. This does not translate well with other cultures more oriented to dishes with all different nutrients (Main courses, in English).


A solution to both of these problems would be coding recipe types as a simple class containing a name. The user could manage their own types to their need and culture, and naming each one in their preferred way and language. This would of course imply creating a new interface, a new model class, implementing new logic and doing a good bit of refactoring. Currently, this is not planned as future work, but is open for change by anyone who wants to fork from my work.

A far more simple addition would be the possibility to filter the recipes by name with a simple search field. Due to my use of the app this was not deemed necessary but it would probably be the first extension I would do, were I to work on this project again.

