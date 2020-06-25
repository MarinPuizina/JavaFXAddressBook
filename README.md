# JavaFXAddressBook

![Image Of The Application](https://i.imgur.com/MeZQq5r.jpg)


**Technologies used:**
1) Java + JavaFX + JavaFXML
2) SceneBuilder
3) Embedded Derby database
4) XML (Generating, reading, storing data from XML into database)
    
**Functionalities:**

Listing all users stored in the databse. With ability to Refresh the list, edit data, delete the data.

![Image Of The List Users](https://i.imgur.com/dmgvZq5.jpg)

Storing user into the database.

![Image Of The Add User](https://i.imgur.com/YT5KGxr.jpg)

If there was an issue with the database, this application has the ability to store the data into Persons.xml
By clicking the Exit button, we are persisting all data that are saved inside the Persons.xml. If there are duplicate data
in XML and database, that data is removed from the XML and will not be persisted.
