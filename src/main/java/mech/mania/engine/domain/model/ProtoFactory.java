package mech.mania.engine.domain.model;

import mech.mania.starter_pack.domain.model.GameState;
import mech.mania.starter_pack.domain.model.board.Board;
import mech.mania.starter_pack.domain.model.board.Tile;
import mech.mania.starter_pack.domain.model.characters.*;
import mech.mania.starter_pack.domain.model.characters.Character;
import mech.mania.starter_pack.domain.model.items.*;

import java.util.List;

public class ProtoFactory {

    public static CharacterProtos.CharacterDecision CharacterDecision(CharacterDecision characterDecision){
        CharacterProtos.CharacterDecision.Builder decisionBuilder = CharacterProtos.CharacterDecision.newBuilder();
        switch(characterDecision.getDecision()) {
            case NONE:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.NONE);
                break;
            case MOVE:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.MOVE);
                break;
            case ATTACK:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.ATTACK);
                break;
            case EQUIP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.EQUIP);
                break;
            case DROP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.DROP);
                break;
            case PICKUP:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.PICKUP);
                break;
            case PORTAL:
                decisionBuilder.setDecisionType(CharacterProtos.DecisionType.PORTAL);
                break;
        }

        decisionBuilder.setIndex(characterDecision.getIndex());
        if(characterDecision.getActionPosition() != null) {
            decisionBuilder.setTargetPosition(ProtoFactory.Position(characterDecision.getActionPosition()));
        }

        return decisionBuilder.build();
    }

    public static CharacterProtos.Position Position(Position position) {
        CharacterProtos.Position.Builder positionBuilder = CharacterProtos.Position.newBuilder();
        positionBuilder.setX(position.getX());
        positionBuilder.setY(position.getY());
        positionBuilder.setBoardId(position.getBoardID());

        return positionBuilder.build();
    }

    public static BoardProtos.Tile Tile(Tile tile){
        Tile.TileType type = tile.getType();
        BoardProtos.Tile.Builder tileBuilder = BoardProtos.Tile.newBuilder();
        switch (type) {
            case VOID:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.VOID);
                break;
            case BLANK:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.BLANK);
                break;
            case IMPASSIBLE:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.IMPASSIBLE);
                break;
            case PORTAL:
                tileBuilder.setTileType(BoardProtos.Tile.TileType.PORTAL);
                break;
        }

        List<Item> items = tile.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item curItem = items.get(i);
            tileBuilder.addItems(Item(curItem));
        }

        return tileBuilder.build();
    }

    public static BoardProtos.Board Board(Board board){
        BoardProtos.Board.Builder boardBuilder = BoardProtos.Board.newBuilder();

        Tile[][] grid = board.getGrid();
        List<Position> portals = board.getPortals();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                boardBuilder.addGrid(r * grid[r].length + c, Tile(grid[r][c]));
            }
        }

        for (int i = 0; i < portals.size(); i++) {
            boardBuilder.addPortals(i, Position(portals.get(i)));
        }

        boardBuilder.setWidth(grid.length);
        boardBuilder.setHeight(grid[0].length);

        return boardBuilder.build();
    }

    private static CharacterProtos.Character Character(Character character) {
        CharacterProtos.Character.Builder characterBuilder = CharacterProtos.Character.newBuilder();

        characterBuilder.setName(character.getName());
        characterBuilder.setBaseSpeed(character.getBaseSpeed());
        characterBuilder.setBaseMaxHealth(character.getBaseMaxHealth());
        characterBuilder.setBaseAttack(character.getBaseAttack());
        characterBuilder.setBaseDefense(character.getBaseDefense());
        characterBuilder.setCurrentHealth(character.getCurrentHealth());
        characterBuilder.setLevel(character.getLevel());
        characterBuilder.setExperience(character.getExperience());

        characterBuilder.setTicksSinceDeath(character.getTicksSinceDeath());
        characterBuilder.setIsDead(character.isDead());

        characterBuilder.setPosition(Position(character.getPosition()));
        characterBuilder.setSpawnPoint(Position(character.getSpawnPoint()));

        if(character.getWeapon() != null) {
            characterBuilder.setWeapon(Weapon(character.getWeapon()));
        }

        for (int i = 0; i < character.getActiveEffects().size(); i++) {
            characterBuilder.addActiveEffectsTempStatusModifier(TempStatusModifier(character.getActiveEffects().get(i).getFirst()));
            characterBuilder.addActiveEffectsSource(character.getActiveEffects().get(i).getSecond());
            characterBuilder.addActiveEffectsIsPlayer(character.getActiveEffects().get(i).getThird());
        }

        characterBuilder.putAllTaggedPlayersDamage(character.getTaggedPlayersDamage());

        return characterBuilder.build();
    }

    public static CharacterProtos.Monster Monster(Monster monster) {
        CharacterProtos.Character characterProtoClass = Character(monster);

        CharacterProtos.Monster.Builder monsterBuilder = CharacterProtos.Monster.newBuilder();
        monsterBuilder.mergeCharacter(characterProtoClass);
        monsterBuilder.setAggroRange(monster.getAggroRange());

        return monsterBuilder.build();
    }

    public static CharacterProtos.Player Player(Player player) {
        CharacterProtos.Character characterProtoClass = Character(player);
        CharacterProtos.Player.Builder playerBuilder = CharacterProtos.Player.newBuilder();

        playerBuilder.mergeCharacter(characterProtoClass);

        for (int i = 0; i < Player.INVENTORY_SIZE; i++) {
            Item curItem = player.getInventory()[i];
            playerBuilder.addInventory(Item(curItem));
        }

        if (player.getHat() != null) {
            playerBuilder.setHat(Hat(player.getHat()));
        }
        if (player.getClothes() != null) {
            playerBuilder.setClothes(Clothes(player.getClothes()));
        }
        if (player.getShoes() != null) {
            playerBuilder.setShoes(Shoes(player.getShoes()));
        }
        if(player.getAccessory() != null){
            playerBuilder.setAccessory(Accessory(player.getAccessory()));
        }

        return playerBuilder.build();
    }

    public static ItemProtos.Item Item(Item item){
        ItemProtos.Item.Builder itemBuilder = ItemProtos.Item.newBuilder();

        if (item instanceof Clothes) {
            itemBuilder.setClothes(Clothes((Clothes)item));
        } else if (item instanceof Hat) {
            itemBuilder.setHat(Hat((Hat)item));
        } else if (item instanceof Shoes) {
            itemBuilder.setShoes(Shoes((Shoes)item));
        } else if (item instanceof Weapon) {
            itemBuilder.setWeapon(Weapon((Weapon)item));
        } else if (item instanceof Consumable) {
            itemBuilder.setConsumable(Consumable((Consumable)item));
        } else if (item instanceof Accessory){
            itemBuilder.setAccessory(Accessory((Accessory)item));
        }

        return itemBuilder.build();
    }

    public static ItemProtos.Clothes Clothes(Clothes clothes) {
        ItemProtos.Clothes.Builder clothesBuilder = ItemProtos.Clothes.newBuilder();
        clothesBuilder.setStats(StatusModifier(clothes.getStats()));
        clothesBuilder.setTurnsToDeletion(clothes.getTurnsToDeletion());

        return clothesBuilder.build();
    }

    public static ItemProtos.Consumable Consumable(Consumable consumable) {
        ItemProtos.Consumable.Builder consumableBuilder = ItemProtos.Consumable.newBuilder();
        consumableBuilder.setMaxStack(consumable.getMaxStack());
        consumableBuilder.setStacks(consumable.getStacks());
        consumableBuilder.setEffect(TempStatusModifier(consumable.getEffect()));
        consumableBuilder.setTurnsToDeletion(consumable.getTurnsToDeletion());

        ItemProtos.Item.Builder itemBuilder = ItemProtos.Item.newBuilder();
        itemBuilder.setConsumable(consumableBuilder.build());

        return consumableBuilder.build();
    }

    public static ItemProtos.Hat Hat(Hat hat) {
        ItemProtos.Hat.Builder hatBuilder = ItemProtos.Hat.newBuilder();

        hatBuilder.setStats(StatusModifier(hat.getStats()));
        hatBuilder.setTurnsToDeletion(hat.getTurnsToDeletion());

        switch(hat.getMagicEffect()){
            default:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.NONE);
                break;
            case CLOTHES_BOOST:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.CLOTHES_BOOST);
                break;
            case SHOES_BOOST:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.SHOES_BOOST);
                break;
            case WEAPON_BOOST:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.WEAPON_BOOST);
                break;
            case TRIPLED_ON_HIT:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.TRIPLED_ON_HIT);
                break;
            case LINGERING_POTIONS:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.LINGERING_POTIONS);
                break;
            case STACKING_BONUS:
                hatBuilder.setMagicEffect(ItemProtos.MagicEffect.STACKING_BONUS);
                break;
        }

        return hatBuilder.build();
    }

    public static ItemProtos.Accessory Accessory(Accessory accessory) {
        ItemProtos.Accessory.Builder accessoryBuilder = ItemProtos.Accessory.newBuilder();

        accessoryBuilder.setStats(StatusModifier(accessory.getStats()));
        accessoryBuilder.setTurnsToDeletion(accessory.getTurnsToDeletion());

        switch(accessory.getMagicEffect()){
            default:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.NONE);
                break;
            case CLOTHES_BOOST:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.CLOTHES_BOOST);
                break;
            case SHOES_BOOST:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.SHOES_BOOST);
                break;
            case WEAPON_BOOST:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.WEAPON_BOOST);
                break;
            case TRIPLED_ON_HIT:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.TRIPLED_ON_HIT);
                break;
            case LINGERING_POTIONS:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.LINGERING_POTIONS);
                break;
            case STACKING_BONUS:
                accessoryBuilder.setMagicEffect(ItemProtos.MagicEffect.STACKING_BONUS);
                break;
        }

        return accessoryBuilder.build();
    }

    public static ItemProtos.Shoes Shoes(Shoes shoes){
        ItemProtos.Shoes.Builder shoesBuilder = ItemProtos.Shoes.newBuilder();
        shoesBuilder.setStats(StatusModifier(shoes.getStats()));
        shoesBuilder.setTurnsToDeletion(shoes.getTurnsToDeletion());
        return shoesBuilder.build();
    }

    public static ItemProtos.StatusModifier StatusModifier(StatusModifier statusModifier) {
        ItemProtos.StatusModifier.Builder statusModifierBuilder = ItemProtos.StatusModifier.newBuilder();

        statusModifierBuilder.setFlatSpeedChange(statusModifier.getFlatSpeedChange());
        statusModifierBuilder.setPercentSpeedChange(statusModifier.getPercentSpeedChange());
        statusModifierBuilder.setFlatHealthChange(statusModifier.getFlatHealthChange());
        statusModifierBuilder.setPercentHealthChange(statusModifier.getPercentHealthChange());
        statusModifierBuilder.setFlatExperienceChange(statusModifier.getFlatExperienceChange());
        statusModifierBuilder.setPercentExperienceChange(statusModifier.getPercentExperienceChange());
        statusModifierBuilder.setFlatAttackChange(statusModifier.getFlatAttackChange());
        statusModifierBuilder.setPercentAttackChange(statusModifier.getPercentAttackChange());
        statusModifierBuilder.setFlatDefenseChange(statusModifier.getFlatDefenseChange());
        statusModifierBuilder.setPercentDefenseChange(statusModifier.getPercentDefenseChange());

        return statusModifierBuilder.build();
    }

    public static ItemProtos.TempStatusModifier TempStatusModifier(TempStatusModifier tempStatusModifier){
        ItemProtos.TempStatusModifier.Builder tempStatusModifierBuilder = ItemProtos.TempStatusModifier.newBuilder();

        tempStatusModifierBuilder.setStats(StatusModifier(tempStatusModifier));
        tempStatusModifierBuilder.setTurnsLeft(tempStatusModifier.getTurnsLeft());
        tempStatusModifierBuilder.setFlatDamagePerTurn(tempStatusModifier.getDamagePerTurn());

        return tempStatusModifierBuilder.build();
    }

    public static ItemProtos.Weapon Weapon(Weapon weapon) {
        ItemProtos.Weapon.Builder weaponBuilder = ItemProtos.Weapon.newBuilder();
        weaponBuilder.setStats(StatusModifier(weapon.getStats()));
        weaponBuilder.setRange(weapon.getRange());
        weaponBuilder.setSplashRadius(weapon.getSplashRadius());
        weaponBuilder.setOnHitEffect(TempStatusModifier(weapon.getOnHitEffect()));
        weaponBuilder.setAttack(weapon.getAttack());
        weaponBuilder.setTurnsToDeletion(weapon.getTurnsToDeletion());

        return weaponBuilder.build();
    }

    public static GameStateProtos.GameState GameState(GameState gameState){
        GameStateProtos.GameState.Builder gameStateBuilder = GameStateProtos.GameState.newBuilder();
        gameStateBuilder.setStateId(gameState.getTurnNumber());

        for (String boardID : gameState.getAllBoards().keySet()) {
            gameStateBuilder.putBoardNames(boardID, Board(gameState.getBoard(boardID)));
        }

        for (String playerName : gameState.getAllPlayers().keySet()) {
            gameStateBuilder.putPlayerNames(playerName, Player(gameState.getPlayer(playerName)));
        }

        for (String monsterName : gameState.getAllMonsters().keySet()) {
            gameStateBuilder.putMonsterNames(monsterName, Monster(gameState.getMonster(monsterName)));
        }

        return gameStateBuilder.build();
    }
}
