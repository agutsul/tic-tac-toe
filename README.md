# tic-tac-toe


// create player


curl -X POST -H "Content-Type: application/json" -d '{"playerName":"testPlayer1"}' http://localhost:8080/players/


curl -X POST -H "Content-Type: application/json" -d '{"playerName":"testPlayer2"}' http://localhost:8080/players/



// create game


curl -X POST -H "Content-Type: application/json" -d '{"playerName1":"testPlayer1","playerName2":"testPlayer2"}' http://localhost:8080/games/



// get game info by uuid/sessionId
curl -X GET http://localhost:8080/games/5f8b5d5b-fbd6-4fed-8f85-99e4b4ad2ca9



// submit player moves


curl -X POST -H "Content-Type: application/json" -d '{"playerName":"testPlayer1","position":1}' http://localhost:8080/games/5f8b5d5b-fbd6-4fed-8f85-99e4b4ad2ca9/move


curl -X POST -H "Content-Type: application/json" -d '{"playerName":"testPlayer2","position":9}' http://localhost:8080/games/5f8b5d5b-fbd6-4fed-8f85-99e4b4ad2ca9/move