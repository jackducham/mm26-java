package mech.mania.starter_pack.domain;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import mech.mania.engine.domain.model.*;
import mech.mania.starter_pack.domain.model.GameState;
import mech.mania.starter_pack.domain.model.characters.Character;
import mech.mania.starter_pack.domain.model.characters.Monster;
import mech.mania.starter_pack.domain.model.characters.Player;
import mech.mania.starter_pack.domain.model.characters.Position;
import mech.mania.engine.domain.model.ProtoFactory;
import mech.mania.starter_pack.domain.model.items.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class API {
    private static final Logger LOGGER = Logger.getLogger(API.class.getName());
    private static final String API_URL = "http://localhost:8082/api/";

    private final GameState gameState;
    private final String playerName;

    public API(GameState gameState, String playerName) {
        this.gameState = gameState;
        this.playerName = playerName;
    }

    /**
     * Finds a path from start to end in the current game state.
     * @param start The position to start from
     * @param end The position to end at
     * @return A list of Position objects from start to end or an empty list if no path is possible.
     */
    public List<Position> pathFinding(Position start, Position end) {
        List<Position> path = new ArrayList<>();

        ApiProtos.APIPathFindingResponse response =
                pathFinding(ProtoFactory.GameState(gameState), ProtoFactory.Position(start), ProtoFactory.Position(end));

        if(response == null) return path;

        for(CharacterProtos.Position p : response.getPathList()){
            path.add(new Position(p));
        }
        return path;
    }


    /**
     * Finds all enemies around a given position and sorts them by distance
     * @param position The center position to search around
     * @return A List of Characters sorted by distance from the given position
     */
    public List<Character> findEnemies(Position position) {
        List<Character> result = new ArrayList<>();
        ApiProtos.APIFindEnemiesByDistanceResponse response =
                findEnemies(ProtoFactory.GameState(gameState), ProtoFactory.Position(position), playerName);
        if(response == null) return result;

        for(CharacterProtos.Character c : response.getEnemiesList()){
            result.add(gameState.getCharacter(c.getName()));
        }
        return result;
    }


    /**
     * Returns a list of nearby monsters sorted by their total XP
     * @param position The center position to search around
     * @return A List of Monster objects sorted by XP
     */
    public List<Monster> findMonsters(Position position) {
        List<Monster> result = new ArrayList<>();
        ApiProtos.APIFindMonstersByExpResponse response =
                findMonsters(ProtoFactory.GameState(gameState), ProtoFactory.Position(position));

        if(response == null) return result;
        for(CharacterProtos.Monster m : response.getMonstersList()){
            result.add(new Monster(m));
        }
        return result;
    }


    /**
     * Finds a list of enemies that would be in range of an attack from your current weapon if you were at the given position
     * @param position The position to assume you are at
     * @return A List of Characters sorted by distance.
     */
    public List<Character> findEnemiesInRange(Position position) {
        List<Character> result = new ArrayList<>();
        ApiProtos.APIFindEnemiesInRangeOfAttackByDistanceResponse response =
                findEnemiesInRange(ProtoFactory.GameState(gameState), ProtoFactory.Position(position), playerName);
        if(response == null) return result;
        for(CharacterProtos.Character c : response.getEnemiesList()){
            result.add(gameState.getCharacter(c.getName()));
        }
        return result;
    }

    /**
     * Finds the closest portal to the given position
     * @param position The position to begin searching from
     * @return A Position representing the location of the closest portal, or null if an error occurred.
     */
    public Position findClosestPortal(Position position) {
        ApiProtos.APIFindClosestPortalResponse response =
                findClosestPortal(ProtoFactory.GameState(gameState), ProtoFactory.Position(position));
        if(response == null) return null;
        return new Position(response.getPortal());
    }


    /**
     * @return The list of current players sorted by total XP
     */
    public List<Player> leaderBoard() {
        ApiProtos.APILeaderBoardRequest request = ApiProtos.APILeaderBoardRequest.newBuilder()
                .setGameState(ProtoFactory.GameState(gameState))
                .build();

        List<Player> result = new ArrayList<>();
        try {
            ApiProtos.APILeaderBoardResponse response =
                    ApiProtos.APILeaderBoardResponse.parseFrom(makeApiRequest("leaderBoard", request));
            if(response == null) return result;
            for(CharacterProtos.Player p : response.getLeaderBoardList()){
                result.add(new Player(p));
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }

        return result;
    }


    /**
     * Finds all enemies that would be hit by your attack if you chose the given position
     * as your actionPosition in an ATTACK decision this turn.
     * @param position The position to test your attack at
     * @return A List of Characters who would be hit by your attack
     */
    public List<Character> findAllEnemiesHit(Position position) {
        List<Character> result = new ArrayList<>();
        ApiProtos.APIFindAllEnemiesHitResponse response =
                findAllEnemiesHit(ProtoFactory.GameState(gameState), playerName, ProtoFactory.Position(position));
        if(response == null) return result;
        for(CharacterProtos.Character c : response.getEnemiesHitList()){
            result.add(gameState.getCharacter(c.getName()));
        }
        return result;
    }

    /**
     * Find out if you would be in range of an attack if you were at the given position
     * @param position The position to assume you are at
     * @return 0 if not in range, 1 if in range, 2 if error
     */
    public int inRangeOfAttack(GameState gameState, Position position) {
        ApiProtos.APIInRangeOfAttackResponse response =
                canBeAttacked(ProtoFactory.GameState(gameState), ProtoFactory.Position(position), playerName);
        if(response == null) return 2;
        return response.getInRangeOfAttack() ? 1 : 0;
    }


    /**
     * Finds all items within a given range of the given position
     * @param position The position around which to search
     * @param range The range to search within
     * @return A List of Items found in the search
     */
    public List<Item> findItemsInRange(Position position, int range) {
        List<Item> result = new ArrayList<>();
        ApiProtos.APIFindItemsInRangeByDistanceResponse response =
                findItemsInRange(ProtoFactory.GameState(gameState), playerName, ProtoFactory.Position(position), range);
        if(response == null) return result;
        for(ItemProtos.Item i : response.getItemsList()){
            if(i.hasClothes()) result.add(new Clothes(i.getClothes()));
            else if(i.hasConsumable()) result.add(new Consumable(i.getMaxStack(), i.getConsumable()));
            else if(i.hasHat()) result.add(new Hat(i.getHat()));
            else if(i.hasShoes()) result.add(new Shoes(i.getShoes()));
            else if(i.hasWeapon()) result.add(new Weapon(i.getWeapon()));
        }
        return result;
    }


    /* --------------- Private helper functions for HTTP interface ------------------- */

    private ApiProtos.APIPathFindingResponse pathFinding(GameStateProtos.GameState gameState,
                                                        CharacterProtos.Position start,
                                                        CharacterProtos.Position end) {
        ApiProtos.APIPathFindingRequest request = ApiProtos.APIPathFindingRequest.newBuilder()
                .setGameState(gameState)
                .setStart(start)
                .setEnd(end)
                .build();
        try {
            return ApiProtos.APIPathFindingResponse.parseFrom(makeApiRequest("pathFinding", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    private ApiProtos.APIFindEnemiesByDistanceResponse findEnemies(GameStateProtos.GameState gameState,
                                                        CharacterProtos.Position position,
                                                        String playerName) {
        ApiProtos.APIFindEnemiesByDistanceRequest request = ApiProtos.APIFindEnemiesByDistanceRequest.newBuilder()
                .setGameState(gameState)
                .setPosition(position)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindEnemiesByDistanceResponse.parseFrom(makeApiRequest("findEnemies", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    private ApiProtos.APIFindMonstersByExpResponse findMonsters(GameStateProtos.GameState gameState,
                                                          CharacterProtos.Position position) {
        ApiProtos.APIFindMonstersByExpRequest request = ApiProtos.APIFindMonstersByExpRequest.newBuilder()
                .setGameState(gameState)
                .setPosition(position)
                .build();
        try {
            return ApiProtos.APIFindMonstersByExpResponse.parseFrom(makeApiRequest("findMonsters", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    private ApiProtos.APIFindEnemiesInRangeOfAttackByDistanceResponse findEnemiesInRange(GameStateProtos.GameState gameState,
                                                                      CharacterProtos.Position position,
                                                                      String playerName) {
        ApiProtos.APIFindEnemiesInRangeOfAttackByDistanceRequest request = ApiProtos.APIFindEnemiesInRangeOfAttackByDistanceRequest.newBuilder()
                .setGameState(gameState)
                .setPosition(position)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindEnemiesInRangeOfAttackByDistanceResponse.parseFrom(makeApiRequest("findEnemiesInRange", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIInRangeOfAttackResponse canBeAttacked(GameStateProtos.GameState gameState,
                                                            CharacterProtos.Position position,
                                                            String playerName) {
        ApiProtos.APIInRangeOfAttackRequest request = ApiProtos.APIInRangeOfAttackRequest.newBuilder()
                .setGameState(gameState)
                .setPosition(position)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIInRangeOfAttackResponse.parseFrom(makeApiRequest("canBeAttacked", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIFindClosestPortalResponse findClosestPortal(GameStateProtos.GameState gameState,
                                                                    CharacterProtos.Position position) {
        ApiProtos.APIFindClosestPortalRequest request = ApiProtos.APIFindClosestPortalRequest.newBuilder()
                .setGameState(gameState)
                .setPosition(position)
                .build();
        try {
            return ApiProtos.APIFindClosestPortalResponse.parseFrom(makeApiRequest("findClosestPortal", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIFindAllEnemiesHitResponse findAllEnemiesHit(GameStateProtos.GameState gameState,
                                                                String playerName,
                                                                CharacterProtos.Position position) {
        ApiProtos.APIFindAllEnemiesHitRequest request = ApiProtos.APIFindAllEnemiesHitRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .setPosition(position)
                .build();
        try {
            return ApiProtos.APIFindAllEnemiesHitResponse.parseFrom(makeApiRequest("findAllEnemiesHit", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIFindItemsInRangeByDistanceResponse findItemsInRange(GameStateProtos.GameState gameState,
                                                              String playerName,
                                                              CharacterProtos.Position position,
                                                              int range) {
        ApiProtos.APIFindItemsInRangeByDistanceRequest request = ApiProtos.APIFindItemsInRangeByDistanceRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .setPosition(position)
                .setRange(range)
                .build();
        try {
            return ApiProtos.APIFindItemsInRangeByDistanceResponse.parseFrom(makeApiRequest("itemsInRange", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    /**
     * Helper method that calls the API given a string for the endpoint and the request object
     * @param apiMethod String representing the endpoint of the server request
     * @param protobufObj A protobuf object representing the request parameters
     * @return A protobuf object (cast it to the correct type upon return) representing the
     *      response object
     */
    private byte[] makeApiRequest(String apiMethod, MessageLite protobufObj) {
        String url = API_URL + apiMethod;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofByteArray(protobufObj.toByteArray())) .build();
        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return response.body();
        } catch (IOException | InterruptedException e) {
            LOGGER.warning("Exception occurred in sending API request: " + e);
        }
        return null;
    }
}
