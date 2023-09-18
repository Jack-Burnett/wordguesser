# Notes

For this project I decided to implement a small game.
I wanted it to be a spring boot service and not need a UI, as well as to depend on an external API.
This limited things - what I came up with is a game where players get definitions for words and have to guess the word.
They get more definitions to help them as they fail.

This requires some state management, as games and instances are persisted over time.
We reuse games for future users - ideally this would let two users play the same game and compare 
(though, there is no way to start a game by id, only a random game, right now)

The list of words are stored locally, but we get definitions from a third party API.
One big improvement I would make given enough time would be to add an extra service in that pre-generates a set
amount of games, and makes a new one whenever one is retrieved. This would give us resilience if we lost
access to the third party (and generally speed things up as the generation would be off the write-path).

One alternative would be to just generate a few games per day, perhaps just one (like Wordle).

---

# Improvements

- We do not use a database, just some lists. Obviously a real implementation would use a database here.
- A "user" is just a string passed with a request, with no validation. We would want accounts, logins and tokens in a full implementation
- A web UI would make it a lot more playable than making repeat requests directly
- More comparison features would be nice
- Testing could be more thorough - a high level test of the running application would be good
- We do not currently examine errors from the third-party
- The game itself is often pretty hard - could tell you if you were close or tell you the expected letter

---

# Running

Created with IntelliJ and Java 17.

`gradle bootRun` should be sufficient to run it.

Once running you can start a new game via `POST localhost:8080/start-game` (must have a header called username, with any value).

You can then copy the instance id from the response to make guesses at what the word is, using the guess endpoint;
```
POST localhost:8080/guess/{instanceId}
{
	"guess": "vehicle"
}
```
and the same username header as before.
