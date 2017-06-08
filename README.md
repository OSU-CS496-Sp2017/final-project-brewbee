# BrewBee Project Proposal

### High Level Description of Our App: 
The app will allow users to search for a specific beer name, brewery, beer style. It would return a list of beers that match the criteria. If the user clicks on any of the results, it will give a whole page about that beer which would have an image, description, ABV%, IBU, and other information regarding that specific beer. We also hope to incorporate a map which the user can click on and be taken to Google Maps so they can directions to the brewery (if relevant). The user can also star the beers and the starred beers would be saved on the device for later access.
Description of 3rd Party API:
We will be using BreweryDB. BreweryDB is a RESTful API made by developers. It contains information about brewing ingredients, beers, and breweries. The information in it is crowdsourced, and is open to input from users.

### Specific API Methods and How They Will Be Used:
Our app will use Get: /beers, /breweries, /categories, and /styles to get all the information the user will use. The nature of our app does not lend to users posting information, so we will only be using Get. We will use /beers: to find beers by name and give information about the beer. We will include a picture of the label if it is available. We will find the breweries that the serve that beer using /beer/:beerId/breweries. This information will be displayed as an explicit intent. We will use similar functionality for the brewery search. We will display beers attached to a brewery using /brewery/:breweryId/beers. This information will be displayed as an explicit intent. We will find the brewery using /brewery/:breweryId/locations, and pass that location as an implicit intent. We will also let the user search for beers by category/style. This will provide a list of beers just like searching by name. Since the terms Category and Style are so interchangeable, we will search both. Category will return categories such as Belgian and French Origin Ales, whereas style returns items such as Classic English-Style Pale Ale, or English-Style India Pale Ale. We will get the id of the /category or /style, then match it to beers with that category or style id.

### Team Members: 
Namtalay Laorattanavech (laorattn) 
Anish Asrani (asrania) 
Bradley Huffman (huffmabr) 
Cameron Nichol (nichocam)
