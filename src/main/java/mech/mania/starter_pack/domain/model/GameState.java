package mech.mania.starter_pack.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mech.mania.starter_pack.domain.model.board.Board;
import mech.mania.starter_pack.domain.model.characters.Monster;
import mech.mania.starter_pack.domain.model.characters.Player;
import mech.mania.starter_pack.domain.model.characters.Character;

public class GameState {
    private final long turnNumber;
    private final Map<String, Board> boardNames;
    private final Map<String, Player> playerNames;
    private final Map<String, Monster> monsterNames;

    /**
     * Gets the pvp board.
     * @return the pvp board
     */
    public Board getPvpBoard() {
        return boardNames.get("pvp");
    }

    /**
     * Getter for a specific player's board.
     * @param boardId id of player whose board is being requested
     * @return board of the specified player; null if no such player exists
     */
    public Board getBoard(String boardId) {
        if (boardNames.containsKey(boardId)) {
            return boardNames.get(boardId);
        }
        return null;
    }


    /**
     * Getter for a specific character (player or monster).
     * @param characterId id of requested character
     * @return the matching character; null if the id doesn't exist
     */
    public Character getCharacter(String characterId) {
        if(playerNames.containsKey(characterId)) {
            return playerNames.get(characterId);
        }

        if(monsterNames.containsKey(characterId)) {
            return monsterNames.get(characterId);
        }

        return null;
    }


    /**
     * Gets the map of character names to character objects.
     * @return the requested map
     */
    public Map<String, Character> getAllCharacters() {
        Map<String, Character> characters = new HashMap<>(getAllPlayers());
        characters.putAll(getAllMonsters());
        return characters;
    }

    /**
     * Gets all characters on a specific board.
     * @param boardId id of the board of interest
     * @return list of characters on given board
     */
    public List<Character> getCharactersOnBoard(String boardId) {
        List<Character> characters = new ArrayList<>(getPlayersOnBoard(boardId));
        characters.addAll(getMonstersOnBoard(boardId));
        return characters;
    }


    /**
     * Getter for a specific player.
     * @param playerId the id of the requested player
     * @return the player matching the id; null if the id doesn't exist
     */
    public Player getPlayer(String playerId) {
        if (playerNames.containsKey(playerId)) {
            return playerNames.get(playerId);
        }
        return null;
    }

    /**
     * Gets the map of player names to player objects.
     * @return the requested map
     */
    public Map<String, Player> getAllPlayers() {
        return playerNames;
    }


    /**
     * Gets all players on a specific board.
     * @param boardId id of the board of interest
     * @return list of players on given board
     */
    public List<Player> getPlayersOnBoard(String boardId) {
        if (!boardNames.containsKey(boardId)) {
            return new ArrayList<>();
        }

        Predicate<Player> byBoard = player -> player.getPosition().getBoardID().equals(boardId);

        return playerNames.values().stream().filter(byBoard)
                .collect(Collectors.toList());
    }

    /**
     * Getter for a specific monster.
     * @param monsterId the id of the requested monster
     * @return the monster matching the id; null if the id doesn't exist
     */
    public Monster getMonster(String monsterId) {
        if (monsterNames.containsKey(monsterId)) {
            return monsterNames.get(monsterId);
        }
        return null;
    }

    /**
     * Gets the map of monster names to monster objects.
     * @return the requested map
     */
    public Map<String, Monster> getAllMonsters() {
        return monsterNames;
    }

    /**
     * Gets all monsters on a specific board.
     * @param boardId id of the board of interest
     * @return list of all monsters on given board
     */
    public List<Monster> getMonstersOnBoard(String boardId) {
        if (!boardNames.containsKey(boardId)) {
            return new ArrayList<>();
        }

        Predicate<Monster> byBoard = monster -> monster.getPosition().getBoardID().equals(boardId);

        return monsterNames.values().stream().filter(byBoard)
                .collect(Collectors.toList());
    }


    /**
     * Builds a GameState object from a given Protocol Buffer.
     * @param gameStateProto Protocol Buffer representing the GameState to be copied
     */
    public GameState(mech.mania.engine.domain.model.GameStateProtos.GameState gameStateProto) {
        boardNames = new HashMap<>();

        Map<String, mech.mania.engine.domain.model.BoardProtos.Board> boardProtoMap = gameStateProto.getBoardNamesMap();

        for (String boardId : boardProtoMap.keySet()) {
            boardNames.put(boardId, new Board(boardProtoMap.get(boardId)));
        }

        turnNumber = gameStateProto.getStateId();

        playerNames = new HashMap<>();
        monsterNames = new HashMap<>();

        Map<String, mech.mania.engine.domain.model.CharacterProtos.Monster> allMonsters = gameStateProto.getMonsterNamesMap();
        for (String monsterName : allMonsters.keySet()) {
            Monster newMonster = new Monster(allMonsters.get(monsterName));
            monsterNames.put(newMonster.getName(), newMonster);
        }

        Map<String, mech.mania.engine.domain.model.CharacterProtos.Player> allPlayers = gameStateProto.getPlayerNamesMap();
        for (String playerName : allPlayers.keySet()) {
            Player newPlayer = new Player(allPlayers.get(playerName));
            playerNames.put(newPlayer.getName(), newPlayer);
        }
    }

    public long getTurnNumber() {
        return turnNumber;
    }

    public Map<String, Board> getAllBoards() {
        return boardNames;
    }
}
