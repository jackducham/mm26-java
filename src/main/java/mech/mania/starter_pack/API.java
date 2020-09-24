package mech.mania.starter_pack;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import mech.mania.engine.domain.model.ApiProtos;
import mech.mania.engine.domain.model.CharacterProtos;
import mech.mania.engine.domain.model.GameStateProtos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

/**
 *
 */
public class API {
    private static final Logger LOGGER = Logger.getLogger(API.class.getName());
    private static final String API_URL = "http://localhost:8082/api/";

    private final GameStateProtos.GameState gameState;
    private final String playerName;

    public API(GameStateProtos.GameState gameState, String playerName) {
        this.gameState = gameState;
        this.playerName = playerName;
    }

    /**
     *
     * @param start
     * @param end
     * @return
     */
    public ApiProtos.APIPathFindingResponse pathFinding(CharacterProtos.Position start,
                                                        CharacterProtos.Position end) {
        return pathFinding(gameState, start, end);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APIFindEnemiesResponse findEnemies() {
        return findEnemies(gameState, playerName);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APIFindMonstersResponse findMonsters() {
        return findMonsters(gameState, playerName);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APIFindEnemiesInRangeResponse findEnemiesInRange() {
        return findEnemiesInRange(gameState, playerName);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APICanBeAttackedResponse canBeAttacked() {
        return canBeAttacked(gameState, playerName);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APIFindClosestPortalResponse findClosestPortal() {
        return findClosestPortal(gameState, playerName);
    }


    /**
     *
     * @return
     */
    public ApiProtos.APILeaderBoardResponse leaderBoard() {
        ApiProtos.APILeaderBoardRequest request = ApiProtos.APILeaderBoardRequest.newBuilder()
                .setGameState(gameState)
                .build();
        try {
            return ApiProtos.APILeaderBoardResponse.parseFrom(makeApiRequest("leaderBoard", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    /**
     *
     * @param position
     * @return
     */
    public ApiProtos.APIAllEnemiesHitResponse findAllEnemiesHit(CharacterProtos.Position position) {
        return findAllEnemiesHit(gameState, playerName, position);
    }


    /**
     *
     * @param range
     * @return
     */
    public ApiProtos.APIItemsInRangeResponse findItemsInRange(int range) {
        return findItemsInRange(gameState, playerName, range);
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


    private ApiProtos.APIFindEnemiesResponse findEnemies(GameStateProtos.GameState gameState,
                                                        String playerName) {
        ApiProtos.APIFindEnemiesRequest request = ApiProtos.APIFindEnemiesRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindEnemiesResponse.parseFrom(makeApiRequest("findEnemies", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    private ApiProtos.APIFindMonstersResponse findMonsters(GameStateProtos.GameState gameState,
                                                          String playerName) {
        ApiProtos.APIFindMonstersRequest request = ApiProtos.APIFindMonstersRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindMonstersResponse.parseFrom(makeApiRequest("findMonsters", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }


    private ApiProtos.APIFindEnemiesInRangeResponse findEnemiesInRange(GameStateProtos.GameState gameState,
                                                                      String playerName) {
        ApiProtos.APIFindEnemiesInRangeRequest request = ApiProtos.APIFindEnemiesInRangeRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindEnemiesInRangeResponse.parseFrom(makeApiRequest("findEnemiesInRange", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APICanBeAttackedResponse canBeAttacked(GameStateProtos.GameState gameState,
                                                            String playerName) {
        ApiProtos.APICanBeAttackedRequest request = ApiProtos.APICanBeAttackedRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APICanBeAttackedResponse.parseFrom(makeApiRequest("canBeAttacked", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIFindClosestPortalResponse findClosestPortal(GameStateProtos.GameState gameState,
                                                                    String playerName) {
        ApiProtos.APIFindClosestPortalRequest request = ApiProtos.APIFindClosestPortalRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .build();
        try {
            return ApiProtos.APIFindClosestPortalResponse.parseFrom(makeApiRequest("findClosestPortal", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIAllEnemiesHitResponse findAllEnemiesHit(GameStateProtos.GameState gameState,
                                                                String playerName,
                                                                CharacterProtos.Position position) {
        ApiProtos.APIAllEnemiesHitRequest request = ApiProtos.APIAllEnemiesHitRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .setTargetSpot(position)
                .build();
        try {
            return ApiProtos.APIAllEnemiesHitResponse.parseFrom(makeApiRequest("findAllEnemiesHit", request));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.warning("Exception occurred in parsing API response: " + e);
        }
        return null;
    }

    private ApiProtos.APIItemsInRangeResponse findItemsInRange(GameStateProtos.GameState gameState,
                                                              String playerName,
                                                              int range) {
        ApiProtos.APIItemsInRangeRequest request = ApiProtos.APIItemsInRangeRequest.newBuilder()
                .setGameState(gameState)
                .setPlayerName(playerName)
                .setRange(range)
                .build();
        try {
            return ApiProtos.APIItemsInRangeResponse.parseFrom(makeApiRequest("itemsInRange", request));
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
